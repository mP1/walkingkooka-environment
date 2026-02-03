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
import walkingkooka.collect.list.Lists;
import walkingkooka.collect.set.Sets;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.predicate.Predicates;
import walkingkooka.props.Properties;
import walkingkooka.props.PropertiesPath;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EnvironmentContextSharedCollectionTest extends EnvironmentContextSharedTestCase<EnvironmentContextSharedCollection>
    implements HashCodeEqualsDefinedTesting2<EnvironmentContextSharedCollection> {

    private final static Indentation INDENTATION = Indentation.SPACES4;

    private final static LineEnding LINE_ENDING = LineEnding.NL;

    private final static Locale LOCALE = Locale.ENGLISH;

    private final static String NAME1 = "hello.111";

    private final static String VALUE1 = "Gday";

    private final static String NAME2 = "zebra.222";

    private final static String VALUE2 = "Orange";

    private final static LocalDateTime NOW = LocalDateTime.MIN;

    private final static EnvironmentContext CONTEXT = EnvironmentContexts.readOnly(
        Predicates.always(), // readOnlyNames
        EnvironmentContexts.empty(
            INDENTATION,
            LINE_ENDING,
            LOCALE,
            () -> NOW,
            EnvironmentContext.ANONYMOUS
        )
    );

    private final static EnvironmentContext FAKE_CONTEXT = new FakeEnvironmentContext() {
        @Override
        public Runnable addEventValueWatcher(final EnvironmentValueWatcher watcher) {
            return () -> {};
        }

        @Override
        public Runnable addEventValueWatcherOnce(final EnvironmentValueWatcher watcher) {
            return () -> {};
        }

        @Override
        public Set<EnvironmentValueName<?>> environmentValueNames() {
            return Sets.of(
                EnvironmentContext.INDENTATION,
                EnvironmentContext.LINE_ENDING,
                EnvironmentContext.LOCALE,
                EnvironmentContext.NOW,
                EnvironmentValueName.USER
            );
        }
    };

    // with.............................................................................................................

    @Test
    public void testWithNullContextsFails() {
        assertThrows(
            NullPointerException.class,
            () -> EnvironmentContextSharedCollection.with(
                null
            )
        );
    }

    @Test
    public void testWithEmptyContextFails() {
        assertThrows(
            IllegalArgumentException.class,
            () -> EnvironmentContextSharedCollection.with(
                Lists.empty()
            )
        );
    }

    @Test
    public void testWithOneEnvironmentContext() {
        assertSame(
            CONTEXT,
            EnvironmentContextSharedCollection.with(
                Lists.of(CONTEXT)
            )
        );
    }

    // cloneEnvironment.................................................................................................

    @Test
    public void testCloneEnvironment() {
        final EnvironmentContext context = EnvironmentContexts.map(
            CONTEXT
        );
        context.setEnvironmentValue(
            EnvironmentValueName.with(NAME1, String.class),
            VALUE1
        );

        final EnvironmentContext collection = EnvironmentContextSharedCollection.with(
            Lists.of(
                context,
                CONTEXT
            )
        );
        final EnvironmentContext clone = collection.cloneEnvironment();

        assertNotSame(
            collection,
            clone
        );

        this.getAllEnvironmentValueAndCheck(
            collection,
            clone
        );

        this.environmentValueAndCheck(
            clone,
            EnvironmentValueName.with(NAME1, String.class),
            VALUE1
        );
    }

    // setEnvironmentContext............................................................................................

    @Test
    public void testSetEnvironmentContext() {
        final EnvironmentContextSharedCollection context = this.createContext();

        final EnvironmentContext different = EnvironmentContextSharedCollection.with(
            Lists.of(
                EnvironmentContexts.empty(
                    INDENTATION,
                    LineEnding.CR,
                    Locale.FRENCH,
                    () -> NOW,
                    EnvironmentContext.ANONYMOUS
                ),
                EnvironmentContexts.empty(
                    INDENTATION,
                    LineEnding.CR,
                    Locale.GERMAN,
                    () -> NOW,
                    EnvironmentContext.ANONYMOUS
                )
            )
        );

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

    // environmentValue.................................................................................................

    @Test
    public void testEnvironmentalValue1() {
        this.environmentValueAndCheck(
            EnvironmentValueName.with(
                NAME1,
                String.class
            ),
            VALUE1
        );
    }

    @Test
    public void testEnvironmentalValue2() {
        this.environmentValueAndCheck(
            EnvironmentValueName.with(
                NAME2,
                String.class
            ),
            VALUE2
        );
    }

    @Test
    public void testEnvironmentalValue1Missing() {
        this.environmentValueAndCheck(
            EnvironmentValueName.with(
                "Unknown",
                Void.class
            )
        );
    }

    // setEnvironmentValue..............................................................................................

    @Test
    public void testSetEnvironmentalValue() {
        final EnvironmentValueName<String> name = EnvironmentValueName.with(
            NAME1,
            String.class
        );

        final EnvironmentContext wrapped = EnvironmentContexts.map(
            CONTEXT
        );
        wrapped.setEnvironmentValue(
            name,
            VALUE1
        );

        final EnvironmentContext collectionEnvironmentContext = EnvironmentContextSharedCollection.with(
            Lists.of(
                CONTEXT.cloneEnvironment(),
                wrapped
            )
        );

        final String value = "replaced";

        this.setEnvironmentValueAndCheck(
            collectionEnvironmentContext,
            name,
            value
        );

        this.environmentValueAndCheck(
            wrapped,
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
    public void testRemoveEnvironmentalValue() {
        final EnvironmentValueName<String> name = EnvironmentValueName.with(
            NAME1,
            String.class
        );

        final EnvironmentContext wrapped = EnvironmentContexts.map(
            CONTEXT
        );
        wrapped.setEnvironmentValue(
            name,
            VALUE1
        );

        final EnvironmentContext collectionEnvironmentContext = EnvironmentContextSharedCollection.with(
            Lists.of(
                CONTEXT.cloneEnvironment(),
                wrapped
            )
        );

        collectionEnvironmentContext.removeEnvironmentValue(name);

        this.environmentValueAndCheck(
            collectionEnvironmentContext,
            name
        );

        this.environmentValueAndCheck(
            wrapped,
            name
        );
    }

    @Override
    public void testRemoveEnvironmentValueWithNowFails() {
        throw new UnsupportedOperationException();
    }

    // setLineEnding....................................................................................................

    @Test
    public void testSetLineEnding() {
        final EnvironmentContext wrapped = EnvironmentContexts.map(
            CONTEXT.cloneEnvironment()
        );

        final EnvironmentContext collectionEnvironmentContext = EnvironmentContextSharedCollection.with(
            Lists.of(
                wrapped,
                FAKE_CONTEXT
            )
        );

        final LineEnding different = LineEnding.CRNL;

        this.checkNotEquals(
            collectionEnvironmentContext.lineEnding(),
            different
        );

        this.setLineEndingAndCheck(
            collectionEnvironmentContext,
            different
        );

        this.lineEndingAndCheck(
            wrapped,
            different
        );
    }

    // setLocale........................................................................................................

    @Test
    public void testSetLocale() {
        final EnvironmentContext wrapped = EnvironmentContexts.map(
            CONTEXT.cloneEnvironment()
        );

        final EnvironmentContext collectionEnvironmentContext = EnvironmentContextSharedCollection.with(
            Lists.of(
                wrapped,
                FAKE_CONTEXT
            )
        );

        final Locale different = Locale.FRENCH;

        this.checkNotEquals(
            collectionEnvironmentContext.locale(),
            different
        );

        this.setLocaleAndCheck(
            collectionEnvironmentContext,
            different
        );

        this.localeAndCheck(
            wrapped,
            different
        );
    }

    // setUser..........................................................................................................

    @Test
    public void testSetUser() {
        final EnvironmentContext wrapped = EnvironmentContexts.map(
            CONTEXT.cloneEnvironment()
        );

        final EnvironmentContext collectionEnvironmentContext = EnvironmentContextSharedCollection.with(
            Lists.of(
                wrapped,
                FAKE_CONTEXT
            )
        );

        final EmailAddress different = EmailAddress.parse("different@example.com");

        this.setUserAndCheck(
            collectionEnvironmentContext,
            different
        );

        this.userAndCheck(
            wrapped,
            different
        );
    }

    // environmentValueNames............................................................................................

    @Test
    public void testEnvironmentalValueNames() {
        final String key11 = "key1";
        final String key12 = "prefix.key1";
        final String key21 = "key2";
        final String key22 = "prefix.key2";

        this.environmentValueNamesAndCheck(
            EnvironmentContextSharedCollection.with(
                Lists.of(
                    EnvironmentContexts.properties(
                        Properties.EMPTY.set(
                            PropertiesPath.parse(key11),
                            "value111"
                        ).set(
                            PropertiesPath.parse(key12),
                            "value222"
                        ),
                        FAKE_CONTEXT
                    ),
                    EnvironmentContexts.properties(
                        Properties.EMPTY.set(
                            PropertiesPath.parse(key21),
                            "value111"
                        ).set(
                            PropertiesPath.parse(key22),
                            "value222"
                        ),
                        FAKE_CONTEXT
                    )
                )
            ),
            EnvironmentValueName.with(
                key11,
                String.class
            ),
            EnvironmentValueName.with(
                key12,
                String.class
            ),
            EnvironmentValueName.with(
                key21,
                String.class
            ),
            EnvironmentValueName.with(
                key22,
                String.class
            ),
            EnvironmentContext.INDENTATION,
            EnvironmentContext.LINE_ENDING,
            EnvironmentContext.LOCALE,
            EnvironmentContext.NOW,
            EnvironmentValueName.USER
        );
    }

    @Override
    public EnvironmentContextSharedCollection createContext() {
        return Cast.to(
            EnvironmentContextSharedCollection.with(
                Lists.of(
                    CONTEXT.cloneEnvironment(),
                    EnvironmentContexts.properties(
                        Properties.EMPTY.set(
                            PropertiesPath.parse(NAME1),
                            VALUE1
                        ),
                        FAKE_CONTEXT
                    ),
                    EnvironmentContexts.properties(
                        Properties.EMPTY.set(
                            PropertiesPath.parse(NAME1),
                            "ignored!!!"
                        ).set(
                            PropertiesPath.parse(NAME2),
                            VALUE2
                        ),
                        FAKE_CONTEXT
                    )
                )
            )
        );
    }

    // hashCode/equals..................................................................................................

    @Test
    public void testEqualsDifferentEnvironmentContexts() {
        this.checkNotEquals(
            EnvironmentContextSharedCollection.with(
                Lists.of(
                    EnvironmentContexts.properties(
                        Properties.EMPTY.set(
                            PropertiesPath.parse(NAME1),
                            VALUE1
                        ),
                        CONTEXT
                    ),
                    EnvironmentContexts.properties(
                        Properties.EMPTY.set(
                            PropertiesPath.parse(NAME1),
                            "different-value"
                        ),
                        CONTEXT
                    )
                )
            )
        );
    }

    @Override
    public EnvironmentContextSharedCollection createObject() {
        return this.createContext();
    }

    // toString.........................................................................................................

    @Test
    public void testToString() {
        this.toStringAndCheck(
            EnvironmentContextSharedCollection.with(
                Lists.of(
                    CONTEXT,
                    EnvironmentContexts.properties(
                        Properties.EMPTY.set(
                            PropertiesPath.parse(NAME1),
                            VALUE1
                        ),
                        CONTEXT
                    ),
                    EnvironmentContexts.properties(
                        Properties.EMPTY.set(
                            PropertiesPath.parse(NAME1),
                            "different-value"
                        ),
                        CONTEXT
                    )
                )
            ),
            "{hello.111=Gday, indentation=    , lineEnding=\\n, locale=en, now=-999999999-01-01T00:00}"
        );
    }

    // class............................................................................................................

    @Override
    public Class<EnvironmentContextSharedCollection> type() {
        return EnvironmentContextSharedCollection.class;
    }
}
