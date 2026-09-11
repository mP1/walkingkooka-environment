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
import walkingkooka.HasCharsetTesting;
import walkingkooka.currency.CurrencyLocaleContextTesting;
import walkingkooka.datetime.DateTimeContextTesting;
import walkingkooka.logging.LoggingContextTesting;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.ThrowableTesting;
import walkingkooka.text.BinaryTextContextTesting;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class EnvironmentContextTest implements ClassTesting<EnvironmentContext>,
    BinaryTextContextTesting,
    CurrencyLocaleContextTesting,
    DateTimeContextTesting,
    HasCharsetTesting,
    LoggingContextTesting,
    ThrowableTesting {

    // environmentValueOrFail...........................................................................................

    @Test
    public void testEnvironmentValueOrFail() {
        final MissingEnvironmentValueException thrown = assertThrows(
            MissingEnvironmentValueException.class,
            () -> EnvironmentContexts.map(
                CAN_LOG,
                CHARSET,
                CURRENCY,
                INDENTATION,
                LINE_ENDING,
                LOCALE,
                LOGGING_LEVEL,
                HAS_NOW,
                EnvironmentContext.ANONYMOUS
            ).environmentValueOrFail(EnvironmentValueName.with(
                    "Hello",
                    String.class
                )
            )
        );

        this.getMessageAndCheck(
            thrown,
            "Missing environment value \"Hello\""
        );
    }

    // createdAuditInfo.................................................................................................

    @Test
    public void testCreatedAuditInfo() {
        final EmailAddress email = EmailAddress.parse("test@example.com");

        this.checkEquals(
            AuditInfo.with(
                email,
                DIFFERENT_NOW,
                email,
                DIFFERENT_NOW
            ),
            new FakeEnvironmentContext() {
                @Override
                public LocalDateTime now() {
                    return DIFFERENT_NOW;
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

        final EmailAddress updatedUser = EmailAddress.parse("modified@example.com");

        this.checkEquals(
            AuditInfo.with(
                createdUser,
                NOW,
                updatedUser,
                DIFFERENT_NOW
            ),
            new FakeEnvironmentContext() {
                @Override
                public LocalDateTime now() {
                    return DIFFERENT_NOW;
                }

                @Override
                public Optional<EmailAddress> user() {
                    return Optional.of(updatedUser);
                }
            }.refreshModifiedAuditInfo(
                AuditInfo.with(
                    createdUser,
                    NOW,
                    createdUser,
                    NOW
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
