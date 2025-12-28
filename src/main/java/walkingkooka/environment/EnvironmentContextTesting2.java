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

/*
 * Copyright 2019 Miroslav Pokorny (github.com/mP1)
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
import walkingkooka.net.email.EmailAddress;
import walkingkooka.reflect.FieldAttributes;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.CaseKind;
import walkingkooka.text.LineEnding;

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
    ContextTesting<C> {

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
            EnvironmentContexts.empty(
                before.lineEnding(),
                before.locale(),
                before, // HasNow
                before.user()
            )
        );

        final EnvironmentContext after = before.setEnvironmentContext(environmentContext);

        assertNotSame(
            before,
            after
        );
    }

    // setLineEnding....................................................................................................

    @Test
    default void testSetLineEndingWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext()
                .setLineEnding(null)
        );
    }

    @Test
    default void testSetLineEndingWithDifferentAndWatcher() {
        final C context = this.createContext();

        LineEnding lineEnding = LineEnding.CRNL;
        if (context.lineEnding().equals(lineEnding)) {
            lineEnding = LineEnding.CR;
        }

        final LineEnding oldLineEnding = context.lineEnding();
        final LineEnding newLineEnding = lineEnding;

        final AtomicBoolean fired = new AtomicBoolean();

        context.addEventValueWatcher(
            (n, ov, nv) -> {
                checkEquals(EnvironmentContext.LINE_ENDING, n, "EnvironmentValueName");
                checkEquals(Optional.of(oldLineEnding), ov, "oldValue");
                checkEquals(Optional.of(newLineEnding), nv, "newValue");

                fired.set(true);
            }
        );

        this.setLineEndingAndCheck(
            context,
            lineEnding
        );

        this.checkEquals(
            true,
            fired.get()
        );
    }
    
    // setLocale........................................................................................................

    @Test
    default void testSetLocaleWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext().setLocale(null)
        );
    }

    @Test
    default void testSetLocaleWithDifferent() {
        final C context = this.createContext();

        Locale locale = Locale.FRENCH;
        if(context.locale().equals(locale)) {
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

        Locale locale = Locale.FRENCH;
        if (context.locale().equals(locale)) {
            locale = Locale.GERMAN;
        }

        final Locale oldLocale = context.locale();
        final Locale newLocale = locale;

        final AtomicBoolean fired = new AtomicBoolean();

        context.addEventValueWatcher(
            (n, ov, nv) -> {
                checkEquals(EnvironmentContext.LOCALE, n, "EnvironmentValueName");
                checkEquals(Optional.of(oldLocale), ov, "oldValue");
                checkEquals(Optional.of(newLocale), nv, "newValue");

                fired.set(true);
            }
        );

        this.setLocaleAndCheck(
            context,
            locale
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
    default void testEnvironmentValueLocaleEqualsLocale() {
        final C context = this.createContext();

        this.environmentValueAndCheck(
            context,
            EnvironmentContext.LOCALE,
            context.locale()
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

        Optional<EmailAddress> user = Optional.of(
            EmailAddress.parse("different@example.com")
        );
        if (context.user().equals(user)) {
            user = Optional.of(
                EmailAddress.parse("different2@example.com")
            );
        }

        final Optional<EmailAddress> oldUser = context.user();
        final Optional<EmailAddress> newUser = user;

        final AtomicBoolean fired = new AtomicBoolean();

        context.addEventValueWatcher(
            (n, ov, nv) -> {
                checkEquals(EnvironmentContext.USER, n, "EnvironmentValueName");
                checkEquals(oldUser, ov, "oldValue");
                checkEquals(newUser, nv, "newValue");

                fired.set(true);
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
    
    // addEventValueWatcher.............................................................................................

    @Test
    default void testAddEventValueWatcherWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext()
                .addEventValueWatcher(null)
        );
    }

    // addEventValueWatcherOnce.........................................................................................

    @Test
    default void testAddEventValueWatcherOnceWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createContext()
                .addEventValueWatcherOnce(null)
        );
    }
}
