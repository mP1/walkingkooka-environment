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

        final Map<EnvironmentValueName<?>, Object> values = Maps.sorted();
        values.put(
            EnvironmentValueName.CHARSET,
            charset
        );
        values.put(
            EnvironmentValueName.CURRENCY,
            currency
        );
        values.put(
            EnvironmentValueName.INDENTATION,
            indentation
        );
        values.put(
            EnvironmentValueName.LINE_ENDING,
            lineEnding
        );
        values.put(
            EnvironmentValueName.LOCALE,
            locale
        );
        values.put(
            EnvironmentValueName.TIME_OFFSET,
            DEFAULT_TIME_OFFSET
        );
        user.ifPresent(
            u -> values.put(
                EnvironmentValueName.USER,
                u
            )
        );

        return new EnvironmentContextSharedMap(
            values,
            hasNow
        );
    }

    private EnvironmentContextSharedMap(final Map<EnvironmentValueName<?>, Object> values,
                                        final HasNow hasNow) {
        super();

        this.values = values;
        this.hasNow = hasNow;
    }

    @Override
    public EnvironmentContext cloneEnvironment() {
        final Map<EnvironmentValueName<?>, Object> values = Maps.sorted();

        values.putAll(this.values);

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
            value = this.values.get(name);
            if (null == value) {
                if (EnvironmentValueName.TIME_OFFSET.equals(name)) {
                    value = DEFAULT_TIME_OFFSET;
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

        names.addAll(this.values.keySet());
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

        final Object oldValue = this.values.put(
            name,
            value
        );

        this.watchers.onValueChange(
            Optional.ofNullable(
                null != oldValue ?
                    EnvironmentValueNameAndValue.with(
                        name,
                        (T) oldValue
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
    final Map<EnvironmentValueName<?>, Object> values;

    // HasEnvironmentWatchers...........................................................................................

    @Override
    public EnvironmentWatchers environmentValueWatchers() {
        return this.watchers;
    }

    private final EnvironmentWatchers watchers = EnvironmentWatchers.empty();

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

        for (EnvironmentValueName<?> name : this.values.keySet()) {
            b.label(name.value());

            final Object value = this.values.get(name);
            if (null != value) {
                b.value(
                    // escape lineEndings
                    name == EnvironmentValueName.LINE_ENDING ?
                        value.toString() :
                        value
                );
            }
        }

        b.append('}');
    }


    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        this.printTreeValues(printer);
    }
}
