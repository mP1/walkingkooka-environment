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

package walkingkooka.environment.logging;

import walkingkooka.Cast;
import walkingkooka.environment.EnvironmentContext;
import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.environment.EnvironmentValueNameAndValue;
import walkingkooka.logging.CanLog;
import walkingkooka.logging.CanLogs;
import walkingkooka.logging.LoggingLevel;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.Printers;

import java.util.Objects;
import java.util.Optional;

/**
 * A {@link CanLog} that watches {@link walkingkooka.text.LineEnding} changes to a given {@link EnvironmentContext}.
 */
public final class EnvironmentContextLineEndingCanLog implements CanLog {

    public static EnvironmentContextLineEndingCanLog with(final StringBuilder builder) {
        return new EnvironmentContextLineEndingCanLog(
            Objects.requireNonNull(builder, "builder")
        );
    }

    private EnvironmentContextLineEndingCanLog(final StringBuilder builder) {
        super();
        this.builder = builder;
    }

    // CanLog...........................................................................................................

    @Override
    public void log(final LoggingLevel loggingLevel,
                    final String message,
                    final Throwable throwable) {
        this.canLog.log(
            loggingLevel,
            message,
            throwable
        );
    }

    private void setLineEnding(final LineEnding lineEnding) {
        this.setCanLog(
            CanLogs.printer(
                Printers.stringBuilder(
                    this.builder,
                    lineEnding
                )
            )
        );
    }

    public void setCanLog(final CanLog canLog) {
        Objects.requireNonNull(canLog, "canLog");

        this.canLog = canLog;
    }

    private CanLog canLog;

    // setEnvironmentContext............................................................................................

    public void setEnvironmentContext(final EnvironmentContext environmentContext) {
        Objects.requireNonNull(environmentContext, "environmentContext");

        environmentContext.addEnvironmentWatcher(
            // EnvironmentWatcher
            (final Optional<EnvironmentValueNameAndValue<?>> oldValue,
             final Optional<EnvironmentValueNameAndValue<?>> newValue) -> {
                if (newValue.isPresent()) {
                    final EnvironmentValueNameAndValue<?> environmentValueNameAndValue = newValue.get();
                    if (EnvironmentValueName.LINE_ENDING.equals(environmentValueNameAndValue.name())) {
                        this.setLineEnding(
                            Cast.to(
                                environmentValueNameAndValue.value()
                            )
                        );
                    }
                }
            }
        );

        this.setLineEnding(
            environmentContext.lineEnding()
        );
    }

    /**
     * The backing {@link StringBuilder} that will be wrapped by a new {@link walkingkooka.text.printer.Printer} with the latest {@link walkingkooka.text.LineEnding}.
     */
    private final StringBuilder builder;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.builder.toString();
    }
}
