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

import walkingkooka.text.CharSequences;

/**
 * A tuple that holds the proper or correct {@link EnvironmentValueName} and value so the name does not get lost.
 * The {@link #environmentValueName} is ignored in {@link #hashCode()} and {@link #equals(Object)}.
 */
final class EnvironmentContextSharedMapValue<T> {

    static <T> EnvironmentContextSharedMapValue<T> with(final EnvironmentValueName<T> environmentValueName,
                                                        final T value) {
        return new EnvironmentContextSharedMapValue<>(
            environmentValueName,
            value
        );
    }

    private EnvironmentContextSharedMapValue(final EnvironmentValueName<T> environmentValueName,
                                             final T value) {
        super();

        this.environmentValueName = environmentValueName;
        this.value = value;
    }

    EnvironmentValueName<?> environmentValueName;

    T value;

    EnvironmentContextSharedMapValue<T> copy() {
        return new EnvironmentContextSharedMapValue(
            this.environmentValueName,
            this.value
        );
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof EnvironmentContextSharedMapValue && this.equals0((EnvironmentContextSharedMapValue<?>) other);
    }

    private boolean equals0(final EnvironmentContextSharedMapValue<?> other) {
        return value.equals(other.value);
    }

    @Override
    public String toString() {
        return CharSequences.quoteIfChars(this.value)
            .toString();
    }
}
