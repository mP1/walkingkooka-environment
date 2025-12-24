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

import walkingkooka.Cast;
import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.TextToTryingShortCircuitingConverter;
import walkingkooka.environment.EnvironmentValueName;

/**
 * A converter that converts a {@link String} or text like value into a {@link EnvironmentValueName}.
 */
final class EnvironmentConverterStringToEnvironmentValueName<C extends ConverterContext> implements TextToTryingShortCircuitingConverter<C> {

    /**
     * Type-safe getter.
     */
    static <C extends ConverterContext> EnvironmentConverterStringToEnvironmentValueName<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static EnvironmentConverterStringToEnvironmentValueName<?> INSTANCE = new EnvironmentConverterStringToEnvironmentValueName<>();


    @Override
    public boolean isTargetType(final Object value,
                                final Class type,
                                final ConverterContext context) {
        return type == EnvironmentValueName.class;
    }

    @Override
    public Object parseText(final String text,
                            final Class type,
                            final ConverterContext context) {
        return EnvironmentValueName.with(text);
    }

    @Override
    public String toString() {
        return "Text to " + EnvironmentValueName.class.getName();
    }
}
