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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.datetime.HasNow;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.text.printer.TreePrintableTesting;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EmptyEnvironmentContextTest implements EnvironmentContextTesting2<EmptyEnvironmentContext>,
    HashCodeEqualsDefinedTesting2<EmptyEnvironmentContext>,
    ToStringTesting<EmptyEnvironmentContext>,
    TreePrintableTesting {

    private final static Indentation INDENTATION = Indentation.SPACES4;
    private final static LineEnding LINE_ENDING = LineEnding.NL;
    private final static Locale LOCALE = Locale.ENGLISH;
    private final static LocalDateTime NOW = LocalDateTime.MIN;
    private final static HasNow HAS_NOW = () -> NOW;

    @Test
    public void testWithNullIndentationFails() {
        assertThrows(
            NullPointerException.class,
            () -> EmptyEnvironmentContext.with(
                INDENTATION,
                null,
                LOCALE,
                HAS_NOW,
                EnvironmentContext.ANONYMOUS
            )
        );
    }

    @Test
    public void testWithNullLineEndingFails() {
        assertThrows(
            NullPointerException.class,
            () -> EmptyEnvironmentContext.with(
                INDENTATION,
                null,
                LOCALE,
                HAS_NOW,
                EnvironmentContext.ANONYMOUS
            )
        );
    }

    @Test
    public void testWithNullLocaleFails() {
        assertThrows(
            NullPointerException.class,
            () -> EmptyEnvironmentContext.with(
                INDENTATION,
                LINE_ENDING,
                null,
                HAS_NOW,
                EnvironmentContext.ANONYMOUS
            )
        );
    }

    @Test
    public void testWithNullHasNowFails() {
        assertThrows(
            NullPointerException.class,
            () -> EmptyEnvironmentContext.with(
                INDENTATION,
                LINE_ENDING,
                LOCALE,
                null,
                EnvironmentContext.ANONYMOUS
            )
        );
    }

    @Test
    public void testWithNullUserFails() {
        assertThrows(
            NullPointerException.class,
            () -> EmptyEnvironmentContext.with(
                INDENTATION,
                LINE_ENDING,
                LOCALE,
                HAS_NOW,
                null
            )
        );
    }

    // cloneEnvironment.................................................................................................

    @Test
    public void testCloneEnvironment() {
        final EmptyEnvironmentContext context = this.createContext();
        final EnvironmentContext cloned = context.cloneEnvironment();

        assertNotSame(
            context,
            cloned
        );

        this.checkEquals(
            cloned,
            context
        );
    }

    // setEnvironmentContext............................................................................................

    @Test
    public void testSetEnvironmentContext() {
        final EmptyEnvironmentContext context = this.createContext();

        final EnvironmentContext different = this.createContext();
        different.setLineEnding(LineEnding.CRNL);

        this.checkNotEquals(
            context,
            different
        );

        assertSame(
            different,
            context.setEnvironmentContext(different)
        );
    }

    // locale...........................................................................................................

    @Test
    public void testLocale() {
        this.localeAndCheck(
            this.createContext(),
            LOCALE
        );
    }

    @Test
    public void testSetLocale() {
        this.setLocaleAndCheck(
            this.createContext(),
            Locale.FRENCH
        );
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
    public void testEnvironmentValueWithLineEnding() {
        this.environmentValueAndCheck(
            this.createContext(),
            EnvironmentValueName.LINE_ENDING,
            LINE_ENDING
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

    @Test
    public void testEnvironmentValueWithNow() {
        this.environmentValueAndCheck(
            this.createContext(),
            EnvironmentValueName.NOW,
            NOW
        );
    }

    @Test
    public void testEnvironmentValueWithUserMissing() {
        this.environmentValueAndCheck(
            this.createContext(
                EnvironmentContext.ANONYMOUS
            ),
            EnvironmentValueName.USER
        );
    }

    @Test
    public void testEnvironmentValueWithUserPresent() {
        final EmailAddress user = EmailAddress.parse("user127@example.com");

        this.environmentValueAndCheck(
            this.createContext(
                Optional.of(user)
            ),
            EnvironmentValueName.USER,
            user
        );
    }

    // setEnvironmentValue..............................................................................................

    @Override
    public void testSetEnvironmentValueWithNowFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testSetEnvironmentValueWithLocale() {
        final EmptyEnvironmentContext context = this.createContext();

        final Locale locale = Locale.GERMAN;
        this.setEnvironmentValueAndCheck(
            context,
            EnvironmentValueName.LOCALE,
            locale
        );
    }

    @Test
    public void testSetEnvironmentValueWithUser() {
        final EmptyEnvironmentContext context = this.createContext();

        final EmailAddress user = EmailAddress.parse("different@example.com");
        this.setEnvironmentValueAndCheck(
            context,
            EnvironmentContext.USER,
            user
        );
    }

    // setTimeout.......................................................................................................

    @Test
    public void testSetTimeoutWithDifferent() {
        this.setTimeOffsetAndCheck(
            this.createContext(),
            ZoneOffset.ofHours(12)
        );
    }

    // setUser..........................................................................................................

    @Test
    public void testSetUserWithDifferent() {
        this.setUserAndCheck(
            this.createContext(),
            EmailAddress.parse("different@example.com")
        );
    }

    @Override
    public EmptyEnvironmentContext createContext() {
        return this.createContext(
            EnvironmentContext.ANONYMOUS
        );
    }

    private EmptyEnvironmentContext createContext(final Optional<EmailAddress> user) {
        return EmptyEnvironmentContext.with(
            INDENTATION,
            LINE_ENDING,
            LOCALE,
            HAS_NOW,
            user
        );
    }

    // removeEnvironmentValue...........................................................................................

    @Override
    public void testRemoveEnvironmentValueWithNowFails() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testRemoveEnvironmentValueIndentationFails() {
        assertThrows(
            ReadOnlyEnvironmentValueException.class,
            () -> this.createContext()
                .removeEnvironmentValue(EnvironmentContext.INDENTATION)
        );
    }

    @Test
    public void testRemoveEnvironmentValueLineEndingFails() {
        assertThrows(
            ReadOnlyEnvironmentValueException.class,
            () -> this.createContext()
                .removeEnvironmentValue(EnvironmentContext.LINE_ENDING)
        );
    }

    @Test
    public void testRemoveEnvironmentValueLocaleFails() {
        assertThrows(
            ReadOnlyEnvironmentValueException.class,
            () -> this.createContext()
                .removeEnvironmentValue(EnvironmentContext.LOCALE)
        );
    }

    @Test
    public void testRemoveEnvironmentValueNowFails() {
        assertThrows(
            ReadOnlyEnvironmentValueException.class,
            () -> this.createContext()
                .removeEnvironmentValue(EnvironmentContext.NOW)
        );
    }

    @Test
    public void testRemoveEnvironmentValueWithUserNotAnonymous() {
        final EmptyEnvironmentContext context = EmptyEnvironmentContext.with(
            INDENTATION,
            LINE_ENDING,
            LOCALE,
            HAS_NOW,
            Optional.of(
                EmailAddress.parse("user@example.com")
            )
        );

        this.removeEnvironmentValueAndCheck(
            context,
            EnvironmentContext.USER
        );
    }

    @Test
    public void testRemoveEnvironmentValueUserAndAnonymous() {
        final EmptyEnvironmentContext context = EmptyEnvironmentContext.with(
            INDENTATION,
            LINE_ENDING,
            LOCALE,
            HAS_NOW,
            EnvironmentContext.ANONYMOUS
        );

        this.removeEnvironmentValueAndCheck(
            context,
            EnvironmentContext.USER
        );
    }

    // environmentValueNames............................................................................................

    @Test
    public void testEnvironmentalValueNames() {
        this.environmentValueNamesAndCheck(
            EmptyEnvironmentContext.with(
                INDENTATION,
                LineEnding.NL,
                LOCALE,
                HAS_NOW,
                Optional.of(
                    EmailAddress.parse("different@example.com")
                )
            ),
            EnvironmentContext.INDENTATION,
            EnvironmentContext.LINE_ENDING,
            EnvironmentContext.LOCALE,
            EnvironmentContext.NOW,
            EnvironmentContext.TIME_OFFSET,
            EnvironmentContext.USER
        );
    }

    @Test
    public void testEnvironmentalValueNamesWithoutUser() {
        this.environmentValueNamesAndCheck(
            EmptyEnvironmentContext.with(
                INDENTATION,
                LineEnding.NL,
                LOCALE,
                HAS_NOW,
                EnvironmentContext.ANONYMOUS
            ),
            EnvironmentContext.INDENTATION,
            EnvironmentContext.LINE_ENDING,
            EnvironmentContext.LOCALE,
            EnvironmentContext.TIME_OFFSET,
            EnvironmentContext.NOW
        );
    }

    // now..............................................................................................................

    @Test
    public void testNowAndZeroTimeOffset() {
        final ZoneOffset zoneOffset = ZoneOffset.UTC;

        final EmptyEnvironmentContext context = this.createContext();
        context.setTimeOffset(zoneOffset);

        this.checkEquals(
            LocalDateTime.MIN.atOffset(zoneOffset)
                .toLocalDateTime(),
            context.now()
        );
    }

    @Test
    public void testNowAndNonZeroTimeOffset() {
        final ZoneOffset zoneOffset = ZoneOffset.ofHours(2);

        final EmptyEnvironmentContext context = this.createContext();
        context.setTimeOffset(zoneOffset);

        this.checkEquals(
            LocalDateTime.MIN.atOffset(zoneOffset)
                .toLocalDateTime(),
            context.now()
        );
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferentIndentation() {
        this.checkNotEquals(
            EmptyEnvironmentContext.with(
                Indentation.SPACES2,
                LineEnding.NL,
                LOCALE,
                HAS_NOW,
                EnvironmentContext.ANONYMOUS
            ),
            EmptyEnvironmentContext.with(
                Indentation.SPACES4,
                LineEnding.NL,
                LOCALE,
                HAS_NOW,
                EnvironmentContext.ANONYMOUS
            )
        );
    }

    @Test
    public void testEqualsDifferentLineEnding() {
        this.checkNotEquals(
            EmptyEnvironmentContext.with(
                INDENTATION,
                LineEnding.NL,
                LOCALE,
                HAS_NOW,
                EnvironmentContext.ANONYMOUS
            ),
            EmptyEnvironmentContext.with(
                INDENTATION,
                LineEnding.CR,
                LOCALE,
                HAS_NOW,
                EnvironmentContext.ANONYMOUS
            )
        );
    }

    @Test
    public void testEqualsDifferentLocale() {
        this.checkNotEquals(
            EmptyEnvironmentContext.with(
                INDENTATION,
                LINE_ENDING,
                LOCALE,
                HAS_NOW,
                EnvironmentContext.ANONYMOUS
            ),
            EmptyEnvironmentContext.with(
                INDENTATION,
                LINE_ENDING,
                Locale.FRANCE,
                HAS_NOW,
                EnvironmentContext.ANONYMOUS
            )
        );
    }

    @Test
    public void testEqualsDifferentHasNow() {
        this.checkNotEquals(
            EmptyEnvironmentContext.with(
                INDENTATION,
                LINE_ENDING,
                LOCALE,
                HAS_NOW,
                EnvironmentContext.ANONYMOUS
            ),
            EmptyEnvironmentContext.with(
                INDENTATION,
                LINE_ENDING,
                Locale.FRANCE,
                LocalDateTime::now,
                EnvironmentContext.ANONYMOUS
            )
        );
    }

    @Test
    public void testEqualsDifferentUser() {
        this.checkNotEquals(
            EmptyEnvironmentContext.with(
                INDENTATION,
                LINE_ENDING,
                LOCALE,
                HAS_NOW,
                Optional.of(
                    EmailAddress.parse("different@example.com")
                )
            )
        );
    }

    @Test
    public void testEqualsDifferentUser2() {
        this.checkNotEquals(
            EmptyEnvironmentContext.with(
                INDENTATION,
                LINE_ENDING,
                LOCALE,
                HAS_NOW,
                Optional.of(
                    EmailAddress.parse("user1@example.com")
                )
            ),
            EmptyEnvironmentContext.with(
                INDENTATION,
                LINE_ENDING,
                LOCALE,
                HAS_NOW,
                Optional.of(
                    EmailAddress.parse("user222@example.com")
                )
            )
        );
    }

    @Override
    public EmptyEnvironmentContext createObject() {
        return this.createContext();
    }

    // toString.........................................................................................................

    @Test
    public void testToStringWithoutUser() {
        this.toStringAndCheck(
            this.createContext(
                EnvironmentContext.ANONYMOUS
            ),
            "{indentation=\"    \", lineEnding=\"\\n\", locale=\"en\", now=-999999999-01-01T00:00}"
        );
    }

    @Test
    public void testToStringWithUser() {
        this.toStringAndCheck(
            this.createContext(
                Optional.of(
                    EmailAddress.parse("user@example.com")
                )
            ),
            "{indentation=\"    \", lineEnding=\"\\n\", locale=\"en\", now=-999999999-01-01T00:00, user=\"user@example.com\"}"
        );
    }

    // TreePrintable....................................................................................................

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
            this.createContext(),
            "EmptyEnvironmentContext\n" +
                "  indentation\n" +
                "    \"    \" (walkingkooka.text.Indentation)\n" +
                "  lineEnding\n" +
                "    \"\\n\"\n" +
                "  now\n" +
                "    -999999999-01-01T00:00 (java.time.LocalDateTime)\n" +
                "  locale\n" +
                "    en (java.util.Locale)\n" +
                "  timeOffset\n" +
                "    Z (java.time.ZoneOffset)\n"
        );
    }

    // type naming......................................................................................................

    @Override
    public String typeNameSuffix() {
        return EnvironmentContext.class.getSimpleName();
    }

    // class............................................................................................................

    @Override
    public Class<EmptyEnvironmentContext> type() {
        return EmptyEnvironmentContext.class;
    }
}
