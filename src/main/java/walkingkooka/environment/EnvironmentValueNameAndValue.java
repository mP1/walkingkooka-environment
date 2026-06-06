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

import walkingkooka.HasValue;
import walkingkooka.naming.HasName;

import java.util.Objects;

/**
 * A pair that holds a {@link EnvironmentValueName} and value.
 */
public final class EnvironmentValueNameAndValue<T> implements HasName<EnvironmentValueName<T>>,
    HasValue<T> {

    public static <T> EnvironmentValueNameAndValue<T> with(final EnvironmentValueName<T> name,
                                                           final T value) {
        return new EnvironmentValueNameAndValue<>(
            Objects.requireNonNull(name, "name"),
            Objects.requireNonNull(value, "value")
        );
    }

    private EnvironmentValueNameAndValue(final EnvironmentValueName<T> name,
                                         final T value) {
        super();

        this.name = name;
        this.value = value;
    }

    // HasName..........................................................................................................

    @Override
    public EnvironmentValueName<T> name() {
        return this.name;
    }

    private final EnvironmentValueName<T> name;

    // HasValue.........................................................................................................

    @Override
    public T value() {
        return this.value;
    }

    private final T value;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
            this.name,
            this.value
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof EnvironmentValueNameAndValue && this.equals0((EnvironmentValueNameAndValue<?>) other);
    }

    private boolean equals0(final EnvironmentValueNameAndValue<?> other) {
        return this.name.equals(other.name) &&
            this.value.equals(other.value);
    }

    @Override
    public String toString() {
        return this.name + "=" + this.value;
    }
}
