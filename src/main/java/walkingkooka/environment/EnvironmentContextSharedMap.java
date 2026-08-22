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
import walkingkooka.ToStringBuilderOption;
import walkingkooka.UsesToStringBuilder;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.datetime.HasNow;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.text.CharSequences;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.IndentingPrinter;

import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A {@link EnvironmentContext} that cascade gets, trying the wrapped {@link EnvironmentContext} and then the internal
 * {@link Map}.
 */
final class EnvironmentContextSharedMap extends EnvironmentContextShared
    implements HasEnvironmentWatchers,
    UsesToStringBuilder {

    static EnvironmentContextSharedMap with(final Charset charset,
                                            final Currency currency,
                                            final Indentation indentation,
                                            final LineEnding lineEnding,
                                            final Locale locale,
                                            final HasNow hasNow,
                                            final Optional<EmailAddress> user) {
        Objects.requireNonNull(charset, "charset");
        Objects.requireNonNull(currency, "currency");
        Objects.requireNonNull(indentation, "indentation");
        Objects.requireNonNull(lineEnding, "lineEnding");
        Objects.requireNonNull(locale, "locale");
        Objects.requireNonNull(hasNow, "hasNow");
        Objects.requireNonNull(user, "user");

        final Map<EnvironmentValueName<?>, EnvironmentContextSharedMapValue<?>> values = Maps.sorted();
        values.put(
            EnvironmentValueName.CHARSET,
            EnvironmentContextSharedMapValue.with(
                EnvironmentValueName.CHARSET,
                charset
            )
        );
        values.put(
            EnvironmentValueName.CURRENCY,
            EnvironmentContextSharedMapValue.with(
                EnvironmentValueName.CURRENCY,
                currency
            )
        );
        values.put(
            EnvironmentValueName.INDENTATION,
            EnvironmentContextSharedMapValue.with(
                EnvironmentValueName.INDENTATION,
                indentation
            )
        );
        values.put(
            EnvironmentValueName.LINE_ENDING,
            EnvironmentContextSharedMapValue.with(
                EnvironmentValueName.LINE_ENDING,
                lineEnding
            )
        );
        values.put(
            EnvironmentValueName.LOCALE,
            EnvironmentContextSharedMapValue.with(
                EnvironmentValueName.LOCALE,
                locale
            )
        );
        values.put(
            EnvironmentValueName.TIME_OFFSET,
            EnvironmentContextSharedMapValue.with(
                EnvironmentValueName.TIME_OFFSET,
                DEFAULT_TIME_OFFSET
            )
        );
        user.ifPresent(
            u -> values.put(
                EnvironmentValueName.USER,
                EnvironmentContextSharedMapValue.with(
                    EnvironmentValueName.USER,
                    u
                )
            )
        );

        return new EnvironmentContextSharedMap(
            values,
            hasNow
        );
    }

    private EnvironmentContextSharedMap(final Map<EnvironmentValueName<?>, EnvironmentContextSharedMapValue<?>> values,
                                        final HasNow hasNow) {
        super();

        this.values = values;
        this.hasNow = hasNow;
    }

    @Override
    public EnvironmentContext cloneEnvironment() {
        final Map<EnvironmentValueName<?>, EnvironmentContextSharedMapValue<?>> values = Maps.sorted();

        for (final EnvironmentContextSharedMapValue<?> value : this.values.values()) {
            values.put(
                value.environmentValueName,
                value.copy()
            );
        }

        return new EnvironmentContextSharedMap(
            values,
            this.hasNow
        );
    }

    /**
     * Returns the given {@link EnvironmentContext}.
     */
    @Override
    public EnvironmentContext setEnvironmentContext(final EnvironmentContext context) {
        return Objects.requireNonNull(context, "context");
    }

    @Override
    public <T> Optional<T> environmentValue(final EnvironmentValueName<T> name) {
        Objects.requireNonNull(name, "name");

        Object value;

        if (EnvironmentValueName.NOW.equals(name)) {
            value = this.hasNow.now();
        } else {
            final EnvironmentContextSharedMapValue<?> environmentContextSharedMapValue = this.values.get(name);
            if (null != environmentContextSharedMapValue) {
                value = environmentContextSharedMapValue.value;
            } else {
                if (EnvironmentValueName.TIME_OFFSET.equals(name)) {
                    value = DEFAULT_TIME_OFFSET;
                } else {
                    value = null;
                }
            }
        }

        return Optional.ofNullable(
            name.cast(value)
        );
    }

    private final HasNow hasNow;

    @Override
    public Set<EnvironmentValueName<?>> environmentValueNames() {
        final Set<EnvironmentValueName<?>> names = SortedSets.tree();

        for (EnvironmentContextSharedMapValue<?> value : this.values.values()) {
            names.add(
                value.environmentValueName
            );
        }

        names.add(EnvironmentValueName.NOW);
        names.add(EnvironmentValueName.TIME_OFFSET);

        return Sets.readOnly(names);
    }

    @Override
    public <T> void setEnvironmentValue(final EnvironmentValueName<T> name,
                                        final T value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");

        if (EnvironmentValueName.NOW.equals(name)) {
            throw name.readOnlyEnvironmentValueException();
        }

        final T oldValue;

        final Map<EnvironmentValueName<?>, EnvironmentContextSharedMapValue<?>> values = this.values;

        final EnvironmentContextSharedMapValue<T> environmentContextSharedMapValue = Cast.to(
            values.get(name)
        );
        if (null == environmentContextSharedMapValue) {
            this.values.put(
                name,
                EnvironmentContextSharedMapValue.with(
                    name,
                    value
                )
            );
            oldValue = null;
        } else {
            oldValue = environmentContextSharedMapValue.value;
            environmentContextSharedMapValue.value = Cast.to(value);
        }

        this.watchers.onValueChange(
            Optional.ofNullable(
                null != oldValue ?
                    EnvironmentValueNameAndValue.with(
                        name,
                        oldValue
                    ) :
                    null
            ),
            Optional.of(
                name.setValue(value)
            )
        );
    }

    @Override
    public void removeEnvironmentValue(final EnvironmentValueName<?> name) {
        Objects.requireNonNull(name, "name");

        if (CHARSET.equals(name) || CURRENCY.equals(name) || INDENTATION.equals(name) || LINE_ENDING.equals(name) || LOCALE.equals(name) || NOW.equals(name)) {
            throw name.readOnlyEnvironmentValueException();
        }

        final Object oldValue = this.values.remove(name);

        this.watchers.onValueChange(
            Optional.ofNullable(
                null != oldValue ?
                    EnvironmentValueNameAndValue.with(
                        name,
                        Cast.to(oldValue)
                    ) :
                    null
            ),
            Optional.empty()
        );
    }

    // @VisibleForTesting
    final Map<EnvironmentValueName<?>, EnvironmentContextSharedMapValue<?>> values;

    // HasEnvironmentWatchers...........................................................................................

    @Override
    public EnvironmentWatchers environmentValueWatchers() {
        return this.watchers;
    }

    private final EnvironmentWatchers watchers = EnvironmentWatchers.empty();

    // CanParseEnvironmentValueName.....................................................................................

    @Override
    public EnvironmentValueName<?> parseEnvironmentValueName(final String value) {
        EnvironmentValueName<?> environmentValueName = EnvironmentValueName.parseEnvironmentValueName(value)
            .orElse(null);
        if (null == environmentValueName) {
            environmentValueName = EnvironmentValueName.with(
                value,
                Object.class
            );

            EnvironmentContextSharedMapValue<?> environmentContextSharedMapValue = this.values.get(environmentValueName);
            if (null != environmentContextSharedMapValue) {
                environmentValueName = environmentContextSharedMapValue.environmentValueName;
            }
        }

        if (null == environmentValueName) {
            throw new IllegalArgumentException("Unknown environment value name " + CharSequences.quoteAndEscape(value));
        }

        return environmentValueName;
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
            this.values,
            this.hasNow
        );
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            (other instanceof EnvironmentContextSharedMap &&
                this.equals0((EnvironmentContextSharedMap) other));
    }

    private boolean equals0(final EnvironmentContextSharedMap other) {
        return this.values.equals(other.values) &&
            this.hasNow.equals(other.hasNow);
    }

    @Override
    public String toString() {
        return ToStringBuilder.buildFrom(this);
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    public void buildToString(final ToStringBuilder b) {
        b.enable(ToStringBuilderOption.ESCAPE);
        b.append('{');
        b.separator(", ");

        for (EnvironmentContextSharedMapValue<?> value : this.values.values()) {
            b.label(value.environmentValueName.value());
            b.value(
                value.value
            );
        }

        b.append('}');
    }


    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        this.printTreeValues(printer);
    }
}
