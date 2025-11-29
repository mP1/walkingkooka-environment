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
import walkingkooka.collect.set.Sets;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.props.Properties;
import walkingkooka.props.PropertiesPath;
import walkingkooka.text.LineEnding;

import java.time.LocalDateTime;
import java.util.Locale;
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
    public EnvironmentContext cloneEnvironment() {
        final EnvironmentContext clone = with(
            this.properties, // immutable no need to clone
            this.context.cloneEnvironment()
        );

        return this.equals(clone) ?
            this :
            clone;
    }

    @Override
    public LineEnding lineEnding() {
        return this.context.lineEnding();
    }

    @Override
    public EnvironmentContext setLineEnding(final LineEnding lineEnding) {
        return this.setEnvironmentValue(
            LINE_ENDING,
            lineEnding
        );
    }
    
    @Override
    public Locale locale() {
        return this.context.locale();
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

        final Optional<?> value;

        if (LOCALE.equals(name)) {
            value = Optional.of(
                this.context.locale()
            );
        } else {
            if (USER.equals(name)) {
                value = this.context.user();
            } else {
                value = this.properties.get(
                    PropertiesPath.parse(name.value())
                );
            }
        }

        return Cast.to(value);
    }

    @Override
    public Set<EnvironmentValueName<?>> environmentValueNames() {
        if (null == this.names) {
            final Set<EnvironmentValueName<?>> names = Sets.ordered();

            this.properties.keys()
                .stream()
                .map(p -> EnvironmentValueName.with(p.value()))
                .forEach(names::add);

            names.add(LINE_ENDING);
            names.add(LOCALE);
            names.add(USER);

            this.names = Sets.immutable(names);
        }

        return this.names;
    }

    private Set<EnvironmentValueName<?>> names;

    @Override
    public <T> EnvironmentContext setEnvironmentValue(final EnvironmentValueName<T> name,
                                                      final T value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");

        if (LINE_ENDING.equals(name)) {
            this.context.setLineEnding((LineEnding) value);
        } else {
            if (LOCALE.equals(name)) {
                this.context.setLocale((Locale) value);
            } else {
                if (USER.equals(name)) {
                    this.context.setUser(
                        Optional.of((EmailAddress) value)
                    );
                } else {
                    throw new UnsupportedOperationException();
                }
            }
        }

        return this;
    }

    @Override
    public EnvironmentContext removeEnvironmentValue(final EnvironmentValueName<?> name) {
        Objects.requireNonNull(name, "name");

        throw new UnsupportedOperationException();
    }

    private final Properties properties;

    @Override
    public LocalDateTime now() {
        return this.context.now();
    }

    @Override
    public Optional<EmailAddress> user() {
        return this.context.user();
    }

    @Override
    public EnvironmentContext setUser(final Optional<EmailAddress> user) {
        this.context.setUser(user);
        return this;
    }

    private final EnvironmentContext context;

    @Override
    public String toString() {
        return this.properties.toString();
    }
}
