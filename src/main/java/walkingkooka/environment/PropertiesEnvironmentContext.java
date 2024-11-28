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

import walkingkooka.datetime.HasNow;
import walkingkooka.props.Properties;
import walkingkooka.props.PropertiesPath;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * An {@link EnvironmentContext} that sources all values from a given {@link Properties}.
 */
final class PropertiesEnvironmentContext implements EnvironmentContext {

    static PropertiesEnvironmentContext with(final Properties properties,
                                             final HasNow hasNow) {
        return new PropertiesEnvironmentContext(
                Objects.requireNonNull(properties, "properties"),
                Objects.requireNonNull(hasNow, "hasNow")
        );
    }

    private PropertiesEnvironmentContext(final Properties properties,
                                         final HasNow hasNow) {
        this.properties = properties;
        this.hasNow = hasNow;
    }

    @Override
    public <T> Optional<T> environmentValue(final EnvironmentValueName<T> name) {
        return (Optional<T>) this.properties.get(
                PropertiesPath.parse(name.value())
        );
    }

    private final Properties properties;

    @Override
    public LocalDateTime now() {
        return this.hasNow.now();
    }

    private final HasNow hasNow;

    @Override
    public String toString() {
        return this.properties.toString();
    }
}
