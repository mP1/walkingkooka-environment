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
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;

/**
 * Wraps another {@link EnvironmentContext} presenting a read only view, with all setXXX and removeXXX
 * throwing {@link UnsupportedOperationException} if the {@link EnvironmentValueName} is matched by the given {@link Predicate}.
 * Note {@link #cloneEnvironment()} returns a clone not the original, which may not be read-only.
 * If the wrapped {@link EnvironmentContext} allows modification then the clone will allow modifications.
 */
final class ReadOnlyEnvironmentContext implements EnvironmentContext,
    TreePrintable {

    static ReadOnlyEnvironmentContext with(final Predicate<EnvironmentValueName<?>> readOnlyNames,
                                           final EnvironmentContext context) {
        Objects.requireNonNull(readOnlyNames, "readOnlyNames");
        Objects.requireNonNull(context, "context");

        ReadOnlyEnvironmentContext readOnlyEnvironmentContext = null;

        EnvironmentContext temp = context;

        if (context instanceof ReadOnlyEnvironmentContext) {
            readOnlyEnvironmentContext = (ReadOnlyEnvironmentContext) context;

            // if same readOnlyNames Predicate unwrap context
            if (readOnlyNames.equals(readOnlyEnvironmentContext.readOnlyNames)) {
                temp = readOnlyEnvironmentContext.context;
            } else {
                readOnlyEnvironmentContext = null;
            }
        }

        return null != readOnlyEnvironmentContext ?
            readOnlyEnvironmentContext :
            new ReadOnlyEnvironmentContext(
                readOnlyNames,
                temp
            );
    }

    private ReadOnlyEnvironmentContext(final Predicate<EnvironmentValueName<?>> readOnlyNames,
                                       final EnvironmentContext context) {
        super();
        this.readOnlyNames = readOnlyNames;
        this.context = context;
    }

    /**
     * Makes a clone of the wrapped {@link EnvironmentContext} returning that.
     */
    @Override
    public EnvironmentContext cloneEnvironment() {
        return this.context.cloneEnvironment();
    }

    /**
     * Always returns the given {@link EnvironmentContext}, which is not read only wrapped.
     */
    @Override
    public EnvironmentContext setEnvironmentContext(final EnvironmentContext context) {
        return Objects.requireNonNull(context, "context");
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
    public <T> void setEnvironmentValue(final EnvironmentValueName<T> name,
                                        final T value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");

        if (this.readOnlyNames.test(name) && false == value.equals(this.environmentValue(name).orElse(null))) {
            throw new ReadOnlyEnvironmentValueException(name);
        }
        this.context.setEnvironmentValue(
            name,
            value
        );
    }

    @Override
    public void removeEnvironmentValue(final EnvironmentValueName<?> name) {
        Objects.requireNonNull(name, "name");

        if (this.readOnlyNames.test(name) && this.environmentValue(name).isPresent()) {
            throw new ReadOnlyEnvironmentValueException(name);
        } else {
            this.context.removeEnvironmentValue(name);
        }
    }

    /**
     * Filter that matches read only environment values.
     */
    final Predicate<EnvironmentValueName<?>> readOnlyNames;

    @Override
    public LineEnding lineEnding() {
        return this.context.lineEnding();
    }

    @Override
    public void setLineEnding(final LineEnding lineEnding) {
        Objects.requireNonNull(lineEnding, "lineEnding");

        this.setEnvironmentValue(
            LINE_ENDING,
            lineEnding
        );
    }

    @Override
    public Locale locale() {
        return this.context.locale();
    }

    @Override
    public void setLocale(final Locale locale) {
        Objects.requireNonNull(locale, "locale");

        this.setEnvironmentValue(
            LOCALE,
            locale
        );
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
    public void setUser(final Optional<EmailAddress> user) {
        Objects.requireNonNull(user, "user");

        if (user.isPresent()) {
            this.setEnvironmentValue(
                USER,
                user.get()
            );
        } else {
            this.removeEnvironmentValue(USER);
        }
    }

    @Override
    public Runnable addEventValueWatcher(final EnvironmentValueWatcher watcher) {
        return this.context.addEventValueWatcher(watcher);
    }

    @Override
    public Runnable addEventValueWatcherOnce(final EnvironmentValueWatcher watcher) {
        return this.context.addEventValueWatcherOnce(watcher);
    }

    final EnvironmentContext context;

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

    // TreePrintable....................................................................................................

    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            printer.println("environmentContext");
            printer.indent();
            {
                TreePrintable.printTreeOrToString(
                    this.context,
                    printer
                );
            }
            printer.outdent();

            printer.println("readOnlyNames");
            printer.indent();
            {
                TreePrintable.printTreeOrToString(
                    this.readOnlyNames,
                    printer
                );
            }
            printer.outdent();
        }
        printer.outdent();
    }
}
