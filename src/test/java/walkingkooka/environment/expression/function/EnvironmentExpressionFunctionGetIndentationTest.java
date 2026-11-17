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

package walkingkooka.environment.expression.function;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.environment.expression.EnvironmentExpressionEvaluationContext;
import walkingkooka.environment.expression.FakeEnvironmentExpressionEvaluationContext;
import walkingkooka.text.Indentation;

public final class EnvironmentExpressionFunctionGetIndentationTest extends EnvironmentExpressionFunctionTestCase<EnvironmentExpressionFunctionGetIndentation<EnvironmentExpressionEvaluationContext>, Indentation>
    implements ToStringTesting {

    private final static Indentation INDENTATION = Indentation.SPACES4;

    @Override
    public void testSetParametersSame() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testApplyWithout() {
        this.applyAndCheck(
            Lists.empty(),
            INDENTATION
        );
    }

    @Test
    public void testApplyWithIndentation() {
        final Indentation indentation = Indentation.SPACES2;

        this.applyAndCheck(
            Lists.of(indentation),
            indentation
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            EnvironmentExpressionFunctionGetIndentation.instance(),
            "getIndentation"
        );
    }

    @Override
    public EnvironmentExpressionFunctionGetIndentation createBiFunction() {
        return EnvironmentExpressionFunctionGetIndentation.instance();
    }

    @Override
    public EnvironmentExpressionEvaluationContext createContext() {
        return new FakeEnvironmentExpressionEvaluationContext() {
            @Override
            public Indentation indentation() {
                return EnvironmentExpressionFunctionGetIndentationTest.INDENTATION;
            }
        };
    }

    @Override
    public Class<EnvironmentExpressionFunctionGetIndentation<EnvironmentExpressionEvaluationContext>> type() {
        return Cast.to(EnvironmentExpressionFunctionGetIndentation.class);
    }

    @Override
    public int minimumParameterCount() {
        return 0;
    }
}
