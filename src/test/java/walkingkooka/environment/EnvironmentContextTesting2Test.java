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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

public final class EnvironmentContextTesting2Test implements ClassTesting2<EnvironmentContextTesting2> {

    @Test
    public void testEnvironmentValueNameConstantsKebabCaseIgnoresNonEnvironmentValueName() throws Exception {
        new TestEnvironmentContextTesting<>(
            TestKebabEnvironmentContext.class
        ).testEnvironmentValueNameConstantsCamelCase();
    }

    final static class TestNonEnvironmentValueNameEnvironmentContext extends FakeEnvironmentContext {
        public final static String STRING = "String";
    }

    @Test
    public void testEnvironmentValueNameConstantsKebabCaseWithKebabCase() throws Exception {
        new TestEnvironmentContextTesting<>(
            TestNonEnvironmentValueNameEnvironmentContext.class
        ).testEnvironmentValueNameConstantsCamelCase();
    }

    final static class TestKebabEnvironmentContext extends FakeEnvironmentContext {
        public final static EnvironmentValueName<?> KEBAB = EnvironmentValueName.with("kebabNext");
    }

    @Test
    public void testEnvironmentValueNameConstantsKebabCaseWithTitleCase() throws Exception {
        boolean thrown = false;

        try {
            new TestEnvironmentContextTesting<>(
                TestTitleCaseEnvironmentContext.class
            ).testEnvironmentValueNameConstantsCamelCase();
        } catch (final AssertionError expected) {
            thrown = true;
        }

        this.checkEquals(
            true,
            thrown
        );
    }

    final static class TestTitleCaseEnvironmentContext extends FakeEnvironmentContext {
        public final static EnvironmentValueName<?> TITLE = EnvironmentValueName.with("Title");
    }

    @Test
    public void testEnvironmentValueNameConstantsKebabCaseWithSnakeCase() throws Exception {
        boolean thrown = false;

        try {
            new TestEnvironmentContextTesting<>(
                TestSnakeCaseEnvironmentContext.class
            ).testEnvironmentValueNameConstantsCamelCase();
        } catch (final AssertionError expected) {
            thrown = true;
        }

        this.checkEquals(
            true,
            thrown
        );
    }

    final static class TestSnakeCaseEnvironmentContext extends FakeEnvironmentContext {
        public final static EnvironmentValueName<?> SNAKE = EnvironmentValueName.with("snake1-snake2");
    }

    @Disabled
    static final class TestEnvironmentContextTesting<C extends EnvironmentContext> implements EnvironmentContextTesting2<C> {

        TestEnvironmentContextTesting(final Class<C> type) {
            this.type = type;
        }

        @Override
        public C createContext() {
            throw new UnsupportedOperationException();
        }

        @Override
        public String typeNameSuffix() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Class<C> type() {
            return this.type;
        }

        private final Class<C> type;
    }

    ;

    // class............................................................................................................

    @Override
    public Class<EnvironmentContextTesting2> type() {
        return EnvironmentContextTesting2.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
