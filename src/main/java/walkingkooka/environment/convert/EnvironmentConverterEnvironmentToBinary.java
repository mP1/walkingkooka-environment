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

import walkingkooka.Binary;
import walkingkooka.Cast;
import walkingkooka.convert.TryingShortCircuitingConverter;
import walkingkooka.environment.Environment;
import walkingkooka.environment.HasEnvironment;

import java.util.Objects;

/**
 * A {@link walkingkooka.convert.Converter} that converts a {@link Environment} to a {@link Binary}.
 * This will be particularly useful when converting an {@link Environment} to a text file on disk.
 * <pre>
 * currency=AUD
 * lineEnding=\\n
 * </pre>
 */
final class EnvironmentConverterEnvironmentToBinary<C extends EnvironmentConverterContext> implements TryingShortCircuitingConverter<C> {

    /**
     * Type-safe getter.
     */
    static <C extends EnvironmentConverterContext> EnvironmentConverterEnvironmentToBinary<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static EnvironmentConverterEnvironmentToBinary<?> INSTANCE = new EnvironmentConverterEnvironmentToBinary<>();


    @Override
    public boolean canConvert(final Object value,
                              final Class<?> type,
                              final C context) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(context, "context");

        return value instanceof HasEnvironment && Binary.class == type;
    }

    @Override
    public Binary tryConvertOrFail(final Object hasEnvironment,
                                   final Class<?> type,
                                   final C context) {
        final Environment environment = ((HasEnvironment) hasEnvironment)
            .environment();

        return context.convertOrFail(
            context.convertOrFail(
                environment,
                String.class
            ),
            Binary.class
        );
    }

    @Override
    public String toString() {
        return Environment.class.getSimpleName() + " to " + Binary.class.getSimpleName();
    }
}
