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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.text.CharSequences;
import walkingkooka.text.LineEnding;
import walkingkooka.watch.Watchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A {@link EnvironmentContext} that tries given context for a value until success.
 * Note the {@link EnvironmentContext} 2nd parameter provides now and user.
 */
final class CollectionEnvironmentContext implements EnvironmentContext {

    static EnvironmentContext with(final List<EnvironmentContext> contexts) {
        Objects.requireNonNull(contexts, "contexts");

        final List<EnvironmentContext> copy = Lists.immutable(contexts);

        final EnvironmentContext context;

        switch (copy.size()) {
            case 0:
                throw new IllegalArgumentException("Empty EnvironmentContext(s)");
            case 1:
                context = copy.get(0);
                break;
            default:
                context = new CollectionEnvironmentContext(copy);
                break;
        }

        return context;
    }

    private CollectionEnvironmentContext(final List<EnvironmentContext> context) {
        this.contexts = context;
        this.first = context.get(0);
    }

    /**
     * Creates a new {@link CollectionEnvironmentContext} cloning each of the given {@link EnvironmentContext}.
     */
    @Override
    public EnvironmentContext cloneEnvironment() {
        return new CollectionEnvironmentContext(
            this.contexts.stream()
                .map(EnvironmentContext::cloneEnvironment)
                .peek(Objects::requireNonNull)
                .collect(Collectors.toList())
        );
    }

    @Override
    public LineEnding lineEnding() {
        return this.environmentValueOrFail(LINE_ENDING);
    }

    @Override
    public void setLineEnding(final LineEnding lineEnding) {
        this.setEnvironmentValue(
            LINE_ENDING,
            lineEnding
        );
    }

    @Override
    public Locale locale() {
        return this.environmentValueOrFail(EnvironmentValueName.LOCALE);
    }

    @Override
    public void setLocale(final Locale locale) {
        this.setEnvironmentValue(
            LOCALE,
            locale
        );
    }

    @Override
    public <T> Optional<T> environmentValue(final EnvironmentValueName<T> name) {
        Objects.requireNonNull(name, "name");

        return LINE_ENDING.equals(name) ?
            Optional.of(
                Cast.to(
                    this.first.lineEnding()
                )
            ) :
            LOCALE.equals(name) ?
                Optional.of(
                    Cast.to(
                        this.first.locale()
                    )
                ) :
                USER.equals(name) ?
                    Cast.to(
                        this.first.user()
                    ) :
                    this.contexts.stream()
                        .map(c -> c.environmentValue(name))
                        .filter(Optional::isPresent)
                        .findFirst()
                        .orElse(Optional.empty());
    }

    // assumes ALL wrapped EnvironmentContext are immutable.
    @Override
    public Set<EnvironmentValueName<?>> environmentValueNames() {
        if (null == this.names) {
            final SortedSet<EnvironmentValueName<?>> names = SortedSets.tree();

            for (final EnvironmentContext context : this.contexts) {
                names.addAll(context.environmentValueNames());
            }

            this.names = SortedSets.immutable(names);
        }

        return this.names;
    }

    private Set<EnvironmentValueName<?>> names;

    @Override
    public <T> void setEnvironmentValue(final EnvironmentValueName<T> name,
                                        final T value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");

        EnvironmentContext set = null;

        if (LINE_ENDING.equals(name) || LOCALE.equals(name) || USER.equals(name)) {
            set = this.first;
        } else {
            for (final EnvironmentContext context : this.contexts) {
                final Optional<?> currentValue = context.environmentValue(name);
                if (currentValue.isPresent()) {
                    set = context;
                    break;
                }

                set = context;
            }
        }

        if (null != set) {
            set.setEnvironmentValue(
                name,
                value
            );
        }
    }

    @Override
    public void removeEnvironmentValue(final EnvironmentValueName<?> name) {
        Objects.requireNonNull(name, "name");

        EnvironmentContext removeFrom = null;

        for (final EnvironmentContext context : this.contexts) {
            final Optional<?> value = context.environmentValue(name);
            if (value.isPresent()) {
                removeFrom = context;
                break;
            }

            removeFrom = context;
        }

        if (null != removeFrom) {
            removeFrom.removeEnvironmentValue(name);
        }
    }

    @Override
    public EnvironmentContext setEnvironmentContext(final EnvironmentContext context) {
        return Objects.requireNonNull(context, "context");
    }

    @Override
    public LocalDateTime now() {
        return this.first.now();
    }

    @Override
    public Optional<EmailAddress> user() {
        return this.environmentValue(USER);
    }

    @Override
    public void setUser(final Optional<EmailAddress> user) {
        Objects.requireNonNull(user, "user");

        final EmailAddress userOrNull = user.orElse(null);
        if (null != userOrNull) {
            this.setEnvironmentValue(
                USER,
                userOrNull
            );
        } else {
            this.removeEnvironmentValue(USER);
        }
    }

    @Override
    public Runnable addEventValueWatcher(final EnvironmentValueWatcher watcher) {
        return this.addWatcher(
            watcher,
            (w) -> w.addEventValueWatcher(watcher)
        );
    }

    @Override
    public Runnable addEventValueWatcherOnce(final EnvironmentValueWatcher watcher) {
        return this.addWatcher(
            watcher,
            (w) -> w.addEventValueWatcherOnce(watcher)
        );
    }

    private Runnable addWatcher(final EnvironmentValueWatcher watcher,
                                final Function<EnvironmentContext, Runnable> adder) {
        Objects.requireNonNull(watcher, "watcher");

        final List<Runnable> removers = Lists.array();

        for (final EnvironmentContext context : this.contexts) {
            removers.add(adder.apply(context));
        }

        return Watchers.runnableCollection(removers);
    }

    private final List<EnvironmentContext> contexts;

    /**
     * The first {@link #contexts} provides the {@link #LINE_ENDING}, {@link #LOCALE}, {@link #USER} and {@link #now()}.
     */
    private final EnvironmentContext first;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.contexts.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            (other instanceof CollectionEnvironmentContext &&
                this.equals0((CollectionEnvironmentContext) other));
    }

    private boolean equals0(final CollectionEnvironmentContext other) {
        return this.contexts.equals(other.contexts);
    }

    @Override
    public String toString() {
        final Map<EnvironmentValueName<?>, Object> map = Maps.ordered();

        for (final EnvironmentValueName<?> name : this.environmentValueNames()) {
            final Object value = this.environmentValue(name)
                .orElse(null);
            if (null != value) {
                map.put(
                    name,
                    LINE_ENDING.equals(name) ?
                        CharSequences.escape(
                            String.valueOf(value)
                        ) :
                        value
                );
            }
        }

        return map.toString();
    }
}
