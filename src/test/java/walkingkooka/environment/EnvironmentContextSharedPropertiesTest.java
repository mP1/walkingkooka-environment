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
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;

import java.time.LocalDateTime;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EnvironmentContextSharedPropertiesTest extends EnvironmentContextSharedTestCase<EnvironmentContextSharedProperties>
    implements HashCodeEqualsDefinedTesting2<EnvironmentContextSharedProperties>,
    ToStringTesting<EnvironmentContextSharedProperties> {

    private final static LocalDateTime NOW = LocalDateTime.MIN;

    private final static EnvironmentContext CONTEXT = EnvironmentContexts.empty(
        Indentation.SPACES4,
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
            () -> EnvironmentContextSharedProperties.with(
                null,
                CONTEXT
            )
        );
    }

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> EnvironmentContextSharedProperties.with(
                Properties.EMPTY,
                null
            )
        );
    }

    // cloneEnvironment.................................................................................................

    @Test
    public void testCloneEnvironment() {
        final EnvironmentContextSharedProperties context = this.createContext();
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
        final EnvironmentContextSharedProperties context = this.createContext();

        final EnvironmentContext different = this.createContext();
        different.setLineEnding(LineEnding.CRNL);

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
            EnvironmentValueName.with(
                NAME,
                String.class
            ),
            VALUE
        );
    }

    @Test
    public void testEnvironmentalValueMissing() {
        this.environmentValueAndCheck(
            this.createContext(),
            EnvironmentValueName.with(
                "Unknown",
                Void.class
            )
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

    // removeEnvironmentValue...........................................................................................

    @Test
    public void testRemoveEnvironmentValueWithPropertyFails() {
        assertThrows(
            ReadOnlyEnvironmentValueException.class,
            () -> this.createContext()
                .removeEnvironmentValue(
                    EnvironmentValueName.with(NAME, Object.class)
                )
        );
    }

    // setEnvironmentValue..............................................................................................

    @Test
    public void testSetEnvironmentValueWithLocale() {
        final EnvironmentContextSharedProperties context = this.createContext();

        final Locale locale = Locale.GERMAN;
        this.setEnvironmentValueAndCheck(
            context,
            EnvironmentValueName.LOCALE,
            locale
        );
    }

    @Test
    public void testSetEnvironmentValueWithUser() {
        final EnvironmentContextSharedProperties context = this.createContext();

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
            EnvironmentContextSharedProperties.with(
                Properties.EMPTY.set(
                    PropertiesPath.parse(key1),
                    "value111"
                ).set(
                    PropertiesPath.parse(key2),
                    "value222"
                ),
                CONTEXT
            ),
            EnvironmentValueName.with(
                key1,
                String.class
            ),
            EnvironmentValueName.with(
                key2,
                String.class
            ),
            EnvironmentContext.INDENTATION,
            EnvironmentContext.LINE_ENDING,
            EnvironmentContext.LOCALE,
            EnvironmentContext.NOW
        );
    }

    @Override
    public EnvironmentContextSharedProperties createContext() {
        return EnvironmentContextSharedProperties.with(
            Properties.EMPTY.set(
                PropertiesPath.parse(NAME),
                VALUE
            ),
            CONTEXT.cloneEnvironment()
        );
    }

    // hashCode/equals..................................................................................................

    @Test
    public void testEqualsDifferentProperties() {
        this.checkNotEquals(
            EnvironmentContextSharedProperties.with(
                Properties.EMPTY,
                CONTEXT
            )
        );
    }

    @Test
    public void testEqualsDifferentContext() {
        final EnvironmentContext context = CONTEXT.cloneEnvironment();
        context.setLineEnding(
            LineEnding.CRNL
        );

        this.checkNotEquals(
            EnvironmentContextSharedProperties.with(
                Properties.EMPTY,
                context
            )
        );
    }

    @Override
    public EnvironmentContextSharedProperties createObject() {
        return this.createContext();
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createContext(),
            "{hello.123=Gday, indentation=\"    \", lineEnding=\"\\n\", locale=en}"
        );
    }

    // TreePrintable......................................................................................................

    @Test
    public void testPrintTree() {
        this.treePrintAndCheck(
            this.createContext(),
            "EnvironmentContextSharedProperties\n" +
                "  EmptyEnvironmentContext\n" +
                "    indentation\n" +
                "      \"    \" (walkingkooka.text.Indentation)\n" +
                "    lineEnding\n" +
                "      \"\\n\"\n" +
                "    now\n" +
                "      -999999999-01-01T00:00 (java.time.LocalDateTime)\n" +
                "    locale\n" +
                "      en (java.util.Locale)\n" +
                "  properties\n" +
                "    hello.123=Gday\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<EnvironmentContextSharedProperties> type() {
        return EnvironmentContextSharedProperties.class;
    }
}
