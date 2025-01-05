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
import walkingkooka.text.CharSequences;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

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
                    ),
                    prefixedEnvironmentContext.context
            );
        } else {
            result = new PrefixedEnvironmentContext(
                    prefix,
                    context
            );
        }

        return result;
    }

    private PrefixedEnvironmentContext(final EnvironmentValueName<?> prefix,
                                       final EnvironmentContext context) {
        this.prefix = prefix.value();
        this.context = context;
    }

    // EnvironmentContext...............................................................................................

    @Override
    public <T> Optional<T> environmentValue(final EnvironmentValueName<T> name) {
        Objects.requireNonNull(name, "name");

        Optional<T> value = Optional.empty();

        final String prefix = this.prefix;
        if (name.value().startsWith(prefix)) {
            value = this.context.environmentValue(
                    EnvironmentValueName.with(
                            name.value()
                                    .substring(prefix.length())
                    )
            );
        }

        return value;
    }

    // @VisibleForTesting
    final String prefix;

    @Override
    public LocalDateTime now() {
        return this.context.now();
    }

    @Override
    public Optional<EmailAddress> user() {
        return this.context.user();
    }

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.context.toString();
    }

    // @VisibleForTesting
    final EnvironmentContext context;
}
