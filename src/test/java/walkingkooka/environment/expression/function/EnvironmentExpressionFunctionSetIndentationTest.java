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
import walkingkooka.text.HasIndentationTesting;
import walkingkooka.text.Indentation;

public final class EnvironmentExpressionFunctionSetIndentationTest extends EnvironmentExpressionFunctionTestCase<EnvironmentExpressionFunctionSetIndentation<EnvironmentExpressionEvaluationContext>, Void>
    implements HasIndentationTesting,
    ToStringTesting {

    private final static Indentation LINE_ENDING = Indentation.SPACES2;

    @Override
    public void testSetParametersSame() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testApplyWithIndentation() {
        final EnvironmentExpressionEvaluationContext context = this.createContext();

        final Indentation indentation = Indentation.SPACES4;
        this.applyAndCheck(
            EnvironmentExpressionFunctionSetIndentation.instance(),
            Lists.of(indentation),
            context,
            null
        );

        this.indentationAndCheck(
            context,
            indentation
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            EnvironmentExpressionFunctionSetIndentation.instance(),
            "setIndentation"
        );
    }

    @Override
    public EnvironmentExpressionFunctionSetIndentation createBiFunction() {
        return EnvironmentExpressionFunctionSetIndentation.instance();
    }

    @Override
    public EnvironmentExpressionEvaluationContext createContext() {
        return new FakeEnvironmentExpressionEvaluationContext() {
            @Override
            public Indentation indentation() {
                return this.indentation;
            }

            @Override
            public void setIndentation(final Indentation indentation) {
                this.indentation = indentation;
            }

            private Indentation indentation = EnvironmentExpressionFunctionSetIndentationTest.LINE_ENDING;
        };
    }

    @Override
    public Class<EnvironmentExpressionFunctionSetIndentation<EnvironmentExpressionEvaluationContext>> type() {
        return Cast.to(EnvironmentExpressionFunctionSetIndentation.class);
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }
}
