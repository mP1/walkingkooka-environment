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

package walkingkooka.environment.convert;

import walkingkooka.convert.BinaryNumberConverterFunctions;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.currency.CurrencyLocaleContextTesting;
import walkingkooka.environment.EnvironmentContextTesting;
import walkingkooka.environment.convert.TestEnvironmentConverterContextDelegatorTest.TestEnvironmentConverterContextDelegator;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContextDelegator;
import walkingkooka.math.DecimalNumberContextTesting;

import java.math.MathContext;

public final class TestEnvironmentConverterContextDelegatorTest implements EnvironmentConverterContextTesting2<TestEnvironmentConverterContextDelegator>,
    CurrencyLocaleContextTesting,
    DecimalNumberContextDelegator,
    DecimalNumberContextTesting,
    EnvironmentContextTesting {

    @Override
    public TestEnvironmentConverterContextDelegator createContext() {
        return new TestEnvironmentConverterContextDelegator();
    }

    final static class TestEnvironmentConverterContextDelegator implements EnvironmentConverterContextDelegator {

        @Override
        public EnvironmentConverterContext environmentConverterContext() {
            return this.context;
        }

        private final EnvironmentConverterContext context = EnvironmentConverterContexts.basic(
            ENVIRONMENT_CONTEXT, // CanParseEnvironmentValueName
            ConverterContexts.basic(
                false, // canNumbersHaveGroupSeparator
                Converters.JAVA_EPOCH_OFFSET, // dateOffset
                ',', // valueSeparator
                Converters.toCsvStringList(),
                BinaryNumberConverterFunctions.multiply(), // multiplier
                BINARY_TEXT_CONTEXT,
                CURRENCY_LOCALE_CONTEXT,
                DATE_TIME_CONTEXT,
                DECIMAL_NUMBER_CONTEXT
            )
        );

        @Override
        public String toString() {
            return this.context.toString();
        }
    }

    @Override
    public DecimalNumberContext decimalNumberContext() {
        return DECIMAL_NUMBER_CONTEXT;
    }

    @Override
    public int decimalNumberDigitCount() {
        return DEFAULT_NUMBER_DIGIT_COUNT;
    }

    @Override
    public MathContext mathContext() {
        return MATH_CONTEXT;
    }

    // class............................................................................................................

    @Override
    public Class<TestEnvironmentConverterContextDelegator> type() {
        return TestEnvironmentConverterContextDelegator.class;
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
