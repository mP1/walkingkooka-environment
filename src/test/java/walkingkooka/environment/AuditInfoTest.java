/*
 * Copyright 2025 Miroslav Pokorny (github.com/mP1)
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
import walkingkooka.HashCodeEqualsDefinedTesting2;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.reflect.ClassTesting2;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.text.printer.TreePrintableTesting;
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.marshall.JsonNodeMarshallingTesting;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public final class AuditInfoTest implements HashCodeEqualsDefinedTesting2<AuditInfo>,
    JsonNodeMarshallingTesting<AuditInfo>,
    ClassTesting2<AuditInfo>,
    TreePrintableTesting {

    private final static EmailAddress CREATED_BY = EmailAddress.parse("created-by@example.com");

    private final static LocalDateTime CREATED_TIMESTAMP = LocalDateTime.parse("1999-12-31T12:58:59");

    private final static EmailAddress MODIFIED_BY = EmailAddress.parse("modified-by@example.com");

    private final static LocalDateTime MODIFIED_TIMESTAMP = LocalDateTime.parse("2000-01-02T12:58:59");

    // with.............................................................................................................

    @Test
    public void testWithNullCreatedByFails() {
        assertThrows(
            NullPointerException.class,
            () -> AuditInfo.with(
                null,
                CREATED_TIMESTAMP,
                MODIFIED_BY,
                MODIFIED_TIMESTAMP
            )
        );
    }

    @Test
    public void testWithNullCreatedTimestampFails() {
        assertThrows(
            NullPointerException.class,
            () -> AuditInfo.with(
                CREATED_BY,
                null,
                MODIFIED_BY,
                MODIFIED_TIMESTAMP
            )
        );
    }

    @Test
    public void testWithNullModifiedByFails() {
        assertThrows(
            NullPointerException.class,
            () -> AuditInfo.with(
                CREATED_BY,
                CREATED_TIMESTAMP,
                null,
                MODIFIED_TIMESTAMP
            )
        );
    }

    @Test
    public void testWithNullModifiedTimestampFails() {
        assertThrows(
            NullPointerException.class,
            () -> AuditInfo.with(
                CREATED_BY,
                CREATED_TIMESTAMP,
                MODIFIED_BY,
                null
            )
        );
    }

    @Test
    public void testWithModifiedTimestampBeforeCreatedByFails() {
        final IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> AuditInfo.with(
                CREATED_BY,
                CREATED_TIMESTAMP,
                MODIFIED_BY,
                CREATED_TIMESTAMP.minusDays(1)
            )
        );

        this.checkEquals(
            "ModifiedTimestamp 1999-12-30T12:58:59 < createdTimestamp 1999-12-31T12:58:59",
            thrown.getMessage(),
            "message"
        );
    }

    @Test
    public void testWith() {
        final AuditInfo info = AuditInfo.with(
            CREATED_BY,
            CREATED_TIMESTAMP,
            MODIFIED_BY,
            MODIFIED_TIMESTAMP
        );

        this.createdByAndCheck(info);
        this.createdTimestampAndCheck(info);
        this.modifiedByAndCheck(info);
        this.modifiedTimestampAndCheck(info);
    }

    // setCreatedBy.....................................................................................................

    @Test
    public void testSetCreatedByWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createObject()
                .setCreatedBy(null)
        );
    }

    @Test
    public void testSetCreatedByWithSame() {
        final AuditInfo info = this.createObject();

        assertSame(
            info,
            info.setCreatedBy(CREATED_BY)
        );
    }

    @Test
    public void testSetCreatedByWithDifferent() {
        final AuditInfo info = this.createObject();

        final EmailAddress differentCreatedBy = EmailAddress.parse("different@example.com");
        final AuditInfo different = info.setCreatedBy(differentCreatedBy);

        assertNotSame(
            info,
            different
        );

        this.createdByAndCheck(info);
        this.createdByAndCheck(different, differentCreatedBy);

        this.createdTimestampAndCheck(info);
        this.createdTimestampAndCheck(different);

        this.modifiedByAndCheck(info);
        this.modifiedByAndCheck(different);

        this.modifiedTimestampAndCheck(info);
        this.modifiedTimestampAndCheck(different);
    }

    private void createdByAndCheck(final AuditInfo info) {
        this.createdByAndCheck(
            info,
            CREATED_BY
        );
    }

    private void createdByAndCheck(final AuditInfo info,
                                   final EmailAddress expected) {
        this.checkEquals(
            expected,
            info.createdBy()
        );
    }

    // setCreatedTimestamp..............................................................................................

    @Test
    public void testSetCreatedTimestampWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createObject()
                .setCreatedTimestamp(null)
        );
    }

    @Test
    public void testSetCreatedTimestampWithSame() {
        final AuditInfo info = this.createObject();

        assertSame(
            info,
            info.setCreatedTimestamp(CREATED_TIMESTAMP)
        );
    }

    @Test
    public void testSetCreatedTimestampWithDifferent() {
        final AuditInfo info = this.createObject();

        final LocalDateTime differentCreatedTimestamp = LocalDateTime.MIN;
        final AuditInfo different = info.setCreatedTimestamp(differentCreatedTimestamp);

        assertNotSame(
            info,
            different
        );

        this.createdByAndCheck(info);
        this.createdByAndCheck(different);

        this.createdTimestampAndCheck(info);
        this.createdTimestampAndCheck(different, differentCreatedTimestamp);

        this.modifiedByAndCheck(info);
        this.modifiedByAndCheck(different);

        this.modifiedTimestampAndCheck(info);
        this.modifiedTimestampAndCheck(different);
    }

    private void createdTimestampAndCheck(final AuditInfo info) {
        this.createdTimestampAndCheck(
            info,
            CREATED_TIMESTAMP
        );
    }

    private void createdTimestampAndCheck(final AuditInfo info,
                                          final LocalDateTime expected) {
        this.checkEquals(
            expected,
            info.createdTimestamp()
        );
    }

    // setModifiedBy....................................................................................................

    @Test
    public void testSetModifiedByWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createObject()
                .setModifiedBy(null)
        );
    }

    @Test
    public void testSetModifiedByWithSame() {
        final AuditInfo info = this.createObject();

        assertSame(
            info,
            info.setModifiedBy(MODIFIED_BY)
        );
    }

    @Test
    public void testSetModifiedByWithDifferent() {
        final AuditInfo info = this.createObject();

        final EmailAddress differentModifiedBy = EmailAddress.parse("different@example.com");
        final AuditInfo different = info.setModifiedBy(differentModifiedBy);

        assertNotSame(
            info,
            different
        );

        this.createdByAndCheck(info);
        this.createdByAndCheck(different);

        this.createdTimestampAndCheck(info);
        this.createdTimestampAndCheck(different);

        this.modifiedByAndCheck(info);
        this.modifiedByAndCheck(different, differentModifiedBy);

        this.modifiedTimestampAndCheck(info);
        this.modifiedTimestampAndCheck(different);
    }

    private void modifiedByAndCheck(final AuditInfo info) {
        this.modifiedByAndCheck(
            info,
            MODIFIED_BY
        );
    }

    private void modifiedByAndCheck(final AuditInfo info,
                                    final EmailAddress expected) {
        this.checkEquals(
            expected,
            info.modifiedBy()
        );
    }

    // setModifiedTimestamp.............................................................................................

    @Test
    public void testSetModifiedTimestampWithNullFails() {
        assertThrows(
            NullPointerException.class,
            () -> this.createObject()
                .setModifiedTimestamp(null)
        );
    }

    @Test
    public void testSetModifiedTimestampWithSame() {
        final AuditInfo info = this.createObject();

        assertSame(
            info,
            info.setModifiedTimestamp(MODIFIED_TIMESTAMP)
        );
    }

    @Test
    public void testSetModifiedTimestampWithDifferent() {
        final AuditInfo info = this.createObject();

        final LocalDateTime differentModifiedTimestamp = LocalDateTime.MAX;
        final AuditInfo different = info.setModifiedTimestamp(differentModifiedTimestamp);

        assertNotSame(
            info,
            different
        );

        this.createdByAndCheck(info);
        this.createdByAndCheck(different);

        this.createdTimestampAndCheck(info);
        this.createdTimestampAndCheck(different);

        this.modifiedByAndCheck(info);
        this.modifiedByAndCheck(different);

        this.modifiedTimestampAndCheck(info);
        this.modifiedTimestampAndCheck(different, differentModifiedTimestamp);
    }

    private void modifiedTimestampAndCheck(final AuditInfo info) {
        this.modifiedTimestampAndCheck(
            info,
            MODIFIED_TIMESTAMP
        );
    }

    private void modifiedTimestampAndCheck(final AuditInfo info,
                                           final LocalDateTime expected) {
        this.checkEquals(
            expected,
            info.modifiedTimestamp()
        );
    }

    // equals...........................................................................................................

    @Test
    public void testEqualsDifferentCreatedBy() {
        this.checkNotEquals(
            AuditInfo.with(
                EmailAddress.parse("different@example.com"),
                CREATED_TIMESTAMP,
                MODIFIED_BY,
                MODIFIED_TIMESTAMP
            )
        );
    }

    @Test
    public void testEqualsDifferentCreatedTimestamp() {
        this.checkNotEquals(
            AuditInfo.with(
                CREATED_BY,
                LocalDateTime.MIN,
                MODIFIED_BY,
                MODIFIED_TIMESTAMP
            )
        );
    }

    @Test
    public void testEqualsDifferentModifiedBy() {
        this.checkNotEquals(
            AuditInfo.with(
                CREATED_BY,
                CREATED_TIMESTAMP,
                EmailAddress.parse("different@example.com"),
                MODIFIED_TIMESTAMP
            )
        );
    }

    @Test
    public void testEqualsDifferentLastTimestamp() {
        this.checkNotEquals(
            AuditInfo.with(
                CREATED_BY,
                CREATED_TIMESTAMP,
                MODIFIED_BY,
                LocalDateTime.MAX
            )
        );
    }

    @Override
    public AuditInfo createObject() {
        return AuditInfo.with(
            CREATED_BY,
            CREATED_TIMESTAMP,
            MODIFIED_BY,
            MODIFIED_TIMESTAMP
        );
    }

    // json.............................................................................................................

    @Test
    public void testMarshall() {
        this.marshallAndCheck(
            this.createJsonNodeMarshallingValue(),
            "{\n" +
                "  \"createdBy\": \"created-by@example.com\",\n" +
                "  \"createdTimestamp\": \"1999-12-31T12:58:59\",\n" +
                "  \"modifiedBy\": \"modified-by@example.com\",\n" +
                "  \"modifiedTimestamp\": \"2000-01-02T12:58:59\"\n" +
                "}"
        );
    }

    @Override
    public AuditInfo unmarshall(final JsonNode json,
                                final JsonNodeUnmarshallContext context) {
        return AuditInfo.unmarshall(
            json,
            context
        );
    }

    @Override
    public AuditInfo createJsonNodeMarshallingValue() {
        return this.createObject();
    }
    
    // class............................................................................................................

    @Override
    public Class<AuditInfo> type() {
        return AuditInfo.class;
    }

    @Override
    public JavaVisibility typeVisibility() {
        return JavaVisibility.PUBLIC;
    }
}
