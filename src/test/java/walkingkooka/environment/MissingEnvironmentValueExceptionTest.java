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
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.ThrowableTesting2;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MissingEnvironmentValueExceptionTest implements ThrowableTesting2<MissingEnvironmentValueException>,
    JsonNodeMarshallingTesting<MissingEnvironmentValueException> {

    // with.............................................................................................................

    @Test
    public void testWithNullEnvironmentValueNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> new MissingEnvironmentValueException(null)
        );
    }

    @Test
    public void testWith() {
        final EnvironmentValueName<?> name = EnvironmentValueName.with("missing-123");

        this.checkEquals(
            name,
            new MissingEnvironmentValueException(name).environmentValueName()
        );
    }

    // with.............................................................................................................

    @Test
    public void testGetMessage() {
        this.checkMessage(
            new MissingEnvironmentValueException(
                EnvironmentValueName.with("missing-123")
            ),
            "Missing environment value \"missing-123\""
        );
    }

    // json.............................................................................................................

    @Test
    public void testMarshall() {
        this.marshallAndCheck(
            this.createJsonNodeMarshallingValue(),
            JsonNode.string("missing-environmental-value-name-123")
        );
    }

    @Test
    public void testUnmarshall() {
        this.unmarshallAndCheck(
            JsonNode.string("missing-environmental-value-name-123"),
            this.createJsonNodeMarshallingValue()
        );
    }

    @Override
    public MissingEnvironmentValueException unmarshall(final JsonNode json,
                                                       final JsonNodeUnmarshallContext context) {
        return MissingEnvironmentValueException.unmarshall(
            json,
            context
        );
    }

    @Override
    public MissingEnvironmentValueException createJsonNodeMarshallingValue() {
        return new MissingEnvironmentValueException(
            EnvironmentValueName.with("missing-environmental-value-name-123")
        );
    }

    // class............................................................................................................

    @Override
    public void testIfClassIsFinalIfAllConstructorsArePrivate() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<MissingEnvironmentValueException> type() {
        return MissingEnvironmentValueException.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
