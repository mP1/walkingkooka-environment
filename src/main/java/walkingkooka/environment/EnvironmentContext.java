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

import walkingkooka.Context;
import walkingkooka.datetime.HasNow;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.text.HasIndentation;
import walkingkooka.text.HasLineEnding;
import walkingkooka.text.Indentation;
import walkingkooka.text.LineEnding;
import walkingkooka.util.HasLocale;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * A {@link Context} that includes methods to get/set/remove environment values including the locale and the {@link EmailAddress}
 * of the current user.
 */
public interface EnvironmentContext extends Context,
    HasIndentation,
    HasLineEnding,
    HasLocale,
    HasNow,
    HasUser {

    Optional<EmailAddress> ANONYMOUS = Optional.empty();

    EnvironmentValueName<Indentation> INDENTATION = EnvironmentValueName.INDENTATION;

    EnvironmentValueName<LineEnding> LINE_ENDING = EnvironmentValueName.LINE_ENDING;

    EnvironmentValueName<Locale> LOCALE = EnvironmentValueName.LOCALE;

    EnvironmentValueName<LocalDateTime> NOW = EnvironmentValueName.NOW;

    EnvironmentValueName<EmailAddress> USER = EnvironmentValueName.USER;

    /**
     * Returns a {@link EnvironmentContext} taking a snapshot of environment values. This is useful for environments
     * where a copy of the environment is needed so new updates do not modify the original.
     */
    EnvironmentContext cloneEnvironment();

    /**
     * This method is intended to provide the capability to replace the {@link EnvironmentContext} component for classes,
     * that extend {@link EnvironmentContext}.
     */
    EnvironmentContext setEnvironmentContext(final EnvironmentContext context);

    /**
     * Returns the value for the given {@link EnvironmentValueName}.
     */
    <T> Optional<T> environmentValue(final EnvironmentValueName<T> name);

    /**
     * Returns the value for the given {@link EnvironmentValueName} throwing a {@link IllegalArgumentException} if
     * the value is unknown or missing.
     */
    default <T> T environmentValueOrFail(final EnvironmentValueName<T> name) {
        return this.environmentValue(name)
            .orElseThrow(name::missingEnvironmentValueException);
    }

    /**
     * Returns a read-only view of all {@link EnvironmentValueName names}.
     */
    Set<EnvironmentValueName<?>> environmentValueNames();

    /**
     * Sets or replaces the given environment variable with a new value.
     */
    <T> void setEnvironmentValue(final EnvironmentValueName<T> name,
                                 final T value);

    /**
     * Accepts an {@link Optional} value calling the {@link #setEnvironmentValue(EnvironmentValueName, Object)} if
     * a value is present or {@link #removeEnvironmentValue(EnvironmentValueName)} if one is missing.
     */
    default <T> void setOrRemoveEnvironmentValue(final EnvironmentValueName<T> name,
                                                 final Optional<T> value) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(value, name.value());

        if (value.isPresent()) {
            this.setEnvironmentValue(
                name,
                value.get()
            );
        } else {
            this.removeEnvironmentValue(name);
        }
    }

    /**
     * Removes the value with the given {@link EnvironmentValueName}.
     */
    void removeEnvironmentValue(final EnvironmentValueName<?> name);

    /**
     * Sets or replaces the current {@link Indentation}
     */
    void setIndentation(final Indentation indentation);
    
    /**
     * Sets or replaces the current {@link LineEnding}
     */
    void setLineEnding(final LineEnding lineEnding);

    /**
     * Sets or replaces the current {@link Locale}
     */
    void setLocale(final Locale locale);

    /**
     * Sets or replaces the user.
     */
    void setUser(final Optional<EmailAddress> user);

    /**
     * Returns a new {@link EnvironmentContext} that now prefixes all lookups with the given {@link EnvironmentValueName prefix}.
     */
    default EnvironmentContext setEnvironmentValuePrefix(final EnvironmentValueName<?> prefix) {
        return EnvironmentContexts.prefixed(
            prefix,
            this
        );
    }

    /**
     * Returns an {@link AuditInfo} querying the timestamp and user.
     */
    default AuditInfo createdAuditInfo() {
        return AuditInfo.create(
            this.userOrFail(),
            this.now()
        );
    }

    /**
     * Returns an {@link AuditInfo} updating its modified properties leaving the createXXX properties.
     */
    default AuditInfo refreshModifiedAuditInfo(final AuditInfo auditInfo) {
        Objects.requireNonNull(auditInfo, "auditInfo");

        return auditInfo.setModifiedBy(
            this.userOrFail()
        ).setModifiedTimestamp(this.now());
    }

    /**
     * Adds a new {@link EnvironmentValueWatcher} that will be notified whenever an {@link EnvironmentValueName} is added,
     * modified or removed.
     */
    Runnable addEventValueWatcher(final EnvironmentValueWatcher watcher);

    /**
     * Adds a new {@link EnvironmentValueWatcher} that will be notified whenever an {@link EnvironmentValueName} is added,
     * modified or removed. The watcher will only ever receive one event and will be automatically removed after that.
     */
    Runnable addEventValueWatcherOnce(final EnvironmentValueWatcher watcher);

    /**
     * Gives an empty {@link EnvironmentContextMissingValues}.
     */
    default EnvironmentContextMissingValues environmentContextMissingValues() {
        return EnvironmentContextMissingValues.with(this);
    }
}
