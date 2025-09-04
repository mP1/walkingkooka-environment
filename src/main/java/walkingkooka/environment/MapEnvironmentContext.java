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

import walkingkooka.Cast;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.email.EmailAddress;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A {@link EnvironmentContext} that stores {@link EnvironmentValueName} values in a {@link Map} delegating other
 * methods to the given {@link EnvironmentContext}.
 */
final class MapEnvironmentContext implements EnvironmentContext {

    static MapEnvironmentContext with(final EnvironmentContext context) {
        final MapEnvironmentContext map = new MapEnvironmentContext(
            Objects.requireNonNull(context, "context")
        );

        map.setEnvironmentValue(
            EnvironmentValueName.LOCALE,
            context.locale()
        );

        return map;
    }

    private MapEnvironmentContext(final EnvironmentContext context) {
        super();
        this.values = Maps.sorted();
        this.context = context;
    }

    @Override
    public EnvironmentContext cloneEnvironment() {
        final MapEnvironmentContext clone = new MapEnvironmentContext(
            Objects.requireNonNull(
                this.context.cloneEnvironment(),
                "context"
            )
        );

        clone.values.putAll(
            this.values
        );

        return clone;
    }

    @Override
    public Locale locale() {
        return this.environmentValueOrFail(EnvironmentValueName.LOCALE);
    }

    @Override
    public EnvironmentContext setLocale(final Locale locale) {
        return this.setEnvironmentValue(
            EnvironmentValueName.LOCALE,
            locale
        );
    }

    @Override
    public <T> Optional<T> environmentValue(final EnvironmentValueName<T> name) {
        Objects.requireNonNull(name, "name");
        return Optional.ofNullable(
            Cast.to(
                this.values.get(name)
            )
        );
    }

    @Override
    public Set<EnvironmentValueName<?>> environmentValueNames() {
        return Sets.readOnly(
            this.values.keySet()
        );
    }

    @Override
    public <T> EnvironmentContext setEnvironmentValue(final EnvironmentValueName<T> name,
                                                      final T value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");

        this.values.put(
            name,
            value
        );
        return this;
    }

    @Override
    public EnvironmentContext removeEnvironmentValue(final EnvironmentValueName<?> name) {
        Objects.requireNonNull(name, "name");

        this.values.remove(name);
        return this;
    }

    private final Map<EnvironmentValueName<?>, Object> values;

    @Override
    public LocalDateTime now() {
        return this.context.now();
    }

    @Override
    public Optional<EmailAddress> user() {
        return this.context.user();
    }

    private final EnvironmentContext context;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.values.toString();
    }
}
