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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.ToStringTesting;
import walkingkooka.collect.set.Sets;
import walkingkooka.reflect.PublicClassTesting;
import walkingkooka.reflect.ThrowableTesting;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EnvironmentContextMissingValuesTest implements EnvironmentContextTesting,
    HashCodeEqualsDefinedTesting2<EnvironmentContextMissingValues>,
    ThrowableTesting,
    ToStringTesting<EnvironmentContextMissingValues>,
    PublicClassTesting<EnvironmentContextMissingValues> {

    @Test
    public void testMissingWhenOne() {
        final EnvironmentValueName<String> name = EnvironmentValueName.with(
            "Hello",
            String.class
        );

        final EnvironmentContextMissingValues missing = EnvironmentContextMissingValues.with(ENVIRONMENT_CONTEXT);

        this.getOrNullAndCheck(
            missing,
            name,
            null
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

        this.getOrNullAndCheck(
            missing,
            name1,
            null
        );

        this.getOrNullAndCheck(
            missing,
            name2,
            null
        );

        this.missingAndCheck(
            missing,
            name1,
            name2
        );
    }

    private final static EnvironmentValueName<String> NAME = EnvironmentValueName.with(
        "Hello",
        String.class
    );
    private final static String VALUE = "world";

    @Test
    public void testMissingWhenNone() {
        final EnvironmentContext environmentContext = ENVIRONMENT_CONTEXT.cloneEnvironment();
        environmentContext.setEnvironmentValue(
            NAME,
            VALUE
        );

        final EnvironmentContextMissingValues missing = EnvironmentContextMissingValues.with(environmentContext);

        this.getOrNullAndCheck(
            missing,
            NAME,
            VALUE
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

    @Test
    public void testReportIfMissingWhenNone() {
        final EnvironmentContext environmentContext = ENVIRONMENT_CONTEXT.cloneEnvironment();
        final EnvironmentContextMissingValues missing = EnvironmentContextMissingValues.with(environmentContext);
        missing.reportIfMissing();
    }

    @Test
    public void testReportIfMissingWhenAllFound() {
        final EnvironmentContext environmentContext = ENVIRONMENT_CONTEXT.cloneEnvironment();
        environmentContext.setEnvironmentValue(
            NAME,
            VALUE
        );

        final EnvironmentContextMissingValues missing = EnvironmentContextMissingValues.with(environmentContext);
        this.getOrNullAndCheck(
            missing,
            NAME,
            VALUE
        );
        missing.reportIfMissing();
    }

    @Test
    public void testReportIfMissingWhenMissing() {
        final EnvironmentContext environmentContext = ENVIRONMENT_CONTEXT.cloneEnvironment();
        environmentContext.setEnvironmentValue(
            NAME,
            VALUE
        );

        final EnvironmentContextMissingValues missing = EnvironmentContextMissingValues.with(environmentContext);
        this.getOrNullAndCheck(
            missing,
            EnvironmentValueName.with(
                "missing123",
                String.class
            ),
            null
        );

        final MissingEnvironmentValuesException thrown = assertThrows(
            MissingEnvironmentValuesException.class,
            () -> missing.reportIfMissing()
        );

        this.getMessageAndCheck(
            thrown,
            "Missing environment value(s): missing123"
        );
    }

    private <T> void getOrNullAndCheck(final EnvironmentContextMissingValues missing,
                                       final EnvironmentValueName<T> name,
                                       final T expected) {
        this.checkEquals(
            expected,
            missing.getOrNull(name),
            () -> missing + " " + name
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
            "missing=Hello1, Hello2 environmentContext={charset=UTF-8, currency=AUD, indentation=\"  \", lineEnding=\"\\n\", locale=en_AU, loggingLevel=NONE, timeOffset=Z, user=user123@example.com}"
        );
    }

    @Test
    public void testToStringWithoutMissing() {
        final EnvironmentContextMissingValues missing = EnvironmentContextMissingValues.with(ENVIRONMENT_CONTEXT);

        this.toStringAndCheck(
            missing,
            "environmentContext={charset=UTF-8, currency=AUD, indentation=\"  \", lineEnding=\"\\n\", locale=en_AU, loggingLevel=NONE, timeOffset=Z, user=user123@example.com}"
        );
    }

    // hashCode/Equals..................................................................................................

    @Test
    public void testEqualsDifferentMissingValues() {
        final EnvironmentContextMissingValues environmentContextMissingValues = EnvironmentContextMissingValues.with(DIFFERENT_ENVIRONMENT_CONTEXT);
        environmentContextMissingValues.getOrNull(EnvironmentValueName.with("hello", String.class));
        this.checkNotEquals(
            environmentContextMissingValues
        );
    }

    @Test
    public void testEqualsDifferentEnvironmentContext() {
        this.checkNotEquals(
            EnvironmentContextMissingValues.with(DIFFERENT_ENVIRONMENT_CONTEXT)
        );
    }

    @Override
    public EnvironmentContextMissingValues createObject() {
        return EnvironmentContextMissingValues.with(ENVIRONMENT_CONTEXT);
    }

    // class............................................................................................................

    @Override
    public Class<EnvironmentContextMissingValues> type() {
        return EnvironmentContextMissingValues.class;
    }
}
