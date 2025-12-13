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
import walkingkooka.Cast;
import walkingkooka.InvalidCharacterException;
import walkingkooka.collect.set.Sets;
import walkingkooka.compare.ComparableTesting2;
import walkingkooka.naming.NameTesting2;
import walkingkooka.reflect.ConstantsTesting;
import walkingkooka.text.CaseSensitivity;
import walkingkooka.text.printer.TreePrintableTesting;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class EnvironmentValueNameTest implements NameTesting2<EnvironmentValueName<String>, EnvironmentValueName<String>>,
    ComparableTesting2<EnvironmentValueName<String>>,
    TreePrintableTesting,
    ConstantsTesting<EnvironmentValueName<String>> {

    @Test
    public void testWithInvalidInitialFails() {
        this.withFails(
            "1abc",
            InvalidCharacterException.class,
            "Invalid character '1' at 0"
        );
    }

    @Test
    public void testWithInvalidPartFails() {
        this.withFails(
            "abc$def",
            InvalidCharacterException.class,
            "Invalid character '$' at 3"
        );
    }

    @Test
    public void testWithDotDotFails() {
        this.withFails(
            "abc..def",
            InvalidCharacterException.class,
            "Invalid character '.' at 4"
        );
    }

    private <T extends IllegalArgumentException> void withFails(final String text,
                                                                final Class<T> throwsClass,
                                                                final String message) {
        final T thrown = assertThrows(
            throwsClass,
            () -> EnvironmentValueName.with(text)
        );

        if (null != message) {
            this.checkEquals(
                message,
                thrown.getMessage(),
                "message"
            );
        }
    }

    @Test
    public void testWith2() {
        this.createNameAndCheck("ZZZ1");
    }

    @Test
    public void testWith3() {
        this.createNameAndCheck("A123Hello");
    }

    @Test
    public void testWith4() {
        this.createNameAndCheck("A1B2C2");
    }

    @Test
    public void testWithLetterDigits() {
        this.createNameAndCheck(
            "A1234567"
        );
    }

    @Test
    public void testWithLetterDigitsLetters() {
        this.createNameAndCheck(
            "A1B"
        );
    }

    @Test
    public void testWithLocale() {
        assertSame(
            EnvironmentValueName.LOCALE,
            EnvironmentValueName.with(
                EnvironmentValueName.LOCALE.value()
            )
        );
    }

    @Test
    public void testEqualsDifferentCase() {
        this.checkEqualsAndHashCode(
            EnvironmentValueName.with("Label123"),
            EnvironmentValueName.with("LABEL123")
        );
    }

    @Override
    public EnvironmentValueName<String> createName(final String name) {
        return EnvironmentValueName.with(name);
    }

    @Override
    public CaseSensitivity caseSensitivity() {
        return CaseSensitivity.INSENSITIVE;
    }

    @Override
    public String nameText() {
        return "state";
    }

    @Override
    public String differentNameText() {
        return "different";
    }

    @Override
    public String nameTextLess() {
        return "postcode";
    }

    @Override
    public int minLength() {
        return 1;
    }

    @Override
    public int maxLength() {
        return EnvironmentValueName.MAX_LENGTH;
    }

    @Override
    public String possibleValidChars(final int position) {
        return 0 == position ?
            ASCII_LETTERS :
            ASCII_LETTERS_DIGITS + "-.";
    }

    @Override
    public String possibleInvalidChars(final int position) {
        return 0 == position ?
            ASCII_DIGITS + CONTROL + "!@#$%^&*()" :
            CONTROL + "!@#$%^&*()";
    }

    // Comparator ......................................................................................................

    @Test
    public void testSort() {
        final EnvironmentValueName<String> a1 = EnvironmentValueName.with("a1");
        final EnvironmentValueName<String> b2 = EnvironmentValueName.with("B2");
        final EnvironmentValueName<String> c3 = EnvironmentValueName.with("C3");
        final EnvironmentValueName<String> d4 = EnvironmentValueName.with("d4");

        this.compareToArraySortAndCheck(
            d4, c3, a1, b2,
            a1, b2, c3, d4
        );
    }

    // toString.........................................................................................................

    @Test
    @Override
    public void testToString() {
        final String value = "Hello123";

        final EnvironmentValueName<?> name = EnvironmentValueName.with(value);
        this.toStringAndCheck(
            name,
            name.text()
        );
    }

    // constants........................................................................................................

    @Override
    public Set<EnvironmentValueName<String>> intentionalDuplicateConstants() {
        return Sets.empty();
    }

    // ClassTesting.....................................................................................................

    @Override
    public Class<EnvironmentValueName<String>> type() {
        return Cast.to(EnvironmentValueName.class);
    }
}
