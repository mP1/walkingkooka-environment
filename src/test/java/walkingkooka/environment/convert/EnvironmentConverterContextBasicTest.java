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
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.CsvStringList;
import walkingkooka.convert.BinaryNumberConverterFunctions;
import walkingkooka.convert.ConverterContext;
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
    EnvironmentContextTesting,
    ToStringTesting<EnvironmentConverterContextBasic> {

    private final static ConverterContext CONVERTER_CONTEXT = ConverterContexts.basic(
        false, // canNumbersHaveGroupSeparator
        Converters.JAVA_EPOCH_OFFSET, // dateOffset
        ',', // valueSeparator
        Converters.toCsvStringList(),
        BinaryNumberConverterFunctions.multiply(), // multiplier
        BINARY_TEXT_CONTEXT,
        CURRENCY_LOCALE_CONTEXT,
        DATE_TIME_CONTEXT,
        DECIMAL_NUMBER_CONTEXT
    );

    @Test
    public void testWithNullConverterContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> EnvironmentConverterContextBasic.with(
                CONVERTER_CONTEXT,
                null
            )
        );
    }

    @Test
    public void testWithNullEnvironmentContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> EnvironmentConverterContextBasic.with(
                null,
                ENVIRONMENT_CONTEXT
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
            CONVERTER_CONTEXT,
            ENVIRONMENT_CONTEXT
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

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createContext(),
            "converterContext=binaryTextContext=charset=\"UTF-8\" indentation=\"  \" lineEnding=\"\\n\" dateTimeContext=symbols=ampms=\"am\", \"pm\" monthNames=\"January\", \"February\", \"March\", \"April\", \"May\", \"June\", \"July\", \"August\", \"September\", \"October\", \"November\", \"December\" monthNameAbbreviations=\"Jan.\", \"Feb.\", \"Mar.\", \"Apr.\", \"May\", \"Jun.\", \"Jul.\", \"Aug.\", \"Sep.\", \"Oct.\", \"Nov.\", \"Dec.\" weekDayNames=\"Sunday\", \"Monday\", \"Tuesday\", \"Wednesday\", \"Thursday\", \"Friday\", \"Saturday\" weekDayNameAbbreviations=\"Sun.\", \"Mon.\", \"Tue.\", \"Wed.\", \"Thu.\", \"Fri.\", \"Sat.\" locale=\"en-AU\" twoDigitYear=50 decimalNumberContext=locale=en_US \"mathContext\" precision=7 roundingMode=HALF_EVEN \"decimalNumberSymbols\" negativeSign='-' positiveSign='+' zeroDigit='0' currencySymbol=\"$\" decimalSeparator='.' exponentSymbol=\"E\" groupSeparator=',' infinitySymbol=\"∞\" monetaryDecimalSeparator='.' nanSymbol=\"NaN\" percentSymbol='%' permillSymb environmentContext={charset=UTF-8, currency=AUD, indentation=\"  \", lineEnding=\"\\n\", locale=en_AU, t"
        );
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
