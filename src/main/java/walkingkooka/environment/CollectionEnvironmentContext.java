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

import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.text.LineEnding;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.stream.Collectors;

/**
 * A {@link EnvironmentContext} that tries given context for a value until success.
 * Note the {@link EnvironmentContext} 2nd parameter provides now and user.
 */
final class CollectionEnvironmentContext implements EnvironmentContext {

    static CollectionEnvironmentContext with(final List<EnvironmentContext> environmentContexts,
                                             final EnvironmentContext context) {
        return new CollectionEnvironmentContext(
            Lists.immutable(
                Objects.requireNonNull(environmentContexts, "environmentContexts")
            ),
            Objects.requireNonNull(context, "context")
        );
    }

    private CollectionEnvironmentContext(final List<EnvironmentContext> environmentContexts,
                                         final EnvironmentContext context) {
        this.environmentContexts = environmentContexts;
        this.context = context;
    }

    /**
     * Creates a new {@link CollectionEnvironmentContext} cloning each of the given {@link EnvironmentContext}.
     */
    @Override
    public EnvironmentContext cloneEnvironment() {
        return new CollectionEnvironmentContext(
            this.environmentContexts.stream()
                .map(EnvironmentContext::cloneEnvironment)
                .peek(Objects::requireNonNull)
                .collect(Collectors.toList()),
            Objects.requireNonNull(
                this.context.cloneEnvironment()
            )
        );
    }

    /**
     * Try all given {@link EnvironmentContext} for a {@link LineEnding} or default to the given {@link EnvironmentContext} as the default.
     */
    @Override
    public LineEnding lineEnding() {
        return this.environmentValue(LINE_ENDING)
            .orElseGet(() -> this.context.lineEnding());
    }

    @Override
    public EnvironmentContext setLineEnding(final LineEnding lineEnding) {
        return this.setEnvironmentValue(
            LINE_ENDING,
            lineEnding
        );
    }
    
    /**
     * Try all given {@link EnvironmentContext} for a locale or default to the given {@link EnvironmentContext} as the default.
     */
    @Override
    public Locale locale() {
        return this.environmentValue(EnvironmentValueName.LOCALE)
            .orElseGet(() -> this.context.locale());
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
        return this.environmentContexts.stream()
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

            for (final EnvironmentContext context : this.environmentContexts) {
                names.addAll(context.environmentValueNames());
            }

            this.names = SortedSets.immutable(names);
        }

        return this.names;
    }

    private Set<EnvironmentValueName<?>> names;

    @Override
    public <T> EnvironmentContext setEnvironmentValue(final EnvironmentValueName<T> name,
                                                      final T value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");

        throw new UnsupportedOperationException();
    }

    @Override
    public EnvironmentContext removeEnvironmentValue(final EnvironmentValueName<?> name) {
        Objects.requireNonNull(name, "name");

        throw new UnsupportedOperationException();
    }

    private final List<EnvironmentContext> environmentContexts;

    @Override
    public EnvironmentContext setEnvironmentContext(final EnvironmentContext context) {
        return Objects.requireNonNull(context, "context");
    }

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
        Objects.requireNonNull(user, "user");
        throw new UnsupportedOperationException();
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
            (other instanceof CollectionEnvironmentContext &&
                this.equals0((CollectionEnvironmentContext) other));
    }

    private boolean equals0(final CollectionEnvironmentContext other) {
        return this.environmentContexts.equals(other.environmentContexts) &&
            this.context.equals(other.context);
    }

    @Override
    public String toString() {
        return this.environmentContexts.toString();
    }
}
