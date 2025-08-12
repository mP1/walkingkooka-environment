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
import walkingkooka.ToStringTesting;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class MapEnvironmentContextTest implements EnvironmentContextTesting2<MapEnvironmentContext>,
    ToStringTesting<MapEnvironmentContext> {

    private final static LocalDateTime NOW = LocalDateTime.MIN;

    private final static EnvironmentContext CONTEXT = EnvironmentContexts.empty(
        () -> NOW,
        EnvironmentContext.ANONYMOUS
    );

    private final static EnvironmentValueName<String> NAME = EnvironmentValueName.with("hello.123");

    private final static String VALUE = "Gday";

    // with.............................................................................................................

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> MapEnvironmentContext.with(
                null
            )
        );
    }

    // environmentValue.................................................................................................

    @Test
    public void testEnvironmentalValue() {
        final MapEnvironmentContext context = MapEnvironmentContext.with(CONTEXT);
        context.setEnvironmentValue(
            NAME,
            VALUE
        );

        this.environmentValueAndCheck(
            context,
            NAME,
            VALUE
        );
    }

    @Test
    public void testEnvironmentalValueMissing() {
        this.environmentValueAndCheck(
            this.createContext(),
            EnvironmentValueName.with("Unknown")
        );
    }

    // environmentValueNames............................................................................................

    @Test
    public void testEnvironmentalValueNames() {
        final EnvironmentValueName<String> name1 = EnvironmentValueName.with("prefix.name1");

        final MapEnvironmentContext context = MapEnvironmentContext.with(CONTEXT);
        context.setEnvironmentValue(
            name1,
            VALUE
        );

        final EnvironmentValueName<String> name2 = EnvironmentValueName.with("prefix.name2");

        context.setEnvironmentValue(
            name2,
            VALUE
        );

        {
            final EnvironmentValueName<String> name3 = EnvironmentValueName.with("REMOVED");

            context.setEnvironmentValue(
                name3,
                VALUE
            );
            context.removeEnvironmentValue(
                name3
            );
        }

        this.environmentValueNamesAndCheck(
            context,
            name1,
            name2
        );
    }

    @Override
    public MapEnvironmentContext createContext() {
        return MapEnvironmentContext.with(CONTEXT);
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        final MapEnvironmentContext context = MapEnvironmentContext.with(CONTEXT);
        context.setEnvironmentValue(
            NAME,
            VALUE
        );

        this.toStringAndCheck(
            context,
            "{hello.123=Gday}"
        );
    }

    // type naming......................................................................................................

    @Override
    public String typeNameSuffix() {
        return EnvironmentContext.class.getSimpleName();
    }

    // class............................................................................................................

    @Override
    public Class<MapEnvironmentContext> type() {
        return MapEnvironmentContext.class;
    }
}
