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

import walkingkooka.net.email.EmailAddress;
import walkingkooka.text.LineEnding;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Wraps another {@link EnvironmentContext} presenting a read only view, with all setXXX and removeXXX
 * throwing {@link UnsupportedOperationException}.
 * Note {@link #cloneEnvironment()} returns a clone not the original, which may not be read-only.
 * If the wrapped {@link EnvironmentContext} allows modification then the clone will allow modifications.
 */
final class ReadOnlyEnvironmentContext implements EnvironmentContext {

    static ReadOnlyEnvironmentContext with(final EnvironmentContext context) {
        ReadOnlyEnvironmentContext readOnlyEnvironmentContext;

        Objects.requireNonNull(context, "context");

        if (context instanceof ReadOnlyEnvironmentContext) {
            readOnlyEnvironmentContext = (ReadOnlyEnvironmentContext) context;
        } else {
            readOnlyEnvironmentContext = new ReadOnlyEnvironmentContext(context);
        }

        return readOnlyEnvironmentContext;
    }

    private ReadOnlyEnvironmentContext(final EnvironmentContext context) {
        super();
        this.context = context;
    }

    @Override
    public LineEnding lineEnding() {
        return this.context.lineEnding();
    }

    @Override
    public EnvironmentContext setLineEnding(final LineEnding lineEnding) {
        Objects.requireNonNull(lineEnding, "lineEnding");

        throw new UnsupportedOperationException();
    }
    
    @Override
    public Locale locale() {
        return this.context.locale();
    }

    @Override
    public EnvironmentContext setLocale(final Locale locale) {
        Objects.requireNonNull(locale, "locale");

        throw new UnsupportedOperationException();
    }

    /**
     * Makes a clone of the wrapped {@link EnvironmentContext} returning that.
     */
    @Override
    public EnvironmentContext cloneEnvironment() {
        return context.cloneEnvironment();
    }

    @Override
    public <T> Optional<T> environmentValue(final EnvironmentValueName<T> name) {
        return this.context.environmentValue(name);
    }

    @Override
    public Set<EnvironmentValueName<?>> environmentValueNames() {
        return this.context.environmentValueNames();
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
            (other instanceof ReadOnlyEnvironmentContext &&
                this.equals0((ReadOnlyEnvironmentContext) other));
    }

    private boolean equals0(final ReadOnlyEnvironmentContext other) {
        return this.context.equals(other.context);
    }


    @Override
    public String toString() {
        return this.context.toString();
    }
}
