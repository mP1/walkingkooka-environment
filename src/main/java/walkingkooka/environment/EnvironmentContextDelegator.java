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

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Currency;
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
    default Charset charset() {
        return CHARSET.getEnvironmentValueOrFail(this);
    }

    @Override
    default void setCharset(final Charset charset) {
        CHARSET.setEnvironmentValue(
            charset,
            this
        );
    }
    
    @Override
    default Currency currency() {
        return CURRENCY.getEnvironmentValueOrFail(this);
    }

    @Override
    default void setCurrency(final Currency currency) {
        CURRENCY.setEnvironmentValue(
            currency,
            this
        );
    }
    
    @Override
    default Indentation indentation() {
        return INDENTATION.getEnvironmentValueOrFail(this);
    }

    @Override
    default void setIndentation(final Indentation indentation) {
        INDENTATION.setEnvironmentValue(
            indentation,
            this
        );
    }
    
    @Override
    default LineEnding lineEnding() {
        return LINE_ENDING.getEnvironmentValueOrFail(this);
    }

    @Override
    default void setLineEnding(final LineEnding lineEnding) {
        LINE_ENDING.setEnvironmentValue(
            lineEnding,
            this
        );
    }

    @Override
    default Locale locale() {
        return LOCALE.getEnvironmentValueOrFail(this);
    }

    @Override
    default void setLocale(final Locale locale) {
        LOCALE.setEnvironmentValue(
            locale,
            this
        );
    }

    @Override
    default LocalDateTime now() {
        return NOW.getEnvironmentValueOrFail(this);
    }

    @Override
    default ZoneOffset timeOffset() {
        return TIME_OFFSET.getEnvironmentValueOrFail(this);
    }

    @Override
    default void setTimeOffset(final ZoneOffset timeOffset) {
        TIME_OFFSET.setEnvironmentValue(
            timeOffset,
            this
        );
    }

    @Override
    default Optional<EmailAddress> user() {
        return USER.getEnvironmentValue(this);
    }

    @Override
    default void setUser(final Optional<EmailAddress> user) {
        USER.setOrRemoveEnvironmentValue(
            user,
            this
        );
    }

    @Override
    default Runnable addEnvironmentWatcher(final EnvironmentWatcher watcher) {
        return this.environmentContext()
            .addEnvironmentWatcher(watcher);
    }

    @Override
    default Runnable addEnvironmentWatcherOnce(final EnvironmentWatcher watcher) {
        return this.environmentContext()
            .addEnvironmentWatcherOnce(watcher);
    }

    EnvironmentContext environmentContext();
}
