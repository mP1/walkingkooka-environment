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
 * A function that sets or replaces a new value for a requested {@link EnvironmentValueName}, returning the previous value.
 */
final class EnvironmentExpressionFunctionSetEnv<C extends EnvironmentExpressionEvaluationContext> extends EnvironmentExpressionFunction<Object, C> {

    /**
     * Type-safe getter.
     */
    static <C extends EnvironmentExpressionEvaluationContext> EnvironmentExpressionFunctionSetEnv<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static EnvironmentExpressionFunctionSetEnv<?> INSTANCE = new EnvironmentExpressionFunctionSetEnv<>();

    private EnvironmentExpressionFunctionSetEnv() {
        super("setEnv");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return PARAMETERS;
    }

    private final static List<ExpressionFunctionParameter<?>> PARAMETERS = Lists.of(
        ENVIRONMENT_VALUE_NAME,
        VALUE
    );

    @Override
    public Class<Object> returnType() {
        return Object.class;
    }

    @Override
    public Object apply(final List<Object> parameters,
                        final C context) {

        final EnvironmentValueName<?> env = ENVIRONMENT_VALUE_NAME.getOrFail(parameters, 0);
        final Object value = VALUE.getOrFail(parameters, 1);

        final Object previous = context.environmentValue(env)
            .orElse(null);
        context.setEnvironmentValue(
            env,
            Cast.to(value)
        );
        return previous;
    }
}
