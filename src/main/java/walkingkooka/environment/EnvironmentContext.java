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
import walkingkooka.text.HasLineEnding;
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
    HasLineEnding,
    HasLocale,
    HasNow,
    HasUser {

    Optional<EmailAddress> ANONYMOUS = Optional.empty();

    EnvironmentValueName<LineEnding> LINE_ENDING = EnvironmentValueName.LINE_ENDING;

    EnvironmentValueName<Locale> LOCALE = EnvironmentValueName.LOCALE;

    EnvironmentValueName<EmailAddress> USER = EnvironmentValueName.USER;

    /**
     * Sets or replaces the current {@link LineEnding}
     */
    EnvironmentContext setLineEnding(final LineEnding lineEnding);

    /**
     * Sets or replaces the current {@link Locale}
     */
    EnvironmentContext setLocale(final Locale locale);

    /**
     * Returns a {@link EnvironmentContext} taking a snapshot of environment values. This is useful for environments
     * where a copy of the environment is needed so new updates do not modify the original.
     */
    EnvironmentContext cloneEnvironment();

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
            .orElseThrow(() -> new MissingEnvironmentValueException(name));
    }

    /**
     * Returns a read-only view of all {@link EnvironmentValueName names}.
     */
    Set<EnvironmentValueName<?>> environmentValueNames();

    /**
     * Sets or replaces the given environment variable with a new value.
     */
    <T> EnvironmentContext setEnvironmentValue(final EnvironmentValueName<T> name,
                                               final T value);

    /**
     * Removes the value with the given {@link EnvironmentValueName}.
     */
    EnvironmentContext removeEnvironmentValue(final EnvironmentValueName<?> name);

    /**
     * Sets or replaces the user.
     */
    EnvironmentContext setUser(final Optional<EmailAddress> user);

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
        final EmailAddress user = this.userOrFail();
        final LocalDateTime timestamp = this.now();

        return AuditInfo.with(
            user,
            timestamp,
            user,
            timestamp
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
}
