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

package walkingkooka.environment.expression.function;

import org.junit.jupiter.api.Test;
import walkingkooka.Cast;
import walkingkooka.collect.list.Lists;
import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.environment.expression.EnvironmentExpressionEvaluationContext;
import walkingkooka.environment.expression.FakeEnvironmentExpressionEvaluationContext;

import java.util.Optional;

public final class EnvironmentExpressionFunctionGetEnvTest extends EnvironmentExpressionFunctionTestCase<EnvironmentExpressionFunctionGetEnv<EnvironmentExpressionEvaluationContext>, Object> {

    private final static EnvironmentValueName<?> VAR = EnvironmentValueName.with("var123");

    @Test
    public void testApply() {
        this.applyAndCheck(
            Lists.of(VAR),
            VAR.value() + VAR.value()
        );
    }

    @Override
    public EnvironmentExpressionFunctionGetEnv<EnvironmentExpressionEvaluationContext> createBiFunction() {
        return EnvironmentExpressionFunctionGetEnv.instance();
    }

    @Override
    public int minimumParameterCount() {
        return 0;
    }

    @Override
    public EnvironmentExpressionEvaluationContext createContext() {
        return new FakeEnvironmentExpressionEvaluationContext() {

            @Override
            public <T> Optional<T> environmentValue(final EnvironmentValueName<T> n) {
                return Optional.of(
                    Cast.to(
                        n.value() + n.value()
                    )
                );
            }
        };
    }

    // class............................................................................................................

    @Override
    public Class<EnvironmentExpressionFunctionGetEnv<EnvironmentExpressionEvaluationContext>> type() {
        return Cast.to(EnvironmentExpressionFunctionGetEnv.class);
    }
}
