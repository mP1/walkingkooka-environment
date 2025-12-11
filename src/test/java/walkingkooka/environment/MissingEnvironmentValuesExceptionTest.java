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
import walkingkooka.collect.set.Sets;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.ThrowableTesting2;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.util.Set;

public final class MissingEnvironmentValuesExceptionTest implements ThrowableTesting2<MissingEnvironmentValuesException>,
    JsonNodeMarshallingTesting<MissingEnvironmentValuesException> {

    @Override
    public void testAllConstructorsVisibility() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testIfClassIsFinalIfAllConstructorsArePrivate() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testGetMessage() {
        final Set<EnvironmentValueName<?>> missing = Sets.of(
            EnvironmentContext.LINE_ENDING,
            EnvironmentContext.LOCALE,
            EnvironmentValueName.USER
        );

        final MissingEnvironmentValuesException thrown = new MissingEnvironmentValuesException(missing);

        this.checkEquals(
            "Missing environment value(s): lineEnding, locale, user",
            thrown.getMessage(),
            "message"
        );

        this.checkEquals(
            missing,
            thrown.environmentValueNames(),
            "environmentValueNames"
        );
    }

    // json.............................................................................................................

    @Test
    public void testMarshall() {
        this.marshallAndCheck(
            this.createJsonNodeMarshallingValue(),
            "[\n" +
                "  \"locale\",\n" +
                "  \"lineEnding\",\n" +
                "  \"user\"\n" +
                "]"
        );
    }

    @Test
    public void testUnmarshall() {
        this.unmarshallAndCheck(
            "[\n" +
                "  \"locale\",\n" +
                "  \"lineEnding\",\n" +
                "  \"user\"\n" +
                "]",
            this.createJsonNodeMarshallingValue()
        );
    }

    @Override
    public MissingEnvironmentValuesException unmarshall(final JsonNode json,
                                                        final JsonNodeUnmarshallContext context) {
        return MissingEnvironmentValuesException.unmarshall(
            json,
            context
        );
    }

    @Override
    public MissingEnvironmentValuesException createJsonNodeMarshallingValue() {
        return new MissingEnvironmentValuesException(
            Sets.of(
                EnvironmentContext.LOCALE,
                EnvironmentValueName.LINE_ENDING,
                EnvironmentValueName.USER
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<MissingEnvironmentValuesException> type() {
        return MissingEnvironmentValuesException.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
