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
import walkingkooka.ContextTesting;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.logging.LoggingContextTesting2;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.reflect.FieldAttributes;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CaseKind;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public interface EnvironmentContextTesting2<C extends EnvironmentContext> extends EnvironmentContextTesting,
    CanParseEnvironmentValueNameTesting2<C>,
    ContextTesting<C>,
    HasEnvironmentContextTesting,
    LoggingContextTesting2<C> {

    // constants........................................................................................................

    @Test
    default void testEnvironmentValueNameConstantsCamelCase() throws Exception {
        final Set<EnvironmentValueName<?>> not = Sets.ordered();

        for (final Field field : this.type().getDeclaredFields()) {
            if (JavaVisibility.PUBLIC == JavaVisibility.of(field) && FieldAttributes.STATIC.is(field) && field.getType() == EnvironmentValueName.class) {
                final EnvironmentValueName<?> environmentValueName = (EnvironmentValueName<?>) field.get(null);
                final String name = environmentValueName.value();

                boolean all = false;
                for (final char c : name.toCharArray()) {
                    all = Character.isLetterOrDigit(c) && Character.isLowerCase(c);
                    if (false == all) {
                        break;
                    }
                }

                if (false == all) {
                    if (name.equals(CaseKind.KEBAB.change(name, CaseKind.CAMEL))) {
                        not.add(environmentValueName);
                    } else {
                        if (name.equals(CaseKind.SNAKE.change(name, CaseKind.CAMEL))) {
                            not.add(environmentValueName);
                        } else {
                            if (name.equals(CaseKind.CAMEL.change(name, CaseKind.TITLE))) {
                                not.add(environmentValueName);
                            }
                        }
                    }
                }
            }
        }

        this.checkEquals(
            Sets.empty(),
            not
        );
    }

    // cloneEnvironment.................................................................................................

    default void getAllEnvironmentValueAndCheck(final EnvironmentContext context,
                                                final EnvironmentContext expected) {
        this.checkEquals(
            this.values(context),
            this.values(expected)
        );
    }

    private Map<EnvironmentValueName<?>, Object> values(final EnvironmentContext context) {
        final Map<EnvironmentValueName<?>, Object> values = Maps.ordered();

        for (final EnvironmentValueName<?> name : context.environmentValueNames()) {
            values.put(
                name,
                context.environmentValue(name)
                    .orElse(null)
            );
        }

        return values;
    }

    // setEnvironmentContext............................................................................................

    @Test
    default void testSetEnvironmentContextWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext()
                .setEnvironmentContext(null)
        );
    }

    @Test
    default void testSetEnvironmentContextWithEqualEnvironmentContext() {
        final C before = this.createContext();

        final EnvironmentContext environmentContext = EnvironmentContexts.map(
            before, // CanLog
            before.charset(),
            before.currency(),
            before.indentation(),
            before.lineEnding(),
            before.locale(),
            before.loggingLevel(),
            before, // HasNow
            before.user()
        );

        final EnvironmentContext after = before.setEnvironmentContext(environmentContext);

        assertNotSame(
            before,
            after
        );
    }

    // charset..........................................................................................................

    @Test
    default void testCharset() {
        this.charsetAndCheck(
            this.createContext(),
            CHARSET
        );
    }

    @Test
    default void testSetCharset() {
        this.setCharsetAndCheck(
            this.createContext(),
            DIFFERENT_CHARSET
        );
    }

    // currency.........................................................................................................

    @Test
    default void testCurrency() {
        this.currencyAndCheck(
            this.createContext(),
            CURRENCY
        );
    }

    @Test
    default void testSetCurrencyWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext()
                .setCurrency(null)
        );
    }

    @Test
    default void testSetCurrency() {
        this.setCurrencyAndCheck(
            this.createContext(),
            DIFFERENT_CURRENCY
        );
    }

    @Test
    default void testSetCurrencyWithDifferentAndWatcher() {
        final C context = this.createContext();

        final AtomicBoolean fired = new AtomicBoolean();

        context.addEnvironmentWatcher(
            new EnvironmentWatcher() {
                @Override
                public void onValueChange(final Optional<EnvironmentValueNameAndValue<?>> oldValue,
                                          final Optional<EnvironmentValueNameAndValue<?>> newValue) {
                    checkEquals(
                        Optional.of(
                            EnvironmentValueName.CURRENCY.setValue(CURRENCY)
                        ),
                        oldValue,
                        "oldValue"
                    );
                    checkEquals(
                        Optional.of(
                            EnvironmentValueName.CURRENCY.setValue(DIFFERENT_CURRENCY)
                        ),
                        newValue,
                        "newValue"
                    );

                    fired.set(true);
                }
            }
        );

        this.setCurrencyAndCheck(
            context,
            DIFFERENT_CURRENCY
        );

        this.checkEquals(
            true,
            fired.get()
        );
    }

    // setIndentation...................................................................................................

    @Test
    default void testIndentation() {
        this.indentationAndCheck(
            this.createContext(),
            INDENTATION
        );
    }

    @Test
    default void testSetIndentationWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext()
                .setIndentation(null)
        );
    }

    @Test
    default void testSetIndentation() {
        this.setIndentationAndCheck(
            this.createContext(),
            DIFFERENT_INDENTATION
        );
    }

    @Test
    default void testSetIndentationWithDifferentAndWatcher() {
        final C context = this.createContext();

        final AtomicBoolean fired = new AtomicBoolean();

        context.addEnvironmentWatcher(
            new EnvironmentWatcher() {
                @Override
                public void onValueChange(final Optional<EnvironmentValueNameAndValue<?>> oldValue,
                                          final Optional<EnvironmentValueNameAndValue<?>> newValue) {
                    checkEquals(
                        Optional.of(
                            EnvironmentValueName.INDENTATION.setValue(INDENTATION)
                        ),
                        oldValue,
                        "oldValue"
                    );
                    checkEquals(
                        Optional.of(
                            EnvironmentValueName.INDENTATION.setValue(DIFFERENT_INDENTATION)
                        ),
                        newValue,
                        "newValue"
                    );

                    fired.set(true);
                }
            }
        );

        this.setIndentationAndCheck(
            context,
            DIFFERENT_INDENTATION
        );

        this.checkEquals(
            true,
            fired.get()
        );
    }

    // lineEnding.......................................................................................................

    @Test
    default void testLineEnding() {
        this.lineEndingAndCheck(
            this.createContext(),
            LINE_ENDING
        );
    }

    @Test
    default void testSetLineEndingWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext()
                .setLineEnding(null)
        );
    }

    @Test
    default void testSetLineEnding() {
        this.setLineEndingAndCheck(
            this.createContext(),
            DIFFERENT_LINE_ENDING
        );
    }

    @Test
    default void testSetLineEndingWithDifferentAndWatcher() {
        final C context = this.createContext();

        final AtomicBoolean fired = new AtomicBoolean();

        context.addEnvironmentWatcher(
            new EnvironmentWatcher() {
                @Override
                public void onValueChange(final Optional<EnvironmentValueNameAndValue<?>> oldValue,
                                          final Optional<EnvironmentValueNameAndValue<?>> newValue) {
                    checkEquals(
                        Optional.of(
                            EnvironmentValueName.LINE_ENDING.setValue(LINE_ENDING)
                        ),
                        oldValue,
                        "oldValue"
                    );
                    checkEquals(
                        Optional.of(
                            EnvironmentValueName.LINE_ENDING.setValue(DIFFERENT_LINE_ENDING)
                        ),
                        newValue,
                        "newValue"
                    );

                    fired.set(true);
                }
            }
        );

        this.setLineEndingAndCheck(
            context,
            DIFFERENT_LINE_ENDING
        );

        this.checkEquals(
            true,
            fired.get()
        );
    }

    // locale...........................................................................................................

    @Test
    default void testSetLocaleWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext()
                .setLocale(null)
        );
    }

    @Test
    default void testSetLocaleWithDifferent() {
        final C context = this.createContext();

        Locale locale = Locale.FRENCH;
        if (context.locale().equals(locale)) {
            locale = Locale.GERMAN;
        }

        this.setLocaleAndCheck(
            context,
            locale
        );
    }

    @Test
    default void testSetLocaleWithDifferentAndWatcher() {
        final C context = this.createContext();

        final AtomicBoolean fired = new AtomicBoolean();

        context.addEnvironmentWatcher(
            new EnvironmentWatcher() {
                @Override
                public void onValueChange(final Optional<EnvironmentValueNameAndValue<?>> oldValue,
                                          final Optional<EnvironmentValueNameAndValue<?>> newValue) {
                    checkEquals(
                        Optional.of(
                            EnvironmentValueName.LOCALE.setValue(LOCALE)
                        ),
                        oldValue,
                        "oldValue"
                    );
                    checkEquals(
                        Optional.of(
                            EnvironmentValueName.LOCALE.setValue(DIFFERENT_LOCALE)
                        ),
                        newValue,
                        "newValue"
                    );

                    fired.set(true);
                }
            }
        );

        this.setLocaleAndCheck(
            context,
            DIFFERENT_LOCALE
        );

        this.checkEquals(
            true,
            fired.get()
        );
    }

    // loggingLevel.....................................................................................................

    @Test
    default void testLoggingLevel() {
        this.loggingLevelAndCheck(
            this.createContext(),
            LOGGING_LEVEL
        );
    }

    @Test
    default void testSetLoggingLevelWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext()
                .setLoggingLevel(null)
        );
    }

    @Test
    default void testSetLoggingLevel() {
        this.setLoggingLevelAndCheck(
            this.createContext(),
            DIFFERENT_LOGGING_LEVEL
        );
    }

    @Test
    default void testSetLoggingLevelWithDifferentAndWatcher() {
        final C context = this.createContext();

        final AtomicBoolean fired = new AtomicBoolean();

        context.addEnvironmentWatcher(
            new EnvironmentWatcher() {
                @Override
                public void onValueChange(final Optional<EnvironmentValueNameAndValue<?>> oldValue,
                                          final Optional<EnvironmentValueNameAndValue<?>> newValue) {
                    checkEquals(
                        Optional.of(
                            EnvironmentValueName.LOGGING_LEVEL.setValue(LOGGING_LEVEL)
                        ),
                        oldValue,
                        "oldValue"
                    );
                    checkEquals(
                        Optional.of(
                            EnvironmentValueName.LOGGING_LEVEL.setValue(DIFFERENT_LOGGING_LEVEL)
                        ),
                        newValue,
                        "newValue"
                    );

                    fired.set(true);
                }
            }
        );

        this.setLoggingLevelAndCheck(
            context,
            DIFFERENT_LOGGING_LEVEL
        );

        this.checkEquals(
            true,
            fired.get()
        );
    }

    // timeOffset.......................................................................................................

    @Test
    default void testSetTimeOffsetWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext()
                .setTimeOffset(null)
        );
    }

    @Test
    default void testSetTimeOffsetWithDifferent() {
        this.setTimeOffsetAndCheck(
            this.createContext(),
            DIFFERENT_TIME_OFFSET
        );
    }

    @Test
    default void testSetTimeOffsetWithDifferentAndWatcher() {
        final C context = this.createContext();

        final AtomicBoolean fired = new AtomicBoolean();

        context.addEnvironmentWatcher(
            new EnvironmentWatcher() {
                @Override
                public void onValueChange(final Optional<EnvironmentValueNameAndValue<?>> oldValue,
                                          final Optional<EnvironmentValueNameAndValue<?>> newValue) {
                    checkEquals(
                        Optional.of(
                            EnvironmentValueName.TIME_OFFSET.setValue(TIME_OFFSET)
                        ),
                        oldValue,
                        "oldValue"
                    );
                    checkEquals(
                        Optional.of(
                            EnvironmentValueName.TIME_OFFSET.setValue(DIFFERENT_TIME_OFFSET)
                        ),
                        newValue,
                        "newValue"
                    );

                    fired.set(true);
                }
            }
        );

        this.setTimeOffsetAndCheck(
            context,
            DIFFERENT_TIME_OFFSET
        );

        this.checkEquals(
            true,
            fired.get()
        );
    }

    // user.............................................................................................................

    @Test
    default void testUserNotNull() {
        this.checkNotEquals(
            null,
            this.createContext().user()
        );
    }

    default void userAndCheck() {
        this.userAndCheck(
            this.createContext()
        );
    }

    default void userAndCheck(final EmailAddress expected) {
        this.userAndCheck(
            this.createContext(),
            expected
        );
    }

    default void userAndCheck(final Optional<EmailAddress> expected) {
        this.userAndCheck(
            this.createContext(),
            expected
        );
    }

    // setUser..........................................................................................................

    @Test
    default void testSetUserWithDifferentAndWatcher() {
        final C context = this.createContext();

        Optional<EmailAddress> user = Optional.of(DIFFERENT_USER);
        if (context.user().equals(user)) {
            user = Optional.of(
                EmailAddress.parse("different2@example.com")
            );
        }

        final Optional<EmailAddress> oldUser = context.user();
        final Optional<EmailAddress> newUser = user;

        final AtomicBoolean fired = new AtomicBoolean();

        context.addEnvironmentWatcher(
            new EnvironmentWatcher() {
                @Override
                public void onValueChange(final Optional<EnvironmentValueNameAndValue<?>> oldValue,
                                          final Optional<EnvironmentValueNameAndValue<?>> newValue) {
                    checkEquals(
                        oldUser.map(
                            EnvironmentValueName.USER::setValue
                        ),
                        oldValue,
                        "oldValue"
                    );
                    checkEquals(
                        newUser.map(
                            EnvironmentValueName.USER::setValue
                        ),
                        newValue,
                        "newValue"
                    );

                    fired.set(true);
                }
            }
        );

        this.setUserAndCheck(
            context,
            user
        );

        this.checkEquals(
            true,
            fired.get()
        );
    }

    // environmentValue.................................................................................................

    @Test
    default void testEnvironmentValueWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext().environmentValue(null)
        );
    }

    @Test
    default void testEnvironmentValueLineEndingEqualsLineEnding() {
        final C context = this.createContext();

        this.environmentValueAndCheck(
            context,
            EnvironmentContext.LINE_ENDING,
            context.lineEnding()
        );
    }

    @Test
    default void testEnvironmentValueLocaleEqualsLocale() {
        final C context = this.createContext();

        this.environmentValueAndCheck(
            context,
            EnvironmentContext.LOCALE,
            context.locale()
        );
    }

    @Test
    default void testEnvironmentValueNowEqualsNow() {
        final C context = this.createContext();

        this.environmentValueAndCheck(
            context,
            EnvironmentContext.NOW,
            context.now()
        );
    }

    @Test
    default void testEnvironmentValueUserEqualsUser() {
        final C context = this.createContext();

        this.environmentValueAndCheck(
            context,
            EnvironmentContext.USER,
            context.user()
        );
    }

    default <T> void environmentValueAndCheck(final EnvironmentValueName<T> name) {
        this.environmentValueAndCheck(
            this.createContext(),
            name
        );
    }

    default <T> void environmentValueAndCheck(final EnvironmentValueName<T> name,
                                              final T expected) {
        this.environmentValueAndCheck(
            name,
            Optional.of(expected)
        );
    }

    default <T> void environmentValueAndCheck(final EnvironmentValueName<T> name,
                                              final Optional<T> expected) {
        this.environmentValueAndCheck(
            this.createContext(),
            name,
            expected
        );
    }

    // environmentValueNames............................................................................................

    default void environmentValueNamesAndCheck(final EnvironmentValueName<?>... expected) {
        this.environmentValueNamesAndCheck(
            Sets.of(expected)
        );
    }

    default void environmentValueNamesAndCheck(final Set<EnvironmentValueName<?>> expected) {
        this.environmentValueNamesAndCheck(
            this.createContext(),
            expected
        );
    }

    // setEnvironmentValue..............................................................................................

    @Test
    default void testSetEnvironmentValueWithNullNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext()
                .setEnvironmentValue(
                    null,
                    this
                )
        );
    }

    @Test
    default void testSetEnvironmentValueWithNullValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext()
                .setEnvironmentValue(
                    EnvironmentValueName.with(
                        "Hello",
                        String.class
                    ),
                    null
                )
        );
    }

    @Test
    default void testSetEnvironmentValueWithNowFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createContext()
                .setEnvironmentValue(
                    EnvironmentContext.NOW,
                    LocalDateTime.now()
                )
        );
    }

    // removeEnvironmentValue...........................................................................................

    @Test
    default void testRemoveEnvironmentValueWithNullNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext()
                .removeEnvironmentValue(
                    null
                )
        );
    }

    @Test
    default void testRemoveEnvironmentValueWithNowFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createContext()
                .removeEnvironmentValue(
                    EnvironmentContext.NOW
                )
        );
    }

    // addEnvironmentWatcher............................................................................................

    @Test
    default void testAddEnvironmentWatcherWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext()
                .addEnvironmentWatcher(null)
        );
    }

    // addEnvironmentWatcherOnce........................................................................................

    @Test
    default void testAddEnvironmentWatcherOnceWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext()
                .addEnvironmentWatcherOnce(null)
        );
    }

    // copyEnvironment..................................................................................................

    @Test
    default void testCopyEnvironmentWithNullEnvironmentFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext()
                .copyEnvironment(null)
        );
    }

    // environmentContextMissingValues..................................................................................

    @Test
    default void testEnvironmentContextMissingValues() {
        final C context = this.createContext();

        this.checkEquals(
            context.environmentContextMissingValues(),
            EnvironmentContextMissingValues.with(context)
        );
    }

    // CanParseEnvironmentValueNameTesting2.............................................................................

    @Override
    default C createCanParseEnvironmentValueName() {
        return this.createContext();
    }

    // HasEnvironmentContext............................................................................................

    @Test
    default void testEnvironmentContext() {
        final C context = this.createContext();

        this.environmentContextAndCheck(
            context,
            context
        );
    }

    // setEnvironmentContext............................................................................................

    default void setEnvironmentContextAndCheck(final EnvironmentContext set,
                                               final EnvironmentContext expected) {
        this.setEnvironmentContextAndCheck(
            this.createContext(),
            set,
            expected
        );
    }
}
