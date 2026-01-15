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

import walkingkooka.environment.EnvironmentContextDelegatorTest.TestEnvironmentContextDelegator;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.text.LineEnding;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public final class EnvironmentContextDelegatorTest implements EnvironmentContextTesting2<TestEnvironmentContextDelegator> {

    @Override
    public void testRemoveEnvironmentValueWithNowFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetEnvironmentValueWithNowFails() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetLineEndingWithDifferentAndWatcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetLocaleWithDifferent() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetLocaleWithDifferentAndWatcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetUserWithDifferentAndWatcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TestEnvironmentContextDelegator createContext() {
        return new TestEnvironmentContextDelegator();
    }

    // class............................................................................................................

    @Override
    public String typeNameSuffix() {
        return "Delegator";
    }

    @Override
    public Class<TestEnvironmentContextDelegator> type() {
        return TestEnvironmentContextDelegator.class;
    }

    final static class TestEnvironmentContextDelegator implements EnvironmentContextDelegator {

        @Override
        public TestEnvironmentContextDelegator cloneEnvironment() {
            throw new UnsupportedOperationException();
        }

        @Override
        public TestEnvironmentContextDelegator setEnvironmentContext(final EnvironmentContext environmentContext) {
            Objects.requireNonNull(environmentContext, "environmentContext");
            return new TestEnvironmentContextDelegator();
        }

        @Override
        public TestEnvironmentContextDelegator setLineEnding(final LineEnding lineEnding) {
            Objects.requireNonNull(lineEnding, "lineEnding");
            throw new UnsupportedOperationException();
        }

        @Override
        public void setLocale(final Locale locale) {
            Objects.requireNonNull(locale, "locale");
            throw new UnsupportedOperationException();
        }

        @Override
        public TestEnvironmentContextDelegator setUser(final Optional<EmailAddress> user) {
            Objects.requireNonNull(user, "user");
            throw new UnsupportedOperationException();
        }

        @Override
        public EnvironmentContext environmentContext() {
            return EnvironmentContexts.empty(
                LineEnding.NL,
                Locale.ENGLISH,
                () -> LocalDateTime.MIN,
                EnvironmentContext.ANONYMOUS
            );
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }
}
