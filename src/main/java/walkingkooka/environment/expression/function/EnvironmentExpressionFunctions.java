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

import walkingkooka.environment.expression.EnvironmentExpressionEvaluationContext;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.reflect.PublicStaticHelper;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.function.ExpressionFunction;

import java.nio.charset.Charset;
import java.time.ZoneOffset;
import java.util.Currency;
import java.util.Locale;

/**
 * A collection of {@link ExpressionEvaluationContext} functions.
 */
public final class EnvironmentExpressionFunctions implements PublicStaticHelper {

    /**
     * {@see EnvironmentExpressionFunctionGetCharset}
     */
    public static <C extends EnvironmentExpressionEvaluationContext> ExpressionFunction<Charset, C> getCharset() {
        return EnvironmentExpressionFunctionGetCharset.instance();
    }

    /**
     * {@see EnvironmentExpressionFunctionGetCurrency}
     */
    public static <C extends EnvironmentExpressionEvaluationContext> ExpressionFunction<Currency, C> getCurrency() {
        return EnvironmentExpressionFunctionGetCurrency.instance();
    }

    /**
     * {@see EnvironmentExpressionFunctionGetEnv}
     */
    public static <C extends EnvironmentExpressionEvaluationContext> ExpressionFunction<Object, C> getEnv() {
        return EnvironmentExpressionFunctionGetEnv.instance();
    }

    /**
     * {@see EnvironmentExpressionFunctionGetIndentation}
     */
    public static <C extends EnvironmentExpressionEvaluationContext> ExpressionFunction<Indentation, C> getIndentation() {
        return EnvironmentExpressionFunctionGetIndentation.instance();
    }
    
    /**
     * {@see EnvironmentExpressionFunctionGetLineEnding}
     */
    public static <C extends EnvironmentExpressionEvaluationContext> ExpressionFunction<LineEnding, C> getLineEnding() {
        return EnvironmentExpressionFunctionGetLineEnding.instance();
    }
    
    /**
     * {@see EnvironmentExpressionFunctionGetLocale}
     */
    public static <C extends EnvironmentExpressionEvaluationContext> ExpressionFunction<Locale, C> getLocale() {
        return EnvironmentExpressionFunctionGetLocale.instance();
    }

    /**
     * {@see EnvironmentExpressionFunctionGetTimeOffset}
     */
    public static <C extends EnvironmentExpressionEvaluationContext> ExpressionFunction<ZoneOffset, C> getTimeOffset() {
        return EnvironmentExpressionFunctionGetTimeOffset.instance();
    }
    
    /**
     * {@see EnvironmentExpressionFunctionGetUser}
     */
    public static <C extends EnvironmentExpressionEvaluationContext> ExpressionFunction<EmailAddress, C> getUser() {
        return EnvironmentExpressionFunctionGetUser.instance();
    }

    /**
     * {@see EnvironmentExpressionFunctionSetCharset}
     */
    public static <C extends EnvironmentExpressionEvaluationContext> ExpressionFunction<Void, C> setCharset() {
        return EnvironmentExpressionFunctionSetCharset.instance();
    }

    /**
     * {@see EnvironmentExpressionFunctionSetCurrency}
     */
    public static <C extends EnvironmentExpressionEvaluationContext> ExpressionFunction<Void, C> setCurrency() {
        return EnvironmentExpressionFunctionSetCurrency.instance();
    }

    /**
     * {@see EnvironmentExpressionFunctionSetEnv}
     */
    public static <C extends EnvironmentExpressionEvaluationContext> ExpressionFunction<Object, C> setEnv() {
        return EnvironmentExpressionFunctionSetEnv.instance();
    }

    /**
     * {@see EnvironmentExpressionFunctionSetIndentation}
     */
    public static <C extends EnvironmentExpressionEvaluationContext> ExpressionFunction<Void, C> setIndentation() {
        return EnvironmentExpressionFunctionSetIndentation.instance();
    }
    
    /**
     * {@see EnvironmentExpressionFunctionSetLineEnding}
     */
    public static <C extends EnvironmentExpressionEvaluationContext> ExpressionFunction<Void, C> setLineEnding() {
        return EnvironmentExpressionFunctionSetLineEnding.instance();
    }

    /**
     * {@see EnvironmentExpressionFunctionRemoveEnv}
     */
    public static <C extends EnvironmentExpressionEvaluationContext> ExpressionFunction<Object, C> removeEnv() {
        return EnvironmentExpressionFunctionRemoveEnv.instance();
    }

    /**
     * {@see EnvironmentExpressionFunctionSetLocale}
     */
    public static <C extends EnvironmentExpressionEvaluationContext> ExpressionFunction<Void, C> setLocale() {
        return EnvironmentExpressionFunctionSetLocale.instance();
    }

    /**
     * {@see EnvironmentExpressionFunctionSetTimeOffset}
     */
    public static <C extends EnvironmentExpressionEvaluationContext> ExpressionFunction<Void, C> setTimeOffset() {
        return EnvironmentExpressionFunctionSetTimeOffset.instance();
    }

    /**
     * Stop creation
     */
    private EnvironmentExpressionFunctions() {
        throw new UnsupportedOperationException();
    }
}
