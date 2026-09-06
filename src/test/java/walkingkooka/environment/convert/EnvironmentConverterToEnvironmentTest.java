package walkingkooka.environment.convert;


import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.currency.CurrencyContextTesting;
import walkingkooka.environment.Environment;
import walkingkooka.environment.EnvironmentValueName;

public final class EnvironmentConverterToEnvironmentTest extends EnvironmentConverterTestCase<EnvironmentConverterToEnvironment<FakeEnvironmentConverterContext>>
    implements CurrencyContextTesting {

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
    public EnvironmentConverterToEnvironment<FakeEnvironmentConverterContext> createConverter() {
        return EnvironmentConverterToEnvironment.instance();
    }

    @Override
    public FakeEnvironmentConverterContext createContext() {
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
    public Class<EnvironmentConverterToEnvironment<FakeEnvironmentConverterContext>> type() {
        return Cast.to(EnvironmentConverterToEnvironment.class);
    }
}
