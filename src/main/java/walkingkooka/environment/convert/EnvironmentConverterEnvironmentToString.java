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
import walkingkooka.convert.TryingShortCircuitingConverter;
import walkingkooka.environment.Environment;
import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.environment.HasEnvironment;
import walkingkooka.text.CharSequences;
import walkingkooka.text.LineEnding;

import java.util.Objects;

/**
 * A {@link walkingkooka.convert.Converter} that converts a {@link String} or text like value into a {@link EnvironmentValueName} using {@link EnvironmentConverterContext#parseEnvironmentValueName(String)}.
 */
final class EnvironmentConverterEnvironmentToString<C extends EnvironmentConverterContext> implements TryingShortCircuitingConverter<C> {

    /**
     * Type-safe getter.
     */
    static <C extends EnvironmentConverterContext> EnvironmentConverterEnvironmentToString<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static EnvironmentConverterEnvironmentToString<?> INSTANCE = new EnvironmentConverterEnvironmentToString<>();


    @Override
    public boolean canConvert(final Object value,
                              final Class<?> type,
                              final C context) {
        Objects.requireNonNull(type, "type");
        Objects.requireNonNull(context, "context");

        return value instanceof HasEnvironment && String.class == type;
    }

    @Override
    public String tryConvertOrFail(final Object hasEnvironment,
                                   final Class<?> type,
                                   final C context) {
        final StringBuilder b = new StringBuilder();

        final LineEnding lineEnding = context.lineEnding();

        final Environment environment = ((HasEnvironment) hasEnvironment).environment();

        for (final EnvironmentValueName<?> name : environment.names()) {
            final Object value = environment.get(name)
                .orElse(null);

            // currency=AUD <LineEnding>
            if (null != value) {
                b.append(
                        context.convertOrFail(
                            name,
                            String.class
                        )
                    ).append(Environment.SEPARATOR)
                    .append(
                        CharSequences.quoteIfNecessary(
                            context.convertOrFail(
                                value,
                                String.class
                            )
                        )
                    );
                b.append(lineEnding);
            }
        }

        return b.toString();
    }

    @Override
    public String toString() {
        return Environment.class.getSimpleName() + " to " + TEXT;
    }
}
