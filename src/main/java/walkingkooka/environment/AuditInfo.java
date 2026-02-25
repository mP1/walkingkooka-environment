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
import walkingkooka.props.HasProperties;
import walkingkooka.props.Properties;
import walkingkooka.props.PropertiesPath;
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Captures the created and modified entries for an entity.
 */
public final class AuditInfo implements HasProperties,
    TreePrintable {

    public static AuditInfo create(final EmailAddress createdBy,
                                   final LocalDateTime createdTimestamp) {
        return with(
            createdBy,
            createdTimestamp,
            createdBy,
            createdTimestamp
        );
    }

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

    // TreePrintable....................................................................................................

    /**
     * <pre>
     * AuditInfo
     *   created
     *     created-by@example.com 1999-12-31T12:58:59
     *   modified
     *     modified-by@example.com 2000-01-02T12:58:59
     * </pre>
     */
    @Override
    public void printTree(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            this.printTreePair(
                "created",
                this.createdBy,
                this.createdTimestamp,
                printer
            );
            this.printTreePair(
                "modified",
                this.modifiedBy,
                this.modifiedTimestamp,
                printer
            );
        }
        printer.outdent();
    }

    private void printTreePair(final String label,
                               final EmailAddress user,
                               final LocalDateTime timestamp,
                               final IndentingPrinter printer) {
        printer.println(label);
        printer.indent();
        {
            printer.print(user.toString());
            printer.print(" ");
            printer.println(timestamp.toString());
        }
        printer.outdent();
    }

    // HasProperties....................................................................................................

    @Override
    public Properties properties() {
        return Properties.EMPTY.set(
            CREATED_BY,
            this.createdBy.toString()
        ).set(
            CREATED_TIMESTAMP,
            this.createdTimestamp.toString()
        ).set(
            MODIFIED_BY,
            this.modifiedBy.toString()
        ).set(
            MODIFIED_TIMESTAMP,
            this.modifiedTimestamp.toString()
        );
    }

    private final static PropertiesPath CREATED_BY = PropertiesPath.parse("createdBy");
    private final static PropertiesPath CREATED_TIMESTAMP = PropertiesPath.parse("createdTimestamp");
    private final static PropertiesPath MODIFIED_BY = PropertiesPath.parse("modifiedBy");
    private final static PropertiesPath MODIFIED_TIMESTAMP = PropertiesPath.parse("modifiedTimestamp");
}
