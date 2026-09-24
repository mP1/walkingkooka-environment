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

import org.junit.jupiter.api.Test;
import walkingkooka.logging.CanLogs;
import walkingkooka.logging.LoggingLevel;
import walkingkooka.text.printer.Printers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EnvironmentContextSharedLoggingTest extends EnvironmentContextSharedTestCase<EnvironmentContextSharedLogging> {

    private final static LoggingLevel LOGGING_LEVEL = LoggingLevel.DEBUG;

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> EnvironmentContextSharedLogging.with(null)
        );
    }

    @Test
    public void testWithEnvironmentContextSharedLogging() {
        final EnvironmentContextSharedLogging context = EnvironmentContextSharedLogging.with(ENVIRONMENT_CONTEXT);

        assertSame(
            context,
            EnvironmentContextSharedLogging.with(context)
        );
    }

    // cloneEnvironment.................................................................................................

    @Test
    public void testCloneEnvironment() {
        final EnvironmentContextSharedLogging context = this.createContext();

        assertNotSame(
            context,
            context.cloneEnvironment()
        );
    }

    @Test
    public void testCloneEnvironmentNotLogging() {
        final EnvironmentContextSharedLogging context = this.createContext();

        final EnvironmentContext cloned = context.cloneEnvironment();
        assertNotSame(
            context,
            cloned
        );

        final EnvironmentValueName<String> name = EnvironmentValueName.with(
            "hello",
            String.class
        );
        final String value = "World123";

        cloned.setEnvironmentValue(
            name,
            value
        );
        this.environmentValueAndCheck(
            cloned,
            name,
            value
        );
    }

    // environmentContext...............................................................................................

    @Test
    @Override
    public void testEnvironmentContext() {
        this.environmentContextAndCheck(
            EnvironmentContextSharedLogging.with(ENVIRONMENT_CONTEXT),
            ENVIRONMENT_CONTEXT
        );
    }

    // setEnvironmentContext............................................................................................

    @Test
    public void testSetEnvironmentContextWithSame() {
        final EnvironmentContext wrapped = ENVIRONMENT_CONTEXT;
        final EnvironmentContextSharedLogging logging = EnvironmentContextSharedLogging.with(wrapped);

        this.setEnvironmentContextAndCheck(
            logging,
            wrapped,
            logging
        );
    }

    @Test
    public void testSetEnvironmentContext() {
        this.checkEquals(
            EnvironmentContextSharedLogging.with(ENVIRONMENT_CONTEXT)
                .setEnvironmentContext(DIFFERENT_ENVIRONMENT_CONTEXT),
            EnvironmentContextSharedLogging.with(DIFFERENT_ENVIRONMENT_CONTEXT)
        );
    }

    // getEnvironmentValue..............................................................................................
    
    @Test
   public void testGetEnvironmentValueUnknown() {
        final StringBuilder b = new StringBuilder();
        
        this.environmentValueAndCheck(
            this.createContext(b),
            EnvironmentValueName.with("MAGIC", String.class)
        );
        
        this.checkEquals(
            "environment DEBUG get MAGIC=null\n",
            b.toString()
        );
   }

    @Test
    public void testGetEnvironmentValue() {
        final StringBuilder b = new StringBuilder();

        this.environmentValueAndCheck(
            this.createContext(b),
            EnvironmentValueName.LINE_ENDING,
            LINE_ENDING
        );

        this.checkEquals(
            "environment DEBUG get lineEnding=\"\\n\"\n",
            b.toString()
        );
    }
    
    // setEnvironmentValue..............................................................................................

    @Test
    public void testSetEnvironmentValue() {
        final StringBuilder b = new StringBuilder();

        this.setEnvironmentValueAndCheck(
            this.createContext(b),
            EnvironmentValueName.LOCALE,
            DIFFERENT_LOCALE
        );

        this.checkEquals(
            "environment DEBUG set locale=en_NZ\n" +
                "environment DEBUG get locale=en_NZ\n",
            b.toString()
        );
    }

    @Test
    public void testSetEnvironmentValueWithSame() {
        final StringBuilder b = new StringBuilder();

        this.setEnvironmentValueAndCheck(
            this.createContext(b),
            EnvironmentValueName.LOCALE,
            LOCALE
        );

        this.checkEquals(
            "environment DEBUG set locale=en_AU\n" +
                "environment DEBUG get locale=en_AU\n",
            b.toString()
        );
    }

    @Test
    public void testSetLocale() {
        final StringBuilder b = new StringBuilder();

        this.setLocaleAndCheck(
            this.createContext(b),
            DIFFERENT_LOCALE
        );

        this.checkEquals(
            "environment DEBUG set locale=en_NZ\n" +
                "environment DEBUG get locale=en_NZ\n" +
                "environment DEBUG get locale=en_NZ\n",
            b.toString()
        );
    }

    // removeEnvironmentValue...........................................................................................

    @Test
    public void testRemoveEnvironmentValueMissing() {
        final StringBuilder b = new StringBuilder();

        this.removeEnvironmentValueAndCheck(
            this.createContext(b),
            EnvironmentValueName.with(
                "Missing",
                String.class
            )
        );

        this.checkEquals(
            "environment DEBUG remove Missing\n" +
                "environment DEBUG get Missing=null\n",
            b.toString()
        );
    }

    // environmentValueNames............................................................................................

    @Test
    public void testEnvironmentalValueNames() {
        final StringBuilder b = new StringBuilder();

        this.environmentValueNamesAndCheck(
            this.createContext(b),
            ENVIRONMENT_CONTEXT.environmentValueNames()
        );

        this.checkEquals(
            "environment DEBUG names=[charset, currency, indentation, lineEnding, locale, loggingLevel, now, timeOffset, user]\n",
            b.toString()
        );
    }

    // addEnvironmentWatcher............................................................................................

    @Test
    public void testAddEnvironmentWatcher() {
        final StringBuilder b = new StringBuilder();

        this.createContext(b)
            .addEnvironmentWatcher(
                new EnvironmentWatcher() {
                    @Override
                    public void onValueChange(final Optional<EnvironmentValueNameAndValue<?>> oldValue,
                                              final Optional<EnvironmentValueNameAndValue<?>> newValue) {

                    }

                    @Override
                    public String toString() {
                        return "EnvironmentWatcher123";
                    }
                }
            );

        this.checkEquals(
            "environment DEBUG addWatcher EnvironmentWatcher123\n",
            b.toString()
        );
    }

    @Test
    public void testAddEnvironmentWatcherOnce() {
        final StringBuilder b = new StringBuilder();

        this.createContext(b)
            .addEnvironmentWatcherOnce(
                new EnvironmentWatcher() {
                    @Override
                    public void onValueChange(final Optional<EnvironmentValueNameAndValue<?>> oldValue,
                                              final Optional<EnvironmentValueNameAndValue<?>> newValue) {

                    }

                    @Override
                    public String toString() {
                        return "EnvironmentWatcher123";
                    }
                }
            );

        this.checkEquals(
            "environment DEBUG addWatcherOnce EnvironmentWatcher123\n",
            b.toString()
        );
    }

    @Override
    public EnvironmentContextSharedLogging createContext() {
        return this.createContext(new StringBuilder());
    }

    private EnvironmentContextSharedLogging createContext(final StringBuilder logging) {
        return EnvironmentContextSharedLogging.with(
            EnvironmentContexts.map(
                CanLogs.printer(
                    Printers.stringBuilder(
                        logging,
                        LINE_ENDING
                    )
                ),
                CHARSET,
                CURRENCY,
                INDENTATION,
                LINE_ENDING,
                LOCALE,
                LOGGING_LEVEL,
                HAS_NOW,
                OPTIONAL_USER
            )
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createContext(),
            "{charset=UTF-8, currency=AUD, indentation=\"  \", lineEnding=\"\\n\", locale=en_AU, loggingLevel=DEBUG, timeOffset=Z, user=user123@example.com}"
        );
    }

    // TreePrintable....................................................................................................

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
            this.createContext(),
            "EnvironmentContextSharedLogging\n" +
                "  EnvironmentContextSharedMap\n" +
                "    charset\n" +
                "      UTF-8 (sun.nio.cs.UTF_8)\n" +
                "    currency\n" +
                "      AUD (java.util.Currency)\n" +
                "    indentation\n" +
                "      \"  \" (walkingkooka.text.Indentation)\n" +
                "    lineEnding\n" +
                "      \"\\n\"\n" +
                "    locale\n" +
                "      en_AU (java.util.Locale)\n" +
                "    loggingLevel\n" +
                "      DEBUG\n" +
                "    now\n" +
                "      1999-12-31T12:58:59 (java.time.LocalDateTime)\n" +
                "    timeOffset\n" +
                "      Z (java.time.ZoneOffset)\n" +
                "    user\n" +
                "      user123@example.com (walkingkooka.net.email.EmailAddress)\n"
        );
    }

    // HasEnvironment...................................................................................................

    @Test
    public void testEnvironment() {
        this.environmentAndCheck(
            this.createContext(),
            ENVIRONMENT_CONTEXT.environment()
                .set(
                    EnvironmentValueName.LOGGING_LEVEL,
                    LOGGING_LEVEL
                )
        );
    }

    // class............................................................................................................

    @Override
    public Class<EnvironmentContextSharedLogging> type() {
        return EnvironmentContextSharedLogging.class;
    }
}
