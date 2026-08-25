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
import walkingkooka.Binary;
import walkingkooka.Cast;
import walkingkooka.Either;
import walkingkooka.collect.list.Lists;
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterTesting2;
import walkingkooka.convert.Converters;
import walkingkooka.datetime.DateTimeContextTesting;
import walkingkooka.environment.Environment;
import walkingkooka.environment.EnvironmentContextTesting;
import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.text.LineEnding;

import java.nio.charset.Charset;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public final class EnvironmentConverterEnvironmentToBinaryTest implements ConverterTesting2<EnvironmentConverterEnvironmentToBinary<FakeEnvironmentConverterContext>, FakeEnvironmentConverterContext>,
    DateTimeContextTesting,
    EnvironmentContextTesting {

    @Test
    public void testConvertEnvironmentToNumberFails() {
        this.convertFails(
            Environment.empty(),
            Number.class
        );
    }

    @Test
    public void testConvertEnvironmentToBinaryWhenLineEndingCr() {
        this.convertAndCheck(
            this.createConverter(),
            Environment.empty()
                .set(
                    EnvironmentValueName.CURRENCY,
                    CURRENCY
                ),
            Binary.class,
            this.createContext(
                LineEnding.CR
            ),
            Binary.with(
                "currency=AUD\r".getBytes(CHARSET)
            )
        );
    }

    @Test
    public void testConvertEnvironmentToBinaryWhenLineEndingNl() {
        this.convertAndCheck(
            Environment.empty()
                .set(
                    EnvironmentValueName.CURRENCY,
                    CURRENCY
                ),
            Binary.with(
                "currency=AUD\n".getBytes(CHARSET)
            )
        );
    }

    @Test
    public void testConvertEnvironmentToString() {
        this.convertAndCheck(
            Environment.empty()
                .set(
                    EnvironmentValueName.CURRENCY,
                    CURRENCY
                ).set(
                    EnvironmentValueName.LINE_ENDING,
                    LINE_ENDING
                ).set(
                    EnvironmentValueName.LOCALE,
                    LOCALE
                ).set(
                    EnvironmentValueName.with("hello-time", LocalTime.class),
                    NOW.toLocalTime()
                ),
            Binary.with(
                (
                    "currency=AUD\n" +
                        "hello-time=59:58:12\n" +
                        "lineEnding=\"\\n\"\n" +
                        "locale=en-AU\n"
                ).getBytes(CHARSET)
            )
        );
    }

    @Override
    public EnvironmentConverterEnvironmentToBinary<FakeEnvironmentConverterContext> createConverter() {
        return EnvironmentConverterEnvironmentToBinary.instance();
    }

    @Override
    public FakeEnvironmentConverterContext createContext() {
        return this.createContext(LINE_ENDING);
    }

    private FakeEnvironmentConverterContext createContext(final LineEnding lineEnding) {
        return new FakeEnvironmentConverterContext() {

            @Override
            public Charset charset() {
                return CHARSET;
            }

            @Override
            public Locale locale() {
                return LOCALE;
            }

            @Override
            public LineEnding lineEnding() {
                return lineEnding;
            }

            @Override
            public int twoDigitYear() {
                return TWO_DIGIT_YEAR;
            }

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
                    Converters.textToBinary(),
                    EnvironmentConverters.environmentToString(),
                    Converters.localTimeToString(
                        (c) -> DateTimeFormatter.ofPattern("ss:mm:hh")
                    ),
                    Converters.localeToString(),
                    Converters.objectToString()
                )
            );
        };
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            EnvironmentConverterEnvironmentToBinary.instance(),
            "Environment to Binary"
        );
    }

    // class............................................................................................................

    @Override
    public Class<EnvironmentConverterEnvironmentToBinary<FakeEnvironmentConverterContext>> type() {
        return Cast.to(EnvironmentConverterEnvironmentToBinary.class);
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }
}
