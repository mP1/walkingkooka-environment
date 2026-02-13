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

import java.util.Currency;
import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

final public class EnvironmentValueNameTest implements NameTesting2<EnvironmentValueName<String>, EnvironmentValueName<String>>,
    ComparableTesting2<EnvironmentValueName<String>>,
    TreePrintableTesting,
    ConstantsTesting<EnvironmentValueName<String>> {

    @Test
    public void testWithInvalidInitialFails() {
        final InvalidCharacterException thrown = assertThrows(
            InvalidCharacterException.class,
            () -> EnvironmentValueName.with(
                "1abc",
                String.class
            )
        );

        this.checkEquals(
            "Invalid character '1' at 0",
            thrown.getMessage(),
            "message"
        );
    }

    @Test
    public void testWithInvalidPartFails() {
        final InvalidCharacterException thrown = assertThrows(
            InvalidCharacterException.class,
            () -> EnvironmentValueName.with(
                "abc$def",
                String.class
            )
        );

        this.checkEquals(
            "Invalid character '$' at 3",
            thrown.getMessage(),
            "message"
        );
    }

    @Test
    public void testWithDotDotFails() {
        final InvalidCharacterException thrown = assertThrows(
            InvalidCharacterException.class,
            () -> EnvironmentValueName.with(
                "abc..def",
                String.class
            )
        );

        this.checkEquals(
            "Invalid character '.' at 4",
            thrown.getMessage(),
            "message"
        );
    }

    @Test
    public void testWithNullTypeFails() {
        assertThrows(
            NullPointerException.class,
            () -> EnvironmentValueName.with(
                "xyz",
                null
            )
        );
    }

    @Test
    public void testWithDifferentTypeFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> EnvironmentValueName.with(
                "locale",
                Integer.class
            )
        );

        this.checkEquals(
            "Invalid type \"java.lang.Integer\" expected \"java.util.Locale\"",
            thrown.getMessage()
        );
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
    public void testWithCurrency() {
        assertSame(
            EnvironmentValueName.CURRENCY,
            EnvironmentValueName.with(
                EnvironmentValueName.CURRENCY.value(),
                Currency.class
            )
        );
    }
    
    @Test
    public void testWithLocale() {
        assertSame(
            EnvironmentValueName.LOCALE,
            EnvironmentValueName.with(
                EnvironmentValueName.LOCALE.value(),
                Locale.class
            )
        );
    }

    // registerConstant.................................................................................................

    @Test
    public void testRegisterConstantWithDifferentType() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> EnvironmentValueName.registerConstant(
                "locale",
                String.class
            )
        );

        this.checkEquals(
            "Invalid type \"java.lang.String\" expected \"java.util.Locale\"",
            thrown.getMessage()
        );
    }

    @Test
    public void testRegisterConstantDuplicateNameAndType() {
        final EnvironmentValueName<Locale> name = EnvironmentValueName.registerConstant(
            "locale",
            Locale.class
        );

        assertSame(
            name,
            EnvironmentValueName.registerConstant(
                "locale",
                Locale.class
            )
        );
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferentCase() {
        this.checkEqualsAndHashCode(
            EnvironmentValueName.with(
                "Label123",
                String.class
            ),
            EnvironmentValueName.with(
                "LABEL123",
                String.class
            )
        );
    }

    @Override
    public EnvironmentValueName<String> createName(final String name) {
        return EnvironmentValueName.with(
            name,
            String.class
        );
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
        final EnvironmentValueName<String> a1 = EnvironmentValueName.with("a1", String.class);
        final EnvironmentValueName<String> b2 = EnvironmentValueName.with("B2", String.class);
        final EnvironmentValueName<String> c3 = EnvironmentValueName.with("C3", String.class);
        final EnvironmentValueName<String> d4 = EnvironmentValueName.with("d4", String.class);

        this.compareToArraySortAndCheck(
            d4, c3, a1, b2,
            a1, b2, c3, d4
        );
    }

    // hashCode/equals..................................................................................................

    @Test
    public void testEqualsDifferentTypeType() {
        final String name = "name123";

        this.checkNotEquals(
            EnvironmentValueName.with(
                name,
                String.class
            ),
            EnvironmentValueName.with(
                name,
                Integer.class
            )
        );
    }

    // toString.........................................................................................................

    @Test
    @Override
    public void testToString() {
        final String value = "Hello123";

        final EnvironmentValueName<String> name = EnvironmentValueName.with(
            value,
            String.class
        );
        this.toStringAndCheck(
            name,
            name.text()
        );
    }

    // cast.............................................................................................................

    @Test
    public void testCast() {
        final Locale locale = Locale.ENGLISH;
        final Object value = locale;

        this.checkEquals(
            locale,
            EnvironmentValueName.LOCALE.cast(value)
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
