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

import java.time.ZoneOffset;

public final class EnvironmentExpressionFunctionGetTimeOffsetTest extends EnvironmentExpressionFunctionTestCase<EnvironmentExpressionFunctionGetTimeOffset<EnvironmentExpressionEvaluationContext>, ZoneOffset>
    implements ToStringTesting {

    private final static ZoneOffset TIME_OFFSET = ZoneOffset.ofHours(1);

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
            TIME_OFFSET
        );
    }

    @Test
    public void testApplyWithTimeOffset() {
        final ZoneOffset timeOffset = ZoneOffset.ofHours(2);

        this.applyAndCheck(
            Lists.of(timeOffset),
            timeOffset
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            EnvironmentExpressionFunctionGetTimeOffset.instance(),
            "getTimeOffset"
        );
    }

    @Override
    public EnvironmentExpressionFunctionGetTimeOffset createBiFunction() {
        return EnvironmentExpressionFunctionGetTimeOffset.instance();
    }

    @Override
    public EnvironmentExpressionEvaluationContext createContext() {
        return new FakeEnvironmentExpressionEvaluationContext() {
            @Override
            public ZoneOffset timeOffset() {
                return EnvironmentExpressionFunctionGetTimeOffsetTest.TIME_OFFSET;
            }
        };
    }

    @Override
    public Class<EnvironmentExpressionFunctionGetTimeOffset<EnvironmentExpressionEvaluationContext>> type() {
        return Cast.to(EnvironmentExpressionFunctionGetTimeOffset.class);
    }

    @Override
    public int minimumParameterCount() {
        return 0;
    }
}
