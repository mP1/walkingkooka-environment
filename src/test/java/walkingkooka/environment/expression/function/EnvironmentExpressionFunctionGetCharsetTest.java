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

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class EnvironmentExpressionFunctionGetCharsetTest extends EnvironmentExpressionFunctionTestCase<EnvironmentExpressionFunctionGetCharset<EnvironmentExpressionEvaluationContext>, Charset>
    implements ToStringTesting {

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
    public void testApplyWithout() {
        this.applyAndCheck(
            Lists.empty(),
            CHARSET
        );
    }

    @Test
    public void testApplyWithCharset() {
        final Charset charset = StandardCharsets.ISO_8859_1;

        this.applyAndCheck(
            Lists.of(charset),
            charset
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            EnvironmentExpressionFunctionGetCharset.instance(),
            "getCharset"
        );
    }

    @Override
    public EnvironmentExpressionFunctionGetCharset createBiFunction() {
        return EnvironmentExpressionFunctionGetCharset.instance();
    }

    @Override
    public EnvironmentExpressionEvaluationContext createContext() {
        return new FakeEnvironmentExpressionEvaluationContext() {
            @Override
            public Charset charset() {
                return EnvironmentExpressionFunctionGetCharsetTest.CHARSET;
            }
        };
    }

    @Override
    public Class<EnvironmentExpressionFunctionGetCharset<EnvironmentExpressionEvaluationContext>> type() {
        return Cast.to(EnvironmentExpressionFunctionGetCharset.class);
    }

    @Override
    public int minimumParameterCount() {
        return 0;
    }
}
