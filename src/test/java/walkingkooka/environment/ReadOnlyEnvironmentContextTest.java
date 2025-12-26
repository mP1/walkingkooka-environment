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
import walkingkooka.text.LineEnding;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class ReadOnlyEnvironmentContextTest implements EnvironmentContextTesting2<ReadOnlyEnvironmentContext>,
    ToStringTesting<ReadOnlyEnvironmentContext> {


    private final static LineEnding LINE_ENDING = LineEnding.NL;

    private final static Locale LOCALE = Locale.FRANCE;

    private final static LocalDateTime NOW = LocalDateTime.MIN;

    private final static EmailAddress USER = EmailAddress.parse("user123@example.com");

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> ReadOnlyEnvironmentContext.with(null)
        );
    }

    @Test
    public void testWithUnwraps() {
        final ReadOnlyEnvironmentContext context = ReadOnlyEnvironmentContext.with(
            EnvironmentContexts.fake()
        );

        assertSame(
            context,
            ReadOnlyEnvironmentContext.with(context)
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

        this.environmentValueAndCheck(
            cloned.setEnvironmentValue(
                name,
                value
            ),
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
        final ReadOnlyEnvironmentContext readOnly = ReadOnlyEnvironmentContext.with(empty);

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
        final ReadOnlyEnvironmentContext readOnly = ReadOnlyEnvironmentContext.with(empty);

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

        final ReadOnlyEnvironmentContext context = ReadOnlyEnvironmentContext.with(
            EnvironmentContexts.map(
                EnvironmentContexts.empty(
                    LINE_ENDING,
                    LOCALE,
                    () -> NOW,
                    Optional.of(USER)
                )
            ).setEnvironmentValue(
                name,
                value
            )
        );

        this.setEnvironmentValueAndCheck(
            context,
            name,
            value
        );
    }

    @Test
    public void testSetEnvironmentValueDifferentFails() {
        final ReadOnlyEnvironmentContext context = this.createContext();

        final Locale locale = Locale.GERMAN;
        this.checkNotEquals(
            LOCALE,
            locale
        );

        assertThrows(
            UnsupportedOperationException.class,
            () -> context.setEnvironmentValue(
                EnvironmentValueName.LOCALE,
                locale
            )
        );

        this.localeAndCheck(
            context,
            LOCALE
        );
    }

    @Test
    public void testSetEnvironmentValueDifferentFails2() {
        final EnvironmentValueName<String> name = EnvironmentValueName.with(
            "hello",
            String.class
        );
        final String value = "value1";

        final ReadOnlyEnvironmentContext context = ReadOnlyEnvironmentContext.with(
            EnvironmentContexts.map(
                EnvironmentContexts.empty(
                    LINE_ENDING,
                    LOCALE,
                    () -> NOW,
                    Optional.of(USER)
                )
            ).setEnvironmentValue(
                name,
                value
            )
        );

        assertThrows(
            UnsupportedOperationException.class,
            () -> context.setEnvironmentValue(
                name,
                "different2"
            )
        );

        this.environmentValueAndCheck(
            context,
            name,
            value
        );
    }

    // removeEnvironmentValue...........................................................................................

    @Test
    public void testRemoveEnvironmentValueMissing() {
        final Optional<EmailAddress> user = EnvironmentContext.ANONYMOUS;

        final ReadOnlyEnvironmentContext context = ReadOnlyEnvironmentContext.with(
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
            UnsupportedOperationException.class,
            () -> context.removeEnvironmentValue(EnvironmentValueName.LOCALE)
        );

        this.localeAndCheck(
            context,
            LOCALE
        );
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

        final ReadOnlyEnvironmentContext context = this.createContext();

        assertThrows(
            UnsupportedOperationException.class,
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

        final ReadOnlyEnvironmentContext context = this.createContext();

        assertThrows(
            UnsupportedOperationException.class,
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
            UnsupportedOperationException.class,
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

    @Override
    public ReadOnlyEnvironmentContext createContext() {
        return ReadOnlyEnvironmentContext.with(
            EnvironmentContexts.map(
                EnvironmentContexts.empty(
                    LINE_ENDING,
                    LOCALE,
                    () -> NOW,
                    Optional.of(USER)
                )
            ).setLocale(LOCALE)
        );
    }

    // environmentValueNames............................................................................................

    @Test
    public void testEnvironmentalValueNames() {
        this.environmentValueNamesAndCheck(
            EnvironmentValueName.LINE_ENDING,
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
