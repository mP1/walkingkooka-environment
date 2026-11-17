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

import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.environment.EnvironmentContext;
import walkingkooka.environment.expression.EnvironmentExpressionEvaluationContext;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.Currency;
import java.util.List;

/**
 * A function that returns a {@link Currency}.
 * A converter is expected to handle converting language-tags in String form to a Currency or get the current from {@link EnvironmentContext#currency()}.
 */
final class EnvironmentExpressionFunctionGetCurrency<C extends EnvironmentExpressionEvaluationContext> extends EnvironmentExpressionFunction<Currency, C> {

    /**
     * Type-safe getter.
     */
    static <C extends EnvironmentExpressionEvaluationContext> EnvironmentExpressionFunctionGetCurrency<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static EnvironmentExpressionFunctionGetCurrency<?> INSTANCE = new EnvironmentExpressionFunctionGetCurrency<>();

    private EnvironmentExpressionFunctionGetCurrency() {
        super("getCurrency");
    }

    @Override
    public Class<Currency> returnType() {
        return Currency.class;
    }

    @Override
    public Currency apply(final List<Object> values,
                          final C context) {
        return CURRENCY.get(
            values,
            0
        ).orElseGet(context::currency);
    }

    /**
     * Given the count assembles the parameters with the correct parameter names and types.
     */
    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        List<ExpressionFunctionParameter<?>> parameters;

        switch (count) {
            case 0:
            case 1:
                parameters = PARAMETERS;
                break;
            default:
                throw new IllegalArgumentException("Too many parameters");
        }

        return parameters;
    }

    private final ExpressionFunctionParameter<Currency> CURRENCY = ExpressionFunctionParameterName.with("currency")
        .optional(Currency.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);

    private final List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.of(CURRENCY);
}
