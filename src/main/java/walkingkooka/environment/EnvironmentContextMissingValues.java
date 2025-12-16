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

import java.util.Set;

/**
 * Used to aggregate that all the required properties are present, tracking those that are missing.
 * This is used only by {@link EnvironmentContext} methods such as getting a {@link walkingkooka.convert.Converter}.
 */
public final class EnvironmentContextMissingValues implements UsesToStringBuilder {

    static EnvironmentContextMissingValues with(final EnvironmentContext context) {
        return new EnvironmentContextMissingValues(context);
    }

    private EnvironmentContextMissingValues(final EnvironmentContext context) {
        super();
        this.context = context;
    }

    public <T> T getOrNull(final EnvironmentValueName<T> environmentValueName) {
        return this.context.environmentValue(environmentValueName)
            .orElseGet(() -> this.addMissing(environmentValueName));
    }

    final EnvironmentContext context;

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
    public String toString() {
        return ToStringBuilder.buildFrom(this);
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    public void buildToString(final ToStringBuilder toStringBuilder) {
        toStringBuilder.label("missing")
            .value(this.missing)
            .label("environmentContext")
            .value(this.context);
    }
}
