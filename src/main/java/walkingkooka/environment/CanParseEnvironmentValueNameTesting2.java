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

import static org.junit.jupiter.api.Assertions.assertThrows;

public interface CanParseEnvironmentValueNameTesting2<C extends CanParseEnvironmentValueName> extends CanParseEnvironmentValueNameTesting {

    @Test
    default void testParseEnvironmentValueNameWithNullNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createCanParseEnvironmentValueName()
                .parseEnvironmentValueName(null)
        );
    }

    @Test
    default void testParseEnvironmentValueNameWithNullUnknownFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> this.createCanParseEnvironmentValueName()
                .parseEnvironmentValueName("?unknown")
        );
    }

    @Test
    default void testParseEnvironmentValueNameWithCharset() {
        this.parseEnvironmentValueNameAndCachedCheck(EnvironmentValueName.CHARSET);
    }

    @Test
    default void testParseEnvironmentValueNameWithCurrency() {
        this.parseEnvironmentValueNameAndCachedCheck(EnvironmentValueName.CURRENCY);
    }

    @Test
    default void testParseEnvironmentValueNameWithIndentation() {
        this.parseEnvironmentValueNameAndCachedCheck(EnvironmentValueName.INDENTATION);
    }

    @Test
    default void testParseEnvironmentValueNameWithLineEnding() {
        this.parseEnvironmentValueNameAndCachedCheck(EnvironmentValueName.LINE_ENDING);
    }

    @Test
    default void testParseEnvironmentValueNameWithLocale() {
        this.parseEnvironmentValueNameAndCachedCheck(EnvironmentValueName.LOCALE);
    }

    @Test
    default void testParseEnvironmentValueNameWithNow() {
        this.parseEnvironmentValueNameAndCachedCheck(EnvironmentValueName.NOW);
    }

    @Test
    default void testParseEnvironmentValueNameWithTimeOffset() {
        this.parseEnvironmentValueNameAndCachedCheck(EnvironmentValueName.TIME_OFFSET);
    }

    @Test
    default void testParseEnvironmentValueNameWithUser() {
        this.parseEnvironmentValueNameAndCachedCheck(EnvironmentValueName.USER);
    }

    default void parseEnvironmentValueNameAndCheck(final EnvironmentValueName<?> expected) {
        this.parseEnvironmentValueNameAndCheck(
            expected.value(),
            expected
        );
    }

    default void parseEnvironmentValueNameAndCheck(final String name) {
        this.parseEnvironmentValueNameAndCheck(
            this.createCanParseEnvironmentValueName(),
            name
        );
    }

    default void parseEnvironmentValueNameAndCheck(final String name,
                                                   final EnvironmentValueName<?> expected) {
        this.parseEnvironmentValueNameAndCheck(
            this.createCanParseEnvironmentValueName(),
            name,
            expected
        );
    }

    default void parseEnvironmentValueNameAndCachedCheck(final EnvironmentValueName<?> expected) {
        this.parseEnvironmentValueNameAndCachedCheck(
            this.createCanParseEnvironmentValueName(),
            expected
        );
    }

    default void parseEnvironmentValueNameAndCachedCheck(final String name,
                                                         final EnvironmentValueName<?> expected) {
        this.parseEnvironmentValueNameAndCachedCheck(
            this.createCanParseEnvironmentValueName(),
            name,
            expected
        );
    }

    C createCanParseEnvironmentValueName();
}
