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

import java.time.LocalDateTime;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EmptyEnvironmentContextTest implements EnvironmentContextTesting2<EmptyEnvironmentContext>,
    ToStringTesting<EmptyEnvironmentContext> {

    private final static Locale LOCALE = Locale.ENGLISH;
    private final static LocalDateTime NOW = LocalDateTime.MIN;

    @Test
    public void testWithNullLocaleFails() {
        assertThrows(
            NullPointerException.class,
            () -> EmptyEnvironmentContext.with(
                null,
                () -> NOW,
                EnvironmentContext.ANONYMOUS
            )
        );
    }

    @Test
    public void testWithNullHasNowFails() {
        assertThrows(
            NullPointerException.class,
            () -> EmptyEnvironmentContext.with(
                LOCALE,
                null,
                EnvironmentContext.ANONYMOUS
            )
        );
    }

    @Test
    public void testWithNullUserFails() {
        assertThrows(
            NullPointerException.class,
            () -> EmptyEnvironmentContext.with(
                LOCALE,
                () -> NOW,
                null
            )
        );
    }

    // cloneEnvironment.................................................................................................

    @Test
    public void testCloneEnvironment() {
        final EmptyEnvironmentContext context = this.createContext();

        assertSame(
            context,
            context.cloneEnvironment()
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
    public EmptyEnvironmentContext createContext() {
        return EmptyEnvironmentContext.with(
            LOCALE,
            () -> NOW,
            EnvironmentContext.ANONYMOUS
        );
    }

    // environmentValueNames............................................................................................

    @Test
    public void testEnvironmentalValueNames() {
        this.environmentValueNamesAndCheck();
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createContext(),
            "{}"
        );
    }

    // type naming......................................................................................................

    @Override
    public String typeNameSuffix() {
        return EnvironmentContext.class.getSimpleName();
    }

    // class............................................................................................................

    @Override
    public Class<EmptyEnvironmentContext> type() {
        return EmptyEnvironmentContext.class;
    }
}
