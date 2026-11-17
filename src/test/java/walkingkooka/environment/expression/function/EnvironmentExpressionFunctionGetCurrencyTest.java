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
import walkingkooka.environment.expression.EnvironmentExpressionEvaluationContext;
import walkingkooka.environment.expression.FakeEnvironmentExpressionEvaluationContext;

import java.util.Currency;

public final class EnvironmentExpressionFunctionGetCurrencyTest extends EnvironmentExpressionFunctionTestCase<EnvironmentExpressionFunctionGetCurrency<EnvironmentExpressionEvaluationContext>, Currency>
    implements ToStringTesting {

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
    public void testApplyWithout() {
        this.applyAndCheck(
            Lists.empty(),
            CURRENCY
        );
    }

    @Test
    public void testApplyWithCurrency() {
        final Currency currency = Currency.getInstance("NZD");

        this.applyAndCheck(
            Lists.of(currency),
            currency
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            EnvironmentExpressionFunctionGetCurrency.instance(),
            "getCurrency"
        );
    }

    @Override
    public EnvironmentExpressionFunctionGetCurrency createBiFunction() {
        return EnvironmentExpressionFunctionGetCurrency.instance();
    }

    @Override
    public EnvironmentExpressionEvaluationContext createContext() {
        return new FakeEnvironmentExpressionEvaluationContext() {
            @Override
            public Currency currency() {
                return EnvironmentExpressionFunctionGetCurrencyTest.CURRENCY;
            }
        };
    }

    @Override
    public Class<EnvironmentExpressionFunctionGetCurrency<EnvironmentExpressionEvaluationContext>> type() {
        return Cast.to(EnvironmentExpressionFunctionGetCurrency.class);
    }

    @Override
    public int minimumParameterCount() {
        return 0;
    }
}
