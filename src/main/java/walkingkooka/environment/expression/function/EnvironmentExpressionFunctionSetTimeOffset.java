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
import walkingkooka.environment.expression.EnvironmentExpressionEvaluationContext;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.time.ZoneOffset;
import java.util.List;

/**
 * A function that updates the environment's {@link walkingkooka.environment.EnvironmentContext#TIME_OFFSET} with a new {@link ZoneOffset}.
 */
final class EnvironmentExpressionFunctionSetTimeOffset<C extends EnvironmentExpressionEvaluationContext> extends EnvironmentExpressionFunction<Void, C> {

    /**
     * Type-safe getter.
     */
    static <C extends EnvironmentExpressionEvaluationContext> EnvironmentExpressionFunctionSetTimeOffset<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static EnvironmentExpressionFunctionSetTimeOffset<?> INSTANCE = new EnvironmentExpressionFunctionSetTimeOffset<>();

    private EnvironmentExpressionFunctionSetTimeOffset() {
        super("setTimeOffset");
    }

    @Override
    public Class<Void> returnType() {
        return Void.class;
    }

    @Override
    public Void apply(final List<Object> values,
                      final C context) {
        context.setTimeOffset(
            TIME_OFFSET.getOrFail(
                values,
                0
            )
        );
        return null;
    }

    /**
     * Given the count assembles the parameters with the correct parameter names and types.
     */
    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        List<ExpressionFunctionParameter<?>> parameters;

        switch (count) {
            case 0:
                throw new IllegalArgumentException("Missing timeOffset");
            case 1:
                parameters = PARAMETERS;
                break;
            default:
                throw new IllegalArgumentException("Missing timeOffset");
        }

        return parameters;
    }

    private final ExpressionFunctionParameter<ZoneOffset> TIME_OFFSET = ExpressionFunctionParameterName.with("timeOffset")
        .required(ZoneOffset.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);

    private final List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.of(TIME_OFFSET);
}
