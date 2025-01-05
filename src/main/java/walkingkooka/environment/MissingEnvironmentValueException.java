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

package walkingkooka.environment;

import walkingkooka.text.CharSequences;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Objects;

/**
 * The exception that should be thrown when a {@link EnvironmentValueName} is not found by all {@link EnvironmentContext}.
 */
public final class MissingEnvironmentValueException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public MissingEnvironmentValueException(final EnvironmentValueName<?> environmentValueName) {
        super("Missing environment value " +
                CharSequences.quoteAndEscape(
                        Objects.requireNonNull(environmentValueName, "environmentValueName")
                                .value()
                )
        );
        this.environmentValueName = environmentValueName;
    }

    public EnvironmentValueName<?> environmentValueName() {
        return this.environmentValueName;
    }

    final EnvironmentValueName<?> environmentValueName;

    // hashCode/equals..................................................................................................

    @Override
    public int hashCode() {
        return this.environmentValueName.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
                other instanceof MissingEnvironmentValueException && this.equals0((MissingEnvironmentValueException) other);
    }

    private boolean equals0(final MissingEnvironmentValueException other) {
        return this.environmentValueName.equals(other.environmentValueName);
    }

    // json.............................................................................................................

    static {
        JsonNodeContext.register(
                JsonNodeContext.computeTypeName(MissingEnvironmentValueException.class),
                MissingEnvironmentValueException::unmarshall,
                MissingEnvironmentValueException::marshall,
                MissingEnvironmentValueException.class
        );
    }

    private JsonNode marshall(final JsonNodeMarshallContext context) {
        return context.marshall(
                this.environmentValueName.value() // EnvironmentValueName
        );
    }

    // @VisibleForTesting
    static MissingEnvironmentValueException unmarshall(final JsonNode node,
                                                       final JsonNodeUnmarshallContext context) {
        return new MissingEnvironmentValueException(
                EnvironmentValueName.with(
                        context.unmarshall(
                                node,
                                String.class
                        )
                )
        );
    }
}
