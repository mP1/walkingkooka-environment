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
import walkingkooka.environment.HasTimeOffsetTesting;
import walkingkooka.environment.expression.EnvironmentExpressionEvaluationContext;
import walkingkooka.environment.expression.FakeEnvironmentExpressionEvaluationContext;

import java.time.ZoneOffset;

public final class EnvironmentExpressionFunctionSetTimeOffsetTest extends EnvironmentExpressionFunctionTestCase<EnvironmentExpressionFunctionSetTimeOffset<EnvironmentExpressionEvaluationContext>, Void>
    implements HasTimeOffsetTesting,
    ToStringTesting {

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
    public void testApplyWithZoneOffset() {
        final EnvironmentExpressionEvaluationContext context = this.createContext();

        final ZoneOffset timeOffset = ZoneOffset.ofHours(2);

        this.applyAndCheck(
            EnvironmentExpressionFunctionSetTimeOffset.instance(),
            Lists.of(timeOffset),
            context,
            null
        );

        this.timeOffsetAndCheck(
            context,
            timeOffset
        );
    }

    @Test
    public void testToString() {
        this.toStringAndCheck(
            EnvironmentExpressionFunctionSetTimeOffset.instance(),
            "setTimeOffset"
        );
    }

    @Override
    public EnvironmentExpressionFunctionSetTimeOffset<EnvironmentExpressionEvaluationContext> createBiFunction() {
        return EnvironmentExpressionFunctionSetTimeOffset.instance();
    }

    @Override
    public EnvironmentExpressionEvaluationContext createContext() {
        return new FakeEnvironmentExpressionEvaluationContext() {
            @Override
            public ZoneOffset timeOffset() {
                return this.timeOffset;
            }

            @Override
            public void setTimeOffset(final ZoneOffset timeOffset) {
                this.timeOffset = timeOffset;
            }

            private ZoneOffset timeOffset = EnvironmentExpressionFunctionSetTimeOffsetTest.TIME_OFFSET;
        };
    }

    @Override
    public Class<EnvironmentExpressionFunctionSetTimeOffset<EnvironmentExpressionEvaluationContext>> type() {
        return Cast.to(EnvironmentExpressionFunctionSetTimeOffset.class);
    }

    @Override
    public int minimumParameterCount() {
        return 1;
    }
}
