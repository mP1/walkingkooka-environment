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
import walkingkooka.Either;
import walkingkooka.collect.list.Lists;
import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.environment.expression.EnvironmentExpressionEvaluationContext;
import walkingkooka.environment.expression.FakeEnvironmentExpressionEvaluationContext;

import java.util.Optional;

public final class EnvironmentExpressionFunctionSetEnvTest extends EnvironmentExpressionFunctionTestCase<EnvironmentExpressionFunctionSetEnv<EnvironmentExpressionEvaluationContext>, Object> {

    private final static EnvironmentValueName<?> NAME = EnvironmentValueName.with(
        "var123",
        Integer.class
    );

    private final static Integer VALUE = 123;

    @Test
    public void testApply() {
        this.applyAndCheck(
            Lists.of(
                NAME,
                String.valueOf(VALUE)
            ),
            VALUE
        );

        this.checkEquals(
            VALUE,
            this.setValue
        );
    }

    @Override
    public EnvironmentExpressionFunctionSetEnv<EnvironmentExpressionEvaluationContext> createBiFunction() {
        return EnvironmentExpressionFunctionSetEnv.instance();
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
                checkEquals(
                    NAME,
                    n,
                    "name"
                );

                return Optional.ofNullable(
                    NAME.equals(n) ?
                        Cast.to(
                            VALUE
                        ) :
                        null
                );
            }

            @Override
            public <T> void setEnvironmentValue(final EnvironmentValueName<T> name,
                                                final T value) {
                checkEquals(NAME, name);
                checkEquals(VALUE, value);

                setValue = value;
            }

            @Override
            public <T> Either<T, String> convert(final Object value,
                                                 final Class<T> target) {
                return this.successfulConversion(
                    Integer.parseInt(
                        value.toString()
                    ),
                    target
                );
            }
        };
    }

    private Object setValue;

    // class............................................................................................................

    @Override
    public Class<EnvironmentExpressionFunctionSetEnv<EnvironmentExpressionEvaluationContext>> type() {
        return Cast.to(EnvironmentExpressionFunctionSetEnv.class);
    }
}
