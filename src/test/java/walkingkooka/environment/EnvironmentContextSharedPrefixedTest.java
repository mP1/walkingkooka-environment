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
import walkingkooka.collect.set.Sets;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.props.Properties;
import walkingkooka.props.PropertiesPath;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EnvironmentContextSharedPrefixedTest extends EnvironmentContextSharedTestCase<EnvironmentContextSharedPrefixed>
    implements EnvironmentContextTesting,
    HashCodeEqualsDefinedTesting2<EnvironmentContextSharedPrefixed>,
    ToStringTesting<EnvironmentContextSharedPrefixed> {

    private final static EnvironmentValueName<?> PREFIX = EnvironmentValueName.with(
        "prefix111.",
        Void.class
    );

    private final static EnvironmentContext CONTEXT = new FakeEnvironmentContext() {
        @Override
        public Set<EnvironmentValueName<?>> environmentValueNames() {
            return Sets.empty();
        }

        @Override
        public Optional<EmailAddress> user() {
            return OPTIONAL_USER;
        }
    };

    @Test
    public void testWithNullPrefixFails() {
        assertThrows(
            NullPointerException.class,
            () -> EnvironmentContextSharedPrefixed.with(
                null,
                CONTEXT
            )
        );
    }

    @Test
    public void testWithPrefixMissingDotFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> EnvironmentContextSharedPrefixed.with(
                EnvironmentValueName.with(
                    "bad-prefix-123",
                    Void.class
                ),
                CONTEXT
            )
        );
    }

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> EnvironmentContextSharedPrefixed.with(
                PREFIX,
                null
            )
        );
    }

    @Test
    public void testWithPrefixedEnvironmentContext() {
        final EnvironmentContextSharedPrefixed prefixed = this.createContext();

        final EnvironmentContextSharedPrefixed context = EnvironmentContextSharedPrefixed.with(
            EnvironmentValueName.with(
                "prefix222.",
                Void.class
            ),
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
            EnvironmentValueName.with(
                "Hello123",
                Void.class
            )
        );
    }

    @Test
    public void testEnvironmentalValueWithPrefixAndValueMissing() {
        this.environmentValueAndCheck(
            this.createContext(),
            EnvironmentValueName.with(
                "prefix111.missing",
                Void.class
            )
        );
    }

    @Test
    public void testEnvironmentalValueWithPrefix() {
        this.environmentValueAndCheck(
            this.createContext(),
            EnvironmentValueName.with(
                "prefix111.key111",
                String.class
            ),
            "value111"
        );
    }

    @Test
    public void testEnvironmentValueWithLocale() {
        final EnvironmentContextSharedPrefixed context = this.createContext();

        this.environmentValueAndCheck(
            context,
            EnvironmentContext.LOCALE,
            LOCALE
        );
    }

    @Test
    public void testEnvironmentValueWithUser() {
        final EnvironmentContextSharedPrefixed context = this.createContext();

        this.environmentValueAndCheck(
            context,
            EnvironmentContext.USER,
            USER
        );
    }

    // setEnvironmentContext............................................................................................

    @Test
    public void testSetEnvironmentContext() {
        final EnvironmentContextSharedPrefixed context = this.createContext();

        assertSame(
            DIFFERENT_ENVIRONMENT_CONTEXT,
            context.setEnvironmentContext(DIFFERENT_ENVIRONMENT_CONTEXT)
        );
    }

    // removeEnvironmentValue...........................................................................................

    @Override
    public void testRemoveEnvironmentValueWithNowFails() {
        throw new UnsupportedOperationException();
    }

    // setEnvironmentValue..............................................................................................

    @Override
    public void testSetEnvironmentValueWithNowFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testSetEnvironmentValueWithWatcher() {
        this.fired = false;

        final EnvironmentContext context = EnvironmentContexts.map(ENVIRONMENT_CONTEXT);

        final EnvironmentContextSharedPrefixed environmentContextSharedPrefixed = EnvironmentContextSharedPrefixed.with(
            PREFIX,
            context
        );

        final EnvironmentValueName<String> name = EnvironmentValueName.with(
            "hello",
            String.class
        );
        final String value = "world";

        final Runnable remover = environmentContextSharedPrefixed.addEnvironmentWatcher(
            new EnvironmentWatcher() {

                @Override
                public void onValueChange(final Optional<EnvironmentValueNameAndValue<?>> oldValue,
                                          final Optional<EnvironmentValueNameAndValue<?>> newValue) {
                    checkEquals(
                        Optional.empty(),
                        oldValue,
                        "oldValue"
                    );
                    checkEquals(
                        Optional.of(
                            EnvironmentValueName.with(
                                PREFIX + name.value(),
                                String.class
                            ).setValue(value)
                        ),
                        newValue,
                        "newValue"
                    );

                    EnvironmentContextSharedPrefixedTest.this.fired = true;
                }
            }
        );

        context.setEnvironmentValue(
            name,
            value
        );

        this.checkEquals(
            true,
            this.fired
        );
    }

    private boolean fired;

    // EnvironmentContextTesting........................................................................................

    @Override
    public EnvironmentContextSharedPrefixed createContext() {
        return EnvironmentContextSharedPrefixed.with(
            PREFIX,
            EnvironmentContexts.properties(
                Properties.parse(
                    "key111=value111"
                ),
                ENVIRONMENT_CONTEXT.cloneEnvironment()
            )
        );
    }

    // environmentValueOrFail...........................................................................................

    @Test
    public void testEnvironmentalValueOrFailMissingPrefix() {
        this.environmentValueOrFailAndCheck(
            this.createContext(),
            EnvironmentValueName.with(
                "Missing222",
                String.class
            ),
            EnvironmentValueName.with(
                "Missing222",
                String.class
            ).missingEnvironmentValueException()
        );
    }

    @Test
    public void testEnvironmentalValueOrFailWithPrefixAndValueMissing() {
        this.environmentValueOrFailAndCheck(
            this.createContext(),
            EnvironmentValueName.with(
                "prefix111.Missing222",
                String.class
            ),
            EnvironmentValueName.with(
                "prefix111.Missing222",
                String.class
            ).missingEnvironmentValueException()
        );
    }

    // environmentValueNames............................................................................................

    @Test
    public void testEnvironmentalValueNames() {
        final String key1 = "prefix.name1";
        final String key2 = "prefix.name2";

        final String prefix = "PREFIX.";

        this.environmentValueNamesAndCheck(
            EnvironmentContextSharedPrefixed.with(
                EnvironmentValueName.with(
                    prefix,
                    Void.class
                ),
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
            EnvironmentValueName.with(
                prefix + key1,
                String.class
            ),
            EnvironmentValueName.with(
                prefix + key2,
                String.class
            ),
            EnvironmentContext.CHARSET,
            EnvironmentContext.CURRENCY,
            EnvironmentContext.INDENTATION,
            EnvironmentValueName.LINE_ENDING,
            EnvironmentContext.LOCALE,
            EnvironmentContext.NOW,
            EnvironmentContext.TIME_OFFSET,
            EnvironmentValueName.USER
        );
    }

    // setEnvironmentValue..............................................................................................

    @Test
    public void testSetEnvironmentValueWithLocale() {
        this.setLocaleAndCheck(
            this.createContext(),
            DIFFERENT_LOCALE
        );
    }

    @Test
    public void testSetEnvironmentValueWithUser() {
        this.setUserAndCheck(
            this.createContext(),
            DIFFERENT_USER
        );
    }

    // hashCode/equals..................................................................................................

    @Test
    public void testEqualsDifferentPrefix() {
        this.checkNotEquals(
            EnvironmentContextSharedPrefixed.with(
                EnvironmentValueName.with(
                    "prefix1.",
                    Void.class
                ),
                CONTEXT
            ),
            EnvironmentContextSharedPrefixed.with(
                EnvironmentValueName.with(
                    "prefix2.",
                    Void.class
                ),
                CONTEXT
            )
        );
    }

    @Test
    public void testEqualsDifferentContext() {
        this.checkNotEquals(
            EnvironmentContextSharedPrefixed.with(
                PREFIX,
                EnvironmentContexts.properties(
                    Properties.EMPTY,
                    CONTEXT
                )
            )
        );
    }

    @Override
    public EnvironmentContextSharedPrefixed createObject() {
        return this.createContext();
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createContext(),
            "{charset=\"UTF-8\", currency=\"AUD\", indentation=\"  \", key111=value111, lineEnding=\"\\n\", locale=en_AU, timeOffset=Z, user=user123@example.com}"
        );
    }

    // TreePrintable....................................................................................................

    @Test
    public void testPrintTree() {
        this.treePrintAndCheck(
            this.createContext(),
            "EnvironmentContextSharedPrefixed\n" +
                "  prefix\n" +
                "    prefix111.\n" +
                "  environmentContext\n" +
                "    EnvironmentContextSharedProperties\n" +
                "      EnvironmentContextSharedMap\n" +
                "        charset\n" +
                "          UTF-8 (sun.nio.cs.UTF_8)\n" +
                "        currency\n" +
                "          AUD (java.util.Currency)\n" +
                "        indentation\n" +
                "          \"  \" (walkingkooka.text.Indentation)\n" +
                "        lineEnding\n" +
                "          \"\\n\"\n" +
                "        locale\n" +
                "          en_AU (java.util.Locale)\n" +
                "        now\n" +
                "          1999-12-31T12:58:59 (java.time.LocalDateTime)\n" +
                "        timeOffset\n" +
                "          Z (java.time.ZoneOffset)\n" +
                "        user\n" +
                "          user123@example.com (walkingkooka.net.email.EmailAddress)\n" +
                "      properties\n" +
                "        key111=value111\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<EnvironmentContextSharedPrefixed> type() {
        return EnvironmentContextSharedPrefixed.class;
    }
}
