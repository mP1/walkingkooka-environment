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

import org.junit.jupiter.api.Test;
import walkingkooka.environment.HasUser;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

public class EnvironmentConverterContextTest implements ClassTesting<EnvironmentConverterContext> {

    @Test
    public void testTypeMissingHasUser() {
        this.checkEquals(
            false,
            new FakeEnvironmentConverterContext() instanceof HasUser
        );
    }

    // class............................................................................................................

    @Override
    public Class<EnvironmentConverterContext> type() {
        return EnvironmentConverterContext.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
