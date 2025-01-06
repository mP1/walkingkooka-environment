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
import walkingkooka.collect.set.Sets;
import walkingkooka.net.email.EmailAddress;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface EnvironmentContextTesting2<C extends EnvironmentContext> extends EnvironmentContextTesting,
        ContextTesting<C> {

    @Test
    default void testEnvironmentValueWithNullFails() {
        assertThrows(
                NullPointerException.class,
                () -> this.createContext().environmentValue(null)
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
                this.createContext(),
                name,
                expected
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

    // user.............................................................................................................

    @Test
    default void testUser() {
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
}
