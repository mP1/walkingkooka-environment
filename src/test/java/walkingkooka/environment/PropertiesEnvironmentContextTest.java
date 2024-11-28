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
import walkingkooka.props.Properties;
import walkingkooka.props.PropertiesPath;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class PropertiesEnvironmentContextTest implements EnvironmentContextTesting2<PropertiesEnvironmentContext>,
        ToStringTesting<PropertiesEnvironmentContext> {

    private final static LocalDateTime NOW = LocalDateTime.MIN;

    private final static String NAME = "hello.123";

    private final static String VALUE = "Gday";

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

    // environmentValue.................................................................................................

    @Test
    public void testEnvironmentalValue() {
        this.environmentValueAndCheck(
                this.createContext(),
                EnvironmentValueName.with(NAME),
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

    @Override
    public PropertiesEnvironmentContext createContext() {
        return PropertiesEnvironmentContext.with(
                Properties.EMPTY.set(
                        PropertiesPath.parse(NAME),
                        VALUE
                ),
                () -> NOW
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
                this.createContext(),
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
    public Class<PropertiesEnvironmentContext> type() {
        return PropertiesEnvironmentContext.class;
    }
}
