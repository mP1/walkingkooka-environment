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
import walkingkooka.net.email.EmailAddress;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EnvironmentContextTest implements ClassTesting<EnvironmentContext> {

    // environmentValueOrFail...........................................................................................

    @Test
    public void testEnvironmentValueOrFail() {
        final MissingEnvironmentValueException thrown = assertThrows(
            MissingEnvironmentValueException.class,
            () -> EnvironmentContexts.empty(
                () -> LocalDateTime.MAX,
                EnvironmentContext.ANONYMOUS
            ).environmentValueOrFail(EnvironmentValueName.with("Hello"))
        );

        this.checkEquals(
            "Missing environment value \"Hello\"",
            thrown.getMessage()
        );
    }

    // createdAuditInfo.................................................................................................

    @Test
    public void testCreatedAuditInfo() {
        final EmailAddress email = EmailAddress.parse("test@example.com");
        final LocalDateTime now = LocalDateTime.of(
            1999,
            12,
            31,
            12,
            58,
            59
        );

        this.checkEquals(
            AuditInfo.with(
                email,
                now,
                email,
                now
            ),
            new FakeEnvironmentContext() {
                @Override
                public LocalDateTime now() {
                    return now;
                }

                @Override
                public Optional<EmailAddress> user() {
                    return Optional.of(email);
                }
            }.createdAuditInfo()
        );
    }

    // refreshModifiedAuditInfo.........................................................................................

    @Test
    public void testRefreshModifiedAuditInfo() {
        final EmailAddress createdUser = EmailAddress.parse("created@example.com");
        final LocalDateTime createdTimestamp = LocalDateTime.of(
            1999,
            12,
            31,
            12,
            58,
            59
        );

        final EmailAddress updatedUser = EmailAddress.parse("modified@example.com");
        final LocalDateTime updatedTimestamp = LocalDateTime.of(
            2000,
            1,
            2,
            3,
            4,
            5
        );

        this.checkEquals(
            AuditInfo.with(
                createdUser,
                createdTimestamp,
                updatedUser,
                updatedTimestamp
            ),
            new FakeEnvironmentContext() {
                @Override
                public LocalDateTime now() {
                    return updatedTimestamp;
                }

                @Override
                public Optional<EmailAddress> user() {
                    return Optional.of(updatedUser);
                }
            }.refreshModifiedAuditInfo(
                AuditInfo.with(
                    createdUser,
                    createdTimestamp,
                    createdUser,
                    createdTimestamp
                )
            )
        );
    }

    // class............................................................................................................

    @Override
    public Class<EnvironmentContext> type() {
        return EnvironmentContext.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
