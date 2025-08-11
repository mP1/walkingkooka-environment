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

import walkingkooka.environment.expression.EnvironmentExpressionEvaluationContext;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.tree.expression.function.ExpressionFunction;

/**
 * A collection of {@link EnvironmentExpressionEvaluationContext} functions.
 */
public final class EnvironmentExpressionFunctions implements PublicStaticHelper {

    /**
     * {@see EnvironmentExpressionFunctionGetEnv}
     */
    public static <C extends EnvironmentExpressionEvaluationContext> ExpressionFunction<Object, C> getEnv() {
        return EnvironmentExpressionFunctionGetEnv.instance();
    }

    /**
     * {@see EnvironmentExpressionFunctionGetUser}
     */
    public static <C extends EnvironmentExpressionEvaluationContext> ExpressionFunction<EmailAddress, C> getUser() {
        return EnvironmentExpressionFunctionGetUser.instance();
    }

    /**
     * {@see EnvironmentExpressionFunctionSetEnv}
     */
    public static <C extends EnvironmentExpressionEvaluationContext> ExpressionFunction<Object, C> setEnv() {
        return EnvironmentExpressionFunctionSetEnv.instance();
    }

    /**
     * {@see EnvironmentExpressionFunctionRemoveEnv}
     */
    public static <C extends EnvironmentExpressionEvaluationContext> ExpressionFunction<Object, C> removeEnv() {
        return EnvironmentExpressionFunctionRemoveEnv.instance();
    }

    /**
     * Stop creation
     */
    private EnvironmentExpressionFunctions() {
        throw new UnsupportedOperationException();
    }
}
