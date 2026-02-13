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
import walkingkooka.text.printer.IndentingPrinter;
import walkingkooka.text.printer.TreePrintable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Currency;
import java.util.Locale;
import java.util.Optional;

/**
 * Base {@link EnvironmentContext} that delegates the typed getters and setters to the generic
 * <ol>
 * <li>{@link #environmentValue(EnvironmentValueName)}</li>
 * <li>{@link #setEnvironmentValue(EnvironmentValueName, Object)}</li>
 * <li>{@link #removeEnvironmentValue(EnvironmentValueName)}</li>
 * </ol>
 */
abstract class EnvironmentContextShared implements EnvironmentContext,
    TreePrintable {

    EnvironmentContextShared() {
        super();
    }

    // HasCurrency...................................................................................................

    @Override
    public final Currency currency() {
        return this.environmentValueOrFail(CURRENCY);
    }

    @Override
    public final void setCurrency(final Currency currency) {
        this.setEnvironmentValue(
            CURRENCY,
            currency
        );
    }
    
    // HasIndentation...................................................................................................

    @Override
    public final Indentation indentation() {
        return this.environmentValueOrFail(INDENTATION);
    }

    @Override
    public final void setIndentation(final Indentation indentation) {
        this.setEnvironmentValue(
            INDENTATION,
            indentation
        );
    }

    // HasLineEnding....................................................................................................

    @Override
    public final LineEnding lineEnding() {
        return this.environmentValueOrFail(LINE_ENDING);
    }

    @Override
    public final void setLineEnding(final LineEnding lineEnding) {
        this.setEnvironmentValue(
            LINE_ENDING,
            lineEnding
        );
    }

    // HasLocale........................................................................................................

    @Override
    public final Locale locale() {
        return this.environmentValueOrFail(LOCALE);
    }

    @Override
    public final void setLocale(final Locale locale) {
        this.setEnvironmentValue(
            LOCALE,
            locale
        );
    }

    // HasNow...........................................................................................................

    @Override
    public final LocalDateTime now() {
        return this.environmentValueOrFail(NOW);
    }

    // timeOffset.......................................................................................................

    @Override
    public final ZoneOffset timeOffset() {
        return this.environmentValueOrFail(TIME_OFFSET);
    }

    @Override
    public final void setTimeOffset(final ZoneOffset timeOffset) {
        this.setEnvironmentValue(
            TIME_OFFSET,
            timeOffset
        );
    }
    
    // HasUser..........................................................................................................

    @Override
    public final Optional<EmailAddress> user() {
        return this.environmentValue(USER);
    }

    @Override
    public final void setUser(final Optional<EmailAddress> user) {
        this.setOrRemoveEnvironmentValue(
            EnvironmentValueName.USER,
            user
        );
    }

    // TreePrintable....................................................................................................

    final void printTreeValues(final IndentingPrinter printer) {
        printer.println(this.getClass().getSimpleName());
        printer.indent();
        {
            for (final EnvironmentValueName<?> name : this.environmentValueNames()) {
                final Object value = this.environmentValue(name)
                    .orElse(null);
                if (null != value) {
                    printer.println(name.value());
                    printer.indent();
                    {
                        TreePrintable.printTreeOrToString(
                            value,
                            printer
                        );
                    }
                    printer.outdent();
                }
            }
        }
        printer.outdent();
    }
}
