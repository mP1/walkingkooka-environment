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
import walkingkooka.reflect.PublicClassTesting;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EnvironmentContextAwareTest implements PublicClassTesting<EnvironmentContextAware>,
    EnvironmentContextTesting {

    @Test
    public void testTrySetEnvironmentContextAwareWithNullMaybeFails() {
        assertThrows(
            NullPointerException.class,
            () -> EnvironmentContextAware.trySetEnvironmentContextAware(
                null,
                ENVIRONMENT_CONTEXT
            )
        );
    }

    @Test
    public void testTrySetEnvironmentContextAwareWithNullEnvironmentContextFails() {
        assertThrows(
            NullPointerException.class,
            () -> EnvironmentContextAware.trySetEnvironmentContextAware(
                new Object(),
                null
            )
        );
    }

    @Test
    public void testTrySetEnvironmentContextAware() {
        this.environmentContext = null;

        EnvironmentContextAware.trySetEnvironmentContextAware(
            new EnvironmentContextAware() {
                @Override
                public void setEnvironmentContext(final EnvironmentContext context) {
                    EnvironmentContextAwareTest.this.environmentContext = context;
                }
            },
            ENVIRONMENT_CONTEXT
        );

        this.checkEquals(
            ENVIRONMENT_CONTEXT,
            this.environmentContext
        );
    }

    private EnvironmentContext environmentContext;

    // class............................................................................................................

    @Override
    public Class<EnvironmentContextAware> type() {
        return EnvironmentContextAware.class;
    }
}
