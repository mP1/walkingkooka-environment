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
import walkingkooka.collect.set.SortedSets;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.text.CharSequences;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;

/**
 * A {@link EnvironmentContext} that expects and removes a prefix before performing a lookup.
 */
final class PrefixedEnvironmentContext implements EnvironmentContext {

    static PrefixedEnvironmentContext with(final EnvironmentValueName<?> prefix,
                                           final EnvironmentContext context) {
        Objects.requireNonNull(prefix, "prefix");
        final String prefixValue = prefix.value();
        if (false == prefixValue.endsWith(".")) {
            throw new IllegalArgumentException("Missing separator '.' from prefix= " + CharSequences.quoteAndEscape(prefixValue));
        }

        Objects.requireNonNull(context, "context");

        final PrefixedEnvironmentContext result;
        if (context instanceof PrefixedEnvironmentContext) {
            final PrefixedEnvironmentContext prefixedEnvironmentContext = (PrefixedEnvironmentContext) context;
            result = new PrefixedEnvironmentContext(
                EnvironmentValueName.with(
                    prefixedEnvironmentContext.prefix + prefixValue
                ).value(),
                prefixedEnvironmentContext.context
            );
        } else {
            result = new PrefixedEnvironmentContext(
                prefix.value(),
                context
            );
        }

        return result;
    }

    private PrefixedEnvironmentContext(final String prefix,
                                       final EnvironmentContext context) {
        this.prefix = prefix;
        this.context = context;
    }

    // HasLocale........................................................................................................

    @Override
    public Locale locale() {
        return this.context.locale();
    }

    @Override
    public EnvironmentContext setLocale(final Locale locale) {
        return this.setEnvironmentValue(
            LOCALE,
            locale
        );
    }

    // EnvironmentContext...............................................................................................

    @Override
    public EnvironmentContext cloneEnvironment() {
        return new PrefixedEnvironmentContext(
            this.prefix,
            Objects.requireNonNull(
                this.context.cloneEnvironment()
            )
        );
    }

    @Override
    public <T> Optional<T> environmentValue(final EnvironmentValueName<T> name) {
        Objects.requireNonNull(name, "name");

        Optional<?> value;

        final String prefix = this.prefix;
        if (name.value().startsWith(prefix)) {
            value = this.context.environmentValue(
                EnvironmentValueName.with(
                    name.value()
                        .substring(prefix.length())
                )
            );
        } else {
            if(LOCALE.equals(name)) {
                value = Optional.of(
                    this.context.locale()
                );
            } else {
                if(USER.equals(name)) {
                    value = this.context.user();
                } else {
                    value = Optional.empty();
                }
            }
        }

        return Cast.to(value);
    }

    // assumes the wrapped EnvironmentContext is immutable.
    @Override
    public Set<EnvironmentValueName<?>> environmentValueNames() {
        final SortedSet<EnvironmentValueName<?>> names = SortedSets.tree();
        names.add(LOCALE);
        names.add(USER);

        for (final EnvironmentValueName<?> name : this.context.environmentValueNames()) {
            if (LOCALE.equals(name) || USER.equals(name)) {
                continue;
            }
            names.add(
                EnvironmentValueName.with(
                    this.prefix + name.value()
                )
            );
        }
        return names;
    }

    // @VisibleForTesting
    final String prefix;

    @Override
    public <T> EnvironmentContext setEnvironmentValue(final EnvironmentValueName<T> name,
                                                      final T value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");

        if (LOCALE.equals(name)) {
            this.context.setLocale((Locale) value);
        } else {
            if (USER.equals(name)) {
                this.context.setUser(
                    Optional.of((EmailAddress) value)
                );
            } else {
                throw new UnsupportedOperationException();
            }
        }

        return this;
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
        this.context.setUser(user);
        return this;
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.context.toString();
    }

    // @VisibleForTesting
    final EnvironmentContext context;
}
