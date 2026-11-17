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

package walkingkooka.environment.expression;

import walkingkooka.currency.CurrencyCode;
import walkingkooka.environment.EnvironmentContext;
import walkingkooka.environment.EnvironmentContextDelegator;
import walkingkooka.logging.LoggingContext;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionEvaluationContextDelegator;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.Locale;

public interface EnvironmentExpressionEvaluationContextDelegator extends EnvironmentExpressionEvaluationContext,
    ExpressionEvaluationContextDelegator,
    EnvironmentContextDelegator {

    // ExpressionEvaluationContextDelegator.............................................................................

    @Override
    default ExpressionEvaluationContext expressionEvaluationContext() {
        return this.environmentExpressionEvaluationContext();
    }

    // EnvironmentContextDelegator......................................................................................

    @Override
    default Charset charset() {
        return EnvironmentContextDelegator.super.charset();
    }

    @Override
    default CurrencyCode currencyCode() {
        return EnvironmentContextDelegator.super.currencyCode();
    }

    @Override
    default Indentation indentation() {
        return EnvironmentContextDelegator.super.indentation();
    }

    @Override
    default LineEnding lineEnding() {
        return EnvironmentContextDelegator.super.lineEnding();
    }

    @Override
    default Locale locale() {
        return EnvironmentContextDelegator.super.locale();
    }

    @Override
    default void setLocale(final Locale locale) {
        EnvironmentContextDelegator.super.setLocale(locale);
    }

    @Override
    default LocalDateTime now() {
        return EnvironmentContextDelegator.super.now();
    }

    @Override
    default EnvironmentContext environmentContext() {
        return this.environmentExpressionEvaluationContext();
    }

    @Override
    default LoggingContext loggingContext() {
        return this.environmentExpressionEvaluationContext();
    }

    // EnvironmentExpressionEvaluationContextDelegator..................................................................

    EnvironmentExpressionEvaluationContext environmentExpressionEvaluationContext();
}
