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
import walkingkooka.convert.Converter;
import walkingkooka.convert.ConverterTesting2;
import walkingkooka.convert.Converters;
import walkingkooka.convert.FakeConverterContext;
import walkingkooka.environment.EnvironmentValueName;

public final class EnvironmentConverterStringToValueNameTest implements ConverterTesting2<EnvironmentConverterStringToValueName<FakeConverterContext>, FakeConverterContext> {

    @Test
    public void testConvertStringToEnvironmentValueName() {
        final String value = "Var123";

        this.convertAndCheck(
            value,
            EnvironmentValueName.with(value)
        );
    }

    @Test
    public void testConvertStringBuilderToEnvironmentValueName() {
        final String value = "Var123";

        this.convertAndCheck(
            new StringBuilder(value),
            EnvironmentValueName.with(value)
        );
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }

    @Override
    public EnvironmentConverterStringToValueName<FakeConverterContext> createConverter() {
        return EnvironmentConverterStringToValueName.instance();
    }

    @Override
    public FakeConverterContext createContext() {
        return new FakeConverterContext() {

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

            private final Converter<FakeConverterContext> converter = Converters.characterOrCharSequenceOrHasTextOrStringToCharacterOrCharSequenceOrString();
        };
    }

    @Override
    public Class<EnvironmentConverterStringToValueName<FakeConverterContext>> type() {
        return Cast.to(EnvironmentConverterStringToValueName.class);
    }
}
