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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;

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

    private final List<EnvironmentContext> environmentContexts;

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
        return this.environmentContexts.toString();
    }
}
