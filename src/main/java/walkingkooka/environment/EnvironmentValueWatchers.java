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


import walkingkooka.collect.list.Lists;
import walkingkooka.watch.Watchers;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A collection of {@link EnvironmentValueWatcher}. Note the event is only fired to watchers if the old and new values
 * are different.
 */
public final class EnvironmentValueWatchers implements EnvironmentValueWatcher {

    public static EnvironmentValueWatchers empty() {
        return new EnvironmentValueWatchers();
    }

    public Runnable add(final EnvironmentValueWatcher watcher) {
        Objects.requireNonNull(watcher, "watcher");

        return this.watchers.add(
            (e) -> e.accept(watcher)
        );
    }

    public Runnable addOnce(final EnvironmentValueWatcher watcher) {
        Objects.requireNonNull(watcher, "watcher");

        final Runnable remover = this.onceWatchers.addOnce(
            (e) -> e.accept(watcher)
        );;
        this.onceRemovers.add(remover);
        return remover;
    }

    /**
     * Note the event is only fired if the old and new values are different.
     */
    @Override
    public void onEnvironmentValueChange(final EnvironmentValueName<?> name,
                                         final Optional<?> oldValue,
                                         final Optional<?> newValue) {
        if (false == Objects.equals(oldValue, newValue)) {
            final EnvironmentValueWatchersEvent event = EnvironmentValueWatchersEvent.with(
                name,
                oldValue,
                newValue
            );

            try {
                this.onceWatchers.accept(event);
                this.watchers.accept(event);
            } finally {
                this.onceRemovers.forEach(Runnable::run);
                this.onceRemovers.clear();
            }
        }
    }

    private final Watchers<EnvironmentValueWatchersEvent> watchers = Watchers.create();

    private final Watchers<EnvironmentValueWatchersEvent> onceWatchers = Watchers.create();

    /**
     * Cant use Watchers#addOnce because that will remove the watcher during #onBegin
     * meaning events afterward will never be received because watcher is gone by then.
     */
    private final List<Runnable> onceRemovers = Lists.copyOnWrite();
    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.watchers.toString();
    }
}
