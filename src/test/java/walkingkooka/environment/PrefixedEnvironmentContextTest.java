/*
 * Copyright 2024 Miroslav Pokorny (github.com/mP1)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package walkingkooka.environment;

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.props.Properties;
import walkingkooka.props.PropertiesPath;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class PrefixedEnvironmentContextTest implements EnvironmentContextTesting2<PrefixedEnvironmentContext>,
    ToStringTesting<PrefixedEnvironmentContext> {

    private final static EnvironmentValueName<?> PREFIX = EnvironmentValueName.with("prefix111.");

    private final static Locale LOCALE = Locale.FRENCH;

    private final static EnvironmentContext CONTEXT = EnvironmentContexts.empty(
        LOCALE,
        () -> LocalDateTime.of(
            1999,
            12,
            31,
            12,
            59
        ),
        Optional.of(
            EmailAddress.parse("user@example.com")
        )
    );

    @Test
    public void testWithNullPrefixFails() {
        assertThrows(
            NullPointerException.class,
            () -> PrefixedEnvironmentContext.with(
                null,
                CONTEXT
            )
        );
    }

    @Test
    public void testWithPrefixMissingDotFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> PrefixedEnvironmentContext.with(
                EnvironmentValueName.with("bad-prefix-123"),
                CONTEXT
            )
        );
    }

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> PrefixedEnvironmentContext.with(
                PREFIX,
                null
            )
        );
    }

    @Test
    public void testWithPrefixedEnvironmentContext() {
        final PrefixedEnvironmentContext prefixed = this.createContext();

        final PrefixedEnvironmentContext context = PrefixedEnvironmentContext.with(
            EnvironmentValueName.with("prefix222."),
            prefixed
        );

        this.checkEquals(
            "prefix111.prefix222.",
            context.prefix,
            "prefix"
        );

        assertSame(
            prefixed.context,
            context.context,
            "context"
        );
    }

    // locale...........................................................................................................

    @Test
    public void testLocale() {
        this.localeAndCheck(
            this.createContext(),
            LOCALE
        );
    }

    // environmentValue.................................................................................................

    @Test
    public void testEnvironmentalValueMissingPrefix() {
        this.environmentValueAndCheck(
            this.createContext(),
            EnvironmentValueName.with("Hello123")
        );
    }

    @Test
    public void testEnvironmentalValueWithPrefixAndValueMissing() {
        this.environmentValueAndCheck(
            this.createContext(),
            EnvironmentValueName.with("prefix111.missing")
        );
    }

    @Test
    public void testEnvironmentalValueWithPrefix() {
        this.environmentValueAndCheck(
            this.createContext(),
            EnvironmentValueName.with("prefix111.key111"),
            "value111"
        );
    }

    @Override
    public PrefixedEnvironmentContext createContext() {
        return PrefixedEnvironmentContext.with(
            PREFIX,
            EnvironmentContexts.properties(
                Properties.parse(
                    "key111=value111"
                ),
                CONTEXT
            )
        );
    }

    // environmentValueOrFail...........................................................................................

    @Test
    public void testEnvironmentalValueOrFailMissingPrefix() {
        this.environmentValueOrFailAndCheck(
            this.createContext(),
            EnvironmentValueName.with("Missing222"),
            new MissingEnvironmentValueException(
                EnvironmentValueName.with("Missing222")
            )
        );
    }

    @Test
    public void testEnvironmentalValueOrFailWithPrefixAndValueMissing() {
        this.environmentValueOrFailAndCheck(
            this.createContext(),
            EnvironmentValueName.with("prefix111.Missing222"),
            new MissingEnvironmentValueException(
                EnvironmentValueName.with("prefix111.Missing222")
            )
        );
    }

    // environmentValueNames............................................................................................

    @Test
    public void testEnvironmentalValueNames() {
        final String key1 = "prefix.name1";
        final String key2 = "prefix.name2";

        final String prefix = "PREFIX.";

        this.environmentValueNamesAndCheck(
            PrefixedEnvironmentContext.with(
                EnvironmentValueName.with(prefix),
                EnvironmentContexts.properties(
                    Properties.EMPTY.set(
                        PropertiesPath.parse(key1),
                        "value111"
                    ).set(
                        PropertiesPath.parse(key2),
                        "value222"
                    ),
                    CONTEXT
                )
            ),
            EnvironmentValueName.with(prefix + key1),
            EnvironmentValueName.with(prefix + key2)
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createContext(),
            "{key111=value111}"
        );
    }

    // type naming......................................................................................................

    @Override
    public String typeNameSuffix() {
        return EnvironmentContext.class.getSimpleName();
    }

    // class............................................................................................................

    @Override
    public Class<PrefixedEnvironmentContext> type() {
        return PrefixedEnvironmentContext.class;
    }
}
