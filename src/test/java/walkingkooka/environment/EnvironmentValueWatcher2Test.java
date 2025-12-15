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

public final class EnvironmentValueWatcher2Test implements ClassTesting2<EnvironmentValueWatcher2> {

    // onEnvironmentValue...............................................................................................

    @Test
    public void testOnEnvironmentValueWithNullNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> new FakeEnvironmentValueWatcher2()
                .onEnvironmentValueChange(
                    null,
                    Optional.empty(),
                    Optional.empty()
                )
        );
    }

    @Test
    public void testOnEnvironmentValueWithNullOldValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> new FakeEnvironmentValueWatcher2()
                .onEnvironmentValueChange(
                    EnvironmentValueName.LOCALE,
                    null,
                    Optional.empty()
                )
        );
    }

    @Test
    public void testOnEnvironmentValueWithNullNewValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> new FakeEnvironmentValueWatcher2()
                .onEnvironmentValueChange(
                    EnvironmentValueName.LOCALE,
                    Optional.empty(),
                    null
                )
        );
    }

    // onEnvironmentValueAdd............................................................................................

    @Test
    public void testOnEnvironmentValueAdd() {
        this.fired = false;

        final EnvironmentValueName<?> name = EnvironmentValueName.LOCALE;
        final Locale value = Locale.ENGLISH;

        new FakeEnvironmentValueWatcher2() {
            @Override
            public void onEnvironmentValueAdd(final EnvironmentValueName<?> n,
                                              final Object nv) {
                checkEquals(name, n, "name");
                checkEquals(value, nv, "newValue");

                EnvironmentValueWatcher2Test.this.fired = true;
            }
        }.onEnvironmentValueChange(
            name,
            Optional.empty(),
            Optional.of(value)
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

        final EnvironmentValueName<?> name = EnvironmentValueName.LOCALE;
        final Locale value = Locale.ENGLISH;

        new FakeEnvironmentValueWatcher2() {

            @Override
            public void onEnvironmentValueRemove(final EnvironmentValueName<?> n,
                                                 final Object ov) {
                checkEquals(name, n, "name");
                checkEquals(value, ov, "oldValue");

                EnvironmentValueWatcher2Test.this.fired = true;
            }

        }.onEnvironmentValueChange(
            name,
            Optional.of(value),
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

        final EnvironmentValueName<?> name = EnvironmentValueName.LOCALE;
        final Locale oldValue = Locale.ENGLISH;
        final Locale newValue = Locale.FRENCH;

        new FakeEnvironmentValueWatcher2() {

            @Override
            public void onEnvironmentValueUpdate(final EnvironmentValueName<?> n,
                                                 final Object ov,
                                                 final Object nv) {
                checkEquals(name, n, "name");
                checkEquals(oldValue, ov, "oldValue");
                checkEquals(newValue, nv, "newValue");

                EnvironmentValueWatcher2Test.this.fired = true;
            }
        }.onEnvironmentValueChange(
            name,
            Optional.of(oldValue),
            Optional.of(newValue)
        );

        this.checkEquals(
            true,
            this.fired
        );
    }

    private boolean fired;

    static class FakeEnvironmentValueWatcher2 implements EnvironmentValueWatcher2 {

        @Override
        public void onEnvironmentValueAdd(final EnvironmentValueName<?> n,
                                          final Object nv) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void onEnvironmentValueRemove(final EnvironmentValueName<?> n,
                                             final Object ov) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void onEnvironmentValueUpdate(final EnvironmentValueName<?> n,
                                             final Object ov,
                                             final Object nv) {
            throw new UnsupportedOperationException();
        }
    }

    // class............................................................................................................

    @Override
    public Class<EnvironmentValueWatcher2> type() {
        return EnvironmentValueWatcher2.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
