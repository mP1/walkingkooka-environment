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

package walkingkooka.environment.expression;

import walkingkooka.datetime.DateTimeSymbols;
import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.environment.expression.EnvironmentExpressionEvaluationContextTestingTest.TestEnvironmentExpressionEvaluationContext;
import walkingkooka.locale.LocaleContext;
import walkingkooka.locale.LocaleContexts;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContexts;
import walkingkooka.math.DecimalNumberSymbols;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionFunctionName;
import walkingkooka.tree.expression.ExpressionReference;

import java.math.MathContext;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public final class EnvironmentExpressionEvaluationContextTestingTest implements EnvironmentExpressionEvaluationContextTesting<TestEnvironmentExpressionEvaluationContext> {

    private final static DecimalNumberContext DECIMAL_NUMBER_CONTEXT = DecimalNumberContexts.american(MathContext.DECIMAL32);

    @Override
    public void testEnterScopeGivesDifferentInstance() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testEvaluateExpressionUnknownFunctionNameFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TestEnvironmentExpressionEvaluationContext createContext() {
        return new TestEnvironmentExpressionEvaluationContext();
    }

    @Override
    public MathContext mathContext() {
        return DECIMAL_NUMBER_CONTEXT.mathContext();
    }

    @Override
    public String currencySymbol() {
        return DECIMAL_NUMBER_CONTEXT.currencySymbol();
    }

    @Override
    public char decimalSeparator() {
        return DECIMAL_NUMBER_CONTEXT.decimalSeparator();
    }

    @Override
    public String exponentSymbol() {
        return DECIMAL_NUMBER_CONTEXT.exponentSymbol();
    }

    @Override
    public char groupSeparator() {
        return DECIMAL_NUMBER_CONTEXT.groupSeparator();
    }

    @Override
    public String infinitySymbol() {
        return DECIMAL_NUMBER_CONTEXT.infinitySymbol();
    }

    @Override
    public char monetaryDecimalSeparator() {
        return DECIMAL_NUMBER_CONTEXT.monetaryDecimalSeparator();
    }

    @Override
    public String nanSymbol() {
        return DECIMAL_NUMBER_CONTEXT.nanSymbol();
    }

    @Override
    public char percentSymbol() {
        return DECIMAL_NUMBER_CONTEXT.percentSymbol();
    }

    @Override
    public char permillSymbol() {
        return DECIMAL_NUMBER_CONTEXT.permillSymbol();
    }

    @Override
    public char negativeSign() {
        return DECIMAL_NUMBER_CONTEXT.negativeSign();
    }

    @Override
    public char positiveSign() {
        return DECIMAL_NUMBER_CONTEXT.positiveSign();
    }

    @Override
    public char zeroDigit() {
        return DECIMAL_NUMBER_CONTEXT.zeroDigit();
    }

    @Override
    public Class<TestEnvironmentExpressionEvaluationContext> type() {
        return TestEnvironmentExpressionEvaluationContext.class;
    }

    final static class TestEnvironmentExpressionEvaluationContext extends FakeEnvironmentExpressionEvaluationContext {

        @Override
        public MathContext mathContext() {
            return DECIMAL_NUMBER_CONTEXT.mathContext();
        }

        @Override
        public String currencySymbol() {
            return DECIMAL_NUMBER_CONTEXT.currencySymbol();
        }

        @Override
        public char decimalSeparator() {
            return DECIMAL_NUMBER_CONTEXT.decimalSeparator();
        }

        @Override
        public String exponentSymbol() {
            return DECIMAL_NUMBER_CONTEXT.exponentSymbol();
        }

        @Override
        public char groupSeparator() {
            return DECIMAL_NUMBER_CONTEXT.groupSeparator();
        }

        @Override
        public String infinitySymbol() {
            return DECIMAL_NUMBER_CONTEXT.infinitySymbol();
        }

        @Override
        public char monetaryDecimalSeparator() {
            return DECIMAL_NUMBER_CONTEXT.monetaryDecimalSeparator();
        }

        @Override
        public String nanSymbol() {
            return DECIMAL_NUMBER_CONTEXT.nanSymbol();
        }

        @Override
        public char percentSymbol() {
            return DECIMAL_NUMBER_CONTEXT.percentSymbol();
        }

        @Override
        public char permillSymbol() {
            return DECIMAL_NUMBER_CONTEXT.permillSymbol();
        }

        @Override
        public char negativeSign() {
            return DECIMAL_NUMBER_CONTEXT.negativeSign();
        }

        @Override
        public char positiveSign() {
            return DECIMAL_NUMBER_CONTEXT.positiveSign();
        }

        @Override
        public char zeroDigit() {
            return DECIMAL_NUMBER_CONTEXT.zeroDigit();
        }

        @Override
        public boolean isPure(final ExpressionFunctionName name) {
            Objects.requireNonNull(name, "name");
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<DateTimeSymbols> dateTimeSymbolsForLocale(final Locale locale) {
            return this.localeContext.dateTimeSymbolsForLocale(locale);
        }

        @Override
        public Optional<DecimalNumberSymbols> decimalNumberSymbolsForLocale(final Locale locale) {
            return this.localeContext.decimalNumberSymbolsForLocale(locale);
        }

        @Override
        public Set<Locale> findByLocaleText(final String text,
                                            final int offset,
                                            final int count) {
            return this.localeContext.findByLocaleText(
                text,
                offset,
                count
            );
        }

        @Override
        public Optional<String> localeText(final Locale locale) {
            return this.localeContext.localeText(locale);
        }

        @Override
        public ExpressionEvaluationContext setLocale(final Locale locale) {
            this.localeContext.setLocale(locale);
            return this;
        }

        private final LocaleContext localeContext = LocaleContexts.jre(Locale.ENGLISH);

        @Override
        public ExpressionEvaluationContext enterScope(final Function<ExpressionReference, Optional<Optional<Object>>> scoped) {
            Objects.requireNonNull(scoped, "scoped");
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> Optional<T> environmentValue(final EnvironmentValueName<T> name) {
            Objects.requireNonNull(name, "name");
            throw new UnsupportedOperationException();
        }

        @Override
        public <T> EnvironmentExpressionEvaluationContext setEnvironmentValue(final EnvironmentValueName<T> name,
                                                                              final T value) {
            Objects.requireNonNull(name, "name");
            Objects.requireNonNull(value, "value");

            throw new UnsupportedOperationException();
        }

        @Override
        public EnvironmentExpressionEvaluationContext removeEnvironmentValue(final EnvironmentValueName<?> name) {
            Objects.requireNonNull(name, "name");

            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<EmailAddress> user() {
            return Optional.of(
                EmailAddress.parse("user@example.com")
            );
        }
    }
}
