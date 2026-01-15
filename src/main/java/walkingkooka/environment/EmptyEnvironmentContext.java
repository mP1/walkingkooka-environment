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
import walkingkooka.ToStringBuilder;
import walkingkooka.UsesToStringBuilder;
import walkingkooka.collect.set.Sets;
import walkingkooka.datetime.HasNow;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.text.CharSequences;
import walkingkooka.text.LineEnding;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A {@link EnvironmentContext} that is mutable but only contains the minimum required values of {@link #LINE_ENDING},
 * {@link #LOCALE} and {@link #USER}.
 * Attempts to get any other {@link EnvironmentValueName} will return nothing, setting/removing will throw {@link UnsupportedOperationException}.
 */
final class EmptyEnvironmentContext implements EnvironmentContext,
    HasEnvironmentValueWatchers,
    UsesToStringBuilder {

    static EmptyEnvironmentContext with(final LineEnding lineEnding,
                                        final Locale locale,
                                        final HasNow hasNow,
                                        final Optional<EmailAddress> user) {
        return new EmptyEnvironmentContext(
            Objects.requireNonNull(lineEnding, "hasLineEnding"),
            Objects.requireNonNull(locale, "locale"),
            Objects.requireNonNull(hasNow, "hasNow"),
            Objects.requireNonNull(user, "user")
        );
    }

    private EmptyEnvironmentContext(final LineEnding lineEnding,
                                    final Locale locale,
                                    final HasNow hasNow,
                                    final Optional<EmailAddress> user) {
        super();
        this.lineEnding = lineEnding;
        this.locale = locale;
        this.hasNow = hasNow;
        this.user = user;
    }

    // EnvironmentContext...............................................................................................

    /**
     * Creates a new instance with the same/current values.
     */
    @Override
    public EnvironmentContext cloneEnvironment() {
        return new EmptyEnvironmentContext(
            this.lineEnding,
            this.locale,
            this.hasNow,
            this.user
        );
    }

    @Override
    public EnvironmentContext setEnvironmentContext(final EnvironmentContext context) {
        return Objects.requireNonNull(context, "context");
    }

    @Override
    public <T> Optional<T> environmentValue(final EnvironmentValueName<T> name) {
        Objects.requireNonNull(name, "name");

        return Cast.to(
            Optional.ofNullable(
                LINE_ENDING.equals(name) ?
                    this.lineEnding :
                    LOCALE.equals(name) ?
                        this.locale :
                        NOW.equals(name) ?
                            this.now() :
                            USER.equals(name) ?
                                this.user.orElse(null) :
                                null
            )
        );
    }

    @Override
    public Set<EnvironmentValueName<?>> environmentValueNames() {
        return this.user.isPresent() ?
            NAMES :
            NAMES_WITHOUT_USER;
    }

    private final static Set<EnvironmentValueName<?>> NAMES = Sets.of(
        LINE_ENDING,
        LOCALE,
        NOW,
        USER
    );

    private final static Set<EnvironmentValueName<?>> NAMES_WITHOUT_USER = Sets.of(
        LINE_ENDING,
        NOW,
        LOCALE
    );

    @Override
    public <T> EnvironmentContext setEnvironmentValue(final EnvironmentValueName<T> name,
                                                      final T value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");

        if (LINE_ENDING.equals(name)) {
            this.setLineEnding((LineEnding) value);
        } else {
            if (LOCALE.equals(name)) {
                this.setLocale((Locale) value);
            } else {
                if (USER.equals(name)) {
                    this.setUser(
                        Optional.of((EmailAddress) value)
                    );
                } else {
                    throw new ReadOnlyEnvironmentValueException(name);
                }
            }
        }

        return this;
    }

    @Override
    public EnvironmentContext removeEnvironmentValue(final EnvironmentValueName<?> name) {
        Objects.requireNonNull(name, "name");

        if (LINE_ENDING.equals(name) || LOCALE.equals(name) || NOW.equals(name) || (USER.equals(name) && this.user.isPresent())) {
            throw new ReadOnlyEnvironmentValueException(name);
        }

        // ignore all other removes because the value doesnt exist
        return this;
    }

    // HasLineEnding....................................................................................................

    @Override
    public LineEnding lineEnding() {
        return this.lineEnding;
    }

    @Override
    public EnvironmentContext setLineEnding(final LineEnding lineEnding) {
        Objects.requireNonNull(lineEnding, "lineEnding");

        final LineEnding oldLineEnding = this.lineEnding;
        this.lineEnding = lineEnding;

        this.watchers.onEnvironmentValueChange(
            LINE_ENDING,
            Optional.of(oldLineEnding),
            Optional.of(lineEnding)
        );

        return this;
    }

    private LineEnding lineEnding;

    // HasLocale........................................................................................................

    @Override
    public Locale locale() {
        return this.locale;
    }

    @Override
    public void setLocale(final Locale locale) {
        Objects.requireNonNull(locale, "locale");

        final Locale oldLocale = this.locale;
        this.locale = locale;

        this.watchers.onEnvironmentValueChange(
            LOCALE,
            Optional.of(oldLocale),
            Optional.of(locale)
        );
    }

    private Locale locale;

    // HasNow...........................................................................................................

    @Override
    public LocalDateTime now() {
        return this.hasNow.now();
    }

    private final HasNow hasNow;

    // HasUser..........................................................................................................

    @Override
    public Optional<EmailAddress> user() {
        return this.user;
    }

    @Override
    public EnvironmentContext setUser(final Optional<EmailAddress> user) {
        Objects.requireNonNull(user, "user");

        final Optional<EmailAddress> oldUser = this.user;
        this.user = user;

        this.watchers.onEnvironmentValueChange(
            USER,
            oldUser,
            user
        );

        return this;
    }

    private Optional<EmailAddress> user;

    // HasEnvironmentValueWatchers......................................................................................

    @Override
    public EnvironmentValueWatchers environmentValueWatchers() {
        return this.watchers;
    }

    private final EnvironmentValueWatchers watchers = EnvironmentValueWatchers.empty();

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
            this.lineEnding,
            this.locale,
            this.hasNow,
            this.user
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            (other instanceof EmptyEnvironmentContext &&
                this.equals0((EmptyEnvironmentContext) other));
    }

    private boolean equals0(final EmptyEnvironmentContext other) {
        return this.lineEnding.equals(other.lineEnding) &&
            this.locale.equals(other.locale) &&
            this.hasNow.equals(other.hasNow) &&
            this.user.equals(other.user);
    }

    @Override
    public String toString() {
        return ToStringBuilder.buildFrom(this);
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    public void buildToString(final ToStringBuilder b) {
        b.labelSeparator("=")
            .separator(", ");
        b.append('{');

        b.label("lineEnding");
        b.value(
            CharSequences.escape(this.lineEnding)
        );

        b.label("locale");
        b.value(this.locale.toLanguageTag());

        b.label("now");
        b.value(this.now());

        b.label("user");
        b.value(this.user.map(EmailAddress::toString));

        b.append('}');
    }
}
