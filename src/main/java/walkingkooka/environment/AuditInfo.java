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
import walkingkooka.tree.json.JsonNode;
import walkingkooka.tree.json.JsonPropertyName;
import walkingkooka.tree.json.marshall.JsonNodeContext;
import walkingkooka.tree.json.marshall.JsonNodeMarshallContext;
import walkingkooka.tree.json.marshall.JsonNodeUnmarshallContext;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Captures the created and modified entries for an entity.
 */
public final class AuditInfo {

    public static AuditInfo with(final EmailAddress createdBy,
                                 final LocalDateTime createdTimestamp,
                                 final EmailAddress modifiedBy,
                                 final LocalDateTime modifiedTimestamp) {
        return new AuditInfo(
            Objects.requireNonNull(createdBy, "createdBy"),
            Objects.requireNonNull(createdTimestamp, "createdTimestamp"),
            Objects.requireNonNull(modifiedBy, "modifiedBy"),
            Objects.requireNonNull(modifiedTimestamp, "modifiedTimestamp")
        );
    }

    private AuditInfo(final EmailAddress createdBy,
                      final LocalDateTime createdTimestamp,
                      final EmailAddress modifiedBy,
                      final LocalDateTime modifiedTimestamp) {
        this.createdBy = createdBy;
        this.createdTimestamp = createdTimestamp;
        this.modifiedBy = modifiedBy;
        this.modifiedTimestamp = modifiedTimestamp;

        if (modifiedTimestamp.isBefore(createdTimestamp)) {
            throw new IllegalArgumentException("ModifiedTimestamp " + modifiedTimestamp + " < createdTimestamp " + createdTimestamp);
        }
    }

    // createdBy........................................................................................................

    public EmailAddress createdBy() {
        return this.createdBy;
    }

    private final EmailAddress createdBy;

    public AuditInfo setCreatedBy(final EmailAddress createdBy) {
        return this.createdBy.equals(createdBy) ?
            this :
            new AuditInfo(
                Objects.requireNonNull(createdBy, "createdBy"),
                this.createdTimestamp,
                this.modifiedBy,
                this.modifiedTimestamp
            );
    }

    // createdTimestamp.................................................................................................

    public LocalDateTime createdTimestamp() {
        return this.createdTimestamp;
    }

    private final LocalDateTime createdTimestamp;

    public AuditInfo setCreatedTimestamp(final LocalDateTime createdTimestamp) {
        return this.createdTimestamp.equals(createdTimestamp) ?
            this :
            new AuditInfo(
                this.createdBy,
                Objects.requireNonNull(createdTimestamp, "createdTimestamp"),
                this.modifiedBy,
                this.modifiedTimestamp
            );
    }

    // modifiedBy.......................................................................................................

    public EmailAddress modifiedBy() {
        return this.modifiedBy;
    }

    private final EmailAddress modifiedBy;

    public AuditInfo setModifiedBy(final EmailAddress modifiedBy) {
        return this.modifiedBy.equals(modifiedBy) ?
            this :
            new AuditInfo(
                this.createdBy,
                this.createdTimestamp,
                Objects.requireNonNull(modifiedBy, "modifiedBy"),
                this.modifiedTimestamp
            );
    }

    public LocalDateTime modifiedTimestamp() {
        return this.modifiedTimestamp;
    }

    private final LocalDateTime modifiedTimestamp;

    public AuditInfo setModifiedTimestamp(final LocalDateTime modifiedTimestamp) {
        return this.modifiedTimestamp.equals(modifiedTimestamp) ?
            this :
            new AuditInfo(
                this.createdBy,
                createdTimestamp,
                this.modifiedBy,
                Objects.requireNonNull(modifiedTimestamp, "modifiedTimestamp")
            );
    }

    // Object...........................................................................................................

    @Override
    public int hashCode() {
        return Objects.hash(
            this.createdBy,
            this.createdTimestamp,
            this.modifiedBy,
            this.modifiedTimestamp
        );
    }

    public boolean equals(final Object other) {
        return this == other ||
            (other instanceof AuditInfo && this.equals0((AuditInfo) other));
    }

    private boolean equals0(final AuditInfo other) {
        return this.createdBy.equals(other.createdBy) &&
            this.createdTimestamp.equals(other.createdTimestamp) &&
            this.modifiedBy.equals(other.modifiedBy) &&
            this.modifiedTimestamp.equals(other.modifiedTimestamp);
    }

    @Override
    public String toString() {
        return this.createdBy +
            " " +
            this.createdTimestamp +
            " " +
            this.modifiedBy +
            " " +
            this.modifiedTimestamp;
    }

    // Json.............................................................................................................

    private final static String CREATED_BY_PROPERTY_STRING = "createdBy";

    private final static String CREATED_TIMESTAMP_PROPERTY_STRING = "createdTimestamp";

    private final static String MODIFIED_BY_PROPERTY_STRING = "modifiedBy";

    private final static String MODIFIED_TIMESTAMP_PROPERTY_STRING = "modifiedTimestamp";

    final static JsonPropertyName CREATED_BY_PROPERTY = JsonPropertyName.with(CREATED_BY_PROPERTY_STRING);

    final static JsonPropertyName CREATED_TIMESTAMP_PROPERTY = JsonPropertyName.with(CREATED_TIMESTAMP_PROPERTY_STRING);

    final static JsonPropertyName MODIFIED_BY_PROPERTY = JsonPropertyName.with(MODIFIED_BY_PROPERTY_STRING);

    final static JsonPropertyName MODIFIED_TIMESTAMP_PROPERTY = JsonPropertyName.with(MODIFIED_TIMESTAMP_PROPERTY_STRING);

    static AuditInfo unmarshall(final JsonNode node,
                                final JsonNodeUnmarshallContext context) {
        EmailAddress createdBy = null;
        LocalDateTime createdTimestamp = null;
        EmailAddress modifiedBy = null;
        LocalDateTime modifiedTimestamp = null;

        for (JsonNode child : node.objectOrFail().children()) {
            final JsonPropertyName name = child.name();
            switch (name.value()) {
                case CREATED_BY_PROPERTY_STRING:
                    createdBy = context.unmarshall(
                        child,
                        EmailAddress.class
                    );
                    break;
                case CREATED_TIMESTAMP_PROPERTY_STRING:
                    createdTimestamp = context.unmarshall(
                        child,
                        LocalDateTime.class
                    );
                    break;
                case MODIFIED_BY_PROPERTY_STRING:
                    modifiedBy = context.unmarshall(
                        child,
                        EmailAddress.class
                    );
                    break;
                case MODIFIED_TIMESTAMP_PROPERTY_STRING:
                    modifiedTimestamp = context.unmarshall(
                        child,
                        LocalDateTime.class
                    );
                    break;
                default:
                    JsonNodeUnmarshallContext.unknownPropertyPresent(name, node);
                    break;
            }
        }

        return with(
            createdBy,
            createdTimestamp,
            modifiedBy,
            modifiedTimestamp
        );
    }

    private JsonNode marshall(final JsonNodeMarshallContext context) {
        return JsonNode.object()
            .set(CREATED_BY_PROPERTY, context.marshall(this.createdBy))
            .set(CREATED_TIMESTAMP_PROPERTY, context.marshall(this.createdTimestamp))
            .set(MODIFIED_BY_PROPERTY, context.marshall(this.modifiedBy))
            .set(MODIFIED_TIMESTAMP_PROPERTY, context.marshall(this.modifiedTimestamp));
    }

    static {
        JsonNodeContext.register(
            JsonNodeContext.computeTypeName(AuditInfo.class),
            AuditInfo::unmarshall,
            AuditInfo::marshall,
            AuditInfo.class
        );
    }
}
