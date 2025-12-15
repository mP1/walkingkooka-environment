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

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link EnvironmentValueWatcher} that routes each event to add/remove/update.
 */
public interface EnvironmentValueWatcher2 extends EnvironmentValueWatcher {

    @Override
    default void onEnvironmentValueChange(final EnvironmentValueName<?> name,
                                          final Optional<?> oldValue,
                                          final Optional<?> newValue) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(oldValue, "oldValue");
        Objects.requireNonNull(newValue, "newValue");

        final boolean oldEmpty = oldValue.isEmpty();
        final boolean newEmpty = newValue.isEmpty();

        if (oldEmpty) {
            this.onEnvironmentValueAdd(
                name,
                newValue.get()
            );
        } else {
            if (newEmpty) {
                this.onEnvironmentValueRemove(
                    name,
                    oldValue.get()
                );
            } else {
                this.onEnvironmentValueUpdate(
                    name,
                    oldValue.get(),
                    newValue.get()
                );
            }
        }
    }

    void onEnvironmentValueAdd(final EnvironmentValueName<?> name,
                               final Object newValue);

    void onEnvironmentValueRemove(final EnvironmentValueName<?> name,
                                  final Object oldValue);

    void onEnvironmentValueUpdate(final EnvironmentValueName<?> name,
                                  final Object oldValue,
                                  final Object newValue);
}
