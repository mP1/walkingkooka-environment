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

import org.junit.jupiter.api.Test;
import walkingkooka.collect.set.ImmutableSortedSetTesting;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.test.ParseStringTesting;
import walkingkooka.text.HasTextTesting;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EnvironmentValueNameSetTest implements ImmutableSortedSetTesting<EnvironmentValueNameSet, EnvironmentValueName<?>>,
    HasTextTesting,
    ParseStringTesting<EnvironmentValueNameSet>,
    TreePrintableTesting,
    JsonNodeMarshallingTesting<EnvironmentValueNameSet> {

    @Test
    public void testWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> EnvironmentValueNameSet.with(null)
        );
    }

    @Test
    public void testWithEnvironmentValueNameSetDoesntWrap() {
        final EnvironmentValueNameSet set = this.createSet();
        assertSame(
            set,
            EnvironmentValueNameSet.with(set)
        );
    }

    @Test
    public void testDeleteBecomesEmpty() {
        final EnvironmentValueName<?> name = EnvironmentValueName.with("value111");

        assertSame(
            EnvironmentValueNameSet.EMPTY,
            EnvironmentValueNameSet.with(
                SortedSets.of(name)
            ).delete(name)
        );
    }

    @Override
    public EnvironmentValueNameSet createSet() {
        return EnvironmentValueNameSet.with(
            SortedSets.of(
                EnvironmentValueName.with("value111"),
                EnvironmentValueName.with("value222")
            )
        );
    }

    // parseString......................................................................................................

    @Override
    public void testParseStringEmptyFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testParseInvalidCharacterFails() {
        this.parseStringInvalidCharacterFails(
            "value!1, value2",
            '!'
        );
    }

    @Test
    public void testParseInvalidCharacterSecondEnvironmentValueNameFails() {
        this.parseStringInvalidCharacterFails(
            "value1, value2!",
            '!'
        );
    }

    @Test
    public void testParseEmpty() {
        assertSame(
            EnvironmentValueNameSet.EMPTY,
            this.parseStringAndCheck(
                "",
                EnvironmentValueNameSet.EMPTY
            )
        );
    }

    @Test
    public void testParseSpaces() {
        assertSame(
            EnvironmentValueNameSet.EMPTY,
            this.parseStringAndCheck(
                "   ",
                EnvironmentValueNameSet.EMPTY
            )
        );
    }

    @Test
    public void testParse() {
        this.parseStringAndCheck(
            "value111,value222",
            this.createSet()
        );
    }

    @Test
    public void testParseWithExtraSpaces() {
        this.parseStringAndCheck(
            " value111 , value222 ",
            this.createSet()
        );
    }

    @Override
    public EnvironmentValueNameSet parseString(final String text) {
        return EnvironmentValueNameSet.parse(text);
    }

    @Override
    public Class<? extends RuntimeException> parseStringFailedExpected(final Class<? extends RuntimeException> type) {
        return type;
    }

    @Override
    public RuntimeException parseStringFailedExpected(final RuntimeException type) {
        return type;
    }

    // HasText..........................................................................................................

    @Test
    public void testText() {
        this.textAndCheck(
            this.createSet(),
            "value111,value222"
        );
    }

    // TreePrintable....................................................................................................

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
            this.createSet(),
            "value111\n" +
                "value222\n"
        );
    }

    // json.............................................................................................................

    @Test
    public void testMarshall() {
        this.marshallAndCheck(
            this.createJsonNodeMarshallingValue(),
            "\"value111,value222\""
        );
    }

    @Override
    public EnvironmentValueNameSet unmarshall(final JsonNode jsonNode,
                                    final JsonNodeUnmarshallContext context) {
        return EnvironmentValueNameSet.unmarshall(
            jsonNode,
            context
        );
    }

    @Override
    public EnvironmentValueNameSet createJsonNodeMarshallingValue() {
        return this.createSet();
    }

    // class............................................................................................................

    @Override
    public Class<EnvironmentValueNameSet> type() {
        return EnvironmentValueNameSet.class;
    }
}
