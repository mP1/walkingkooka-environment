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

import walkingkooka.ToStringBuilder;
import walkingkooka.UsesToStringBuilder;
import walkingkooka.collect.set.SortedSets;

import java.util.Objects;
import java.util.Set;

/**
 * Used to aggregate that all the required properties are present, tracking those that are missing.
 * This is used only by {@link EnvironmentLike} methods such as getting a {@link walkingkooka.convert.Converter}.
 */
public final class EnvironmentLikeMissingValues implements UsesToStringBuilder {

    static EnvironmentLikeMissingValues with(final EnvironmentLike environmentLike) {
        return new EnvironmentLikeMissingValues(environmentLike);
    }

    private EnvironmentLikeMissingValues(final EnvironmentLike environmentLike) {
        super();
        this.environmentLike = environmentLike;
    }

    public <T> T getOrNull(final EnvironmentValueName<T> environmentValueName) {
        return this.environmentLike.environmentValue(environmentValueName)
            .orElseGet(() -> this.addMissing(environmentValueName));
    }

    final EnvironmentLike environmentLike;

    private <T> T addMissing(final EnvironmentValueName<?> environmentValueName) {
        this.missing.add(environmentValueName);
        return null;
    }

    public void reportIfMissing() throws MissingEnvironmentValuesException {
        final Set<EnvironmentValueName<?>> missing = this.missing;
        if (false == missing.isEmpty()) {
            throw new MissingEnvironmentValuesException(missing);
        }
    }

    public void addMissing(final MissingEnvironmentValuesException missing) {
        this.missing.addAll(missing.environmentValueNames());
    }

    final Set<EnvironmentValueName<?>> missing = SortedSets.tree();

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
            this.missing,
            this.environmentLike
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            (other instanceof EnvironmentLikeMissingValues &&
                this.equals0((EnvironmentLikeMissingValues) other));
    }

    private boolean equals0(final EnvironmentLikeMissingValues other) {
        return this.missing.equals(other.missing) &&
            this.environmentLike.equals(other.environmentLike);
    }

    @Override
    public String toString() {
        return ToStringBuilder.buildFrom(this);
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    public void buildToString(final ToStringBuilder toStringBuilder) {
        toStringBuilder.label("missing")
            .value(this.missing)
            .label("environmentLike")
            .value(this.environmentLike);
    }
}
