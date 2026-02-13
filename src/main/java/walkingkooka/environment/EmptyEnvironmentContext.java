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
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Currency;
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
    UsesToStringBuilder,
    TreePrintable {

    static EmptyEnvironmentContext with(final Currency currency,
                                        final Indentation indentation,
                                        final LineEnding lineEnding,
                                        final Locale locale,
                                        final HasNow hasNow,
                                        final Optional<EmailAddress> user) {
        return new EmptyEnvironmentContext(
            Objects.requireNonNull(currency, "currency"),
            Objects.requireNonNull(indentation, "indentation"),
            Objects.requireNonNull(lineEnding, "hasLineEnding"),
            Objects.requireNonNull(locale, "locale"),
            Objects.requireNonNull(hasNow, "hasNow"),
            DEFAULT_TIME_OFFSET,
            Objects.requireNonNull(user, "user")
        );
    }

    private EmptyEnvironmentContext(final Currency currency,
                                    final Indentation indentation,
                                    final LineEnding lineEnding,
                                    final Locale locale,
                                    final HasNow hasNow,
                                    final ZoneOffset timeOffset,
                                    final Optional<EmailAddress> user) {
        super();

        this.currency = currency;
        this.indentation = indentation;
        this.lineEnding = lineEnding;
        this.locale = locale;
        this.hasNow = hasNow;
        this.timeOffset = timeOffset;
        this.user = user;
    }

    // EnvironmentContext...............................................................................................

    /**
     * Creates a new instance with the same/current values.
     */
    @Override
    public EnvironmentContext cloneEnvironment() {
        return new EmptyEnvironmentContext(
            this.currency,
            this.indentation,
            this.lineEnding,
            this.locale,
            this.hasNow,
            this.timeOffset,
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
                CURRENCY.equals(name) ?
                    this.currency :
                    INDENTATION.equals(name) ?
                        this.indentation :
                        LINE_ENDING.equals(name) ?
                            this.lineEnding :
                            LOCALE.equals(name) ?
                                this.locale :
                                NOW.equals(name) ?
                                    this.now() :
                                    TIME_OFFSET.equals(name) ?
                                        this.timeOffset :
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
        CURRENCY,
        INDENTATION,
        LINE_ENDING,
        LOCALE,
        NOW,
        TIME_OFFSET,
        USER
    );

    private final static Set<EnvironmentValueName<?>> NAMES_WITHOUT_USER = Sets.of(
        CURRENCY,
        INDENTATION,
        LINE_ENDING,
        NOW,
        LOCALE,
        TIME_OFFSET
    );

    @Override
    public <T> void setEnvironmentValue(final EnvironmentValueName<T> name,
                                        final T value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");

        if (CURRENCY.equals(name)) {
            this.setCurrency((Currency) value);
        } else {
            if (INDENTATION.equals(name)) {
                this.setIndentation((Indentation) value);
            } else {
                if (LINE_ENDING.equals(name)) {
                    this.setLineEnding((LineEnding) value);
                } else {
                    if (LOCALE.equals(name)) {
                        this.setLocale((Locale) value);
                    } else {
                        if (TIME_OFFSET.equals(name)) {
                            this.setTimeOffset((ZoneOffset) value);
                        } else {
                            if (USER.equals(name)) {
                                this.setUser(
                                    Optional.of((EmailAddress) value)
                                );
                            } else {
                                throw name.readOnlyEnvironmentValueException();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void removeEnvironmentValue(final EnvironmentValueName<?> name) {
        Objects.requireNonNull(name, "name");

        if (CURRENCY.equals(name) || INDENTATION.equals(name) || LINE_ENDING.equals(name) || LOCALE.equals(name) || NOW.equals(name)) {
            throw name.readOnlyEnvironmentValueException();
        } else {
            if (TIME_OFFSET.equals(name)) {
                this.timeOffset = DEFAULT_TIME_OFFSET;
            } else {
                if (USER.equals(name)) {
                    this.user = ANONYMOUS;
                }
            }
        }

        // ignore all other removes because the value doesnt exist
    }

    // HasCurrency...................................................................................................

    @Override
    public Currency currency() {
        return this.currency;
    }

    @Override
    public void setCurrency(final Currency currency) {
        Objects.requireNonNull(currency, "currency");

        final Currency oldCurrency = this.currency;
        this.currency = currency;

        this.watchers.onEnvironmentValueChange(
            CURRENCY,
            Optional.of(oldCurrency),
            Optional.of(currency)
        );
    }

    private Currency currency;

    // HasIndentation...................................................................................................

    @Override
    public Indentation indentation() {
        return this.indentation;
    }

    @Override
    public void setIndentation(final Indentation indentation) {
        Objects.requireNonNull(indentation, "indentation");

        final Indentation oldIndentation = this.indentation;
        this.indentation = indentation;

        this.watchers.onEnvironmentValueChange(
            INDENTATION,
            Optional.of(oldIndentation),
            Optional.of(indentation)
        );
    }

    private Indentation indentation;

    // HasLineEnding....................................................................................................

    @Override
    public LineEnding lineEnding() {
        return this.lineEnding;
    }

    @Override
    public void setLineEnding(final LineEnding lineEnding) {
        Objects.requireNonNull(lineEnding, "lineEnding");

        final LineEnding oldLineEnding = this.lineEnding;
        this.lineEnding = lineEnding;

        this.watchers.onEnvironmentValueChange(
            LINE_ENDING,
            Optional.of(oldLineEnding),
            Optional.of(lineEnding)
        );
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
        return this.hasNow.now()
            .atOffset(this.timeOffset())
            .toLocalDateTime();
    }

    private final HasNow hasNow;

    // timeOffset.......................................................................................................

    @Override
    public ZoneOffset timeOffset() {
        return this.timeOffset;
    }

    @Override
    public void setTimeOffset(final ZoneOffset timeOffset) {
        final ZoneOffset oldTimeOffset = this.timeOffset;
        this.timeOffset = Objects.requireNonNull(timeOffset, "timeOffset");

        this.watchers.onEnvironmentValueChange(
            TIME_OFFSET,
            Optional.of(oldTimeOffset),
            Optional.of(timeOffset)
        );
    }

    private ZoneOffset timeOffset;

    // HasUser..........................................................................................................

    @Override
    public Optional<EmailAddress> user() {
        return this.user;
    }

    @Override
    public void setUser(final Optional<EmailAddress> user) {
        Objects.requireNonNull(user, "user");

        final Optional<EmailAddress> oldUser = this.user;
        this.user = user;

        this.watchers.onEnvironmentValueChange(
            USER,
            oldUser,
            user
        );
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
            this.currency,
            this.indentation,
            this.lineEnding,
            this.locale,
            this.hasNow,
            this.timeOffset,
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
        return this.currency.equals(other.currency) &&
            this.indentation.equals(other.indentation) &&
            this.lineEnding.equals(other.lineEnding) &&
            this.locale.equals(other.locale) &&
            this.hasNow.equals(other.hasNow) &&
            this.timeOffset.equals(other.timeOffset) &&
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

        b.label("currency");
        b.value(
            CharSequences.escape(this.currency.toString())
        );

        b.label("indentation");
        b.value(
            CharSequences.escape(this.indentation)
        );

        b.label("lineEnding");
        b.value(
            CharSequences.escape(this.lineEnding)
        );

        b.label("locale");
        b.value(this.locale.toLanguageTag());

        b.label("now");
        b.value(this.now());

        final ZoneOffset timeOffset = this.timeOffset;

        if (timeOffset.getTotalSeconds() != 0) {
            b.label("timeOffset");
            b.value(this.timeOffset);
        }

        b.label("user");
        b.value(this.user.map(EmailAddress::toString));

        b.append('}');
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            for (final EnvironmentValueName<?> name : this.environmentValueNames()) {
                final Object value = this.environmentValue(name)
                    .orElse(null);
                if (null != value) {
                    printer.println(name.value());
                    printer.indent();
                    {
                        TreePrintable.printTreeOrToString(
                            value,
                            printer
                        );
                    }
                    printer.outdent();
                }
            }
        }
        printer.outdent();
    }
}
