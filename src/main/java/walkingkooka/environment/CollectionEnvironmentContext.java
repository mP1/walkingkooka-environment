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
import walkingkooka.datetime.HasNow;
import walkingkooka.net.email.EmailAddress;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link EnvironmentContext} that tries given context for a value until success.
 */
final class CollectionEnvironmentContext implements EnvironmentContext {

    static CollectionEnvironmentContext with(final List<EnvironmentContext> environmentContexts,
                                             final HasNow hasNow,
                                             final Optional<EmailAddress> user) {
        return new CollectionEnvironmentContext(
                Lists.immutable(
                        Objects.requireNonNull(environmentContexts, "environmentContexts")
                ),
                Objects.requireNonNull(hasNow, "hasNow"),
                Objects.requireNonNull(user, "user")
        );
    }

    private CollectionEnvironmentContext(final List<EnvironmentContext> environmentContexts,
                                         final HasNow hasNow,
                                         final Optional<EmailAddress> user) {
        this.environmentContexts = environmentContexts;
        this.hasNow = hasNow;
        this.user = user;
    }

    @Override
    public <T> Optional<T> environmentValue(final EnvironmentValueName<T> name) {
        return this.environmentContexts.stream()
                .map(c -> c.environmentValue(name))
                        .filter(Optional::isPresent)
                        .findFirst()
                .orElse(Optional.empty());
    }

    private List<EnvironmentContext> environmentContexts;

    @Override
    public LocalDateTime now() {
        return this.hasNow.now();
    }

    private final HasNow hasNow;

    @Override
    public Optional<EmailAddress> user() {
        return this.user;
    }

    private final Optional<EmailAddress> user;

    @Override
    public String toString() {
        return this.environmentContexts.toString();
    }
}
