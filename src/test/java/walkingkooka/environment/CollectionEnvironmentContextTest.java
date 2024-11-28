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
import walkingkooka.collect.list.Lists;
import walkingkooka.props.Properties;
import walkingkooka.props.PropertiesPath;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class CollectionEnvironmentContextTest implements EnvironmentContextTesting2<CollectionEnvironmentContext> {

    private final static String NAME1 = "hello.111";

    private final static String VALUE1 = "Gday";

    private final static String NAME2 = "zebra.222";

    private final static String VALUE2 = "Orange";

    private final LocalDateTime NOW = LocalDateTime.MIN;

    @Test
    public void testWithNullPropertiesFails() {
        assertThrows(
                NullPointerException.class,
                () -> PropertiesEnvironmentContext.with(
                        null,
                        () -> NOW
                )
        );
    }

    @Test
    public void testWithNullHasNowFails() {
        assertThrows(
                NullPointerException.class,
                () -> PropertiesEnvironmentContext.with(
                        Properties.EMPTY,
                        null
                )
        );
    }

    @Test
    public void testEnvironmentalValue1() {
        this.environmentValueAndCheck(
                EnvironmentValueName.with(NAME1),
                VALUE1
        );
    }

    @Test
    public void testEnvironmentalValue2() {
        this.environmentValueAndCheck(
                EnvironmentValueName.with(NAME2),
                VALUE2
        );
    }

    @Test
    public void testEnvironmentalValue1Missing() {
        this.environmentValueAndCheck(
                EnvironmentValueName.with("Unknown")
        );
    }

    @Override
    public CollectionEnvironmentContext createContext() {
        return CollectionEnvironmentContext.with(
                Lists.of(
                        EnvironmentContexts.properties(
                                Properties.EMPTY.set(
                                        PropertiesPath.parse(NAME1),
                                        VALUE1
                                ),
                                () -> NOW
                        ),
                        EnvironmentContexts.properties(
                                Properties.EMPTY.set(
                                        PropertiesPath.parse(NAME1),
                                        "ignored!!!"
                                ).set(
                                        PropertiesPath.parse(NAME2),
                                        VALUE2
                                ),
                                () -> NOW
                        )
                ),
                () -> NOW
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createContext(),
                "[{hello.111=Gday}, {hello.111=ignored!!!, zebra.222=Orange}]"
        );
    }

    // type naming......................................................................................................

    @Override
    public String typeNameSuffix() {
        return EnvironmentContext.class.getSimpleName();
    }

    // class............................................................................................................

    @Override
    public Class<CollectionEnvironmentContext> type() {
        return CollectionEnvironmentContext.class;
    }
}
