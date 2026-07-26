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

import org.junit.jupiter.api.Test;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.set.Sets;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;

import java.util.Set;

public final class EnvironmentContextMissingValuesTest implements EnvironmentContextTesting,
    ToStringTesting<EnvironmentContextMissingValues>,
    ClassTesting2<EnvironmentContextMissingValues> {

    @Test
    public void testMissingWhenOne() {
        final EnvironmentValueName<String> name = EnvironmentValueName.with(
            "Hello",
            String.class
        );

        final EnvironmentContextMissingValues missing = EnvironmentContextMissingValues.with(ENVIRONMENT_CONTEXT);

        this.checkEquals(
            null,
            missing.getOrNull(name)
        );

        this.missingAndCheck(
            missing,
            name
        );
    }

    @Test
    public void testMissingWhenTwo() {
        final EnvironmentValueName<String> name1 = EnvironmentValueName.with(
            "Hello1",
            String.class
        );
        final EnvironmentValueName<String> name2 = EnvironmentValueName.with(
            "Hello2",
            String.class
        );

        final EnvironmentContextMissingValues missing = EnvironmentContextMissingValues.with(ENVIRONMENT_CONTEXT);

        this.checkEquals(
            null,
            missing.getOrNull(name1)
        );

        this.checkEquals(
            null,
            missing.getOrNull(name2)
        );

        this.missingAndCheck(
            missing,
            name1,
            name2
        );
    }

    @Test
    public void testMissingWhenNone() {
        final EnvironmentValueName<String> name = EnvironmentValueName.with(
            "Hello",
            String.class
        );
        final String value = "world";

        final EnvironmentContext environmentContext = ENVIRONMENT_CONTEXT.cloneEnvironment();
        environmentContext.setEnvironmentValue(
            name,
            value
        );

        final EnvironmentContextMissingValues missing = EnvironmentContextMissingValues.with(environmentContext);

        this.checkEquals(
            value,
            missing.getOrNull(name)
        );

        this.missingAndCheck(
            missing
        );
    }

    private void missingAndCheck(final EnvironmentContextMissingValues missing,
                                 final EnvironmentValueName<?>... expected) {
        this.missingAndCheck(
            missing,
            Sets.of(expected)
        );
    }

    private void missingAndCheck(final EnvironmentContextMissingValues missing,
                                 final Set<EnvironmentValueName<?>> expected) {
        this.checkEquals(
            expected,
            missing.missing,
            missing::toString
        );
    }

    // toString.........................................................................................................

    @Test
    public void testToStringWithMissing() {
        final EnvironmentValueName<String> name1 = EnvironmentValueName.with(
            "Hello1",
            String.class
        );
        final EnvironmentValueName<String> name2 = EnvironmentValueName.with(
            "Hello2",
            String.class
        );

        final EnvironmentContextMissingValues missing = EnvironmentContextMissingValues.with(ENVIRONMENT_CONTEXT);

        missing.getOrNull(name1);
        missing.getOrNull(name2);

        this.toStringAndCheck(
            missing,
            "missing=Hello1, Hello2 environmentContext={charset=\"UTF-8\", currency=\"AUD\", indentation=\"  \", lineEnding=\"\\n\", locale=en_AU, timeOffset=Z, user=user123@example.com}"
        );
    }

    @Test
    public void testToStringWithoutMissing() {
        final EnvironmentContextMissingValues missing = EnvironmentContextMissingValues.with(ENVIRONMENT_CONTEXT);

        this.toStringAndCheck(
            missing,
            "environmentContext={charset=\"UTF-8\", currency=\"AUD\", indentation=\"  \", lineEnding=\"\\n\", locale=en_AU, timeOffset=Z, user=user123@example.com}"
        );
    }

    // class............................................................................................................

    @Override
    public Class<EnvironmentContextMissingValues> type() {
        return EnvironmentContextMissingValues.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
