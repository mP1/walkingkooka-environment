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
import walkingkooka.HasValueTesting;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.naming.HasNameTesting;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.LineEnding;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EnvironmentValueNameAndValueTest implements HasNameTesting,
    HasValueTesting,
    HashCodeEqualsDefinedTesting2<EnvironmentValueNameAndValue<LineEnding>>,
    ToStringTesting<EnvironmentValueNameAndValue<LineEnding>>,
    ClassTesting<EnvironmentValueNameAndValue<LineEnding>> {


    private final static EnvironmentValueName<LineEnding> NAME = EnvironmentValueName.LINE_ENDING;

    private final static LineEnding VALUE = LineEnding.NL;

    // with.............................................................................................................

    @Test
    public void testWithNullNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> EnvironmentValueNameAndValue.with(
                null,
                VALUE
            )
        );
    }

    @Test
    public void testWithNullValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> EnvironmentValueNameAndValue.with(
                NAME,
                null
            )
        );
    }

    @Test
    public void testWith() {
        final EnvironmentValueNameAndValue<LineEnding> nameAndValue = this.createObject();
        this.nameAndCheck(
            nameAndValue,
            NAME
        );
        this.valueAndCheck(
            nameAndValue,
            VALUE
        );
    }

    // setName..........................................................................................................

    @Test
    public void testSetNameWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createObject()
                .setName(null)
        );
    }

    @Test
    public void testSetNameWithSame() {
        final EnvironmentValueNameAndValue<LineEnding> environmentValueNameAndValue = this.createObject();

        assertSame(
            environmentValueNameAndValue,
            environmentValueNameAndValue.setName(NAME)
        );
    }

    @Test
    public void testSetNameWithDifferent() {
        final EnvironmentValueNameAndValue<LineEnding> environmentValueNameAndValue = this.createObject();

        final EnvironmentValueName<LineEnding> differentName = EnvironmentValueName.with(
            "DifferentLineEnding",
            LineEnding.class
        );

        final EnvironmentValueNameAndValue<LineEnding> different = environmentValueNameAndValue.setName(differentName);

        assertNotSame(
            environmentValueNameAndValue,
            different
        );

        this.nameAndCheck(
            different,
            differentName
        );

        this.valueAndCheck(
            different,
            VALUE
        );

        this.nameAndCheck(
            environmentValueNameAndValue,
            NAME
        );

        this.valueAndCheck(
            environmentValueNameAndValue,
            VALUE
        );
    }

    // hashCode/equals..................................................................................................

    @Test
    public void testEqualsDifferentName() {
        this.checkNotEquals(
            EnvironmentValueNameAndValue.with(
                EnvironmentValueName.with(
                    "Hello",
                    LineEnding.class
                ),
                VALUE
            )
        );
    }

    @Test
    public void testEqualsDifferentValue() {
        this.checkNotEquals(
            EnvironmentValueNameAndValue.with(
                NAME,
                LineEnding.CRNL
            )
        );
    }

    @Override
    public EnvironmentValueNameAndValue<LineEnding> createObject() {
        return EnvironmentValueNameAndValue.with(
            NAME,
            VALUE
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createObject(),
            "lineEnding=\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<EnvironmentValueNameAndValue<LineEnding>> type() {
        return Cast.to(EnvironmentValueNameAndValue.class);
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
