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
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EnvironmentWatcher2Test implements ClassTesting2<EnvironmentWatcher2> {

    // onEnvironmentValue...............................................................................................

    @Test
    public void testOnEnvironmentValueWithNullOldValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> new FakeEnvironmentWatcher2()
                .onEnvironmentValueChange(
                    null,
                    Optional.empty()
                )
        );
    }

    @Test
    public void testOnEnvironmentValueWithNullNewValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> new FakeEnvironmentWatcher2()
                .onEnvironmentValueChange(
                    Optional.empty(),
                    null
                )
        );
    }

    // onEnvironmentValueAdd............................................................................................

    @Test
    public void testOnEnvironmentValueAdd() {
        this.fired = false;

        final EnvironmentValueName<Locale> name = EnvironmentValueName.LOCALE;
        final Locale value = Locale.ENGLISH;

        new FakeEnvironmentWatcher2() {
            @Override
            public void onEnvironmentValueAdd(final EnvironmentValueNameAndValue<?> nv) {
                checkEquals(
                    name.setValue(value),
                    nv
                );

                EnvironmentWatcher2Test.this.fired = true;
            }
        }.onEnvironmentValueChange(
            Optional.empty(),
            Optional.of(
                name.setValue(value)
            )
        );

        this.checkEquals(
            true,
            this.fired
        );
    }

    // onEnvironmentValueRemove.........................................................................................

    @Test
    public void testOnEnvironmentValueRemove() {
        this.fired = false;

        final EnvironmentValueName<Locale> name = EnvironmentValueName.LOCALE;
        final Locale value = Locale.ENGLISH;

        new FakeEnvironmentWatcher2() {

            @Override
            public void onEnvironmentValueRemove(final EnvironmentValueNameAndValue<?> nv) {
                checkEquals(
                    name.setValue(value),
                    nv
                );

                EnvironmentWatcher2Test.this.fired = true;
            }

        }.onEnvironmentValueChange(
            Optional.of(
                name.setValue(value)
            ),
            Optional.empty()
        );

        this.checkEquals(
            true,
            this.fired
        );
    }

    // onEnvironmentValueUpdate.........................................................................................

    @Test
    public void testOnEnvironmentValueUpdate() {
        this.fired = false;

        final EnvironmentValueName<Locale> name = EnvironmentValueName.LOCALE;
        final Locale oldValue = Locale.ENGLISH;
        final Locale newValue = Locale.FRENCH;

        new FakeEnvironmentWatcher2() {

            @Override
            public void onEnvironmentValueUpdate(final EnvironmentValueNameAndValue<?> ov,
                                                 final EnvironmentValueNameAndValue<?> nv) {
                checkEquals(
                    name.setValue(oldValue),
                    ov,
                    "oldValue"
                );
                checkEquals(
                    name.setValue(newValue),
                    nv,
                    "newValue"
                );

                EnvironmentWatcher2Test.this.fired = true;
            }
        }.onEnvironmentValueChange(
            Optional.of(
                name.setValue(oldValue)
            ),
            Optional.of(
                name.setValue(newValue)
            )
        );

        this.checkEquals(
            true,
            this.fired
        );
    }

    private boolean fired;

    static class FakeEnvironmentWatcher2 implements EnvironmentWatcher2 {

        @Override
        public void onEnvironmentValueAdd(final EnvironmentValueNameAndValue<?> nv) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void onEnvironmentValueRemove(final EnvironmentValueNameAndValue<?> nv) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void onEnvironmentValueUpdate(final EnvironmentValueNameAndValue<?> ov,
                                             final EnvironmentValueNameAndValue<?> nv) {
            throw new UnsupportedOperationException();
        }
    }

    // class............................................................................................................

    @Override
    public Class<EnvironmentWatcher2> type() {
        return EnvironmentWatcher2.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
