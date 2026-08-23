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

import walkingkooka.collect.set.Sets;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A {@link EnvironmentContext} around the parent {@link Environment}.
 */
final class EnvironmentEnvironmentContext implements EnvironmentContext {

    static EnvironmentEnvironmentContext with(final Environment environment) {
        return new EnvironmentEnvironmentContext(environment);
    }

    private EnvironmentEnvironmentContext(final Environment environment) {
        super();

        this.environment = environment;
    }

    @Override
    public EnvironmentContext cloneEnvironment() {
        throw new UnsupportedOperationException();
    }

    /**
     * Always returns the given {@link EnvironmentContext}.
     */
    @Override
    public EnvironmentContext setEnvironmentContext(final EnvironmentContext context) {
        Objects.requireNonNull(context, "context");
        return context;
    }

    @Override
    public <T> Optional<T> environmentValue(final EnvironmentValueName<T> name) {
        return this.environment.get(name);
    }

    @Override
    public Set<EnvironmentValueName<?>> environmentValueNames() {
        return Sets.readOnly(
            this.environment.values.keySet()
        );
    }

    @Override
    public <T> void setEnvironmentValue(final EnvironmentValueName<T> name,
                                        final T value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");

        throw name.readOnlyEnvironmentValueException();
    }

    @Override
    public void removeEnvironmentValue(final EnvironmentValueName<?> name) {
        Objects.requireNonNull(name, "name");

        throw name.readOnlyEnvironmentValueException();
    }

    @Override
    public Charset charset() {
        return this.environmentValueOrFail(CHARSET);
    }

    @Override
    public void setCharset(final Charset charset) {
        Objects.requireNonNull(charset, "charset");

        throw CHARSET.readOnlyEnvironmentValueException();
    }

    @Override
    public Currency currency() {
        return this.environmentValueOrFail(CURRENCY);
    }

    @Override
    public void setCurrency(final Currency currency) {
        Objects.requireNonNull(currency, "currency");

        throw CURRENCY.readOnlyEnvironmentValueException();
    }

    @Override
    public Indentation indentation() {
        return this.environmentValueOrFail(INDENTATION);
    }

    @Override
    public void setIndentation(final Indentation indentation) {
        Objects.requireNonNull(indentation, "indentation");

        throw INDENTATION.readOnlyEnvironmentValueException();
    }

    @Override
    public LineEnding lineEnding() {
        return this.environmentValueOrFail(LINE_ENDING);
    }

    @Override
    public void setLineEnding(final LineEnding lineEnding) {
        Objects.requireNonNull(lineEnding, "lineEnding");

        throw LINE_ENDING.readOnlyEnvironmentValueException();
    }

    @Override
    public Locale locale() {
        return this.environmentValueOrFail(LOCALE);
    }

    @Override
    public void setLocale(final Locale locale) {
        Objects.requireNonNull(locale, "locale");

        throw LOCALE.readOnlyEnvironmentValueException();
    }

    @Override
    public LocalDateTime now() {
        return this.environmentValueOrFail(NOW);
    }

    @Override
    public ZoneOffset timeOffset() {
        return this.environmentValueOrFail(TIME_OFFSET);
    }

    @Override
    public void setTimeOffset(final ZoneOffset timeOffset) {
        Objects.requireNonNull(timeOffset, "timeOffset");

        throw TIME_OFFSET.readOnlyEnvironmentValueException();
    }

    @Override
    public Optional<EmailAddress> user() {
        return this.environmentValue(USER);
    }

    @Override
    public void setUser(final Optional<EmailAddress> user) {
        Objects.requireNonNull(user, "user");

        throw USER.readOnlyEnvironmentValueException();
    }

    @Override
    public Runnable addEnvironmentWatcher(final EnvironmentWatcher watcher) {
        Objects.requireNonNull(watcher, "watcher");

        throw new UnsupportedOperationException();
    }

    @Override
    public Runnable addEnvironmentWatcherOnce(final EnvironmentWatcher watcher) {
        Objects.requireNonNull(watcher, "watcher");

        throw new UnsupportedOperationException();
    }

    @Override
    public EnvironmentValueName<?> parseEnvironmentValueName(final String name) {
        Objects.requireNonNull(name, "name");

        throw new UnsupportedOperationException();
    }

    // HasEnvironment...................................................................................................

    @Override
    public Environment environment() {
        return this.environment;
    }

    private final Environment environment;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.environment.toString();
    }
}
