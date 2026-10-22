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

import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.TreePrintableTesting;

import static org.junit.jupiter.api.Assertions.assertSame;

public interface CanParseEnvironmentValueNameTesting extends TreePrintableTesting {

    CanParseEnvironmentValueName CAN_PARSE_ENVIRONMENT_VALUE_NAME = new CanParseEnvironmentValueName() {
        @Override
        public EnvironmentValueName<?> parseEnvironmentValueName(String name) {
            return EnvironmentContextTesting.ENVIRONMENT_CONTEXT.parseEnvironmentValueName(name);
        }

        // stable toString so #toString tests wont break
        @Override
        public String toString() {
            return EnvironmentContextTesting.ENVIRONMENT_CONTEXT.toString();
        }
    };

    default void parseEnvironmentValueNameAndCheck(final CanParseEnvironmentValueName can,
                                                   final String expected) {
        final EnvironmentValueName<?> environmentValueName = can.parseEnvironmentValueName(expected);
        this.checkEquals(
            expected,
            environmentValueName.value()
        );
    }

    default void parseEnvironmentValueNameAndCheck(final CanParseEnvironmentValueName can,
                                                   final EnvironmentValueName<?> expected) {
        this.parseEnvironmentValueNameAndCheck(
            can,
            expected.value(),
            expected
        );
    }

    default void parseEnvironmentValueNameAndCheck(final CanParseEnvironmentValueName can,
                                                   final String name,
                                                   final EnvironmentValueName<?> expected) {
        this.checkEquals(
            expected,
            can.parseEnvironmentValueName(name),
            name
        );
    }

    default void parseEnvironmentValueNameAndCachedCheck(final CanParseEnvironmentValueName can,
                                                         final EnvironmentValueName<?> expected) {
        this.parseEnvironmentValueNameAndCachedCheck(
            can,
            expected.value(),
            expected
        );
    }

    default void parseEnvironmentValueNameAndCachedCheck(final CanParseEnvironmentValueName can,
                                                         final String name,
                                                         final EnvironmentValueName<?> expected) {
        this.parseEnvironmentValueNameAndCheck(
            can,
            name,
            expected
        );

        assertSame(
            can.parseEnvironmentValueName(name),
            can.parseEnvironmentValueName(name),
            () -> "parseEnvironmentValueName " + CharSequences.quoteIfChars(name) + " not cached"
        );
    }
}
