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
import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.environment.expression.EnvironmentExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.ExpressionPurityContext;
import walkingkooka.tree.expression.function.ExpressionFunction;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterKind;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;

import java.util.Optional;

abstract class EnvironmentExpressionFunction<T, C extends EnvironmentExpressionEvaluationContext> implements ExpressionFunction<T, C> {

    EnvironmentExpressionFunction(final String name) {
        super();
        this.name = Optional.of(
            ExpressionFunctionName.with(name)
        );
    }

    @Override
    public final Optional<ExpressionFunctionName> name() {
        return this.name;
    }

    private final Optional<ExpressionFunctionName> name;

    /**
     * Environment functions are never pure.
     */
    @Override
    public final boolean isPure(final ExpressionPurityContext context) {
        return false;
    }

    @Override
    public final String toString() {
        return this.name()
            .get()
            .toString();
    }

    final static Class<EnvironmentValueName<?>> ENVIRONMENT_VALUE_NAME_CLASS = Cast.to(EnvironmentValueName.class);

    final static ExpressionFunctionParameter<EnvironmentValueName<?>> ENVIRONMENT_VALUE_NAME = ExpressionFunctionParameterName.with("env")
        .required(ENVIRONMENT_VALUE_NAME_CLASS)
        .setKinds(ExpressionFunctionParameterKind.CONVERT_EVALUATE);

    final static ExpressionFunctionParameter<Object> VALUE = ExpressionFunctionParameterName.with("value")
        .optional(Object.class)
        .setKinds(ExpressionFunctionParameterKind.EVALUATE_RESOLVE_REFERENCES);
}
