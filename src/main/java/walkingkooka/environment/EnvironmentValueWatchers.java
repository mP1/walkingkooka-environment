package walkingkooka.environment;


import walkingkooka.watch.Watchers;

import java.util.Optional;

/**
 * A collection of {@link EnvironmentValueWatcher}.
 */
public final class EnvironmentValueWatchers implements EnvironmentValueWatcher {

    public static EnvironmentValueWatchers empty() {
        return new EnvironmentValueWatchers();
    }

    public Runnable add(final EnvironmentValueWatcher watcher) {
        return this.watchers.add(
            (e) -> e.accept(watcher)
        );
    }

    public Runnable addOnce(final EnvironmentValueWatcher watcher) {
        return this.watchers.addOnce(
            (e) -> e.accept(watcher)
        );
    }

    @Override
    public void onEnvironmentValueChange(final EnvironmentValueName<?> name,
                                         final Optional<?> oldValue,
                                         final Optional<?> newValue) {
        this.watchers.accept(
            EnvironmentValueWatchersEvent.with(
                name,
                oldValue,
                newValue
            )
        );
    }

    private final Watchers<EnvironmentValueWatchersEvent> watchers = Watchers.create();

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.watchers.toString();
    }
}
