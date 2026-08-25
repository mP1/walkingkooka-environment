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
import walkingkooka.Cast;
import walkingkooka.Either;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterTesting2;
import walkingkooka.convert.Converters;
import walkingkooka.currency.CurrencyCode;
import walkingkooka.currency.CurrencyContextTesting;
import walkingkooka.environment.Environment;
import walkingkooka.environment.EnvironmentContextTesting;
import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.locale.LocaleLanguageTag;

import java.util.Currency;
import java.util.Locale;
import java.util.Optional;

public final class EnvironmentConverterStringToEnvironmentTest implements ConverterTesting2<EnvironmentConverterStringToEnvironment<FakeEnvironmentConverterContext>, FakeEnvironmentConverterContext>,
    CurrencyContextTesting,
    EnvironmentContextTesting {

    private final static EnvironmentValueName<String> HELLO = EnvironmentValueName.with(
        "hello",
        String.class
    );

    @Test
    public void testConvertStringToEnvironmentValueNameMissingValueFails() {
        this.convertFails(
            "currency",
            EnvironmentValueName.class
        );
    }

    @Test
    public void testConvertStringToEnvironmentValueWithInvalidValueFails() {
        this.convertFails(
            "currency=A",
            EnvironmentValueName.class
        );
    }

    @Test
    public void testConvertStringToEnvironmentValueWithUnclosedSingleQuoteFails() {
        this.convertFails(
            "currency=\'AUD",
            EnvironmentValueName.class
        );
    }

    @Test
    public void testConvertStringToEnvironmentValueWithExtraSingleQuoteFails() {
        this.convertFails(
            "currency=\'AUD\' \'",
            EnvironmentValueName.class
        );
    }

    @Test
    public void testConvertStringToEnvironmentValueWithUnclosedDoubleQuoteFails() {
        this.convertFails(
            "currency=\"AUD",
            EnvironmentValueName.class
        );
    }

    @Test
    public void testConvertStringToEnvironmentValueWithExtraDoubleQuoteFails() {
        this.convertFails(
            "currency=\'AUD\' \'",
            EnvironmentValueName.class
        );
    }

    @Test
    public void testConvertStringToEnvironmentRawString() {
        this.convertAndCheck(
            "currency=AUD",
            Environment.empty()
                .set(
                    EnvironmentValueName.CURRENCY,
                    CURRENCY
                )
        );
    }

    @Test
    public void testConvertStringToEnvironmentRawStringEmptyValue() {
        this.convertAndCheck(
            "hello=",
            Environment.empty()
                .set(
                    HELLO,
                    ""
                )
        );
    }

    @Test
    public void testConvertStringToEnvironmentRawStringIncludesUnicode() {
        this.convertAndCheck(
            "currency=\u0041UD",
            Environment.empty()
                .set(
                    EnvironmentValueName.CURRENCY,
                    CURRENCY
                )
        );
    }

    @Test
    public void testConvertStringToEnvironmentSpaceBeforeName() {
        this.convertAndCheck(
            " currency=AUD",
            Environment.empty()
                .set(
                    EnvironmentValueName.CURRENCY,
                    CURRENCY
                )
        );
    }

    @Test
    public void testConvertStringToEnvironmentManySpaceBeforeName() {
        this.convertAndCheck(
            "   currency=AUD",
            Environment.empty()
                .set(
                    EnvironmentValueName.CURRENCY,
                    CURRENCY
                )
        );
    }

    @Test
    public void testConvertStringToEnvironmentSpaceAfterName() {
        this.convertAndCheck(
            "currency =AUD",
            Environment.empty()
                .set(
                    EnvironmentValueName.CURRENCY,
                    CURRENCY
                )
        );
    }

    @Test
    public void testConvertStringToEnvironmentManySpacesAfterName() {
        this.convertAndCheck(
            "currency   =AUD",
            Environment.empty()
                .set(
                    EnvironmentValueName.CURRENCY,
                    CURRENCY
                )
        );
    }

    @Test
    public void testConvertStringToEnvironmentManySpacesBeforeAndAfterName() {
        this.convertAndCheck(
            "   currency   =AUD ",
            Environment.empty()
                .set(
                    EnvironmentValueName.CURRENCY,
                    CURRENCY
                )
        );
    }

    @Test
    public void testConvertStringToEnvironmentUnnecessaryDoubleQuotedValue() {
        this.convertAndCheck(
            "currency=\"AUD\"",
            Environment.empty()
                .set(
                    EnvironmentValueName.CURRENCY,
                    CURRENCY
                )
        );
    }

    @Test
    public void testConvertStringToEnvironmentUnnecessarySingleQuotedValue() {
        this.convertAndCheck(
            "currency='AUD'",
            Environment.empty()
                .set(
                    EnvironmentValueName.CURRENCY,
                    CURRENCY
                )
        );
    }

    @Test
    public void testConvertStringToEnvironmentSingleQuotedValueIncludesEscapedSingleQuote() {
        this.convertAndCheck(
            "hello='world\\\'\\\"'",
            Environment.empty()
                .set(
                    HELLO,
                    "world\'\""
                )
        );
    }

    @Test
    public void testConvertStringToEnvironmentDoubleQuoted() {
        this.convertAndCheck(
            "currency=\"AUD\"",
            Environment.empty()
                .set(
                    EnvironmentValueName.CURRENCY,
                    CURRENCY
                )
        );
    }

    @Test
    public void testConvertStringToEnvironmentDoubleQuotedWithEscapedEol() {
        this.convertAndCheck(
            "lineEnding=\"\\n\"",
            Environment.empty()
                .set(
                    EnvironmentValueName.LINE_ENDING,
                    LINE_ENDING
                )
        );
    }

    @Test
    public void testConvertStringToEnvironmentDoubleQuotedWithEscapedDoubleQuote() {
        this.convertAndCheck(
            "hello=\"world\\\"\"",
            Environment.empty()
                .set(
                    HELLO,
                    "world\""
                )
        );
    }

    @Test
    public void testConvertStringToEnvironmentDoubleQuotedSpaceBefore() {
        this.convertAndCheck(
            "lineEnding= \"\\n\"",
            Environment.empty()
                .set(
                    EnvironmentValueName.LINE_ENDING,
                    LINE_ENDING
                )
        );
    }

    @Test
    public void testConvertStringToEnvironmentDoubleQuotedManySpaceBefore() {
        this.convertAndCheck(
            "lineEnding=   \"\\n\"",
            Environment.empty()
                .set(
                    EnvironmentValueName.LINE_ENDING,
                    LINE_ENDING
                )
        );
    }

    @Test
    public void testConvertStringToEnvironmentMultipleEntries() {
        this.convertAndCheck(
            "currency=AUD\nlocale=en-AU\n",
            Environment.empty()
                .set(
                    EnvironmentValueName.CURRENCY,
                    CURRENCY
                ).set(
                    EnvironmentValueName.LOCALE,
                    LOCALE
                )
        );
    }

    @Test
    public void testConvertStringToEnvironmentMultipleEntriesIncludingEscapings() {
        this.convertAndCheck(
            "currency=AUD\nlineEnding=\"\\n\"",
            Environment.empty()
                .set(
                    EnvironmentValueName.CURRENCY,
                    CURRENCY
                ).set(
                    EnvironmentValueName.LINE_ENDING,
                    LINE_ENDING
                )
        );
    }

    @Override
    public EnvironmentConverterStringToEnvironment<FakeEnvironmentConverterContext> createConverter() {
        return EnvironmentConverterStringToEnvironment.instance();
    }

    @Override
    public FakeEnvironmentConverterContext createContext() {
        return new FakeEnvironmentConverterContext() {

            @Override
            public boolean canConvert(final Object value,
                                      final Class<?> type) {
                return this.converter.canConvert(
                    value,
                    type,
                    this
                );
            }

            @Override
            public <T> Either<T, String> convert(final Object value,
                                                 final Class<T> target) {
                return this.converter.convert(
                    value,
                    target,
                    this
                );
            }

            private final Converter<FakeEnvironmentConverterContext> converter = Converters.collection(
                Lists.of(
                    Converters.characterOrCharSequenceOrHasTextOrStringToCharacterOrCharSequenceOrString(),
                    Converters.textToCurrencyCode(),
                    Converters.textToCurrency(),
                    Converters.textToLineEnding(),
                    Converters.textToLocale()
                )
            );

            @Override
            public Optional<Currency> currencyForCurrencyCode(final CurrencyCode currencyCode) {
                return CURRENCY_CONTEXT.currencyForCurrencyCode(currencyCode);
            }

            @Override
            public Optional<Locale> localeForLanguageTag(final LocaleLanguageTag languageTag) {
                return LOCALE_CONTEXT.localeForLanguageTag(languageTag);
            }

            @Override
            public EnvironmentValueName<?> parseEnvironmentValueName(final String name) {
                return EnvironmentValueName.CASE_SENSITIVITY.equals(
                    HELLO.value(),
                    name
                ) ?
                    HELLO :
                    ENVIRONMENT_CONTEXT.parseEnvironmentValueName(
                        name.toUpperCase()
                    );
            }
        };
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            EnvironmentConverterStringToEnvironment.instance(),
            "TEXT to Environment"
        );
    }

    // class............................................................................................................

    @Override
    public Class<EnvironmentConverterStringToEnvironment<FakeEnvironmentConverterContext>> type() {
        return Cast.to(EnvironmentConverterStringToEnvironment.class);
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
