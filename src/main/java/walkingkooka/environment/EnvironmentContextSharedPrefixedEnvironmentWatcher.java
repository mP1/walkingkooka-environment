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

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link EnvironmentWatcher} that handles fixing the {@link EnvironmentValueName} before firing to the target.
 */
final class EnvironmentContextSharedPrefixedEnvironmentWatcher implements EnvironmentWatcher {

    static EnvironmentContextSharedPrefixedEnvironmentWatcher with(final String prefix,
                                                                   final EnvironmentWatcher watcher) {
        return new EnvironmentContextSharedPrefixedEnvironmentWatcher(
            prefix,
            Objects.requireNonNull(watcher, "watcher")
        );
    }

    private EnvironmentContextSharedPrefixedEnvironmentWatcher(final String prefix,
                                                               final EnvironmentWatcher watcher) {
        this.prefix = prefix;
        this.watcher = watcher;
    }

    @Override
    public void onEnvironmentValueChange(final Optional<EnvironmentValueNameAndValue<?>> oldValue,
                                         final Optional<EnvironmentValueNameAndValue<?>> newValue) {
        this.watcher.onEnvironmentValueChange(
            this.maybePrefixName(oldValue),
            this.maybePrefixName(newValue)
        );
    }

    private Optional<EnvironmentValueNameAndValue<?>> maybePrefixName(final Optional<EnvironmentValueNameAndValue<?>> value) {
        return value.map(
            nv -> {
                EnvironmentValueName<?> name = nv.name();

                if(name.equals(EnvironmentContext.CURRENCY) || name.equals(EnvironmentContext.INDENTATION) || name.equals(EnvironmentContext.LINE_ENDING) || name.equals(EnvironmentContext.LOCALE) || name.equals(EnvironmentContext.TIME_OFFSET) || name.equals(EnvironmentContext.USER)) {
                    // nop
                } else {
                    name = EnvironmentValueName.with(
                        this.prefix + name.value(),
                        name.type()
                    );
                }

                return nv.setName(
                    Cast.to(name)
                );
            }
        );
    }

    private final String prefix;
    private final EnvironmentWatcher watcher;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.watcher.toString();
    }
}
