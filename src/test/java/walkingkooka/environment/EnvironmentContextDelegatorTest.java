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

import walkingkooka.environment.EnvironmentContextDelegatorTest.TestEnvironmentContextDelegator;

import java.time.LocalDateTime;
import java.util.Objects;

public final class EnvironmentContextDelegatorTest implements EnvironmentContextTesting2<TestEnvironmentContextDelegator> {

    @Override
    public TestEnvironmentContextDelegator createContext() {
        return new TestEnvironmentContextDelegator();
    }

    // class............................................................................................................

    @Override
    public String typeNameSuffix() {
        return "Delegator";
    }

    @Override
    public Class<TestEnvironmentContextDelegator> type() {
        return TestEnvironmentContextDelegator.class;
    }

    final static class TestEnvironmentContextDelegator implements EnvironmentContextDelegator {

        @Override
        public EnvironmentContext environmentContext() {
            return EnvironmentContexts.empty(
                LocalDateTime::now,
                EnvironmentContext.ANONYMOUS
            );
        }

        @Override
        public <T> EnvironmentContext setEnvironmentValue(final EnvironmentValueName<T> name,
                                                          final T value) {
            Objects.requireNonNull(name, "name");
            Objects.requireNonNull(value, "value");

            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }
}
