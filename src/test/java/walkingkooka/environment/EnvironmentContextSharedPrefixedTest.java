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
import walkingkooka.datetime.HasNow;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.props.Properties;
import walkingkooka.props.PropertiesPath;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EnvironmentContextSharedPrefixedTest extends EnvironmentContextSharedTestCase<EnvironmentContextSharedPrefixed>
    implements HashCodeEqualsDefinedTesting2<EnvironmentContextSharedPrefixed>,
    ToStringTesting<EnvironmentContextSharedPrefixed> {

    private final static EnvironmentValueName<?> PREFIX = EnvironmentValueName.with(
        "prefix111.",
        Void.class
    );

    private final static Indentation INDENTATION = Indentation.SPACES4;

    private final static LineEnding LINE_ENDING = LineEnding.NL;

    private final static Locale LOCALE = Locale.FRENCH;

    private final static HasNow HAS_NOW = () -> LocalDateTime.of(
        1999,
        12,
        31,
        12,
        59
    );

    private final static Optional<EmailAddress> USER = Optional.of(
        EmailAddress.parse("user@example.com")
    );

    private final static EnvironmentContext CONTEXT = new FakeEnvironmentContext() {
        @Override
        public Set<EnvironmentValueName<?>> environmentValueNames() {
            return Sets.empty();
        }

        @Override
        public Optional<EmailAddress> user() {
            return EnvironmentContextSharedPrefixedTest.USER;
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
            context.locale()
        );
    }

    @Test
    public void testEnvironmentValueWithUser() {
        final EnvironmentContextSharedPrefixed context = this.createContext();

        this.environmentValueAndCheck(
            context,
            EnvironmentContext.USER,
            context.user()
        );
    }

    // setEnvironmentContext............................................................................................

    @Test
    public void testSetEnvironmentContext() {
        final EnvironmentContextSharedPrefixed context = this.createContext();

        final EnvironmentContext different = this.createContext();
        different.setLineEnding(LineEnding.CRNL);

        this.checkNotEquals(
            context,
            different
        );

        assertSame(
            different,
            context.setEnvironmentContext(different)
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

        final EnvironmentContext context = EnvironmentContexts.map(
            EnvironmentContexts.empty(
                INDENTATION,
                LINE_ENDING,
                LOCALE,
                HAS_NOW,
                EnvironmentContext.ANONYMOUS
            )
        );

        final EnvironmentContextSharedPrefixed environmentContextSharedPrefixed = EnvironmentContextSharedPrefixed.with(
            PREFIX,
            context
        );

        final EnvironmentValueName<String> name = EnvironmentValueName.with(
            "hello",
            String.class
        );
        final String value = "world";

        final Runnable remover = environmentContextSharedPrefixed.addEventValueWatcher(
            new EnvironmentValueWatcher() {
                @Override
                public void onEnvironmentValueChange(final EnvironmentValueName<?> n,
                                                     final Optional<?> oldValue,
                                                     final Optional<?> newValue) {
                    checkEquals(
                        EnvironmentValueName.with(
                            PREFIX + name.value(),
                            String.class
                        ),
                        n,
                        "name"
                    );
                    checkEquals(
                        Optional.empty(),
                        oldValue,
                        "oldValue"
                    );
                    checkEquals(
                        Optional.of(value),
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
                EnvironmentContexts.empty(
                    INDENTATION,
                    LINE_ENDING,
                    LOCALE,
                    HAS_NOW,
                    USER
                )
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
            EnvironmentContext.INDENTATION,
            EnvironmentValueName.LINE_ENDING,
            EnvironmentContext.LOCALE,
            EnvironmentContext.NOW,
            EnvironmentValueName.USER
        );
    }

    // setEnvironmentValue..............................................................................................

    @Test
    public void testSetEnvironmentValueWithLocale() {
        final EnvironmentContextSharedPrefixed context = this.createContext();

        final Locale locale = Locale.GERMAN;
        this.setEnvironmentValueAndCheck(
            context,
            EnvironmentValueName.LOCALE,
            locale
        );
    }

    @Test
    public void testSetEnvironmentValueWithUser() {
        final EnvironmentContextSharedPrefixed context = this.createContext();

        final EmailAddress user = EmailAddress.parse("different@example.com");
        this.setEnvironmentValueAndCheck(
            context,
            EnvironmentContext.USER,
            user
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
            "{indentation=\"    \", key111=value111, lineEnding=\"\\n\", locale=fr, user=user@example.com}"
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
                "      EmptyEnvironmentContext\n" +
                "        indentation\n" +
                "          \"    \" (walkingkooka.text.Indentation)\n" +
                "        lineEnding\n" +
                "          \"\\n\"\n" +
                "        locale\n" +
                "          fr (java.util.Locale)\n" +
                "        now\n" +
                "          1999-12-31T12:59 (java.time.LocalDateTime)\n" +
                "        user\n" +
                "          user@example.com (walkingkooka.net.email.EmailAddress)\n" +
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
