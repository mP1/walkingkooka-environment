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
import walkingkooka.util.HasLocaleTesting;

import java.util.Locale;

public final class EnvironmentExpressionFunctionSetLocaleTest extends EnvironmentExpressionFunctionTestCase<EnvironmentExpressionFunctionSetLocale<EnvironmentExpressionEvaluationContext>, Void>
    implements HasLocaleTesting,
    ToStringTesting<EnvironmentExpressionFunctionSetLocale<EnvironmentExpressionEvaluationContext>> {

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
        final EnvironmentExpressionEvaluationContext context = this.createContext();

        final Locale locale = Locale.FRENCH;
        this.applyAndCheck(
            EnvironmentExpressionFunctionSetLocale.instance(),
            Lists.of(locale),
            context,
            null
        );

        this.localeAndCheck(
            context,
            locale
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            EnvironmentExpressionFunctionSetLocale.instance(),
            "setLocale"
        );
    }

    @Override
    public EnvironmentExpressionFunctionSetLocale createBiFunction() {
        return EnvironmentExpressionFunctionSetLocale.instance();
    }

    @Override
    public EnvironmentExpressionEvaluationContext createContext() {
        return new FakeEnvironmentExpressionEvaluationContext() {
            @Override
            public Locale locale() {
                return this.locale;
            }

            @Override
            public EnvironmentExpressionEvaluationContext setLocale(final Locale locale) {
                this.locale = locale;
                return this;
            }

            private Locale locale = EnvironmentExpressionFunctionSetLocaleTest.LOCALE;
        };
    }

    @Override
    public Class<EnvironmentExpressionFunctionSetLocale<EnvironmentExpressionEvaluationContext>> type() {
        return Cast.to(EnvironmentExpressionFunctionSetLocale.class);
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }
}
