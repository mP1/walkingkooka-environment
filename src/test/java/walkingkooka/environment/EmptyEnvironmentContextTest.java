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
import walkingkooka.ToStringTesting;

public final class EmptyEnvironmentContextTest implements EnvironmentContextTesting2<EmptyEnvironmentContext>,
        ToStringTesting<EmptyEnvironmentContext> {

    @Test
    public void testEnvironmentalValue() {
        this.environmentValueAndCheck(
                this.createContext(),
                EnvironmentValueName.with("Hello123")
        );
    }

    @Override
    public EmptyEnvironmentContext createContext() {
        return EmptyEnvironmentContext.INSTANCE;
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createContext(),
                "{}"
        );
    }

    // type naming......................................................................................................

    @Override
    public String typeNameSuffix() {
        return EnvironmentContext.class.getSimpleName();
    }

    // class............................................................................................................

    @Override
    public Class<EmptyEnvironmentContext> type() {
        return EmptyEnvironmentContext.class;
    }
}
