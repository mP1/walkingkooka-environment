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

import walkingkooka.datetime.HasNow;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.props.Properties;
import walkingkooka.reflect.PublicStaticHelper;

import java.util.List;
import java.util.Optional;

/**
 * A collection of {@link EnvironmentContext} factory methods.
 */
public final class EnvironmentContexts implements PublicStaticHelper {

    /**
     * {@see CollectionEnvironmentContext}
     */
    public static EnvironmentContext collection(final List<EnvironmentContext> environmentContexts,
                                                final HasNow hasNow,
                                                final Optional<EmailAddress> user) {
        return CollectionEnvironmentContext.with(
                environmentContexts,
                hasNow,
                user
        );
    }

    /**
     * {@see EmptyEnvironmentContext}
     */
    public static EnvironmentContext empty(final HasNow hasNow,
                                           final Optional<EmailAddress> user) {
        return EmptyEnvironmentContext.with(
                hasNow,
                user
        );
    }

    /**
     * {@see FakeEnvironmentContext}
     */
    public static EnvironmentContext fake() {
        return new FakeEnvironmentContext();
    }

    /**
     * {@see PropertiesEnvironmentContext}
     */
    public static EnvironmentContext properties(final Properties properties,
                                                final HasNow hasNow,
                                                final Optional<EmailAddress> user) {
        return PropertiesEnvironmentContext.with(
                properties,
                hasNow,
                user
        );
    }

    /**
     * Stop creation
     */
    private EnvironmentContexts() {
        throw new UnsupportedOperationException();
    }
}
