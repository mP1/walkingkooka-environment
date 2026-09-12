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
import walkingkooka.CanBeEmptyTesting;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.set.Sets;
import walkingkooka.currency.HasCurrencyTesting;
import walkingkooka.io.FileExtension;
import walkingkooka.io.HasFileExtensionTesting;
import walkingkooka.logging.CanLogs;
import walkingkooka.logging.LoggingLevel;
import walkingkooka.net.header.HasContentTypeTesting;
import walkingkooka.net.header.MediaType;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.BinaryTextContext;
import walkingkooka.text.BinaryTextContextTesting;
import walkingkooka.text.CharSequences;
import walkingkooka.text.HasIndentationTesting;
import walkingkooka.text.HasLineEndingTesting;
import walkingkooka.text.printer.Printers;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EnvironmentTest implements HashCodeEqualsDefinedTesting2<Environment>,
    BinaryTextContextTesting,
    CanBeEmptyTesting,
    ClassTesting<Environment>,
    HasContentTypeTesting,
    HasCurrencyTesting,
    HasEnvironmentTesting,
    HasFileExtensionTesting,
    HasIndentationTesting,
    HasLineEndingTesting,
    HasTimeOffsetTesting,
    HasUserTesting,
    ToStringTesting<Environment>,
    EnvironmentContextTesting {

    // get..............................................................................................................

    @Test
    public void testGetWithNullNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> Environment.empty().get(null)
        );
    }

    @Test
    public void testGetUnknown() {
        this.getAndCheck(
            Environment.empty(),
            EnvironmentValueName.LOCALE,
            Optional.empty()
        );
    }

    @Test
    public void testGet() {
        this.getAndCheck(
            this.createObject(),
            EnvironmentValueName.CURRENCY,
            Optional.of(
                CURRENCY
            )
        );
    }

    private <T> void getAndCheck(final Environment environment,
                                 final EnvironmentValueName<T> name,
                                 final Optional<T> expected) {
        this.checkEquals(
            expected,
            environment.get(name),
            () -> " get " + name
        );
    }

    // getOrFail........................................................................................................

    @Test
    public void testGetOrFailMissingFails() {
        assertThrows(
            MissingEnvironmentValueException.class,
            () -> this.createObject()
                .getOrFail(EnvironmentValueName.LOCALE)
        );
    }

    @Test
    public void testGetOrFail() {
        this.getOrFailAndCheck(
            this.createObject(),
            EnvironmentValueName.CURRENCY,
            CURRENCY
        );
    }

    private <T> void getOrFailAndCheck(final Environment environment,
                                       final EnvironmentValueName<T> name,
                                       final T expected) {
        this.checkEquals(
            expected,
            environment.getOrFail(name),
            () -> " getOrFail " + name
        );
    }

    // set..............................................................................................................

    @Test
    public void testSetWithNullNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> Environment.empty().set(
                null,
                CURRENCY
            )
        );
    }

    @Test
    public void testSetWithNullValueFails() {
        assertThrows(
            NullPointerException.class,
            () -> Environment.empty().set(
                EnvironmentValueName.CURRENCY,
                null
            )
        );
    }

    @Test
    public void testSetWithSame() {
        this.setAndCheck(
            this.createObject(),
            EnvironmentValueName.CURRENCY,
            CURRENCY
        );
    }

    @Test
    public void testSetWithDifferent() {
        this.setAndCheck(
            Environment.empty(),
            EnvironmentValueName.CURRENCY,
            CURRENCY,
            Environment.empty()
                .set(
                    EnvironmentValueName.CURRENCY,
                    CURRENCY
                )
        );
    }

    @Test
    public void testSet2() {
        this.setAndCheck(
            this.createObject(),
            EnvironmentValueName.LOCALE,
            LOCALE,
            this.createObject()
                .set(
                    EnvironmentValueName.LOCALE,
                    LOCALE
                )
        );
    }

    private <T> void setAndCheck(final Environment environment,
                                 final EnvironmentValueName<T> name,
                                 final T value) {
        this.setAndCheck(
            environment,
            name,
            value,
            environment
        );
    }

    private <T> void setAndCheck(final Environment environment,
                                 final EnvironmentValueName<T> name,
                                 final T value,
                                 final Environment expected) {
        final Environment after = environment.set(
            name,
            value
        );

        if (expected.equals(environment)) {
            assertSame(
                after,
                environment
            );
        } else {
            this.checkEquals(
                expected,
                after,
                () -> " set " + name + " " + CharSequences.quoteIfChars(value)
            );
        }

        this.isEmptyAndCheck(
            after,
            false
        );
    }

    // remove..............................................................................................................

    @Test
    public void testRemoveWithNullNameFails() {
        assertThrows(
            NullPointerException.class,
            () -> Environment.empty()
                .remove(
                    null
                )
        );
    }

    @Test
    public void testRemoveWithUnknown() {
        this.removeAndCheck(
            this.createObject(),
            EnvironmentValueName.LOCALE
        );
    }

    @Test
    public void testRemoveExisting() {
        this.removeAndCheck(
            this.createObject(),
            EnvironmentValueName.CURRENCY,
            Environment.empty()
        );
    }

    @Test
    public void testRemove2() {
        this.removeAndCheck(
            this.createObject()
                .set(
                    EnvironmentValueName.LOCALE,
                    LOCALE
                ),
            EnvironmentValueName.LOCALE,
            this.createObject()
        );
    }

    private void removeAndCheck(final Environment environment,
                                final EnvironmentValueName<?> name) {
        this.removeAndCheck(
            environment,
            name,
            environment
        );
    }

    private void removeAndCheck(final Environment environment,
                                final EnvironmentValueName<?> name,
                                final Environment expected) {
        final Environment after = environment.remove(name);

        if (expected.equals(environment)) {
            assertSame(
                after,
                environment
            );
        } else {
            this.checkEquals(
                expected,
                after,
                () -> " remove " + name
            );
        }
    }

    // names............................................................................................................

    @Test
    public void testNamesReadOnly() {
        final Set<EnvironmentValueName<?>> names = this.createObject()
            .names();

        assertThrows(
            UnsupportedOperationException.class,
            () -> names.add(
                EnvironmentValueName.with(
                    "magic",
                    String.class
                )
            )
        );
    }

    @Test
    public void testNames() {
        this.checkEquals(
            Sets.of(
                EnvironmentValueName.CURRENCY
            ),
            this.createObject()
                .names()
        );
    }

    // CanBeEmpty.......................................................................................................

    @Test
    public void testCanBeEmptyWhenEmpty() {
        this.isEmptyAndCheck(
            Environment.empty(),
            true
        );
    }

    @Test
    public void testCanBeEmptyWhenNotEmpty() {
        this.isEmptyAndCheck(
            this.createObject(),
            false
        );
    }

    // hashCode/equals..................................................................................................

    @Test
    public void testEqualsDifferentValues() {
        this.checkNotEquals(
            Environment.empty()
                .set(
                    EnvironmentValueName.CURRENCY,
                    DIFFERENT_CURRENCY
                )
        );
    }

    @Test
    public void testEqualsDifferentCanLog() {
        this.checkNotEquals(
            Environment.empty()
                .set(
                    EnvironmentValueName.CURRENCY,
                    DIFFERENT_CURRENCY
                ).setCanLog(
                    CanLogs.fake()
                )
        );
    }

    @Override
    public Environment createObject() {
        return Environment.empty()
            .set(
                EnvironmentValueName.CURRENCY,
                CURRENCY
            );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createObject(),
            "{currency=AUD}"
        );
    }

    // HasContentType...................................................................................................

    @Test
    public void testContentType() {
        this.contentTypeAndCheck(
            Environment.empty(),
            MediaType.TEXT_ENV
        );
    }

    // HasEnvironment...................................................................................................

    @Test
    public void testEnvironment() {
        final Environment environment = this.createObject();

        this.environmentAndCheck(
            environment,
            environment
        );
    }

    // HasFileExtension.................................................................................................

    @Test
    public void testFileExtension() {
        this.fileExtensionAndCheck(
            this.createObject(),
            FileExtension.ENV
        );
    }

    // HasCharset...................................................................................................

    @Test
    public void testCharset() {
        this.charsetAndCheck(
            Environment.empty()
                .set(
                    EnvironmentValueName.CHARSET,
                    CHARSET
                ),
            CHARSET
        );
    }

    @Test
    public void testCharset2() {
        this.charsetAndCheck(
            Environment.empty()
                .set(
                    EnvironmentValueName.CHARSET,
                    DIFFERENT_CHARSET
                ),
            DIFFERENT_CHARSET
        );
    }

    // HasCurrency......................................................................................................

    @Test
    public void testCurrency() {
        this.currencyAndCheck(
            Environment.empty()
                .set(
                    EnvironmentValueName.CURRENCY,
                    CURRENCY
                ),
            CURRENCY
        );
    }

    @Test
    public void testCurrency2() {
        this.currencyAndCheck(
            Environment.empty()
                .set(
                    EnvironmentValueName.CURRENCY,
                    DIFFERENT_CURRENCY
                ),
            DIFFERENT_CURRENCY
        );
    }
    
    // HasIndentation...................................................................................................

    @Test
    public void testIndentation() {
        this.indentationAndCheck(
            Environment.empty()
                .set(
                    EnvironmentValueName.INDENTATION,
                    INDENTATION
                ),
            INDENTATION
        );
    }

    @Test
    public void testIndentation2() {
        this.indentationAndCheck(
            Environment.empty()
                .set(
                    EnvironmentValueName.INDENTATION,
                    DIFFERENT_INDENTATION
                ),
            DIFFERENT_INDENTATION
        );
    }

    // HasLineEnding....................................................................................................

    @Test
    public void testLineEnding() {
        this.lineEndingAndCheck(
            Environment.empty()
                .set(
                    EnvironmentValueName.LINE_ENDING,
                    LINE_ENDING
                ),
            LINE_ENDING
        );
    }

    @Test
    public void testLineEnding2() {
        this.lineEndingAndCheck(
            Environment.empty()
                .set(
                    EnvironmentValueName.LINE_ENDING,
                    DIFFERENT_LINE_ENDING
                ),
            DIFFERENT_LINE_ENDING
        );
    }

    // HasLocale........................................................................................................

    @Test
    public void testLocale() {
        this.localeAndCheck(
            Environment.empty()
                .set(
                    EnvironmentValueName.LOCALE,
                    LOCALE
                ),
            LOCALE
        );
    }

    @Test
    public void testLocale2() {
        this.localeAndCheck(
            Environment.empty()
                .set(
                    EnvironmentValueName.LOCALE,
                    DIFFERENT_LOCALE
                ),
            DIFFERENT_LOCALE
        );
    }

    // HasTimeOffset....................................................................................................

    @Test
    public void testTimeOffset() {
        this.timeOffsetAndCheck(
            Environment.empty()
                .set(
                    EnvironmentValueName.TIME_OFFSET,
                    TIME_OFFSET
                ),
            TIME_OFFSET
        );
    }

    @Test
    public void testTimeOffset2() {
        this.timeOffsetAndCheck(
            Environment.empty()
                .set(
                    EnvironmentValueName.TIME_OFFSET,
                    DIFFERENT_TIME_OFFSET
                ),
            DIFFERENT_TIME_OFFSET
        );
    }
    
    // HasUser........................................................................................................

    @Test
    public void testUser() {
        this.userAndCheck(
            Environment.empty()
                .set(
                    EnvironmentValueName.USER,
                    USER
                ),
            USER
        );
    }

    @Test
    public void testUser2() {
        this.userAndCheck(
            Environment.empty()
                .set(
                    EnvironmentValueName.USER,
                    DIFFERENT_USER
                ),
            DIFFERENT_USER
        );
    }

    // HasBinaryTextContext.............................................................................................

    @Test
    public void testHasBinaryTextContext() {
        final BinaryTextContext binaryTextContext = Environment.empty()
            .set(
                EnvironmentValueName.CHARSET,
                CHARSET
            ).set(
                EnvironmentValueName.INDENTATION,
                INDENTATION
            ).set(
                EnvironmentValueName.LINE_ENDING,
                LINE_ENDING
            );

        this.charsetAndCheck(
            binaryTextContext,
            CHARSET
        );
        this.indentationAndCheck(
            binaryTextContext,
            INDENTATION
        );
        this.lineEndingAndCheck(
            binaryTextContext,
            LINE_ENDING
        );
    }

    // EnvironmentContext...............................................................................................

    @Test
    public void testEnvironmentContextReadOnly() {
        final Environment environment = Environment.empty();

        final EnvironmentContext context = environment.environmentContext();

        assertThrows(
            ReadOnlyEnvironmentValueException.class,
            () -> context.setCharset(DIFFERENT_CHARSET)
        );
    }

    @Test
    public void testEnvironmentContext() {
        final Environment environment = Environment.empty()
            .set(
                EnvironmentValueName.CHARSET,
                CHARSET
            ).set(
                EnvironmentValueName.CURRENCY,
                CURRENCY
            ).set(
                EnvironmentValueName.INDENTATION,
                INDENTATION
            ).set(
                EnvironmentValueName.LINE_ENDING,
                LINE_ENDING
            ).set(
                EnvironmentValueName.LOCALE,
                LOCALE
            ).set(
                EnvironmentValueName.LOGGING_LEVEL,
                LoggingLevel.NONE
            ).set(
                EnvironmentValueName.NOW,
                NOW
            ).set(
                EnvironmentValueName.USER,
                USER
            );

        final EnvironmentContext context = environment.environmentContext()
            .cloneEnvironment();

        this.charsetAndCheck(
            context,
            CHARSET
        );
        this.currencyAndCheck(
            context,
            CURRENCY
        );
        this.indentationAndCheck(
            context,
            INDENTATION
        );
        this.lineEndingAndCheck(
            context,
            LINE_ENDING
        );
        this.localeAndCheck(
            context,
            LOCALE
        );
        this.loggingLevelAndCheck(
            context,
            LOGGING_LEVEL
        );
        this.nowAndCheck(
            context,
            NOW
        );
        this.userAndCheck(
            context,
            USER
        );
    }

    @Test
    public void testEnvironmentContextNonStandardValue() {
        final EnvironmentValueName<String> hello = EnvironmentValueName.with(
            "hello",
            String.class
        );
        final String value = "World123!";

        final Environment environment = Environment.empty()
            .set(
                EnvironmentValueName.CHARSET,
                CHARSET
            ).set(
                EnvironmentValueName.CURRENCY,
                CURRENCY
            ).set(
                EnvironmentValueName.INDENTATION,
                INDENTATION
            ).set(
                EnvironmentValueName.LINE_ENDING,
                LINE_ENDING
            ).set(
                EnvironmentValueName.LOCALE,
                LOCALE
            ).set(
                EnvironmentValueName.LOGGING_LEVEL,
                LoggingLevel.NONE
            ).set(
                EnvironmentValueName.NOW,
                NOW
            ).set(
                EnvironmentValueName.USER,
                USER
            ).set(
                hello,
                value
            );

        final EnvironmentContext context = environment.environmentContext();

        this.charsetAndCheck(
            context,
            CHARSET
        );
        this.currencyAndCheck(
            context,
            CURRENCY
        );
        this.indentationAndCheck(
            context,
            INDENTATION
        );
        this.lineEndingAndCheck(
            context,
            LINE_ENDING
        );
        this.localeAndCheck(
            context,
            LOCALE
        );
        this.loggingLevelAndCheck(
            context,
            LOGGING_LEVEL
        );
        this.nowAndCheck(
            context,
            NOW
        );
        this.userAndCheck(
            context,
            USER
        );

        this.environmentValueAndCheck(
            context,
            hello,
            value
        );
    }

    // EnvironmentContextLog.............................................................................................

    private final static String MESSAGE1 = "Message111";

    private final static String MESSAGE2 = "Message222";

    @Test
    public void testEnvironmentContextLog() {
        final StringBuilder printed = new StringBuilder();

        final Environment environment = Environment.empty()
            .set(
                EnvironmentValueName.CHARSET,
                CHARSET
            ).set(
                EnvironmentValueName.CURRENCY,
                CURRENCY
            ).set(
                EnvironmentValueName.INDENTATION,
                INDENTATION
            ).set(
                EnvironmentValueName.LINE_ENDING,
                LINE_ENDING
            ).set(
                EnvironmentValueName.LOCALE,
                LOCALE
            ).set(
                EnvironmentValueName.LOGGING_LEVEL,
                LoggingLevel.NONE
            ).set(
                EnvironmentValueName.NOW,
                NOW
            ).setCanLog(
                CanLogs.printer(
                    Printers.stringBuilder(
                        printed,
                        LINE_ENDING
                    )
                )
            );

        final EnvironmentContext context = environment.environmentContext()
            .cloneEnvironment();

        context.setLoggingLevel(LoggingLevel.DEBUG);

        context.debug(MESSAGE1);

        this.checkEquals(
            MESSAGE1 + LINE_ENDING,
            printed.toString()
        );
    }

    @Test
    public void testEnvironmentContextLogChangedLoggingLevel() {
        final StringBuilder printed = new StringBuilder();

        final Environment environment = Environment.empty()
            .set(
                EnvironmentValueName.CHARSET,
                CHARSET
            ).set(
                EnvironmentValueName.CURRENCY,
                CURRENCY
            ).set(
                EnvironmentValueName.INDENTATION,
                INDENTATION
            ).set(
                EnvironmentValueName.LINE_ENDING,
                LINE_ENDING
            ).set(
                EnvironmentValueName.LOCALE,
                LOCALE
            ).set(
                EnvironmentValueName.LOGGING_LEVEL,
                LoggingLevel.NONE
            ).set(
                EnvironmentValueName.NOW,
                NOW
            ).setCanLog(
                CanLogs.printer(
                    Printers.stringBuilder(
                        printed,
                        LINE_ENDING
                    )
                )
            );

        final EnvironmentContext context = environment.environmentContext()
            .cloneEnvironment();

        context.setLoggingLevel(LoggingLevel.INFO);
        context.debug(MESSAGE1);

        context.setLoggingLevel(LoggingLevel.DEBUG);
        context.debug(MESSAGE2);

        this.checkEquals(
            MESSAGE2 + LINE_ENDING,
            printed.toString()
        );
    }

    // class............................................................................................................

    @Override
    public Class<Environment> type() {
        return Environment.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
