package walkingkooka.environment.convert;


import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.convert.ConverterTesting2;
import walkingkooka.currency.CurrencyContextTesting;
import walkingkooka.environment.Environment;
import walkingkooka.environment.EnvironmentValueName;

public final class EnvironmentConverterToEnvironmentTest implements ConverterTesting2<EnvironmentConverterToEnvironment<EnvironmentConverterContext>, EnvironmentConverterContext>,
    CurrencyContextTesting {

    @Test
    public void testConvertStringToEnvironmentFails() {
        this.convertFails(
            "",
            Environment.class
        );
    }

    @Test
    public void testConvertNullToEnvironment() {
        this.convertAndCheck(
            null,
            Environment.class
        );
    }

    @Test
    public void testConvertHasEnvironmentToEnvironment() {
        final Environment environment = Environment.empty()
            .set(
                EnvironmentValueName.CURRENCY,
                CURRENCY
            );

        this.convertAndCheck(
            environment
        );
    }

    @Override
    public EnvironmentConverterToEnvironment<EnvironmentConverterContext> createConverter() {
        return EnvironmentConverterToEnvironment.instance();
    }

    @Override
    public EnvironmentConverterContext createContext() {
        return EnvironmentConverterContexts.fake();
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createConverter(),
            "to Environment"
        );
    }

    // class............................................................................................................

    @Override
    public Class<EnvironmentConverterToEnvironment<EnvironmentConverterContext>> type() {
        return Cast.to(EnvironmentConverterToEnvironment.class);
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
