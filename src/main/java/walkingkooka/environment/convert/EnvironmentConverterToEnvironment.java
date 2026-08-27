package walkingkooka.environment.convert;

import walkingkooka.Cast;
import walkingkooka.convert.TryingShortCircuitingConverter;
import walkingkooka.environment.Environment;
import walkingkooka.environment.HasEnvironment;

/**
 * A Converter that converts any {@link HasEnvironment} into a {@link Environment}.
 */
final class EnvironmentConverterToEnvironment<C extends EnvironmentConverterContext> implements TryingShortCircuitingConverter<C> {

    /**
     * Type safe instance getter
     */
    static <C extends EnvironmentConverterContext> EnvironmentConverterToEnvironment<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static EnvironmentConverterToEnvironment<?> INSTANCE = new EnvironmentConverterToEnvironment<>();

    /**
     * Private to stop sub classing.
     */
    private EnvironmentConverterToEnvironment() {
        super();
    }

    @Override
    public boolean canConvert(final Object value,
                              final Class<?> type,
                              final C context) {
        return (null == value ||
            value instanceof HasEnvironment) &&
            Environment.class == type;
    }

    @Override
    public Object tryConvertOrFail(final Object value,
                                   final Class<?> type,
                                   final C context) {
        return null == value ?
            null :
            ((HasEnvironment) value).environment();
    }

    @Override
    public String toString() {
        return "to " + Environment.class.getSimpleName();
    }
}
