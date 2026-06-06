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

import walkingkooka.Cast;
import walkingkooka.ToStringBuilder;
import walkingkooka.UsesToStringBuilder;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * The event payload used by {@link EnvironmentWatchers}.
 */
final class EnvironmentWatchersEvent<T> implements Consumer<EnvironmentWatcher>,
    UsesToStringBuilder {

    static <T> EnvironmentWatchersEvent<T> with(final Optional<EnvironmentValueNameAndValue<T>> oldValue,
                                                final Optional<EnvironmentValueNameAndValue<T>> newValue) {
        return new EnvironmentWatchersEvent<>(
            Objects.requireNonNull(oldValue, "oldValue"),
            Objects.requireNonNull(newValue, "newValue")
        );
    }

    private EnvironmentWatchersEvent(final Optional<EnvironmentValueNameAndValue<T>> oldValue,
                                     final Optional<EnvironmentValueNameAndValue<T>> newValue) {
        super();
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    // Consumer<EnvironmentWatcher>................................................................................

    @Override
    public void accept(final EnvironmentWatcher watcher) {
        watcher.onEnvironmentValueChange(
            Cast.to(this.oldValue),
            Cast.to(this.newValue)
        );
    }

    private final Optional<EnvironmentValueNameAndValue<T>> oldValue;
    private final Optional<EnvironmentValueNameAndValue<T>> newValue;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return ToStringBuilder.buildFrom(this);
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    public void buildToString(final ToStringBuilder b) {
        b.value(this.oldValue);
        b.value(this.newValue);
    }
}
