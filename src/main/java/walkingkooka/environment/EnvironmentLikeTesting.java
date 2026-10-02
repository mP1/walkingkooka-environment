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

import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface EnvironmentLikeTesting extends TreePrintableTesting {

    // environmentValue.................................................................................................

    default <T> void environmentValueAndCheck(final EnvironmentLike environmentLike,
                                              final EnvironmentValueName<T> name) {
        this.environmentValueAndCheck(
            environmentLike,
            name,
            Optional.empty()
        );
    }

    default <T> void environmentValueAndCheck(final EnvironmentLike environmentLike,
                                              final EnvironmentValueName<T> name,
                                              final T expected) {
        this.environmentValueAndCheck(
            environmentLike,
            name,
            Optional.of(expected)
        );
    }

    default <T> void environmentValueAndCheck(final EnvironmentLike environmentLike,
                                              final EnvironmentValueName<T> name,
                                              final Optional<T> expected) {
        this.checkEquals(
            expected,
            environmentLike.environmentValue(name),
            () -> "environmentValue " + name
        );
    }

    // environmentValueOrFail...........................................................................................

    default void environmentValueOrFailAndCheck(final EnvironmentLike environmentLike,
                                                final EnvironmentValueName<?> name,
                                                final MissingEnvironmentValueException expected) {
        final MissingEnvironmentValueException thrown = assertThrows(
            MissingEnvironmentValueException.class,
            () -> environmentLike.environmentValueOrFail(name)
        );

        this.checkEquals(
            expected,
            thrown
        );
    }
}
