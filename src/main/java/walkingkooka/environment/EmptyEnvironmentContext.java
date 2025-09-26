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
import walkingkooka.datetime.HasNow;
import walkingkooka.net.email.EmailAddress;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A {@link EnvironmentContext} that always returns no value. All setter like methods including {@link Locale} or
 * {@link EnvironmentValueName} values throw {@link UnsupportedOperationException}.
 */
final class EmptyEnvironmentContext implements EnvironmentContext {

    static EmptyEnvironmentContext with(final Locale locale,
                                        final HasNow hasNow,
                                        final Optional<EmailAddress> user) {
        return new EmptyEnvironmentContext(
            Objects.requireNonNull(locale, "locale"),
            Objects.requireNonNull(hasNow, "hasNow"),
            Objects.requireNonNull(user, "user")
        );
    }

    private EmptyEnvironmentContext(final Locale locale,
                                    final HasNow hasNow,
                                    final Optional<EmailAddress> user) {
        super();
        this.locale = locale;
        this.hasNow = hasNow;
        this.user = user;
    }

    @Override
    public Locale locale() {
        return this.locale;
    }

    @Override
    public EnvironmentContext setLocale(final Locale locale) {
        Objects.requireNonNull(locale, "locale");

        throw new UnsupportedOperationException();
    }

    private final Locale locale;

    /**
     * No need to clone because instances are immutable.
     */
    @Override
    public EnvironmentContext cloneEnvironment() {
        return this;
    }

    @Override
    public <T> Optional<T> environmentValue(final EnvironmentValueName<T> name) {
        Objects.requireNonNull(name, "name");
        return Optional.ofNullable(
            EnvironmentValueName.LOCALE.equals(name) ?
                Cast.to(this.locale) :
                null
        );
    }

    @Override
    public Set<EnvironmentValueName<?>> environmentValueNames() {
        return Sets.empty();
    }

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
        return "{}";
    }
}
