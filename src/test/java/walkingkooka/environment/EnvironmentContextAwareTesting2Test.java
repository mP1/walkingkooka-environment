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

import walkingkooka.environment.EnvironmentContextAwareTesting2Test.TestEnvironmentContextAware;

import java.util.Objects;

public final class EnvironmentContextAwareTesting2Test implements EnvironmentContextAwareTesting2<TestEnvironmentContextAware> {

    @Override
    public TestEnvironmentContextAware createEnvironmentContextAware() {
        return new TestEnvironmentContextAware();
    }

    final static class TestEnvironmentContextAware implements EnvironmentContextAware {
        @Override
        public void setEnvironmentContext(EnvironmentContext context) {
            Objects.requireNonNull(context, "context");
        }
    }
}
