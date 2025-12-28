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
import walkingkooka.collect.set.SortedSets;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.props.Properties;
import walkingkooka.props.PropertiesPath;
import walkingkooka.text.CharSequences;
import walkingkooka.text.LineEnding;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
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
        final EnvironmentContext before = this.context;
        final EnvironmentContext after = before.cloneEnvironment();

        return before == after ?
            this :
            with(
                this.properties, // immutable no need to clone
                after
            );
    }

    @Override
    public EnvironmentContext setEnvironmentContext(final EnvironmentContext context) {
        return Objects.requireNonNull(context, "context");
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
        final Set<EnvironmentValueName<?>> names = SortedSets.tree();

        this.properties.keys()
            .stream()
            .map(p -> EnvironmentValueName.with(
                    p.value(),
                    String.class
                )
            ).forEach(names::add);

        names.addAll(this.context.environmentValueNames());

        return Sets.immutable(names);
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

    @Override
    public Runnable addEventValueWatcher(final EnvironmentValueWatcher watcher) {
        return this.context.addEventValueWatcher(watcher);
    }

    @Override
    public Runnable addEventValueWatcherOnce(final EnvironmentValueWatcher watcher) {
        return this.context.addEventValueWatcherOnce(watcher);
    }

    private final EnvironmentContext context;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.context.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            (other instanceof PropertiesEnvironmentContext &&
                this.equals0((PropertiesEnvironmentContext) other));
    }

    private boolean equals0(final PropertiesEnvironmentContext other) {
        return this.properties.equals(other.properties) &&
            this.context.equals(other.context);
    }

    @Override
    public String toString() {
        final Map<EnvironmentValueName<?>, Object> map = Maps.sorted();

        for (final Entry<PropertiesPath, String> entry : this.properties.entries()) {
            map.put(
                EnvironmentValueName.with(
                    entry.getKey()
                        .value(),
                    String.class
                ),
                entry.getValue()
            );
        }

        map.put(
            LINE_ENDING,
            CharSequences.quoteAndEscape(
                this.lineEnding()
                    .toString()
            )
        );
        map.put(
            LOCALE,
            this.locale()
        );

        final EmailAddress user = this.user()
            .orElse(null);
        if (null != user) {
            map.put(
                USER,
                user
            );
        }

        return map.toString();
    }
}
