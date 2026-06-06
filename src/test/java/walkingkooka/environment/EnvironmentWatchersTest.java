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
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EnvironmentWatchersTest implements ClassTesting<EnvironmentWatchers> {

    @Test
    public void testAddWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> EnvironmentWatchers.empty()
                .add(null)
        );
    }

    @Test
    public void testAddOnceWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> EnvironmentWatchers.empty()
                .addOnce(null)
        );
    }

    @Test
    public void testAddThenFire() {
        this.fired = false;

        final EnvironmentValueName<Locale> name = EnvironmentValueName.LOCALE;
        final Locale oldValue = Locale.FRANCE;
        final Locale newValue = Locale.GERMANY;

        final EnvironmentWatchers watchers = EnvironmentWatchers.empty();
        watchers.add(
            new EnvironmentWatcher() {


                @Override
                public void onEnvironmentValueChange(final Optional<EnvironmentValueNameAndValue<?>> ov,
                                                     final Optional<EnvironmentValueNameAndValue<?>> nv) {
                    checkEquals(
                        Optional.of(
                            name.setValue(oldValue)
                        ),
                        ov
                    );
                    checkEquals(
                        Optional.of(
                            name.setValue(newValue)
                        ),
                        nv
                    );

                    fired = true;
                }
            });
        watchers.onEnvironmentValueChange(
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

    @Test
    public void testAddThenFireEqualValues() {
        this.fired = false;

        final EnvironmentWatchers watchers = EnvironmentWatchers.empty();
        watchers.add(
            new EnvironmentWatcher() {
                @Override
                public void onEnvironmentValueChange(final Optional<EnvironmentValueNameAndValue<?>> oldValue,
                                                     final Optional<EnvironmentValueNameAndValue<?>> newValue) {
                    throw new UnsupportedOperationException();
                }
            }
        );

        final EnvironmentValueNameAndValue<Locale> environmentValueNameAndValue = EnvironmentValueName.LOCALE.setValue(Locale.FRANCE);

        watchers.onEnvironmentValueChange(
            Optional.of(environmentValueNameAndValue),
            Optional.of(environmentValueNameAndValue)
        );

        this.checkEquals(
            false,
            this.fired
        );
    }

    @Test
    public void testAddOnceThenFire() {
        this.fired = false;

        final EnvironmentValueName<Locale> name = EnvironmentValueName.LOCALE;
        final Optional<EnvironmentValueNameAndValue<?>> oldValue = Optional.of(
            name.setValue(Locale.FRANCE)
        );
        final Optional<EnvironmentValueNameAndValue<?>> newValue = Optional.of(
            name.setValue(Locale.GERMANY)
        );

        final EnvironmentWatchers watchers = EnvironmentWatchers.empty();
        watchers.addOnce(
            new EnvironmentWatcher() {
                @Override
                public void onEnvironmentValueChange(final Optional<EnvironmentValueNameAndValue<?>> ov,
                                                     final Optional<EnvironmentValueNameAndValue<?>> nv) {
                    checkEquals(
                        false,
                        fired,
                        "event should only have been fired once!"
                    );

                    checkEquals(oldValue, ov);
                    checkEquals(newValue, nv);

                    fired = true;
                }
            });
        watchers.onEnvironmentValueChange(
            oldValue,
            newValue
        );

        this.checkEquals(
            true,
            this.fired
        );
    }

    private boolean fired = false;

    // ClassTesting....................................................................................................

    @Override
    public Class<EnvironmentWatchers> type() {
        return EnvironmentWatchers.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
