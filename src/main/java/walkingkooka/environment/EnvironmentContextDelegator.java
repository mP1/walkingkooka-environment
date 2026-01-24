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
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public interface EnvironmentContextDelegator extends EnvironmentContext {

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
    default <T> void setEnvironmentValue(final EnvironmentValueName<T> name,
                                         final T value) {
        this.environmentContext()
            .setEnvironmentValue(
                name,
                value
            );
    }

    @Override
    default void removeEnvironmentValue(final EnvironmentValueName<?> name) {
        Objects.requireNonNull(name, "name");

        this.environmentContext()
            .removeEnvironmentValue(name);
    }

    @Override
    default Indentation indentation() {
        return this.environmentValueOrFail(INDENTATION);
    }

    @Override
    default void setIndentation(final Indentation indentation) {
        this.setEnvironmentValue(
            INDENTATION,
            indentation
        );
    }
    
    @Override
    default LineEnding lineEnding() {
        return this.environmentValueOrFail(LINE_ENDING);
    }

    @Override
    default void setLineEnding(final LineEnding lineEnding) {
        this.setEnvironmentValue(
            LINE_ENDING,
            lineEnding
        );
    }

    @Override
    default Locale locale() {
        return this.environmentValueOrFail(LOCALE);
    }

    @Override
    default void setLocale(final Locale locale) {
        this.setEnvironmentValue(
            LOCALE,
            locale
        );
    }

    @Override
    default LocalDateTime now() {
        return this.environmentValueOrFail(NOW);
    }

    @Override
    default Optional<EmailAddress> user() {
        return this.environmentValue(USER);
    }

    @Override
    default void setUser(final Optional<EmailAddress> user) {
        Objects.requireNonNull(user, "user");

        if (user.isPresent()) {
            this.setEnvironmentValue(
                USER,
                user.get()
            );
        } else {
            this.removeEnvironmentValue(USER);
        }
    }

    @Override
    default Runnable addEventValueWatcher(final EnvironmentValueWatcher watcher) {
        return this.environmentContext()
            .addEventValueWatcher(watcher);
    }

    @Override
    default Runnable addEventValueWatcherOnce(final EnvironmentValueWatcher watcher) {
        return this.environmentContext()
            .addEventValueWatcherOnce(watcher);
    }

    EnvironmentContext environmentContext();
}
