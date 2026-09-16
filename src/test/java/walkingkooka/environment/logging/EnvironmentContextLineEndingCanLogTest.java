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

import org.junit.jupiter.api.Test;
import walkingkooka.environment.EnvironmentContext;
import walkingkooka.environment.EnvironmentContextTesting;
import walkingkooka.logging.CanLogTesting2;
import walkingkooka.logging.CanLogs;
import walkingkooka.logging.LoggingLevel;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.PublicClassTesting;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.Printers;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EnvironmentContextLineEndingCanLogTest implements CanLogTesting2<EnvironmentContextLineEndingCanLog>,
    PublicClassTesting<EnvironmentContextLineEndingCanLog>,
    EnvironmentContextTesting {

    @Test
    public void testWithNullStringBuilderFails() {
        assertThrows(
            NullPointerException.class,
            () -> EnvironmentContextLineEndingCanLog.with(null)
        );
    }

    private final static String MESSAGE1 = "Message111";
    private final static String MESSAGE2 = "Message222";

    @Test
    public void testLog() {
        final StringBuilder b = new StringBuilder();

        final EnvironmentContextLineEndingCanLog canLog = EnvironmentContextLineEndingCanLog.with(
            (final LineEnding lineEnding) -> CanLogs.printer(
                Printers.stringBuilder(
                    b,
                    lineEnding
                )
            )
        );

        final EnvironmentContext environmentContext = ENVIRONMENT_CONTEXT.cloneEnvironment();
        canLog.setEnvironmentContext(environmentContext);

        canLog.log(
            LoggingLevel.DEBUG,
            MESSAGE1
        );

        canLog.log(
            LoggingLevel.DEBUG,
            MESSAGE2
        );

        this.checkEquals(
            MESSAGE1 + LINE_ENDING +
                MESSAGE2 + LINE_ENDING,
            b.toString()
        );
    }

    @Test
    public void testLogChangeLineEnding() {
        final StringBuilder b = new StringBuilder();

        final EnvironmentContextLineEndingCanLog canLog = EnvironmentContextLineEndingCanLog.with(
            (LineEnding lineEnding) -> CanLogs.printer(
                Printers.stringBuilder(
                    b,
                    lineEnding
                )
            )
        );

        final EnvironmentContext environmentContext = ENVIRONMENT_CONTEXT.cloneEnvironment();
        canLog.setEnvironmentContext(environmentContext);

        canLog.log(
            LoggingLevel.DEBUG,
            MESSAGE1
        );

        environmentContext.setLineEnding(DIFFERENT_LINE_ENDING);

        canLog.log(
            LoggingLevel.DEBUG,
            MESSAGE2
        );

        this.checkEquals(
            MESSAGE1 + LINE_ENDING +
                MESSAGE2 + DIFFERENT_LINE_ENDING,
            b.toString()
        );
    }

    @Override
    public EnvironmentContextLineEndingCanLog createCanLog() {
        return EnvironmentContextLineEndingCanLog.with(
            (LineEnding lineEnding) -> CanLogs.printer(
                Printers.stringBuilder(
                    new StringBuilder(),
                    lineEnding
                )
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<EnvironmentContextLineEndingCanLog> type() {
        return EnvironmentContextLineEndingCanLog.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
