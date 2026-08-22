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

import walkingkooka.environment.CanParseEnvironmentValueNameDelegatorTest.TestCanParseEnvironmentValueNameDelegator;

public final class CanParseEnvironmentValueNameDelegatorTest implements CanParseEnvironmentValueNameTesting2<TestCanParseEnvironmentValueNameDelegator>,
    EnvironmentContextTesting{

    @Override
    public TestCanParseEnvironmentValueNameDelegator createCanParseEnvironmentValueName() {
        return new TestCanParseEnvironmentValueNameDelegator();
    }

    final static class TestCanParseEnvironmentValueNameDelegator implements CanParseEnvironmentValueNameDelegator {

        @Override
        public CanParseEnvironmentValueName canParseEnvironmentValueName() {
            return EnvironmentContexts.map(
                CHARSET,
                CURRENCY,
                INDENTATION,
                LINE_ENDING,
                LOCALE,
                HAS_NOW,
                OPTIONAL_USER
            );
        }
    }
}
