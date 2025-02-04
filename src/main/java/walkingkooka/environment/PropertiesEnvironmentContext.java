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

import walkingkooka.collect.set.ImmutableSet;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.props.Properties;
import walkingkooka.props.PropertiesPath;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * An {@link EnvironmentContext} that sources all values from a given {@link Properties}.
 */
final class PropertiesEnvironmentContext implements EnvironmentContext {

    static PropertiesEnvironmentContext with(final Properties properties,
                                             final EnvironmentContext context) {
        return new PropertiesEnvironmentContext(
            Objects.requireNonNull(properties, "properties"),
            Objects.requireNonNull(context, "context")
        );
    }

    private PropertiesEnvironmentContext(final Properties properties,
                                         final EnvironmentContext context) {
        this.properties = properties;
        this.context = context;
    }

    @Override
    public <T> Optional<T> environmentValue(final EnvironmentValueName<T> name) {
        return (Optional<T>) this.properties.get(
            PropertiesPath.parse(name.value())
        );
    }

    @Override
    public Set<EnvironmentValueName<?>> environmentValueNames() {
        if (null == this.names) {
            this.names = this.properties.keys()
                .stream()
                .map(p -> EnvironmentValueName.with(p.value()))
                .collect(ImmutableSet.collector());
        }

        return this.names;
    }

    private Set<EnvironmentValueName<?>> names;

    private final Properties properties;

    @Override
    public LocalDateTime now() {
        return this.context.now();
    }

    @Override
    public Optional<EmailAddress> user() {
        return this.context.user();
    }

    private final EnvironmentContext context;

    @Override
    public String toString() {
        return this.properties.toString();
    }
}
