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

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ReadOnlyEnvironmentValueNameExceptionTest implements ThrowableTesting2<ReadOnlyEnvironmentValueNameException> {

    @Override
    public void testIfClassIsFinalIfAllConstructorsArePrivate() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testWithNullNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> new ReadOnlyEnvironmentValueNameException(null)
        );
    }

    @Test
    public void testGetMessage() {
        this.checkEquals(
            "Read only environment value: locale",
            new ReadOnlyEnvironmentValueNameException(EnvironmentValueName.LOCALE)
                .getMessage()
        );
    }

    // class............................................................................................................

    @Override
    public Class<ReadOnlyEnvironmentValueNameException> type() {
        return ReadOnlyEnvironmentValueNameException.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
