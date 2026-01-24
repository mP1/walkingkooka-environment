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
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * A collection of {@link EnvironmentContext} factory methods.
 */
public final class EnvironmentContexts implements PublicStaticHelper {

    /**
     * {@see CollectionEnvironmentContext}
     */
    public static EnvironmentContext collection(final List<EnvironmentContext> contexts) {
        return CollectionEnvironmentContext.with(
            contexts
        );
    }

    /**
     * {@see EmptyEnvironmentContext}
     */
    public static EnvironmentContext empty(final Indentation indentation,
                                           final LineEnding lineEnding,
                                           final Locale locale,
                                           final HasNow hasNow,
                                           final Optional<EmailAddress> user) {
        return EmptyEnvironmentContext.with(
            indentation,
            lineEnding,
            locale,
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
     * {@see MapEnvironmentContext}
     */
    public static EnvironmentContext map(final EnvironmentContext context) {
        return MapEnvironmentContext.with(context);
    }

    /**
     * {@see PrefixedEnvironmentContext}
     */
    public static EnvironmentContext prefixed(final EnvironmentValueName<?> prefix,
                                              final EnvironmentContext context) {
        return PrefixedEnvironmentContext.with(
            prefix,
            context
        );
    }

    /**
     * {@see PropertiesEnvironmentContext}
     */
    public static EnvironmentContext properties(final Properties properties,
                                                final EnvironmentContext context) {
        return PropertiesEnvironmentContext.with(
            properties,
            context
        );
    }

    /**
     * {@see ReadOnlyEnvironmentContext}
     */
    public static EnvironmentContext readOnly(final Predicate<EnvironmentValueName<?>> readOnlyNames,
                                              final EnvironmentContext context) {
        return ReadOnlyEnvironmentContext.with(
            readOnlyNames,
            context
        );
    }

    /**
     * Stop creation
     */
    private EnvironmentContexts() {
        throw new UnsupportedOperationException();
    }
}
