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

import walkingkooka.InvalidCharacterException;
import walkingkooka.collect.iterator.Iterators;
import walkingkooka.collect.set.ImmutableSortedSetDefaults;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.text.CharacterConstant;
import walkingkooka.text.HasText;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.AbstractSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * An immutable {@link SortedSet} that contains {@link EnvironmentValueName}. The primary purpose is to support json
 * marshalling/unmarshalling.
 */
public final class EnvironmentValueNameSet extends AbstractSet<EnvironmentValueName<?>> implements ImmutableSortedSetDefaults<EnvironmentValueNameSet, EnvironmentValueName<?>>,
    HasText,
    TreePrintable {

    /**
     * Empty constant
     */
    public final static EnvironmentValueNameSet EMPTY = new EnvironmentValueNameSet(SortedSets.empty());

    /**
     * Factory that creates a {@link EnvironmentValueNameSet} after taking a copy.
     */
    public static EnvironmentValueNameSet with(final SortedSet<EnvironmentValueName<?>> names) {
        Objects.requireNonNull(names, "names");

        return withCopy(
            SortedSets.immutable(names)
        );
    }

    private static EnvironmentValueNameSet withCopy(final SortedSet<EnvironmentValueName<?>> names) {
        return names.isEmpty() ?
            EMPTY :
            new EnvironmentValueNameSet(names);
    }

    // @VisibleForTesting
    private EnvironmentValueNameSet(final SortedSet<EnvironmentValueName<?>> names) {
        super();
        this.names = names;
    }

    @Override
    public Iterator<EnvironmentValueName<?>> iterator() {
        return Iterators.readOnly(this.names.iterator());
    }

    @Override
    public int size() {
        return this.names.size();
    }

    @Override
    public Comparator<? super EnvironmentValueName<?>> comparator() {
        return null; // no comparator
    }

    @Override
    public EnvironmentValueNameSet subSet(final EnvironmentValueName<?> from,
                                          final EnvironmentValueName<?> to) {
        return withCopy(
            this.names.subSet(
                from,
                to
            )
        );
    }

    @Override
    public EnvironmentValueNameSet headSet(final EnvironmentValueName<?> name) {
        return withCopy(
            this.names.headSet(name)
        );
    }

    @Override
    public EnvironmentValueNameSet tailSet(final EnvironmentValueName<?> name) {
        return withCopy(
            this.names.tailSet(name)
        );
    }

    @Override
    public EnvironmentValueName<?> first() {
        return this.names.first();
    }

    @Override
    public EnvironmentValueName<?> last() {
        return this.names.last();
    }

    @Override
    public EnvironmentValueNameSet setElements(final SortedSet<EnvironmentValueName<?>> names) {
        final TreeSet<EnvironmentValueName<?>> copy = new TreeSet<>(
            Objects.requireNonNull(names, "names")
        );
        return this.names.equals(copy) ?
            this :
            withCopy(copy);
    }

    @Override
    public SortedSet<EnvironmentValueName<?>> toSet() {
        return new TreeSet<>(this);
    }

    private final SortedSet<EnvironmentValueName<?>> names;

    // HasText..........................................................................................................

    @Override
    public String text() {
        return this.names.stream()
            .map(EnvironmentValueName::value)
            .collect(Collectors.joining(","));
    }

    final static CharacterConstant SEPARATOR = CharacterConstant.COMMA;

    // HasText..........................................................................................................

    /**
     * Parsers a CSV file of {@link EnvironmentValueName}.
     */
    public static EnvironmentValueNameSet parse(final String text) {
        Objects.requireNonNull(text, "text");

        final SortedSet<EnvironmentValueName<?>> names = SortedSets.tree();
        final EnvironmentValueNameSetParser parser = EnvironmentValueNameSetParser.with(
            text
        );

        parser.spaces();

        if (parser.isNotEmpty()) {
            for (; ; ) {
                parser.spaces();

                final int offset = parser.cursor.lineInfo().textOffset();
                try {
                    names.add(
                        EnvironmentValueName.with(
                            parser.name()
                        )
                    );
                } catch (final InvalidCharacterException invalid) {
                    throw invalid.setTextAndPosition(
                        text,
                        offset + invalid.position()
                    );
                }

                parser.spaces();

                if (SEPARATOR.string().equals(parser.comma())) {
                    continue;
                }

                if (parser.isEmpty()) {
                    break;
                }

                parser.invalidCharacterException();
            }
        }

        return withCopy(names);
    }

    // json.............................................................................................................

    static EnvironmentValueNameSet unmarshall(final JsonNode node,
                                              final JsonNodeUnmarshallContext context) {
        return parse(
            node.stringOrFail()
        );
    }

    private JsonNode marshall(final JsonNodeMarshallContext context) {
        return JsonNode.string(this.text());
    }

    static {
        JsonNodeContext.register(
            JsonNodeContext.computeTypeName(EnvironmentValueNameSet.class),
            EnvironmentValueNameSet::unmarshall,
            EnvironmentValueNameSet::marshall,
            EnvironmentValueNameSet.class
        );
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        for (final EnvironmentValueName<?> name : this) {
            TreePrintable.printTreeOrToString(
                name,
                printer
            );
            printer.lineStart();
        }
    }
}
