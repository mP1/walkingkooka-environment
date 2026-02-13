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
import walkingkooka.collect.map.Maps;
import walkingkooka.collect.set.Sets;
import walkingkooka.collect.set.SortedSets;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.props.Properties;
import walkingkooka.props.PropertiesPath;
import walkingkooka.text.CharSequences;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.time.ZoneOffset;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * An {@link EnvironmentContext} that sources all values from a given {@link Properties}.
 */
final class EnvironmentContextSharedProperties extends EnvironmentContextShared {

    static EnvironmentContextSharedProperties with(final Properties properties,
                                                   final EnvironmentContext context) {
        return new EnvironmentContextSharedProperties(
            Objects.requireNonNull(properties, "properties"),
            Objects.requireNonNull(context, "context")
        );
    }

    private EnvironmentContextSharedProperties(final Properties properties,
                                               final EnvironmentContext context) {
        super();
        this.properties = properties;
        this.context = context;
    }

    @Override
    public EnvironmentContext cloneEnvironment() {
        final EnvironmentContext before = this.context;
        final EnvironmentContext after = before.cloneEnvironment();

        return before == after ?
            this :
            with(
                this.properties, // immutable no need to clone
                after
            );
    }

    @Override
    public EnvironmentContext setEnvironmentContext(final EnvironmentContext context) {
        return Objects.requireNonNull(context, "context");
    }

    @Override
    public <T> Optional<T> environmentValue(final EnvironmentValueName<T> name) {
        Objects.requireNonNull(name, "name");

        final Optional<?> value;

        if (CURRENCY.equals(name)) {
            value = Optional.of(
                this.context.currency()
            );
        } else {
            if (INDENTATION.equals(name)) {
                value = Optional.of(
                    this.context.indentation()
                );
            } else {
                if (LINE_ENDING.equals(name)) {
                    value = Optional.of(
                        this.context.lineEnding()
                    );
                } else {
                    if (LOCALE.equals(name)) {
                        value = Optional.of(
                            this.context.locale()
                        );
                    } else {
                        if (NOW.equals(name)) {
                            value = Optional.of(
                                this.context.now()
                            );
                        } else {
                            if (TIME_OFFSET.equals(name)) {
                                value = Optional.of(
                                    this.context.timeOffset()
                                );
                            } else {
                                if (USER.equals(name)) {
                                    value = this.context.user();
                                } else {
                                    value = this.properties.get(
                                        PropertiesPath.parse(name.value())
                                    );
                                }
                            }
                        }
                    }
                }
            }
        }

        return Cast.to(value);
    }

    @Override
    public Set<EnvironmentValueName<?>> environmentValueNames() {
        final Set<EnvironmentValueName<?>> names = SortedSets.tree();

        this.properties.keys()
            .stream()
            .map(p -> EnvironmentValueName.with(
                    p.value(),
                    String.class
                )
            ).forEach(names::add);

        names.addAll(this.context.environmentValueNames());

        return Sets.immutable(names);
    }

    private Set<EnvironmentValueName<?>> names;

    @Override
    public <T> void setEnvironmentValue(final EnvironmentValueName<T> name,
                                        final T value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");

        if (CURRENCY.equals(name)) {
            this.context.setCurrency((Currency) value);
        } else {
            if (INDENTATION.equals(name)) {
                this.context.setIndentation((Indentation) value);
            } else {
                if (LINE_ENDING.equals(name)) {
                    this.context.setLineEnding((LineEnding) value);
                } else {
                    if (LOCALE.equals(name)) {
                        this.context.setLocale((Locale) value);
                    } else {
                        if (TIME_OFFSET.equals(name)) {
                            this.context.setTimeOffset((ZoneOffset) value);
                        } else {
                            if (USER.equals(name)) {
                                this.context.setUser(
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

        final Object exists = this.context.environmentValue(name)
            .orElse(null);
        if (null != exists) {
            this.context.removeEnvironmentValue(name);
        } else {
            final Object exists2 = this.environmentValue(name)
                .orElse(null);
            if (null != exists2) {
                throw name.readOnlyEnvironmentValueException();
            }
        }
    }

    private final Properties properties;

    private final EnvironmentContext context;

    @Override
    public Runnable addEventValueWatcher(final EnvironmentValueWatcher watcher) {
        return this.context.addEventValueWatcher(watcher);
    }

    @Override
    public Runnable addEventValueWatcherOnce(final EnvironmentValueWatcher watcher) {
        return this.context.addEventValueWatcherOnce(watcher);
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.context.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            (other instanceof EnvironmentContextSharedProperties &&
                this.equals0((EnvironmentContextSharedProperties) other));
    }

    private boolean equals0(final EnvironmentContextSharedProperties other) {
        return this.properties.equals(other.properties) &&
            this.context.equals(other.context);
    }

    @Override
    public String toString() {
        final Map<EnvironmentValueName<?>, Object> map = Maps.sorted();

        for (final Entry<PropertiesPath, String> entry : this.properties.entries()) {
            map.put(
                EnvironmentValueName.with(
                    entry.getKey()
                        .value(),
                    String.class
                ),
                entry.getValue()
            );
        }

        map.put(
            CURRENCY,
            CharSequences.quoteAndEscape(
                this.currency()
                    .toString()
            )
        );

        map.put(
            INDENTATION,
            CharSequences.quoteAndEscape(
                this.indentation()
                    .toString()
            )
        );

        map.put(
            LINE_ENDING,
            CharSequences.quoteAndEscape(
                this.lineEnding()
                    .toString()
            )
        );
        map.put(
            LOCALE,
            this.locale()
        );

        map.put(
            TIME_OFFSET,
            this.timeOffset()
        );

        final EmailAddress user = this.user()
            .orElse(null);
        if (null != user) {
            map.put(
                USER,
                user
            );
        }

        return map.toString();
    }

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            TreePrintable.printTreeOrToString(
                this.context,
                printer
            );

            printer.println("properties");

            printer.indent();
            {
                this.properties.printTree(printer);
            }
            printer.outdent();
        }
        printer.outdent();
    }
}
