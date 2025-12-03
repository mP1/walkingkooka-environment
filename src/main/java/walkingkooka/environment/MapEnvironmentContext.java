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
import walkingkooka.text.LineEnding;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A {@link EnvironmentContext} that stores {@link EnvironmentValueName} values in a {@link Map} delegating other
 * methods to the given {@link EnvironmentContext}. Note values from the given {@link EnvironmentContext} are ignored,
 * and it is never queried for values or names, cascading is not supported.
 */
final class MapEnvironmentContext implements EnvironmentContext {

    static MapEnvironmentContext with(final EnvironmentContext context) {
        return new MapEnvironmentContext(
            Objects.requireNonNull(context, "context")
        );
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
    public <T> Optional<T> environmentValue(final EnvironmentValueName<T> name) {
        Objects.requireNonNull(name, "name");

        Object value = this.values.get(name);
        if (null == value) {
            if (LINE_ENDING.equals(name)) {
                value = this.context.lineEnding();
            } else {
                if (LOCALE.equals(name)) {
                    value = this.context.locale();
                } else {
                    if (USER.equals(name)) {
                        value = this.context.user()
                            .orElse(null);
                    }
                }
            }
        }

        return Optional.ofNullable(
            Cast.to(value)
        );
    }

    @Override
    public Set<EnvironmentValueName<?>> environmentValueNames() {
        final Set<EnvironmentValueName<?>> names = SortedSets.tree();
        names.addAll(this.values.keySet());
        names.add(LINE_ENDING);
        names.add(LOCALE);

        if (this.context.user().isPresent()) {
            names.add(USER);
        }

        return Sets.readOnly(names);
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

    // HasLineEnding....................................................................................................

    @Override
    public LineEnding lineEnding() {
        return this.environmentValueOrFail(LINE_ENDING);
    }

    @Override
    public EnvironmentContext setLineEnding(final LineEnding lineEnding) {
        return this.setEnvironmentValue(
            LINE_ENDING,
            lineEnding
        );
    }

    // HasLocale........................................................................................................

    @Override
    public Locale locale() {
        return this.environmentValueOrFail(LOCALE);
    }

    @Override
    public EnvironmentContext setLocale(final Locale locale) {
        return this.setEnvironmentValue(
            LOCALE,
            locale
        );
    }

    // HasNow...........................................................................................................

    @Override
    public LocalDateTime now() {
        return this.context.now();
    }

    // HasUser..........................................................................................................

    @Override
    public Optional<EmailAddress> user() {
        return this.environmentValue(USER);
    }

    @Override
    public EnvironmentContext setUser(final Optional<EmailAddress> user) {
        Objects.requireNonNull(user, "user");

        return user.isPresent() ?
            this.setEnvironmentValue(
                USER,
                user.orElse(null)
            ) :
            this.removeEnvironmentValue(
                USER
            );
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
            (other instanceof MapEnvironmentContext &&
                this.equals0((MapEnvironmentContext) other));
    }

    private boolean equals0(final MapEnvironmentContext other) {
        return this.values.equals(other.values) &&
            this.context.equals(other.context);
    }

    @Override
    public String toString() {
        return this.values.toString();
    }
}
