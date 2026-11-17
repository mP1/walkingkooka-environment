/*
 * Copyright 2025 Miroslav Pokorny (github.com/mP1)
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

package walkingkooka.environment.expression;

import org.junit.jupiter.api.Test;
import walkingkooka.environment.EnvironmentContext;
import walkingkooka.reflect.PublicClassTesting;
import walkingkooka.tree.expression.ExpressionEvaluationContext;
import walkingkooka.tree.expression.ExpressionReference;

import java.util.Optional;
import java.util.function.Function;

public final class EnvironmentExpressionEvaluationContextDelegatorTest implements PublicClassTesting<EnvironmentExpressionEvaluationContextDelegator> {

    @Test
    public void testTestEnvironmentExpressionEvaluationContextDelegator() {
        new TestEnvironmentExpressionEvaluationContextDelegator();
    }

    final static class TestEnvironmentExpressionEvaluationContextDelegator implements EnvironmentExpressionEvaluationContextDelegator {

        @Override
        public EnvironmentExpressionEvaluationContext environmentExpressionEvaluationContext() {
            throw new UnsupportedOperationException();
        }

        @Override
        public EnvironmentContext cloneEnvironment() {
            throw new UnsupportedOperationException();
        }

        @Override
        public EnvironmentContext setEnvironmentContext(final EnvironmentContext environmentContext) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ExpressionEvaluationContext enterScope(final Function<ExpressionReference, Optional<Optional<Object>>> function) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Optional<Optional<Object>> reference(final ExpressionReference reference) {
            return Optional.empty();
        }
    }

    @Override
    public Class<EnvironmentExpressionEvaluationContextDelegator> type() {
        return EnvironmentExpressionEvaluationContextDelegator.class;
    }
}
