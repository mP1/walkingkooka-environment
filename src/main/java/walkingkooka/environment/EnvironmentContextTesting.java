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

import walkingkooka.net.email.EmailAddress;
import walkingkooka.test.Testing;

import java.util.Optional;

public interface EnvironmentContextTesting extends Testing {

    default <T> void environmentValueAndCheck(final EnvironmentContext context,
                                              final EnvironmentValueName<T> name) {
        this.environmentValueAndCheck(
                context,
                name,
                Optional.empty()
        );
    }

    default <T> void environmentValueAndCheck(final EnvironmentContext context,
                                              final EnvironmentValueName<T> name,
                                              final T expected) {
        this.environmentValueAndCheck(
                context,
                name,
                Optional.of(expected)
        );
    }

    default <T> void environmentValueAndCheck(final EnvironmentContext context,
                                              final EnvironmentValueName<T> name,
                                              final Optional<T> expected) {
        this.checkEquals(
                expected,
                context.environmentValue(name),
                () -> "environmentValue " + name
        );
    }

    default void userAndCheck(final EnvironmentContext context) {
        this.userAndCheck(
                context,
                Optional.empty()
        );
    }

    default void userAndCheck(final EnvironmentContext context,
                              final EmailAddress expected) {
        this.userAndCheck(
                context,
                Optional.of(expected)
        );
    }

    default void userAndCheck(final EnvironmentContext context,
                              final Optional<EmailAddress> expected) {
        this.checkEquals(
                expected,
                context.user()
        );
    }
}
