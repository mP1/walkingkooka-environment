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

import walkingkooka.datetime.HasNow;
import walkingkooka.net.email.EmailAddress;
import walkingkooka.reflect.ClassTesting;
import walkingkooka.reflect.JavaVisibility;
import walkingkooka.reflect.TypeNameTesting;
import walkingkooka.text.LineEnding;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Locale;

public abstract class EnvironmentContextSharedTestCase<C extends EnvironmentContextShared> implements EnvironmentContextTesting2<C>,
    ClassTesting<C>,
    TypeNameTesting<C> {

    final static Currency CURRENCY = Currency.getInstance("AUD");

    final static LineEnding LINE_ENDING = LineEnding.NL;

    final static Locale LOCALE = Locale.FRENCH;

    final static LocalDateTime NOW = LocalDateTime.MIN;

    final static HasNow HAS_NOW = () -> NOW;

    final static EmailAddress USER = EmailAddress.parse("user123@example.com");

    EnvironmentContextSharedTestCase() {
        super();
    }

    // class............................................................................................................

    @Override
    public final String typeNamePrefix() {
        return EnvironmentContextShared.class.getSimpleName();
    }

    @Override
    public final String typeNameSuffix() {
        return "";
    }

    @Override
    public final JavaVisibility typeVisibility() {
        return JavaVisibility.PACKAGE_PRIVATE;
    }
}
