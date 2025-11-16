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
            EnvironmentValueName.LOCALE
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createContext(),
            "{locale=de}"
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
