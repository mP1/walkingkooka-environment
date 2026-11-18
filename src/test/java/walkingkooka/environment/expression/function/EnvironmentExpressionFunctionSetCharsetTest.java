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
import walkingkooka.HasCharsetTesting;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.list.Lists;
import walkingkooka.environment.expression.EnvironmentExpressionEvaluationContext;
import walkingkooka.environment.expression.FakeEnvironmentExpressionEvaluationContext;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class EnvironmentExpressionFunctionSetCharsetTest extends EnvironmentExpressionFunctionTestCase<EnvironmentExpressionFunctionSetCharset<EnvironmentExpressionEvaluationContext>, Void>
    implements HasCharsetTesting,
    ToStringTesting {

    private final static Charset CHARSET = StandardCharsets.UTF_8;

    @Override
    public void testSetParametersSame() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void testTypeNaming() {
        throw new UnsupportedOperationException();
    }

    @Test
    public void testApplyWithCharset() {
        final EnvironmentExpressionEvaluationContext context = this.createContext();

        final Charset charset = StandardCharsets.ISO_8859_1;
        this.applyAndCheck(
            EnvironmentExpressionFunctionSetCharset.instance(),
            Lists.of(charset),
            context,
            null
        );

        this.charsetAndCheck(
            context,
            charset
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            EnvironmentExpressionFunctionSetCharset.instance(),
            "setCharset"
        );
    }

    @Override
    public EnvironmentExpressionFunctionSetCharset<EnvironmentExpressionEvaluationContext> createBiFunction() {
        return EnvironmentExpressionFunctionSetCharset.instance();
    }

    @Override
    public EnvironmentExpressionEvaluationContext createContext() {
        return new FakeEnvironmentExpressionEvaluationContext() {
            @Override
            public Charset charset() {
                return this.charset;
            }

            @Override
            public void setCharset(final Charset charset) {
                this.charset = charset;
            }

            private Charset charset = EnvironmentExpressionFunctionSetCharsetTest.CHARSET;
        };
    }

    @Override
    public Class<EnvironmentExpressionFunctionSetCharset<EnvironmentExpressionEvaluationContext>> type() {
        return Cast.to(EnvironmentExpressionFunctionSetCharset.class);
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }
}
