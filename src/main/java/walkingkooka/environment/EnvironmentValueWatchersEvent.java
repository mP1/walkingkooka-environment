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

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * The event payload used by {@link EnvironmentValueWatchers}.
 */
final class EnvironmentValueWatchersEvent implements Consumer<EnvironmentValueWatcher>,
    UsesToStringBuilder {

    static EnvironmentValueWatchersEvent with(final EnvironmentValueName<?> name,
                                              final Optional<?> oldValue,
                                              final Optional<?> newValue) {
        return new EnvironmentValueWatchersEvent(
            Objects.requireNonNull(name, "name"),
            Objects.requireNonNull(oldValue, "oldValue"),
            Objects.requireNonNull(newValue, "newValue")
        );
    }

    private EnvironmentValueWatchersEvent(final EnvironmentValueName<?> name,
                                          final Optional<?> oldValue,
                                          final Optional<?> newValue) {
        this.name = name;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    // Consumer<EnvironmentValueWatcher>................................................................................

    @Override
    public void accept(final EnvironmentValueWatcher watcher) {
        watcher.onEnvironmentValueChange(
            this.name,
            this.oldValue,
            this.newValue
        );
    }

    private final EnvironmentValueName<?> name;
    private final Optional<?> oldValue;
    private final Optional<?> newValue;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return ToStringBuilder.buildFrom(this);
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    public void buildToString(final ToStringBuilder b) {
        b.value(this.name);
        b.value(this.oldValue);
        b.value(this.newValue);
    }
}
