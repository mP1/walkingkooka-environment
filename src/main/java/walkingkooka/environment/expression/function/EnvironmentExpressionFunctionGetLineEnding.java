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
import walkingkooka.text.LineEnding;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.List;

/**
 * A function that returns a {@link LineEnding}.
 * A converter is expected to handle converting language-tags in String form to a LineEnding or get the current from {@link EnvironmentContext#lineEnding()}.
 */
final class EnvironmentExpressionFunctionGetLineEnding<C extends EnvironmentExpressionEvaluationContext> extends EnvironmentExpressionFunction<LineEnding, C> {

    /**
     * Type-safe getter.
     */
    static <C extends EnvironmentExpressionEvaluationContext> EnvironmentExpressionFunctionGetLineEnding<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static EnvironmentExpressionFunctionGetLineEnding<?> INSTANCE = new EnvironmentExpressionFunctionGetLineEnding<>();

    private EnvironmentExpressionFunctionGetLineEnding() {
        super("getLineEnding");
    }

    @Override
    public Class<LineEnding> returnType() {
        return LineEnding.class;
    }

    @Override
    public LineEnding apply(final List<Object> values,
                            final C context) {
        return LINE_ENDING.get(
            values,
            0
        ).orElseGet(context::lineEnding);
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

    private final ExpressionFunctionParameter<LineEnding> LINE_ENDING = ExpressionFunctionParameterName.with("lineEnding")
        .optional(LineEnding.class)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);

    private final List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.of(LINE_ENDING);
}
