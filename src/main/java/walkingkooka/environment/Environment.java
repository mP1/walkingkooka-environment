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

import walkingkooka.CanBeEmpty;
import walkingkooka.Cast;
import walkingkooka.HasCharset;
import walkingkooka.ToStringBuilder;
import walkingkooka.UsesToStringBuilder;
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.currency.HasCurrency;
import walkingkooka.io.FileExtension;
import walkingkooka.io.HasFileExtension;
import walkingkooka.net.header.HasContentType;
import walkingkooka.net.header.MediaType;
import walkingkooka.text.HasIndentation;
import walkingkooka.text.Indentation;

import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * An immutable store of {@link EnvironmentValueName} and values.
 */
public final class Environment implements CanBeEmpty,
    HasCharset,
    HasContentType,
    HasCurrency,
    HasEnvironment,
    HasEnvironmentContext,
    HasFileExtension,
    HasIndentation,
    UsesToStringBuilder {

    /**
     * The separator to be used between a {@link EnvironmentValueName} and value when converting an {@link String} to {@link Environment}.
     */
    public final static String SEPARATOR = "=";

    /**
     * An new and empty {@link Environment}.
     */
    public static Environment empty() {
        return new Environment(
            Maps.sorted()
        );
    }

    private Environment(final Map<EnvironmentValueName<?>, Object> values) {
        super();

        this.values = values;
    }

    public <T> Optional<T> get(final EnvironmentValueName<T> name) {
        Objects.requireNonNull(name, "name");

        return Optional.ofNullable(
            name.cast(
                this.values.get(name)
            )
        );
    }

    public <T> T getOrFail(final EnvironmentValueName<T> name) {
        return this.get(name)
            .orElseThrow(() -> name.missingEnvironmentValueException());
    }

    public <T> Environment set(final EnvironmentValueName<T> name,
                               final T value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");

        final Map<EnvironmentValueName<?>, Object> values = Maps.sorted();
        values.putAll(this.values);
        values.put(
            name,
            value
        );

        return this.setValues(values);
    }

    public <T> Environment remove(final EnvironmentValueName<T> name) {
        Objects.requireNonNull(name, "name");

        final Map<EnvironmentValueName<?>, Object> values = Maps.sorted();
        values.putAll(this.values);
        values.remove(name);

        return this.setValues(values);
    }

    private Environment setValues(final Map<EnvironmentValueName<?>, Object> values) {
        return this.values.equals(values) ?
            this :
            new Environment(values);
    }

    public Set<EnvironmentValueName<?>> names() {
        return Sets.readOnly(
            this.values.keySet()
        );
    }

    // EnvironmentEnvironmentContext
    final Map<EnvironmentValueName<?>, Object> values;

    // CanBeEmpty.......................................................................................................

    @Override
    public boolean isEmpty() {
        return this.values.isEmpty();
    }

    // HasCharset.......................................................................................................

    @Override
    public Charset charset() {
        return this.getOrFail(EnvironmentValueName.CHARSET);
    }

    // HasCurrency......................................................................................................

    @Override
    public Currency currency() {
        return this.getOrFail(EnvironmentValueName.CURRENCY);
    }

    // HasIndentation...................................................................................................

    @Override
    public Indentation indentation() {
        return this.getOrFail(EnvironmentValueName.INDENTATION);
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.values.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            other instanceof Environment &&
                this.equals0(Cast.to(other));
    }

    private boolean equals0(final Environment other) {
        return this.values.equals(other.values);
    }

    @Override
    public String toString() {
        return ToStringBuilder.buildFrom(this);
    }

    // UsesToStringBuilder..............................................................................................

    @Override
    public void buildToString(final ToStringBuilder builder) {
        builder.append("{");
        {
            builder.value(this.values);
        }
        builder.append("}");
    }

    // HasContentType...................................................................................................

    public final static MediaType CONTENT_TYPE = MediaType.TEXT_ENV;

    @Override
    public Optional<MediaType> contentType() {
        return Optional.of(CONTENT_TYPE);
    }

    // HasEnvironment...................................................................................................

    @Override
    public Environment environment() {
        return this;
    }

    // HasEnvironmentContext............................................................................................

    @Override
    public EnvironmentContext environmentContext() {
        return EnvironmentEnvironmentContext.with(this);
    }

    // HasFileExtension.................................................................................................

    public final static FileExtension FILE_EXTENSION = FileExtension.ENV;

    @Override
    public Optional<FileExtension> fileExtension() {
        return Optional.of(FILE_EXTENSION);
    }
}
