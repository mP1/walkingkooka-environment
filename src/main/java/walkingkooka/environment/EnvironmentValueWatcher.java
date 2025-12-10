package walkingkooka.environment;


import java.util.Optional;

/**
 * A watcher that receives all {@link EnvironmentContext} value change events.
 */
public interface EnvironmentValueWatcher {

    void onEnvironmentValueChange(final EnvironmentValueName<?> name,
                                  final Optional<?> oldValue,
                                  final Optional<?> newValue);
}
