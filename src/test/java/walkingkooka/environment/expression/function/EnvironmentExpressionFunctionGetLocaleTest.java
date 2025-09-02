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

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.environment.expression.EnvironmentExpressionEvaluationContext;
import walkingkooka.environment.expression.FakeEnvironmentExpressionEvaluationContext;

import java.util.Locale;

public final class EnvironmentExpressionFunctionGetLocaleTest extends EnvironmentExpressionFunctionTestCase<EnvironmentExpressionFunctionGetLocale<EnvironmentExpressionEvaluationContext>, Locale>
    implements ToStringTesting<EnvironmentExpressionFunctionGetLocale<EnvironmentExpressionEvaluationContext>> {

    private final static Locale LOCALE = Locale.forLanguageTag("en-AU");

    @Override
    public void testSetParametersSame() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testApplyWithLocale() {
        this.applyAndCheck(
            Lists.of(LOCALE),
            LOCALE
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            EnvironmentExpressionFunctionGetLocale.instance(),
            "locale"
        );
    }

    @Override
    public EnvironmentExpressionFunctionGetLocale createBiFunction() {
        return EnvironmentExpressionFunctionGetLocale.instance();
    }

    @Override
    public EnvironmentExpressionEvaluationContext createContext() {
        return new FakeEnvironmentExpressionEvaluationContext() {
            @Override
            public Locale locale() {
                return LOCALE;
            }
        };
    }

    @Override
    public Class<EnvironmentExpressionFunctionGetLocale<EnvironmentExpressionEvaluationContext>> type() {
        return Cast.to(EnvironmentExpressionFunctionGetLocale.class);
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }
}
