/*
 * Copyright 2024 Miroslav Pokorny (github.com/mP1)
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
import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.environment.expression.EnvironmentExpressionEvaluationContext;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.List;

/**
 * A function that removes the {@link EnvironmentValueName}, returning the previous value.
 */
final class EnvironmentExpressionFunctionRemoveEnv<C extends EnvironmentExpressionEvaluationContext> extends EnvironmentExpressionFunction<Object, C> {

    /**
     * Type-safe getter.
     */
    static <C extends EnvironmentExpressionEvaluationContext> EnvironmentExpressionFunctionRemoveEnv<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static EnvironmentExpressionFunctionRemoveEnv<?> INSTANCE = new EnvironmentExpressionFunctionRemoveEnv<>();

    private EnvironmentExpressionFunctionRemoveEnv() {
        super("removeEnv");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.of(
        ENVIRONMENT_VALUE_NAME
    );

    @Override
    public Class<Object> returnType() {
        return Object.class;
    }

    @Override
    public Object apply(final List<Object> parameters,
                        final C context) {

        final EnvironmentValueName<?> env = ENVIRONMENT_VALUE_NAME.getOrFail(parameters, 0);

        final Object previous = context.environmentValue(env)
            .orElse(null);
        context.removeEnvironmentValue(env);
        return previous;
    }
}
