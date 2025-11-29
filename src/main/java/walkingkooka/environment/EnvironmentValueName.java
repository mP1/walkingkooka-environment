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

import walkingkooka.Cast;
import walkingkooka.InvalidCharacterException;
import walkingkooka.InvalidTextLengthException;
import walkingkooka.collect.map.Maps;
import walkingkooka.compare.Comparators;
import walkingkooka.naming.Name;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.predicate.character.CharPredicate;
import walkingkooka.predicate.character.CharPredicates;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.LineEnding;
import walkingkooka.tree.expression.ExpressionReference;
import walkingkooka.tree.expression.function.ExpressionFunctionParameterName;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Locale;
import java.util.Map;

/**
 * The name of an environment value. Names must start with a letter, followed by letters/digits/dash and are case-sensitive.
 * <br>
 * A {@link EnvironmentValueName} is also a {@link ExpressionReference} allowing usage as a default value for a parameter
 * which can then be resolved to an actual value.
 */
final public class EnvironmentValueName<T> implements Name,
    Comparable<EnvironmentValueName<T>>,
    ExpressionReference {

    /**
     * Names must start with a letter.
     */
    public final static CharPredicate INITIAL = CharPredicates.letter();

    /**
     * Valid characters for characters following the first, which may be a letter, digit or dash.
     */
    public final static CharPredicate PART = INITIAL.or(
        CharPredicates.range('0', '9') // numbers
    ).or(
        CharPredicates.any("-.")
    );

    /**
     * The maximum valid length for a environment value name.
     */
    public final static int MAX_LENGTH = 255;

    private final static Map<String, EnvironmentValueName<?>> CONSTANTS = Maps.sorted();

    private static <T> EnvironmentValueName<T> registerConstant(final String name) {
        final EnvironmentValueName<?> constant = new EnvironmentValueName<>(name);
        CONSTANTS.put(name, constant);
        return Cast.to(constant);
    }

    public final static EnvironmentValueName<LineEnding> LINE_ENDING = registerConstant("lineEnding");

    public final static EnvironmentValueName<Locale> LOCALE = registerConstant("locale");

    public final static EnvironmentValueName<EmailAddress> USER = registerConstant("user");

    /**
     * Factory that creates a {@link EnvironmentValueName}
     */
    public static <T> EnvironmentValueName<T> with(final String name) {
        CharPredicates.failIfNullOrEmptyOrInitialAndPartFalse(
            name,
            "name",
            INITIAL,
            PART
        );

        EnvironmentValueName<T> environmentValueName = Cast.to(CONSTANTS.get(name));
        if (null == environmentValueName) {
            if (name.length() >= MAX_LENGTH) {
                throw new InvalidTextLengthException("name", name, 0, MAX_LENGTH);
            }

            final int dotdot = name.indexOf("..");
            if (-1 != dotdot) {
                throw new InvalidCharacterException(name, 1 + dotdot);
            }

            environmentValueName = new EnvironmentValueName<>(name);
        }

        return environmentValueName;
    }

    /**
     * Private constructor
     */
    private EnvironmentValueName(final String name) {
        super();
        this.name = name;
    }

    @Override
    public String value() {
        return this.name;
    }

    private final String name;

    // Comparable........................................................................................................

    @Override
    public int compareTo(final EnvironmentValueName other) {
        return CASE_SENSITIVITY.comparator().compare(this.name, other.name);
    }

    // ExpressionReference..............................................................................................

    @Override
    public boolean testParameterName(final ExpressionFunctionParameterName name) {
        return false; // dont care if this matches the name.
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return CASE_SENSITIVITY.hash(this.name);
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof EnvironmentValueName && this.equals0((EnvironmentValueName<?>) other);
    }

    private boolean equals0(final EnvironmentValueName<?> other) {
        return this.compareTo(other) == Comparators.EQUAL;
    }

    @Override
    public String toString() {
        return this.name;
    }

    // HasCaseSensitivity...............................................................................................

    @Override
    public CaseSensitivity caseSensitivity() {
        return CASE_SENSITIVITY;
    }

    public final static CaseSensitivity CASE_SENSITIVITY = CaseSensitivity.INSENSITIVE;

    // json.............................................................................................................

    private JsonNode marshall(final JsonNodeMarshallContext context) {
        return JsonNode.string(this.name);
    }

    static EnvironmentValueName<?> unmarshall(final JsonNode node,
                                              final JsonNodeUnmarshallContext context) {
        return with(
            node.stringOrFail()
        );
    }

    static {
        JsonNodeContext.register(
            JsonNodeContext.computeTypeName(EnvironmentValueName.class),
            EnvironmentValueName::unmarshall,
            EnvironmentValueName::marshall,
            EnvironmentValueName.class
        );
    }
}
