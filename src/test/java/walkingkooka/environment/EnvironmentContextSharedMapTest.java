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
import walkingkooka.Cast;
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.datetime.HasNow;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EnvironmentContextSharedMapTest extends EnvironmentContextSharedTestCase<EnvironmentContextSharedMap>
    implements HashCodeEqualsDefinedTesting2<EnvironmentContextSharedMap>,
    ToStringTesting<EnvironmentContextSharedMap> {

    private final static Currency CURRENCY = Currency.getInstance("AUD");

    private final static Indentation INDENTATION = Indentation.SPACES4;

    private final static LineEnding LINE_ENDING = LineEnding.NL;

    private final static Locale LOCALE = Locale.FRENCH;

    private final static LocalDateTime NOW = LocalDateTime.MIN;

    private final static HasNow HAS_NOW = () -> NOW;

    private final static EnvironmentContext CONTEXT = EnvironmentContexts.empty(
        CURRENCY,
        INDENTATION,
        LINE_ENDING,
        LOCALE,
        HAS_NOW,
        EnvironmentContext.ANONYMOUS
    );

    private final static EnvironmentValueName<String> NAME = EnvironmentValueName.with(
        "hello.123",
        String.class
    );

    private final static String VALUE = "Gday";

    // with.............................................................................................................

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> EnvironmentContextSharedMap.with(
                null
            )
        );
    }

    // currency...........................................................................................................

    @Test
    public void testCurrency() {
        this.currencyAndCheck(
            this.createContext(),
            CURRENCY
        );
    }

    @Test
    public void testSetCurrency() {
        final EnvironmentContextSharedMap context = this.createContext();

        final Currency currency = Currency.getInstance("NZD");
        this.checkNotEquals(
            CURRENCY,
            currency
        );

        this.setCurrencyAndCheck(
            context,
            currency
        );
    }
    
    // lineEnding...........................................................................................................

    @Test
    public void testLineEnding() {
        this.lineEndingAndCheck(
            this.createContext(),
            LINE_ENDING
        );
    }

    @Test
    public void testSetLineEnding() {
        final EnvironmentContextSharedMap context = this.createContext();

        final LineEnding lineEnding = LineEnding.CR;
        this.checkNotEquals(
            LINE_ENDING,
            lineEnding
        );

        this.setLineEndingAndCheck(
            context,
            lineEnding
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
        final EnvironmentContextSharedMap context = this.createContext();

        final Locale locale = Locale.GERMANY;

        context.setLocale(locale);

        this.localeAndCheck(
            context,
            locale
        );
    }

    // environmentValue.................................................................................................

    @Test
    public void testEnvironmentalValue() {
        final EnvironmentContextSharedMap context = EnvironmentContextSharedMap.with(CONTEXT);
        context.setEnvironmentValue(
            NAME,
            VALUE
        );

        this.environmentValueAndCheck(
            context,
            NAME,
            VALUE
        );
    }

    @Test
    public void testEnvironmentalValueMissing() {
        this.environmentValueAndCheck(
            this.createContext(),
            EnvironmentValueName.with(
                "Unknown",
                Void.class
            )
        );
    }

    @Test
    public void testEnvironmentalValueWithLocale() {
        this.environmentValueAndCheck(
            this.createContext(),
            EnvironmentValueName.LOCALE,
            LOCALE
        );
    }

    @Test
    public void testEnvironmentalValueWithLocaleAfterWrappedContextLocaleChange() {
        final EnvironmentContextSharedMap context = EnvironmentContextSharedMap.with(
            new FakeEnvironmentContext() {

                @Override
                public <T> Optional<T> environmentValue(final EnvironmentValueName<T> name) {
                    return Optional.ofNullable(
                        name.equals(LOCALE) ?
                            Cast.to(EnvironmentContextSharedMapTest.LOCALE) :
                            null
                    );
                }
            }
        );

        this.locale = LOCALE;

        this.environmentValueAndCheck(
            context,
            EnvironmentValueName.LOCALE,
            LOCALE
        );

        this.locale = Locale.FRENCH;

        this.environmentValueAndCheck(
            context,
            EnvironmentValueName.LOCALE,
            this.locale
        );
    }

    private Locale locale;

    private final static EmailAddress USER = EmailAddress.parse("user@example.com");

    @Test
    public void testEnvironmentalValueWithUser() {
        this.environmentValueAndCheck(
            this.createContext(
                Optional.of(USER)
            ),
            EnvironmentValueName.USER,
            USER
        );
    }

    @Test
    public void testEnvironmentalValueWithUserAfterWrappedContextUserChange() {
        final EnvironmentContextSharedMap context = EnvironmentContextSharedMap.with(
            new FakeEnvironmentContext() {

                @Override
                public <T> Optional<T> environmentValue(final EnvironmentValueName<T> name) {
                    return Optional.ofNullable(
                        name.equals(USER) ?
                            Cast.to(EnvironmentContextSharedMapTest.this.user) :
                            null
                    );
                }
            }
        );

        this.user = USER;

        this.environmentValueAndCheck(
            context,
            EnvironmentValueName.USER,
            USER
        );

        this.user = EmailAddress.parse("user-changed@example.com");

        this.environmentValueAndCheck(
            context,
            EnvironmentValueName.USER,
            this.user
        );
    }

    private EmailAddress user;

    // environmentValueNames............................................................................................

    @Test
    public void testEnvironmentalValueNamesWhenAnonymous() {
        final EnvironmentValueName<String> name1 = EnvironmentValueName.with(
            "prefix.name1",
            String.class
        );

        final EnvironmentContextSharedMap context = EnvironmentContextSharedMap.with(CONTEXT);
        context.setEnvironmentValue(
            name1,
            VALUE
        );

        final EnvironmentValueName<String> name2 = EnvironmentValueName.with(
            "prefix.name2",
            String.class
        );

        context.setEnvironmentValue(
            name2,
            VALUE
        );

        {
            final EnvironmentValueName<String> name3 = EnvironmentValueName.with(
                "REMOVED",
                String.class
            );

            context.setEnvironmentValue(
                name3,
                VALUE
            );
            context.removeEnvironmentValue(
                name3
            );
        }

        this.environmentValueNamesAndCheck(
            context,
            EnvironmentValueName.CURRENCY,
            EnvironmentValueName.INDENTATION,
            EnvironmentValueName.LINE_ENDING,
            EnvironmentValueName.LOCALE,
            EnvironmentValueName.NOW,
            name1,
            name2,
            EnvironmentValueName.TIME_OFFSET
        );
    }

    @Test
    public void testEnvironmentalValueNamesWhenUserPresent() {
        final EnvironmentValueName<String> name1 = EnvironmentValueName.with(
            "prefix.name1",
            String.class
        );

        final EnvironmentContextSharedMap context = this.createContext(
            Optional.of(
                EmailAddress.parse("user183@example.com")
            )
        );
        context.setEnvironmentValue(
            name1,
            VALUE
        );

        final EnvironmentValueName<String> name2 = EnvironmentValueName.with(
            "prefix.name2",
            String.class
        );

        context.setEnvironmentValue(
            name2,
            VALUE
        );

        {
            final EnvironmentValueName<String> name3 = EnvironmentValueName.with(
                "REMOVED",
                String.class
            );

            context.setEnvironmentValue(
                name3,
                VALUE
            );
            context.removeEnvironmentValue(
                name3
            );
        }

        this.environmentValueNamesAndCheck(
            context,
            EnvironmentValueName.CURRENCY,
            EnvironmentValueName.INDENTATION,
            EnvironmentValueName.LINE_ENDING,
            EnvironmentValueName.LOCALE,
            EnvironmentValueName.NOW,
            EnvironmentValueName.TIME_OFFSET,
            EnvironmentValueName.USER,
            name1,
            name2
        );
    }

    // setUser..........................................................................................................

    @Test
    public void testSetUser() {
        final EnvironmentContextSharedMap context = EnvironmentContextSharedMap.with(CONTEXT);

        final EmailAddress email = EmailAddress.parse("different@example.com");

        this.setUserAndCheck(
            context,
            email
        );
    }

    // setEnvironmentContext............................................................................................

    @Test
    public void testSetEnvironmentContext() {
        final EnvironmentContextSharedMap context = this.createContext();

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

    // Context..........................................................................................................

    @Override
    public EnvironmentContextSharedMap createContext() {
        return this.createContext(EnvironmentContext.ANONYMOUS);
    }

    public EnvironmentContextSharedMap createContext(final Optional<EmailAddress> user) {
        return EnvironmentContextSharedMap.with(
            EnvironmentContexts.empty(
                CURRENCY,
                INDENTATION,
                LINE_ENDING,
                LOCALE,
                HAS_NOW,
                user
            )
        );
    }

    // hashCode/equals..................................................................................................

    @Test
    public void testEqualsDifferentContext() {
        this.checkNotEquals(
            EnvironmentContextSharedMap.with(
                EnvironmentContexts.empty(
                    CURRENCY,
                    INDENTATION,
                    LINE_ENDING,
                    Locale.FRANCE,
                    HAS_NOW,
                    EnvironmentContext.ANONYMOUS
                )
            ),
            EnvironmentContextSharedMap.with(
                EnvironmentContexts.empty(
                    CURRENCY,
                    INDENTATION,
                    LINE_ENDING,
                    Locale.GERMAN,
                    HAS_NOW,
                    Optional.of(
                        EmailAddress.parse("different@example.com")
                    )
                )
            )
        );
    }

    @Test
    public void testEqualsDifferentValues() {
        final EnvironmentValueName<String> name = EnvironmentValueName.with(
            "Hello",
            String.class
        );

        final EnvironmentContext context = this.createContext();
        context.setEnvironmentValue(
            name,
            "World1"
        );

        final EnvironmentContext different = this.createContext();
        different.setEnvironmentValue(
            name,
            "World22"
        );

        this.checkNotEquals(
            context,
            different
        );
    }

    @Override
    public EnvironmentContextSharedMap createObject() {
        return this.createContext();
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        final EnvironmentContextSharedMap context = EnvironmentContextSharedMap.with(CONTEXT);
        context.setEnvironmentValue(
            NAME,
            VALUE
        );

        this.toStringAndCheck(
            context,
            "{currency=\"AUD\", hello.123=Gday, indentation=\"    \", lineEnding=\"\\n\", locale=fr, timeOffset=Z}"
        );
    }

    @Test
    public void testToStringWithUser() {
        final EnvironmentContextSharedMap context = EnvironmentContextSharedMap.with(CONTEXT);
        context.setEnvironmentValue(
            NAME,
            VALUE
        );
        context.setUser(
            Optional.of(
                EmailAddress.parse("user@example.com")
            )
        );

        this.toStringAndCheck(
            context,
            "{currency=\"AUD\", hello.123=Gday, indentation=\"    \", lineEnding=\"\\n\", locale=fr, timeOffset=Z, user=user@example.com}"
        );
    }

    // TreePrintable....................................................................................................

    @Test
    public void testTreePrint() {
        this.treePrintAndCheck(
            this.createContext(),
            "EnvironmentContextSharedMap\n" +
                "  currency\n" +
                "    AUD (java.util.Currency)\n" +
                "  indentation\n" +
                "    \"    \" (walkingkooka.text.Indentation)\n" +
                "  lineEnding\n" +
                "    \"\\n\"\n" +
                "  locale\n" +
                "    fr (java.util.Locale)\n" +
                "  now\n" +
                "    -999999999-01-01T00:00 (java.time.LocalDateTime)\n" +
                "  timeOffset\n" +
                "    Z (java.time.ZoneOffset)\n"
        );
    }

    @Test
    public void testTreePrint2() {
        final EnvironmentContextSharedMap context = this.createContext();
        context.setEnvironmentValue(
            EnvironmentValueName.with(
                "Hello",
                String.class
            ),
            "world"
        );

        this.treePrintAndCheck(
            context,
            "EnvironmentContextSharedMap\n" +
                "  currency\n" +
                "    AUD (java.util.Currency)\n" +
                "  Hello\n" +
                "    \"world\"\n" +
                "  indentation\n" +
                "    \"    \" (walkingkooka.text.Indentation)\n" +
                "  lineEnding\n" +
                "    \"\\n\"\n" +
                "  locale\n" +
                "    fr (java.util.Locale)\n" +
                "  now\n" +
                "    -999999999-01-01T00:00 (java.time.LocalDateTime)\n" +
                "  timeOffset\n" +
                "    Z (java.time.ZoneOffset)\n"
        );
    }

    // class............................................................................................................

    @Override
    public Class<EnvironmentContextSharedMap> type() {
        return EnvironmentContextSharedMap.class;
    }
}
