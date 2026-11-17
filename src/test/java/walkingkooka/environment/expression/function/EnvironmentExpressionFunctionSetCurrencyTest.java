/*
 * Copyright 2025 Miroslav Pokorny (github.com/mP1)
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

package walkingkooka.environment.expression.function;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.currency.HasCurrencyTesting;
import walkingkooka.environment.expression.EnvironmentExpressionEvaluationContext;
import walkingkooka.environment.expression.FakeEnvironmentExpressionEvaluationContext;

import java.util.Currency;

public final class EnvironmentExpressionFunctionSetCurrencyTest extends EnvironmentExpressionFunctionTestCase<EnvironmentExpressionFunctionSetCurrency<EnvironmentExpressionEvaluationContext>, Void>
    implements HasCurrencyTesting,
    ToStringTesting {

    private final static Currency CURRENCY = Currency.getInstance("AUD");

    @Override
    public void testSetParametersSame() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testApplyWithCurrency() {
        final EnvironmentExpressionEvaluationContext context = this.createContext();

        final Currency currency = Currency.getInstance("NZD");
        this.applyAndCheck(
            EnvironmentExpressionFunctionSetCurrency.instance(),
            Lists.of(currency),
            context,
            null
        );

        this.currencyAndCheck(
            context,
            currency
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            EnvironmentExpressionFunctionSetCurrency.instance(),
            "setCurrency"
        );
    }

    @Override
    public EnvironmentExpressionFunctionSetCurrency createBiFunction() {
        return EnvironmentExpressionFunctionSetCurrency.instance();
    }

    @Override
    public EnvironmentExpressionEvaluationContext createContext() {
        return new FakeEnvironmentExpressionEvaluationContext() {
            @Override
            public Currency currency() {
                return this.currency;
            }

            @Override
            public void setCurrency(final Currency currency) {
                this.currency = currency;
            }

            private Currency currency = EnvironmentExpressionFunctionSetCurrencyTest.CURRENCY;
        };
    }

    @Override
    public Class<EnvironmentExpressionFunctionSetCurrency<EnvironmentExpressionEvaluationContext>> type() {
        return Cast.to(EnvironmentExpressionFunctionSetCurrency.class);
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }
}
