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

import walkingkooka.collect.set.Sets;
import walkingkooka.text.CharSequences;

import java.util.Objects;
import java.util.Set;

/**
 * The exception that should be thrown when one or more {@link EnvironmentValueName} are not found.
 */
public final class MissingEnvironmentValuesException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    MissingEnvironmentValuesException(final Set<EnvironmentValueName<?>> environmentValueNames) {
        super("Missing environment value(s): " +
            CharSequences.subSequence(
                Objects.requireNonNull(environmentValueNames, "environmentValueNames")
                    .toString(),
                1,
                -1
            )
        );
        this.environmentValueNames = Sets.immutable(environmentValueNames);
    }

    public Set<EnvironmentValueName<?>> environmentValueNames() {
        return this.environmentValueNames;
    }

    final Set<EnvironmentValueName<?>> environmentValueNames;

    // hashCode/equals..................................................................................................

    @Override
    public int hashCode() {
        return this.environmentValueNames.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof MissingEnvironmentValuesException && this.equals0((MissingEnvironmentValuesException) other);
    }

    private boolean equals0(final MissingEnvironmentValuesException other) {
        return this.environmentValueNames.equals(other.environmentValueNames);
    }
}
