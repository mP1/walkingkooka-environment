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

import walkingkooka.convert.ConverterContext;
import walkingkooka.convert.ConverterContextDelegator;

import java.util.Objects;

final class EnvironmentConverterContextBasic implements EnvironmentConverterContext,
    ConverterContextDelegator {

    static EnvironmentConverterContextBasic with(final ConverterContext context) {
        return new EnvironmentConverterContextBasic(
            Objects.requireNonNull(context, "context")
        );
    }

    private EnvironmentConverterContextBasic(final ConverterContext context) {
        super();
        this.context = context;
    }

    // ConverterContextDelegator........................................................................................

    @Override
    public ConverterContext converterContext() {
        return this.context;
    }

    private final ConverterContext context;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.context.toString();
    }
}
