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
import walkingkooka.datetime.HasNow;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.text.LineEnding;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ReadOnlyEnvironmentContextTest implements EnvironmentContextTesting2<ReadOnlyEnvironmentContext>,
    ToStringTesting<ReadOnlyEnvironmentContext> {

    private final static Locale LOCALE = Locale.GERMAN;
    private final static LocalDateTime NOW = LocalDateTime.MIN;

    @Test
    public void testWithNullContxtFails() {
        assertThrows(
            NullPointerException.class,
            () -> ReadOnlyEnvironmentContext.with(null)
        );
    }

    @Test
    public void testWithUnwraps() {
        final ReadOnlyEnvironmentContext context = ReadOnlyEnvironmentContext.with(
            EnvironmentContexts.fake()
        );

        assertSame(
            context,
            ReadOnlyEnvironmentContext.with(context)
        );
    }

    // cloneEnvironment.................................................................................................

    @Test
    public void testCloneEnvironment() {
        final ReadOnlyEnvironmentContext context = this.createContext();

        assertNotSame(
            context,
            context.cloneEnvironment()
        );
    }

    @Test
    public void testCloneEnvironmentNotReadOnly() {
        final ReadOnlyEnvironmentContext context = this.createContext();

        final EnvironmentContext cloned = context.cloneEnvironment();
        assertNotSame(
            context,
            cloned
        );

        final EnvironmentValueName<String> name = EnvironmentValueName.with("hello");
        final String value = "World123";

        this.environmentValueAndCheck(
            cloned.setEnvironmentValue(
                name,
                value
            ),
            name,
            value
        );
    }

    // setEnvironmentContext............................................................................................

    @Test
    public void testSetEnvironmentContextWithSame() {
        final EnvironmentContext empty = EnvironmentContexts.empty(
            LineEnding.NL,
            Locale.FRENCH,
            LocalDateTime::now,
            Optional.of(
                EmailAddress.parse("user123@example.com")
            )
        );
        final ReadOnlyEnvironmentContext readOnly = ReadOnlyEnvironmentContext.with(empty);

        assertSame(
            readOnly,
            readOnly.setEnvironmentContext(empty)
        );
    }

    @Test
    public void testSetEnvironmentContext() {
        final HasNow hasNow = () -> NOW;

        final EnvironmentContext empty = EnvironmentContexts.empty(
            LineEnding.NL,
            Locale.FRENCH,
            hasNow,
            Optional.of(
                EmailAddress.parse("user123@example.com")
            )
        );
        final ReadOnlyEnvironmentContext readOnly = ReadOnlyEnvironmentContext.with(empty);

        final EnvironmentContext different = EnvironmentContexts.empty(
            LineEnding.CRNL,
            Locale.GERMAN,
            hasNow,
            Optional.of(
                EmailAddress.parse("user123@example.com")
            )
        );

        this.checkNotEquals(
            empty,
            different
        );

        final EnvironmentContext set = readOnly.setEnvironmentContext(different);

        assertNotSame(
            empty,
            set
        );

        this.checkEquals(
            ReadOnlyEnvironmentContext.with(different),
            set
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

    // setLocale........................................................................................................

    @Override
    public void testSetLocaleWithDifferent() {
        throw new UnsupportedOperationException();
    }

    // environmentValue.................................................................................................

    @Test
    public void testEnvironmentalValue() {
        this.environmentValueAndCheck(
            this.createContext(),
            EnvironmentValueName.with("Hello123")
        );
    }

    @Test
    public void testEnvironmentValueWithLocale() {
        this.environmentValueAndCheck(
            this.createContext(),
            EnvironmentValueName.LOCALE,
            LOCALE
        );
    }

    @Override
    public ReadOnlyEnvironmentContext createContext() {
        return ReadOnlyEnvironmentContext.with(
            EnvironmentContexts.map(
                EnvironmentContexts.empty(
                    LineEnding.NL,
                    Locale.FRANCE,
                    () -> NOW,
                    EnvironmentContext.ANONYMOUS
                )
            ).setLocale(LOCALE)
        );
    }

    // environmentValueNames............................................................................................

    @Test
    public void testEnvironmentalValueNames() {
        this.environmentValueNamesAndCheck(
            EnvironmentValueName.LINE_ENDING,
            EnvironmentValueName.LOCALE
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createContext(),
            "{lineEnding=\"\\n\", locale=de}"
        );
    }

    // type naming......................................................................................................

    @Override
    public String typeNameSuffix() {
        return EnvironmentContext.class.getSimpleName();
    }

    // class............................................................................................................

    @Override
    public Class<ReadOnlyEnvironmentContext> type() {
        return ReadOnlyEnvironmentContext.class;
    }
}
