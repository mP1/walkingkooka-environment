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

import walkingkooka.text.CharSequences;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Wraps another {@link EnvironmentContext} and logs all operations inputs and outputs.
 */
final class EnvironmentContextSharedLogging extends EnvironmentContextShared
    implements EnvironmentContextDelegator {

    static EnvironmentContextSharedLogging with(final EnvironmentContext context) {
        return context instanceof EnvironmentContextSharedLogging ?
            (EnvironmentContextSharedLogging) context :
            new EnvironmentContextSharedLogging(
                Objects.requireNonNull(context, "context")
            );
    }

    private EnvironmentContextSharedLogging(final EnvironmentContext context) {
        super();
        this.context = context;
    }

    /**
     * Makes a clone of the wrapped {@link EnvironmentContext} returning that.
     */
    @Override
    public EnvironmentContext cloneEnvironment() {
        final EnvironmentContext context = this.context;
        final EnvironmentContext clone = context.cloneEnvironment();

        return context == clone ?
            this :
            with(clone);
    }

    /**
     * Wraps the given {@link EnvironmentContext} if necessary.
     */
    @Override
    public EnvironmentContext setEnvironmentContext(final EnvironmentContext context) {
        return this.context.equals(context) ?
            this :
            with(context);
    }

    @Override
    public <T> Optional<T> environmentValue(final EnvironmentValueName<T> name) {
        return this.logEnterAndExitEnvironment(
            () -> {
                final Optional<T> value = this.context.environmentValue(name);
                if (this.isInfoEnabled()) {
                    // get CHARSET = "UTF-8"
                    this.info("get " + name + "=" + CharSequences.quoteIfChars(value.orElse(null)));
                }
                return value;
            }
        );
    }

    @Override
    public Set<EnvironmentValueName<?>> environmentValueNames() {
        return this.logEnterAndExitEnvironment(
            () -> {
                final Set<EnvironmentValueName<?>> names = this.context.environmentValueNames();

                if (this.isInfoEnabled()) {
                    // A, B, C
                    this.info("names=" + names);
                }
                return names;
            }
        );
    }

    @Override
    public <T> void setEnvironmentValue(final EnvironmentValueName<T> name,
                                        final T value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, "value");

        this.logEnterAndExitEnvironment(
            () -> {
                if (this.isInfoEnabled()) {
                    // set CHARSET = "UTF-8"
                    this.info("set " + name + "=" + CharSequences.quoteIfChars(value));
                }

                this.context.setEnvironmentValue(
                    name,
                    value
                );
                return null;
            }
        );
    }

    @Override
    public void removeEnvironmentValue(final EnvironmentValueName<?> name) {
        Objects.requireNonNull(name, "name");

        this.logEnterAndExitEnvironment(
            () -> {
                if (this.isInfoEnabled()) {
                    this.info("remove " + name);
                }

                this.context.removeEnvironmentValue(name);
                return null;
            }
        );
    }

    @Override
    public Runnable addEnvironmentWatcher(final EnvironmentWatcher watcher) {
        return this.logEnterAndExitEnvironment(
            () -> {
                if (this.isInfoEnabled()) {
                    this.info("addWatcher " + watcher);
                }

                return EnvironmentContextDelegator.super.addEnvironmentWatcher(watcher);
            }
        );
    }

    @Override
    public Runnable addEnvironmentWatcherOnce(final EnvironmentWatcher watcher) {
        return this.logEnterAndExitEnvironment(
            () -> {
                if (this.isInfoEnabled()) {
                    this.info("addWatcherOnce " + watcher);
                }

                return EnvironmentContextDelegator.super.addEnvironmentWatcherOnce(watcher);
            }
        );
    }

    private <T> T logEnterAndExitEnvironment(final Supplier<T> supplier) {
        return this.logEnterAndExit(
            ENVIRONMENT_LOGGER,
            supplier
        );
    }

    // EnvironmentContextDelegator......................................................................................

    @Override
    public EnvironmentContext environmentContext() {
        return this.context;
    }

    private final EnvironmentContext context;

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return this.context.hashCode();
    }

    @Override
    public boolean equals(final Object other) {
        return this == other ||
            (other instanceof EnvironmentContextSharedLogging &&
                this.equals0((EnvironmentContextSharedLogging) other));
    }

    private boolean equals0(final EnvironmentContextSharedLogging other) {
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
            TreePrintable.printTreeOrToString(
                this.context,
                printer
            );
        }
        printer.outdent();
    }
}
