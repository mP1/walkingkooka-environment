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
