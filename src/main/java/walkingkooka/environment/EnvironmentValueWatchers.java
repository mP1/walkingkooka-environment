package walkingkooka.environment;


import walkingkooka.watch.Watchers;

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

        return this.watchers.addOnce(
            (e) -> e.accept(watcher)
        );
    }

    /**
     * Note the event is only fired if the old and new values are different.
     */
    @Override
    public void onEnvironmentValueChange(final EnvironmentValueName<?> name,
                                         final Optional<?> oldValue,
                                         final Optional<?> newValue) {
        final EnvironmentValueWatchersEvent event = EnvironmentValueWatchersEvent.with(
            name,
            oldValue,
            newValue
        );

        if (false == Objects.equals(oldValue, newValue)) {
            this.watchers.accept(event);
        }
    }

    private final Watchers<EnvironmentValueWatchersEvent> watchers = Watchers.create();

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.watchers.toString();
    }
}
