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
import walkingkooka.environment.EnvironmentContextAware;
import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.environment.EnvironmentValueNameAndValue;
import walkingkooka.logging.CanLog;
import walkingkooka.logging.CanLogDelegator;
import walkingkooka.text.LineEnding;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

/**
 * A {@link CanLog} that watches {@link walkingkooka.text.LineEnding} changes to a given {@link EnvironmentContext}.
 */
public final class EnvironmentContextLineEndingCanLog implements CanLogDelegator,
    EnvironmentContextAware {

    public static EnvironmentContextLineEndingCanLog with(final Function<LineEnding, CanLog> canLogFactory) {
        return new EnvironmentContextLineEndingCanLog(
            Objects.requireNonNull(canLogFactory, "canLogFactory")
        );
    }

    private EnvironmentContextLineEndingCanLog(final Function<LineEnding, CanLog> canLogFactory) {
        super();
        this.canLogFactory = canLogFactory;
    }

    // CanLog...........................................................................................................

    private void setLineEnding(final LineEnding lineEnding) {
        this.setCanLog(
            this.canLogFactory.apply(lineEnding)
        );
    }

    private void setCanLog(final CanLog canLog) {
        Objects.requireNonNull(canLog, "canLog");

        this.canLog = canLog;
    }

    // CanLogDelegator..................................................................................................

    @Override
    public CanLog canLog() {
        return this.canLog;
    }

    private CanLog canLog;

    // EnvironmentContextAware..........................................................................................

    @Override
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
     * The backing {@link Function<LineEnding, CanLog>} that will be wrapped by a new {@link walkingkooka.text.printer.Printer} with the latest {@link walkingkooka.text.LineEnding}.
     */
    private final Function<LineEnding, CanLog> canLogFactory;

    // Object...........................................................................................................

    @Override
    public String toString() {
        return this.canLogFactory.toString();
    }
}
