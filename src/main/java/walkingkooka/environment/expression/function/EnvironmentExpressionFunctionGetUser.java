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
import walkingkooka.environment.EnvironmentContext;
import walkingkooka.environment.expression.EnvironmentExpressionEvaluationContext;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.tree.expression.function.ExpressionFunctionParameter;

import java.util.List;

/**
 * A function that returns the user querying {@link EnvironmentContext#user()}.
 */
final class EnvironmentExpressionFunctionGetUser<C extends EnvironmentExpressionEvaluationContext> extends EnvironmentExpressionFunction<EmailAddress, C> {

    /**
     * Type-safe getter.
     */
    static <C extends EnvironmentExpressionEvaluationContext> EnvironmentExpressionFunctionGetUser<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static EnvironmentExpressionFunctionGetUser<?> INSTANCE = new EnvironmentExpressionFunctionGetUser<>();

    private EnvironmentExpressionFunctionGetUser() {
        super("getUser");
    }

    @Override
    public List<ExpressionFunctionParameter<?>> parameters(final int count) {
        return NO_PARAMETERS;
    }

    @Override
    public Class<EmailAddress> returnType() {
        return EmailAddress.class;
    }

    @Override
    public EmailAddress apply(final List<Object> values,
                              final C context) {
        return context.user()
            .orElse(null);
    }
}
