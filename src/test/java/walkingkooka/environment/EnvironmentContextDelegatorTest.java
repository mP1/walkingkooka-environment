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
import walkingkooka.environment.EnvironmentContextDelegatorTest.TestEnvironmentContextDelegator;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;

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
    public void testSetCurrencyWithDifferentAndWatcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetIndentationWithDifferentAndWatcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetLineEndingWithDifferentAndWatcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetLocaleWithDifferentAndWatcher() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testSetTimeOffsetWithDifferentAndWatcher() {
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

    // HasEnvironmentContext............................................................................................

    @Test
    @Override
    public void testEnvironmentContext() {
        final TestEnvironmentContextDelegator context = this.createContext();

        this.environmentContextAndCheck(
            context,
            context.environmentContext
        );
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
        public void setIndentation(final Indentation indentation) {
            this.environmentContext.setIndentation(indentation);
        }
        
        @Override
        public void setLineEnding(final LineEnding lineEnding) {
            this.environmentContext.setLineEnding(lineEnding);
        }

        @Override
        public void setLocale(final Locale locale) {
            this.environmentContext.setLocale(locale);
        }

        @Override
        public void setUser(final Optional<EmailAddress> user) {
            this.environmentContext.setUser(user);
        }

        @Override
        public EnvironmentContext environmentContext() {
            return this.environmentContext;
        }

        private final EnvironmentContext environmentContext = ENVIRONMENT_CONTEXT.cloneEnvironment();

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }
}
