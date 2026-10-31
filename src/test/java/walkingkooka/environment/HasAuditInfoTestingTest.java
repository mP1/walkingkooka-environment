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

public final class HasAuditInfoTestingTest implements HasAuditInfoTesting,
    PublicClassTesting<HasAuditInfoTesting> {

    @Test
    public void testConstantsDifferent() {
        this.checkNotEquals(
            AUDIT_INFO,
            DIFFERENT_AUDIT_INFO
        );
    }

    @Test
    public void testConstantsDifferentCreatedBy() {
        this.checkNotEquals(
            AUDIT_INFO.createdBy(),
            DIFFERENT_AUDIT_INFO.createdBy()
        );
    }

    @Test
    public void testConstantsDifferentCreateTimestamp() {
        this.checkNotEquals(
            AUDIT_INFO.createdTimestamp(),
            DIFFERENT_AUDIT_INFO.createdTimestamp()
        );
    }

    @Test
    public void testConstantsDifferentModifiedBy() {
        this.checkNotEquals(
            AUDIT_INFO.modifiedBy(),
            DIFFERENT_AUDIT_INFO.modifiedBy()
        );
    }

    @Test
    public void testConstantsDifferentModifiedTimestamp() {
        this.checkNotEquals(
            AUDIT_INFO.modifiedTimestamp(),
            DIFFERENT_AUDIT_INFO.modifiedTimestamp()
        );
    }

    @Test
    public void testAuditInfoAndCheck() {
        this.auditInfoAndCheck(
            HAS_AUDIT_INFO,
            AUDIT_INFO
        );
    }

    // class............................................................................................................

    @Override
    public Class<HasAuditInfoTesting> type() {
        return HasAuditInfoTesting.class;
    }
}
