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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.props.Properties;
import walkingkooka.props.PropertiesPath;
import walkingkooka.text.LineEnding;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class PropertiesEnvironmentContextTest implements EnvironmentContextTesting2<PropertiesEnvironmentContext>,
    HashCodeEqualsDefinedTesting2<PropertiesEnvironmentContext>,
    ToStringTesting<PropertiesEnvironmentContext> {

    private final static LocalDateTime NOW = LocalDateTime.MIN;

    private final static EnvironmentContext CONTEXT = EnvironmentContexts.empty(
        LineEnding.NL,
        Locale.ENGLISH,
        () -> NOW,
        EnvironmentContext.ANONYMOUS
    );

    private final static String NAME = "hello.123";

    private final static String VALUE = "Gday";

    // with.............................................................................................................

    @Test
    public void testWithNullPropertiesFails() {
        assertThrows(
            NullPointerException.class,
            () -> PropertiesEnvironmentContext.with(
                null,
                CONTEXT
            )
        );
    }

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> PropertiesEnvironmentContext.with(
                Properties.EMPTY,
                null
            )
        );
    }

    // cloneEnvironment.................................................................................................

    @Test
    public void testCloneEnvironment() {
        final PropertiesEnvironmentContext context = this.createContext();
        final EnvironmentContext clone = context.cloneEnvironment();

        assertNotSame(
            context,
            clone
        );

        this.getAllEnvironmentValueAndCheck(
            context,
            clone
        );
    }

    // setEnvironmentContext............................................................................................

    @Test
    public void testSetEnvironmentContext() {
        final PropertiesEnvironmentContext context = this.createContext();

        final EnvironmentContext different = this.createContext()
            .setLineEnding(LineEnding.CRNL);

        assertNotSame(
            context,
            different
        );

        assertSame(
            different,
            context.setEnvironmentContext(different)
        );
    }

    // environmentValue.................................................................................................

    @Test
    public void testEnvironmentalValue() {
        this.environmentValueAndCheck(
            this.createContext(),
            EnvironmentValueName.with(NAME),
            VALUE
        );
    }

    @Test
    public void testEnvironmentalValueMissing() {
        this.environmentValueAndCheck(
            this.createContext(),
            EnvironmentValueName.with("Unknown")
        );
    }

    @Test
    public void testEnvironmentalValueDefaultsLocale() {
        this.environmentValueAndCheck(
            this.createContext(),
            EnvironmentValueName.LOCALE,
            CONTEXT.locale()
        );
    }

    // setEnvironmentValue..............................................................................................

    @Test
    public void testSetEnvironmentValueWithLocale() {
        final PropertiesEnvironmentContext context = this.createContext();

        final Locale locale = Locale.GERMAN;
        this.setEnvironmentValueAndCheck(
            context,
            EnvironmentValueName.LOCALE,
            locale
        );
    }

    @Test
    public void testSetEnvironmentValueWithUser() {
        final PropertiesEnvironmentContext context = this.createContext();

        final EmailAddress user = EmailAddress.parse("different@example.com");
        this.setEnvironmentValueAndCheck(
            context,
            EnvironmentContext.USER,
            user
        );
    }

    // environmentValueNames............................................................................................

    @Test
    public void testEnvironmentalValueNames() {
        final String key1 = "prefix.name1";
        final String key2 = "prefix.name2";

        this.environmentValueNamesAndCheck(
            PropertiesEnvironmentContext.with(
                Properties.EMPTY.set(
                    PropertiesPath.parse(key1),
                    "value111"
                ).set(
                    PropertiesPath.parse(key2),
                    "value222"
                ),
                CONTEXT
            ),
            EnvironmentValueName.with(key1),
            EnvironmentValueName.with(key2),
            EnvironmentContext.LINE_ENDING,
            EnvironmentContext.LOCALE,
            EnvironmentValueName.USER
        );
    }

    @Override
    public PropertiesEnvironmentContext createContext() {
        return PropertiesEnvironmentContext.with(
            Properties.EMPTY.set(
                PropertiesPath.parse(NAME),
                VALUE
            ),
            CONTEXT
        );
    }

    // hashCode/equals..................................................................................................

    @Test
    public void testEqualsDifferentProperties() {
        this.checkNotEquals(
            PropertiesEnvironmentContext.with(
                Properties.EMPTY,
                CONTEXT
            )
        );
    }

    @Test
    public void testEqualsDifferentContext() {
        this.checkNotEquals(
            PropertiesEnvironmentContext.with(
                Properties.EMPTY,
                CONTEXT.cloneEnvironment()
                    .setLineEnding(
                        LineEnding.CRNL
                    )
            )
        );
    }

    @Override
    public PropertiesEnvironmentContext createObject() {
        return this.createContext();
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createContext(),
            "{hello.123=Gday, lineEnding=\"\\n\", locale=fr}"
        );
    }

    // type naming......................................................................................................

    @Override
    public String typeNameSuffix() {
        return EnvironmentContext.class.getSimpleName();
    }

    // class............................................................................................................

    @Override
    public Class<PropertiesEnvironmentContext> type() {
        return PropertiesEnvironmentContext.class;
    }
}
