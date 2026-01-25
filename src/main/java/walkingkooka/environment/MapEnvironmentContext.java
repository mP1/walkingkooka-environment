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
import walkingkooka.text.CharSequences;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A {@link EnvironmentContext} that cascade gets, trying the wrapped {@link EnvironmentContext} and then the internal
 * {@link Map}.
 */
final class MapEnvironmentContext extends EnvironmentContextShared
    implements HasEnvironmentValueWatchers {

    static MapEnvironmentContext with(final EnvironmentContext context) {
        return new MapEnvironmentContext(
            Objects.requireNonNull(context, "context")
        );
    }

    private MapEnvironmentContext(final EnvironmentContext context) {
        super(context);
        this.values = Maps.sorted();
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
    public EnvironmentContext setEnvironmentContext(final EnvironmentContext context) {
        return Objects.requireNonNull(context, "context");
    }

    @Override
    public <T> Optional<T> environmentValue(final EnvironmentValueName<T> name) {
        Objects.requireNonNull(name, "name");

        Object value = this.context.environmentValue(name)
            .orElse(null);
        if (null == value) {
            value = this.values.get(name);
        }

        return Optional.ofNullable(
            Cast.to(value)
        );
    }

    @Override
    public Set<EnvironmentValueName<?>> environmentValueNames() {
        final Set<EnvironmentValueName<?>> names = SortedSets.tree();
        names.addAll(this.values.keySet());
        names.add(INDENTATION);
        names.add(LINE_ENDING);
        names.add(LOCALE);
        names.add(NOW);

        if (this.context.user().isPresent()) {
            names.add(USER);
        }

        return Sets.readOnly(names);
    }

    @Override
    public <T> void setEnvironmentValue(final EnvironmentValueName<T> name,
                                        final T value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");

        final EnvironmentContext context = this.context;

        Object oldValue;
        boolean put = false;

        if (INDENTATION.equals(name)) {
            oldValue = context.indentation();
        } else {
            if (LINE_ENDING.equals(name)) {
                oldValue = context.lineEnding();
            } else {
                if (LOCALE.equals(name)) {
                    oldValue = context.locale();
                } else {
                    if (NOW.equals(name)) {
                        oldValue = context.now();
                    } else {
                        if (USER.equals(name)) {
                            oldValue = this.context.user()
                                .orElse(null);
                        } else {
                            oldValue = this.values.get(name);
                            put = true;
                        }
                    }
                }
            }
        }

        if (put) {
            this.values.put(
                name,
                value
            );
        } else {
            context.setEnvironmentValue(
                name,
                value
            );
        }

        this.watchers.onEnvironmentValueChange(
            name,
            Optional.ofNullable(oldValue),
            Optional.of(value)
        );
    }

    @Override
    public void removeEnvironmentValue(final EnvironmentValueName<?> name) {
        Objects.requireNonNull(name, "name");

        final EnvironmentContext context = this.context;

        Object oldValue;

        if (INDENTATION.equals(name)) {
            oldValue = context.indentation();
            context.removeEnvironmentValue(name);
        } else {
            if (LINE_ENDING.equals(name)) {
                oldValue = context.lineEnding();
                context.removeEnvironmentValue(name);
            } else {
                if (LOCALE.equals(name)) {
                    oldValue = context.locale();
                    context.removeEnvironmentValue(name);
                } else {
                    if (NOW.equals(name)) {
                        oldValue = context.now();
                        context.removeEnvironmentValue(name);
                    } else {
                        if (USER.equals(name)) {
                            oldValue = context.user()
                                .orElse(null);
                            context.removeEnvironmentValue(name);
                        } else {
                            oldValue = this.values.get(name);
                            this.values.remove(name);
                        }
                    }
                }
            }
        }

        this.watchers.onEnvironmentValueChange(
            name,
            Optional.ofNullable(oldValue),
            Optional.empty()
        );
    }

    private final Map<EnvironmentValueName<?>, Object> values;

    // HasEnvironmentValueWatchers......................................................................................

    @Override
    public EnvironmentValueWatchers environmentValueWatchers() {
        return this.watchers;
    }

    private final EnvironmentValueWatchers watchers = EnvironmentValueWatchers.empty();

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
        final Map<EnvironmentValueName<?>, Object> map = Maps.sorted();
        map.putAll(this.values);

        map.put(
            INDENTATION,
            CharSequences.quoteAndEscape(
                this.indentation()
                    .toString()
            )
        );

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
