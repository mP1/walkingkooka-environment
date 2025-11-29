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
import walkingkooka.text.LineEnding;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class MapEnvironmentContextTest implements EnvironmentContextTesting2<MapEnvironmentContext>,
    HashCodeEqualsDefinedTesting2<MapEnvironmentContext>,
    ToStringTesting<MapEnvironmentContext> {

    private final static LineEnding LINE_ENDING = LineEnding.NL;

    private final static Locale LOCALE = Locale.FRENCH;

    private final static LocalDateTime NOW = LocalDateTime.MIN;

    private final static HasNow HAS_NOW = () -> NOW;

    private final static EnvironmentContext CONTEXT = EnvironmentContexts.empty(
        LINE_ENDING,
        LOCALE,
        HAS_NOW,
        EnvironmentContext.ANONYMOUS
    );

    private final static EnvironmentValueName<String> NAME = EnvironmentValueName.with("hello.123");

    private final static String VALUE = "Gday";

    // with.............................................................................................................

    @Test
    public void testWithNullContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> MapEnvironmentContext.with(
                null
            )
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
        final MapEnvironmentContext context = this.createContext();

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
        final MapEnvironmentContext context = this.createContext();

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
        final MapEnvironmentContext context = MapEnvironmentContext.with(CONTEXT);
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
            EnvironmentValueName.with("Unknown")
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
        final MapEnvironmentContext context = MapEnvironmentContext.with(
            new FakeEnvironmentContext() {

                @Override
                public Locale locale() {
                    return MapEnvironmentContextTest.this.locale;
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
        final MapEnvironmentContext context = MapEnvironmentContext.with(
            new FakeEnvironmentContext() {

                @Override
                public Optional<EmailAddress> user() {
                    return Optional.ofNullable(
                        MapEnvironmentContextTest.this.user
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
        final EnvironmentValueName<String> name1 = EnvironmentValueName.with("prefix.name1");

        final MapEnvironmentContext context = MapEnvironmentContext.with(CONTEXT);
        context.setEnvironmentValue(
            name1,
            VALUE
        );

        final EnvironmentValueName<String> name2 = EnvironmentValueName.with("prefix.name2");

        context.setEnvironmentValue(
            name2,
            VALUE
        );

        {
            final EnvironmentValueName<String> name3 = EnvironmentValueName.with("REMOVED");

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
            EnvironmentValueName.LINE_ENDING,
            EnvironmentValueName.LOCALE,
            name1,
            name2
        );
    }

    @Test
    public void testEnvironmentalValueNamesWhenUserPresent() {
        final EnvironmentValueName<String> name1 = EnvironmentValueName.with("prefix.name1");

        final MapEnvironmentContext context = this.createContext(
            Optional.of(
                EmailAddress.parse("user183@example.com")
            )
        );
        context.setEnvironmentValue(
            name1,
            VALUE
        );

        final EnvironmentValueName<String> name2 = EnvironmentValueName.with("prefix.name2");

        context.setEnvironmentValue(
            name2,
            VALUE
        );

        {
            final EnvironmentValueName<String> name3 = EnvironmentValueName.with("REMOVED");

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
            EnvironmentValueName.LINE_ENDING,
            EnvironmentValueName.LOCALE,
            EnvironmentValueName.USER,
            name1,
            name2
        );
    }

    // setUser..........................................................................................................

    @Test
    public void testSetUser() {
        final MapEnvironmentContext context = MapEnvironmentContext.with(CONTEXT);

        final EmailAddress email = EmailAddress.parse("different@example.com");

        this.setUserAndCheck(
            context,
            email
        );
    }

    // Context..........................................................................................................

    @Override
    public MapEnvironmentContext createContext() {
        return this.createContext(EnvironmentContext.ANONYMOUS);
    }

    public MapEnvironmentContext createContext(final Optional<EmailAddress> user) {
        return MapEnvironmentContext.with(
            EnvironmentContexts.empty(
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
            MapEnvironmentContext.with(
                EnvironmentContexts.empty(
                    LINE_ENDING,
                    Locale.FRANCE,
                    HAS_NOW,
                    EnvironmentContext.ANONYMOUS
                )
            ),
            MapEnvironmentContext.with(
                EnvironmentContexts.empty(
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
        final EnvironmentValueName<String> name = EnvironmentValueName.with("Hello");

        this.checkNotEquals(
            this.createContext()
                .setEnvironmentValue(
                    name,
                    "World1"
                ),
            this.createContext()
                .setEnvironmentValue(
                    name,
                    "World22"
                )
        );
    }

    @Override
    public MapEnvironmentContext createObject() {
        return this.createContext();
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        final MapEnvironmentContext context = MapEnvironmentContext.with(CONTEXT);
        context.setEnvironmentValue(
            NAME,
            VALUE
        );

        this.toStringAndCheck(
            context,
            "{hello.123=Gday}"
        );
    }

    // type naming......................................................................................................

    @Override
    public String typeNameSuffix() {
        return EnvironmentContext.class.getSimpleName();
    }

    // class............................................................................................................

    @Override
    public Class<MapEnvironmentContext> type() {
        return MapEnvironmentContext.class;
    }
}
