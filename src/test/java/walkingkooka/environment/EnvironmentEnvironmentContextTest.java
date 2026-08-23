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

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EnvironmentEnvironmentContextTest implements EnvironmentContextTesting2<EnvironmentEnvironmentContext> {

    private final static EnvironmentValueName<String> NAME = EnvironmentValueName.with(
        "Magic",
        String.class
    );

    private final static String VALUE = "123";

    @Test
    public void testGet() {
        this.environmentValueAndCheck(
            NAME,
            VALUE
        );
    }

    @Test
    public void testGetWhenMissing() {
        this.environmentValueAndCheck(
            Environment.empty()
                .environmentContext(),
            NAME
        );
    }

    @Test
    public void testGetWhenMissing2() {
        this.environmentValueAndCheck(
            Environment.empty()
                .environmentContext(),
            EnvironmentValueName.LOCALE
        );
    }

    @Test
    public void testSetEnvironmentValueFails() {
        assertThrows(
            ReadOnlyEnvironmentValueException.class,
            () -> this.createContext()
                .setEnvironmentValue(
                    EnvironmentContext.LOCALE,
                    LOCALE
                )
        );
    }

    @Test
    public void testRemoveEnvironmentValueFails() {
        assertThrows(
            ReadOnlyEnvironmentValueException.class,
            () -> this.createContext()
                .removeEnvironmentValue(EnvironmentContext.LOCALE)
        );
    }

    @Test
    public void testSetEnvironmentContext() {
        final EnvironmentContext context = EnvironmentContexts.fake();

        assertSame(
            this.createContext()
                .setEnvironmentContext(context),
            context
        );
    }

    @Test
    public void testCloneEnvironmentFails() {
        assertThrows(
            UnsupportedOperationException.class,
            () -> this.createContext()
                .cloneEnvironment()
        );
    }

    // parseEnvironmentValueName........................................................................................

    @Override
    public void testParseEnvironmentValueNameWithNullUnknownFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testParseEnvironmentValueNameWithCharset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testParseEnvironmentValueNameWithCurrency() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testParseEnvironmentValueNameWithIndentation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testParseEnvironmentValueNameWithLineEnding() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testParseEnvironmentValueNameWithLocale() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testParseEnvironmentValueNameWithNow() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testParseEnvironmentValueNameWithTimeOffset() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testParseEnvironmentValueNameWithUser() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetCurrencyWithDifferentAndWatcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetIndentationWithDifferentAndWatcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetLineEndingWithDifferentAndWatcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetLocaleWithDifferent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetLocaleWithDifferentAndWatcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetTimeOffsetWithDifferentAndWatcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetUserWithDifferentAndWatcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public EnvironmentEnvironmentContext createContext() {
        return (EnvironmentEnvironmentContext)
            Environment.empty()
                .set(
                    NAME,
                    VALUE
                ).set(
                    EnvironmentValueName.CHARSET,
                    CHARSET
                ).set(
                    EnvironmentValueName.CURRENCY,
                    CURRENCY
                ).set(
                    EnvironmentValueName.INDENTATION,
                    INDENTATION
                ).set(
                    EnvironmentValueName.LINE_ENDING,
                    LINE_ENDING
                ).set(
                    EnvironmentValueName.LOCALE,
                    LOCALE
                ).set(
                    EnvironmentValueName.NOW,
                    NOW
                ).set(
                    EnvironmentValueName.TIME_OFFSET,
                    EnvironmentContext.DEFAULT_TIME_OFFSET
                ).set(
                    EnvironmentValueName.USER,
                    USER
                ).environmentContext();
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createContext(),
            "{charset=UTF-8, currency=AUD, indentation=\"  \", lineEnding=\"\n" +
                "\", locale=en_AU, Magic=\"123\", now=1999-12-31T12:58:59, timeOffset=Z, user=user123@example.com}"
        );
    }

    // class............................................................................................................

    @Override
    public String typeNameSuffix() {
        return EnvironmentContext.class.getSimpleName();
    }

    @Override
    public Class<EnvironmentEnvironmentContext> type() {
        return EnvironmentEnvironmentContext.class;
    }
}
