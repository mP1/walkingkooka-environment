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
 * A {@link EnvironmentValueWatcher} that handles fixing the {@link EnvironmentValueName} before firing to the target.
 */
final class PrefixedEnvironmentContextEnvironmentValueWatcher implements EnvironmentValueWatcher {

    static PrefixedEnvironmentContextEnvironmentValueWatcher with(final String prefix,
                                                                  final EnvironmentValueWatcher watcher) {
        return new PrefixedEnvironmentContextEnvironmentValueWatcher(
            prefix,
            Objects.requireNonNull(watcher, "watcher")
        );
    }

    private PrefixedEnvironmentContextEnvironmentValueWatcher(final String prefix,
                                                              final EnvironmentValueWatcher watcher) {
        this.prefix = prefix;
        this.watcher = watcher;
    }

    @Override
    public void onEnvironmentValueChange(final EnvironmentValueName<?> name,
                                         final Optional<?> oldValue,
                                         final Optional<?> newValue) {
        EnvironmentValueName<?> fireName;

        if(name.equals(EnvironmentContext.INDENTATION) || name.equals(EnvironmentContext.LINE_ENDING) || name.equals(EnvironmentContext.LOCALE) || name.equals(EnvironmentContext.USER)) {
            fireName = name;
        } else {
            fireName = EnvironmentValueName.with(
                this.prefix + name.value(),
                name.type()
            );
        }

        this.watcher.onEnvironmentValueChange(
            fireName,
            oldValue,
            newValue
        );
    }

    private final String prefix;
    private final EnvironmentValueWatcher watcher;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.watcher.toString();
    }
}
