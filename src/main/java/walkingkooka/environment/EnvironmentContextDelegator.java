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

import walkingkooka.net.email.EmailAddress;
import walkingkooka.text.LineEnding;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public interface EnvironmentContextDelegator extends EnvironmentContext {

    @Override
    default LineEnding lineEnding() {
        return this.environmentContext()
            .lineEnding();
    }

    @Override
    default EnvironmentContext setLineEnding(final LineEnding lineEnding) {
        this.environmentContext()
            .setLineEnding(lineEnding);
        return this;
    }
    
    @Override
    default Locale locale() {
        return this.environmentContext()
            .locale();
    }

    @Override
    default <T> Optional<T> environmentValue(final EnvironmentValueName<T> name) {
        return this.environmentContext()
            .environmentValue(name);
    }

    @Override
    default Set<EnvironmentValueName<?>> environmentValueNames() {
        return this.environmentContext().environmentValueNames();
    }

    @Override
    default <T> EnvironmentContext setEnvironmentValue(final EnvironmentValueName<T> name,
                                                       final T value) {
        this.environmentContext()
            .setEnvironmentValue(
                name,
                value
            );
        return this;
    }

    @Override
    default EnvironmentContext removeEnvironmentValue(final EnvironmentValueName<?> name) {
        Objects.requireNonNull(name, "name");

        this.environmentContext()
            .removeEnvironmentValue(name);
        return this;
    }

    @Override
    default LocalDateTime now() {
        return this.environmentContext()
            .now();
    }

    @Override
    default Optional<EmailAddress> user() {
        return this.environmentContext()
            .user();
    }

    EnvironmentContext environmentContext();
}
