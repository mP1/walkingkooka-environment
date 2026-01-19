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
import walkingkooka.ToStringTesting;
import walkingkooka.datetime.HasNow;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.predicate.Predicates;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.TreePrintableTesting;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ReadOnlyEnvironmentContextTest implements EnvironmentContextTesting2<ReadOnlyEnvironmentContext>,
    ToStringTesting<ReadOnlyEnvironmentContext>,
    TreePrintableTesting {

    // ONLY user is readonly
    private final Predicate<EnvironmentValueName<?>> READ_ONLY_NAMES = Predicates.is(
        EnvironmentContext.USER
    );

    private final static LineEnding LINE_ENDING = LineEnding.NL;

    private final static Locale LOCALE = Locale.FRANCE;

    private final static LocalDateTime NOW = LocalDateTime.MIN;

    private final static EmailAddress USER = EmailAddress.parse("user123@example.com");

    @Test
    public void testWithNullReadOnlyNamesFails() {
        assertThrows(
            NullPointerException.class,
            () -> ReadOnlyEnvironmentContext.with(
                null,
                EnvironmentContexts.fake()
            )
        );
    }

    @Test
    public void testWithNullEnvironmentContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> ReadOnlyEnvironmentContext.with(
                READ_ONLY_NAMES,
                null
            )
        );
    }

    @Test
    public void testWithReadOnlyNamesWithSameReadOnlyNamesPredicate() {
        final ReadOnlyEnvironmentContext context = ReadOnlyEnvironmentContext.with(
            READ_ONLY_NAMES,
            EnvironmentContexts.fake()
        );

        assertSame(
            context,
            ReadOnlyEnvironmentContext.with(
                READ_ONLY_NAMES,
                context
            )
        );
    }

    @Test
    public void testWithReadOnlyNamesWithDifferentReadOnlyNamesPredicate() {
        final EnvironmentValueName<String> hello = EnvironmentValueName.with(
            "Hello",
            String.class
        );
        final String helloValue = "World";

        final EnvironmentContext environmentContext = EnvironmentContexts.map(
            EnvironmentContexts.empty(
                LINE_ENDING,
                LOCALE,
                () -> NOW,
                Optional.of(USER)
            )
        );
        environmentContext.setEnvironmentValue(
            hello,
            helloValue
        );

        final ReadOnlyEnvironmentContext wrapped = ReadOnlyEnvironmentContext.with(
            Predicates.is(hello),
            environmentContext
        );

        final ReadOnlyEnvironmentContext readOnlyEnvironmentContext = ReadOnlyEnvironmentContext.with(
            READ_ONLY_NAMES,
            wrapped
        );

        assertNotSame(
            wrapped,
            readOnlyEnvironmentContext
        );

        assertSame(
            READ_ONLY_NAMES,
            readOnlyEnvironmentContext.readOnlyNames,
            "readOnlyNames"
        );

        assertSame(
            wrapped,
            readOnlyEnvironmentContext.context,
            "context"
        );
    }

    // cloneEnvironment.................................................................................................

    @Test
    public void testCloneEnvironment() {
        final ReadOnlyEnvironmentContext context = this.createContext();

        assertNotSame(
            context,
            context.cloneEnvironment()
        );
    }

    @Test
    public void testCloneEnvironmentNotReadOnly() {
        final ReadOnlyEnvironmentContext context = this.createContext();

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

    // setEnvironmentContext............................................................................................

    @Test
    public void testSetEnvironmentContextWithSame() {
        final EnvironmentContext empty = EnvironmentContexts.empty(
            LineEnding.NL,
            Locale.FRENCH,
            LocalDateTime::now,
            Optional.of(
                EmailAddress.parse("user123@example.com")
            )
        );
        final ReadOnlyEnvironmentContext readOnly = ReadOnlyEnvironmentContext.with(
            READ_ONLY_NAMES,
            empty
        );

        assertSame(
            readOnly.setEnvironmentContext(empty),
            empty
        );
    }

    @Test
    public void testSetEnvironmentContext() {
        final HasNow hasNow = () -> NOW;

        final EnvironmentContext empty = EnvironmentContexts.empty(
            LineEnding.NL,
            Locale.FRENCH,
            hasNow,
            Optional.of(
                EmailAddress.parse("user123@example.com")
            )
        );
        final ReadOnlyEnvironmentContext readOnly = ReadOnlyEnvironmentContext.with(
            READ_ONLY_NAMES,
            empty
        );

        final EnvironmentContext different = EnvironmentContexts.empty(
            LineEnding.CRNL,
            Locale.GERMAN,
            hasNow,
            Optional.of(
                EmailAddress.parse("user123@example.com")
            )
        );

        this.checkNotEquals(
            empty,
            different
        );

        final EnvironmentContext set = readOnly.setEnvironmentContext(different);

        assertSame(
            different,
            set
        );
    }

    // setEnvironmentValue..............................................................................................

    @Test
    public void testSetEnvironmentValueSame() {
        final ReadOnlyEnvironmentContext context = ReadOnlyEnvironmentContext.with(
            READ_ONLY_NAMES,
            EnvironmentContexts.empty(
                LINE_ENDING,
                LOCALE,
                () -> NOW,
                Optional.of(USER)
            )
        );

        this.setEnvironmentValueAndCheck(
            context,
            EnvironmentContext.USER,
            USER
        );
    }

    @Test
    public void testSetEnvironmentValueSame2() {
        final EnvironmentValueName<String> name = EnvironmentValueName.with(
            "hello",
            String.class
        );
        final String value = "World123";

        final EnvironmentContext context = EnvironmentContexts.map(
            EnvironmentContexts.empty(
                LINE_ENDING,
                LOCALE,
                () -> NOW,
                Optional.of(USER)
            )
        );
        context.setEnvironmentValue(
            name,
            value
        );

        this.setEnvironmentValueAndCheck(
            ReadOnlyEnvironmentContext.with(
                READ_ONLY_NAMES,
                context
            ),
            name,
            value
        );
    }

    @Test
    public void testSetEnvironmentValueDifferentNotReadOnly() {
        final EnvironmentValueName<Locale> name = EnvironmentValueName.LOCALE;

        final ReadOnlyEnvironmentContext context = this.createContext(
            Predicates.never()
        );

        final Locale locale = Locale.GERMAN;
        this.checkNotEquals(
            LOCALE,
            locale
        );

        this.setEnvironmentValueAndCheck(
            context,
            name,
            locale
        );
    }

    @Test
    public void testSetEnvironmentValueDifferentReadOnlyFails() {
        final EnvironmentValueName<Locale> name = EnvironmentValueName.LOCALE;

        final ReadOnlyEnvironmentContext context = this.createContext(
            Predicates.is(name)
        );

        final Locale locale = Locale.GERMAN;
        this.checkNotEquals(
            LOCALE,
            locale
        );

        assertThrows(
            ReadOnlyEnvironmentValueException.class,
            () -> context.setEnvironmentValue(
                name,
                locale
            )
        );

        this.localeAndCheck(
            context,
            LOCALE
        );
    }

    @Test
    public void testSetEnvironmentValueDifferentReadOnlyFails2() {
        final EnvironmentValueName<String> name = EnvironmentValueName.with(
            "hello",
            String.class
        );
        final String value = "value1";

        final EnvironmentContext context = EnvironmentContexts.map(
            EnvironmentContexts.empty(
                LINE_ENDING,
                LOCALE,
                () -> NOW,
                Optional.of(USER)
            )
        );
        context.setEnvironmentValue(
            name,
            value
        );
        final ReadOnlyEnvironmentContext readOnly = ReadOnlyEnvironmentContext.with(
            Predicates.is(name),
            context
        );

        assertThrows(
            ReadOnlyEnvironmentValueException.class,
            () -> readOnly.setEnvironmentValue(
                name,
                "different2"
            )
        );

        this.environmentValueAndCheck(
            readOnly,
            name,
            value
        );
    }

    @Override
    public void testSetEnvironmentValueWithNowFails() {
        throw new UnsupportedOperationException();
    }

    // removeEnvironmentValue...........................................................................................

    @Test
    public void testRemoveEnvironmentValueMissing() {
        final Optional<EmailAddress> user = EnvironmentContext.ANONYMOUS;

        final ReadOnlyEnvironmentContext context = ReadOnlyEnvironmentContext.with(
            READ_ONLY_NAMES,
            EnvironmentContexts.empty(
                LINE_ENDING,
                LOCALE,
                () -> NOW,
                user
            )
        );

        this.removeEnvironmentValueAndCheck(
            context,
            EnvironmentValueName.USER
        );
    }

    @Test
    public void testRemoveEnvironmentValuePresentFails() {
        final ReadOnlyEnvironmentContext context = this.createContext();

        assertThrows(
            ReadOnlyEnvironmentValueException.class,
            () -> context.removeEnvironmentValue(EnvironmentValueName.LOCALE)
        );

        this.localeAndCheck(
            context,
            LOCALE
        );
    }

    @Override
    public void testRemoveEnvironmentValueWithNowFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testRemoveWithReadOnlyFails() {
        final ReadOnlyEnvironmentContext context = this.createContext(
            Predicates.is(EnvironmentContext.USER)
        );

        this.userAndCheck(
            context,
            USER
        );

        assertThrows(
            ReadOnlyEnvironmentValueException.class,
            () -> context.removeEnvironmentValue(EnvironmentContext.USER)
        );

        this.userAndCheck(
            context,
            USER
        );
    }

    @Test
    public void testRemoveWithReadOnly() {
        final ReadOnlyEnvironmentContext context = this.createContext(
            Predicates.never()
        );

        this.userAndCheck(
            context,
            USER
        );

        this.removeEnvironmentValueAndCheck(
            context,
            EnvironmentValueName.USER
        );

        this.userAndCheck(context);
    }

    // setLineEnding....................................................................................................

    @Test
    public void testSetLineEndingWithSame() {
        this.setLineEndingAndCheck(
            this.createContext(),
            LINE_ENDING
        );
    }

    @Test
    public void testSetLineEndingWithDifferent() {
        final LineEnding lineEnding = LineEnding.CRNL;

        this.checkNotEquals(
            LINE_ENDING,
            lineEnding
        );

        final ReadOnlyEnvironmentContext context = this.createContext(
            Predicates.is(EnvironmentContext.LINE_ENDING)
        );

        assertThrows(
            ReadOnlyEnvironmentValueException.class,
            () -> context.setLineEnding(lineEnding)
        );

        this.lineEndingAndCheck(
            context,
            LINE_ENDING
        );
    }

    @Override
    public void testSetLineEndingWithDifferentAndWatcher() {
        throw new UnsupportedOperationException();
    }

    // locale...........................................................................................................

    @Test
    public void testLocale() {
        this.localeAndCheck(
            this.createContext(),
            LOCALE
        );
    }

    // setLocale........................................................................................................

    @Override
    public void testSetLocaleWithDifferent() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testSetLineEndingWithDifferentFails() {
        final Locale locale = Locale.GERMAN;

        this.checkNotEquals(
            LOCALE,
            locale
        );

        final ReadOnlyEnvironmentContext context = this.createContext(
            Predicates.is(EnvironmentContext.LOCALE)
        );

        assertThrows(
            ReadOnlyEnvironmentValueException.class,
            () -> context.setLocale(locale)
        );

        this.localeAndCheck(
            context,
            LOCALE
        );
    }

    @Override
    public void testSetLocaleWithDifferentAndWatcher() {
        throw new UnsupportedOperationException();
    }

    // environmentValue.................................................................................................

    @Test
    public void testEnvironmentalValue() {
        this.environmentValueAndCheck(
            this.createContext(),
            EnvironmentValueName.with(
                "Hello123",
                String.class
            )
        );
    }

    @Test
    public void testEnvironmentValueWithLocale() {
        this.environmentValueAndCheck(
            this.createContext(),
            EnvironmentValueName.LOCALE,
            LOCALE
        );
    }

    // setUser..........................................................................................................

    @Test
    public void testSetUserWithSame() {
        this.setUserAndCheck(
            this.createContext(),
            USER
        );
    }

    @Test
    public void testSetUserWithDifferent() {
        final EmailAddress user = EmailAddress.parse("different@examoke.com");

        this.checkNotEquals(
            USER,
            user
        );

        final ReadOnlyEnvironmentContext context = this.createContext();

        assertThrows(
            ReadOnlyEnvironmentValueException.class,
            () -> context.setUser(
                Optional.of(user)
            )
        );

        this.userAndCheck(
            context,
            USER
        );
    }

    @Test
    public void testSetUserWithSameAnonymous() {
        final Optional<EmailAddress> user = EnvironmentContext.ANONYMOUS;

        final ReadOnlyEnvironmentContext context = ReadOnlyEnvironmentContext.with(
            READ_ONLY_NAMES,
            EnvironmentContexts.empty(
                LINE_ENDING,
                LOCALE,
                () -> NOW,
                user
            )
        );

        this.setUserAndCheck(
            context,
            user
        );
    }

    @Override
    public void testSetUserWithDifferentAndWatcher() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testAddWatcherAndSetEnvironmentValue() {
        final EnvironmentValueName<Locale> name = EnvironmentValueName.LOCALE;

        final ReadOnlyEnvironmentContext context = this.createContext(
            Predicates.never()
        );

        final Locale locale = Locale.GERMAN;
        this.checkNotEquals(
            LOCALE,
            locale
        );

        this.fired = false;

        context.addEventValueWatcher(
            new EnvironmentValueWatcher() {
                @Override
                public void onEnvironmentValueChange(final EnvironmentValueName<?> n,
                                                     final Optional<?> oldValue,
                                                     final Optional<?> newValue) {
                    checkEquals(
                        name,
                        n,
                        "name"
                    );
                    checkEquals(
                        Optional.of(LOCALE),
                        oldValue,
                        "oldValue"
                    );
                    checkEquals(
                        Optional.of(locale),
                        newValue,
                        "newValue"
                    );

                    ReadOnlyEnvironmentContextTest.this.fired = true;
                }
            }
        );

        this.setEnvironmentValueAndCheck(
            context,
            name,
            locale
        );

        this.checkEquals(
            true,
            this.fired,
            "setEnvironmentValue event not fired"
        );
    }

    @Test
    public void testAddWatcherOnceAndSetEnvironmentValue() {
        final EnvironmentValueName<Locale> name = EnvironmentValueName.LOCALE;

        final ReadOnlyEnvironmentContext context = this.createContext(
            Predicates.never()
        );

        final Locale locale = Locale.GERMAN;
        this.checkNotEquals(
            LOCALE,
            locale
        );

        this.fired = false;

        context.addEventValueWatcherOnce(
            new EnvironmentValueWatcher() {
                @Override
                public void onEnvironmentValueChange(final EnvironmentValueName<?> n,
                                                     final Optional<?> oldValue,
                                                     final Optional<?> newValue) {
                    checkEquals(
                        false,
                        ReadOnlyEnvironmentContextTest.this.fired,
                        "event should not have been fired twice"
                    );

                    checkEquals(
                        name,
                        n,
                        "name"
                    );
                    checkEquals(
                        Optional.of(LOCALE),
                        oldValue,
                        "oldValue"
                    );
                    checkEquals(
                        Optional.of(locale),
                        newValue,
                        "newValue"
                    );

                    ReadOnlyEnvironmentContextTest.this.fired = true;
                }
            }
        );

        this.setEnvironmentValueAndCheck(
            context,
            name,
            locale
        );

        this.checkEquals(
            true,
            this.fired,
            "setEnvironmentValue event not fired"
        );

        this.setEnvironmentValueAndCheck(
            context,
            name,
            LOCALE
        );

        this.checkEquals(
            true,
            this.fired,
            "setEnvironmentValue event should not have been fired"
        );
    }

    private boolean fired;

    @Override
    public ReadOnlyEnvironmentContext createContext() {
        return this.createContext(READ_ONLY_NAMES);
    }

    private ReadOnlyEnvironmentContext createContext(final Predicate<EnvironmentValueName<?>> readOnlyNames) {
        final EnvironmentContext context = EnvironmentContexts.map(
            EnvironmentContexts.empty(
                LINE_ENDING,
                LOCALE,
                () -> NOW,
                Optional.of(USER)
            )
        );
        context.setLocale(LOCALE);
        return ReadOnlyEnvironmentContext.with(
            readOnlyNames,
            context
        );
    }

    // environmentValueNames............................................................................................

    @Test
    public void testEnvironmentalValueNames() {
        this.environmentValueNamesAndCheck(
            EnvironmentValueName.LINE_ENDING,
            EnvironmentValueName.NOW,
            EnvironmentValueName.LOCALE,
            EnvironmentValueName.USER
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            this.createContext(),
            "{lineEnding=\"\\n\", locale=fr_FR, user=user123@example.com}"
        );
    }

    // TreePrintable....................................................................................................

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
            this.createContext(),
            "ReadOnlyEnvironmentContext\n" +
                "  environmentContext\n" +
                "    MapEnvironmentContext\n" +
                "      lineEnding\n" +
                "        \"\\n\"\n" +
                "      locale\n" +
                "        fr_FR (java.util.Locale)\n" +
                "      now\n" +
                "        -999999999-01-01T00:00 (java.time.LocalDateTime)\n" +
                "      user\n" +
                "        user123@example.com (walkingkooka.net.email.EmailAddress)\n" +
                "  readOnlyNames\n" +
                "    user (walkingkooka.predicate.ObjectEqualityPredicate)\n"
        );
    }

    // type naming......................................................................................................

    @Override
    public String typeNameSuffix() {
        return EnvironmentContext.class.getSimpleName();
    }

    // class............................................................................................................

    @Override
    public Class<ReadOnlyEnvironmentContext> type() {
        return ReadOnlyEnvironmentContext.class;
    }
}
