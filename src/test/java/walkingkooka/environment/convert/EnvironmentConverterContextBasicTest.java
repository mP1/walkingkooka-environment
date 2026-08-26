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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.list.CsvStringList;
import walkingkooka.convert.BinaryNumberConverterFunctions;
import walkingkooka.convert.ConverterContexts;
import walkingkooka.convert.Converters;
import walkingkooka.currency.CurrencyLocaleContextTesting;
import walkingkooka.environment.EnvironmentContextTesting;
import walkingkooka.math.DecimalNumberContext;
import walkingkooka.math.DecimalNumberContextDelegator;
import walkingkooka.math.DecimalNumberContextTesting;
import walkingkooka.text.BinaryTextContextTesting;

import java.math.MathContext;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EnvironmentConverterContextBasicTest implements EnvironmentConverterContextTesting2<EnvironmentConverterContextBasic>,
    BinaryTextContextTesting,
    CurrencyLocaleContextTesting,
    DecimalNumberContextTesting,
    DecimalNumberContextDelegator,
    EnvironmentContextTesting {

    @Test
    public void testWithNullCanParseEnvironmentValueNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> EnvironmentConverterContextBasic.with(
                null,
                ConverterContexts.fake()
            )
        );
    }

    @Test
    public void testWithNullConverterContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> EnvironmentConverterContextBasic.with(
                CAN_PARSE_ENVIRONMENT_VALUE_NAME,
                null
            )
        );
    }

    @Test
    public void testConverterConvert() {
        final CsvStringList csvStringList = CsvStringList.parse("a,b,c");

        this.convertAndCheck(
            csvStringList,
            CsvStringList.class,
            csvStringList
        );
    }

    @Override
    public EnvironmentConverterContextBasic createContext() {
        return EnvironmentConverterContextBasic.with(
            CAN_PARSE_ENVIRONMENT_VALUE_NAME,
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
    public Class<EnvironmentConverterContextBasic> type() {
        return EnvironmentConverterContextBasic.class;
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
