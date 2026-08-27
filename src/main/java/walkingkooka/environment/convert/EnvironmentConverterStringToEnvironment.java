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

package walkingkooka.environment.convert;

import walkingkooka.Cast;
import walkingkooka.InvalidCharacterException;
import walkingkooka.convert.TextToTryingShortCircuitingConverter;
import walkingkooka.environment.Environment;
import walkingkooka.environment.EnvironmentValueName;
import walkingkooka.text.Ascii;
import walkingkooka.text.cursor.TextCursor;
import walkingkooka.text.cursor.TextCursorLineInfo;
import walkingkooka.text.cursor.TextCursorSavePoint;
import walkingkooka.text.cursor.TextCursors;

/**
 * A {@link walkingkooka.convert.Converter} that converts a {@link String} or text like value into a {@link EnvironmentValueName} using {@link EnvironmentConverterContext#parseEnvironmentValueName(String)}.
 */
final class EnvironmentConverterStringToEnvironment<C extends EnvironmentConverterContext> implements TextToTryingShortCircuitingConverter<C> {

    /**
     * Type-safe getter.
     */
    static <C extends EnvironmentConverterContext> EnvironmentConverterStringToEnvironment<C> instance() {
        return Cast.to(INSTANCE);
    }

    /**
     * Singleton
     */
    private final static EnvironmentConverterStringToEnvironment<?> INSTANCE = new EnvironmentConverterStringToEnvironment<>();

    @Override
    public boolean isTargetType(final Object value,
                                final Class<?> type,
                                final C context) {
        return Environment.class == type;
    }

    public final static char COMMENT = '#';

    private final static char BACKSLASH = '\\';

    private final static char BELL = Ascii.BELL;

    private final static char FORM_FEED = '\f';

    private final static char SPACES = ' ';

    private final static char TAB = '\t';

    private final static char CR = '\r';

    private final static char NL = '\n';

    private final static char SINGLE_QUOTE = '\'';

    private final static char DOUBLE_QUOTE = '"';

    private final static char ASSIGNMENT = '=';

    @Override
    public Object parseText(final String text,
                            final Class<?> type,
                            final C context) {
        final int MODE_TOKEN_START_OF_LINE = 1;

        final int MODE_TOKEN_COMMENT = 2;
        final int MODE_TOKEN_COMMENT_CR = 3;

        final int MODE_TOKEN_SPACES = 4;
        final int MODE_TOKEN_SPACES_CR = 5;

        final int MODE_TOKEN_KEY = 6;
        final int MODE_TOKEN_KEY_SPACES = 7;

        final int MODE_TOKEN_ASSIGNMENT_SPACES = 8;

        final int MODE_TOKEN_RAW_VALUE = 9;
        final int MODE_TOKEN_SINGLE_QUOTED_VALUE = 10;
        final int MODE_TOKEN_DOUBLE_QUOTED_VALUE = 11;
        final int MODE_TOKEN_QUOTED_VALUE_AFTER = 12;

        final TextCursor textCursor = TextCursors.charSequence(text);
        TextCursorSavePoint start = null;

        int tokenMode = MODE_TOKEN_START_OF_LINE;

        StringBuilder b = null;

        final int MODE_CHAR = 1;
        final int MODE_CHAR_BACKSPACES_ESCAPING = 2;
        final int MODE_CHAR_BACKSPACES_ESCAPING_CR = 3;
        final int MODE_CHAR_BACKSPACES_ESCAPING_NL = 4;
        final int MODE_CHAR_UNICODE_0 = 5;
        final int MODE_CHAR_UNICODE_1 = 6;
        final int MODE_CHAR_UNICODE_2 = 7;
        final int MODE_CHAR_UNICODE_3 = 8;

        int unicodeChar = 0;
        int charMode = MODE_CHAR;

        Environment environment = Environment.empty();

        EnvironmentValueName<?> name = null;

        while (textCursor.isNotEmpty()) {
            final char c = textCursor.at();

            switch (tokenMode) {
                case MODE_TOKEN_START_OF_LINE:
                    switch (c) {
                        case COMMENT:
                            tokenMode = MODE_TOKEN_COMMENT;
                            textCursor.next();
                            break;
                        case FORM_FEED:
                        case SPACES:
                        case TAB:
                            tokenMode = MODE_TOKEN_SPACES;
                            textCursor.next();
                            break;
                        default:
                            // try again in new mode will consume $c
                            tokenMode = MODE_TOKEN_KEY;
                            b = new StringBuilder();
                            break;
                    }
                    break;
                case MODE_TOKEN_COMMENT:
                    switch (c) {
                        case CR:
                            // could be followed by NL
                            tokenMode = MODE_TOKEN_COMMENT_CR;
                            textCursor.next();
                            break;
                        case NL:
                            tokenMode = MODE_TOKEN_START_OF_LINE;
                            textCursor.next();
                            break;
                        default:
                            // continue consuming comment
                            break;
                    }
                    break;
                case MODE_TOKEN_COMMENT_CR:
                case MODE_TOKEN_SPACES_CR:
                    switch (c) {
                        case NL:
                            textCursor.next();
                            break;
                        default:
                            // dont advance TextCursor try current character again
                            break;
                    }
                    tokenMode = MODE_TOKEN_START_OF_LINE;
                    break;
                case MODE_TOKEN_SPACES:
                    switch (c) {
                        case FORM_FEED:
                        case SPACES:
                        case TAB:
                            textCursor.next();
                            break;
                        case CR:
                            textCursor.next();
                            tokenMode = MODE_TOKEN_SPACES_CR;
                            break;
                        case NL:
                            textCursor.next();
                            tokenMode = MODE_TOKEN_START_OF_LINE;
                            break;
                        default:
                            // dont advance try again with first key character
                            tokenMode = MODE_TOKEN_KEY;
                            start = textCursor.save();
                            b = new StringBuilder();
                            break;
                    }
                    break;
                case MODE_TOKEN_KEY:
                    switch (c) {
                        case ASSIGNMENT:
                            name = this.parseEnvironmentValueName(
                                b,
                                start,
                                context
                            );
                            b = null;
                            textCursor.next();
                            tokenMode = MODE_TOKEN_ASSIGNMENT_SPACES;
                            break;
                        case FORM_FEED:
                        case SPACES:
                        case TAB:
                            name = this.parseEnvironmentValueName(
                                b,
                                start,
                                context
                            );
                            b = null;
                            textCursor.next();
                            tokenMode = MODE_TOKEN_KEY_SPACES;
                            break;
                        default:
                            break;
                    }
                    break;
                case MODE_TOKEN_KEY_SPACES:
                    switch (c) {
                        case ASSIGNMENT:
                            // dont advance try again
                            tokenMode = MODE_TOKEN_ASSIGNMENT_SPACES;
                            textCursor.next();
                            break;
                        case FORM_FEED:
                        case SPACES:
                        case TAB:
                            textCursor.next();
                            break;
                        default:
                            throw textCursor.lineInfo()
                                .invalidCharacterException()
                                .orElseThrow();
                    }
                    break;
                case MODE_TOKEN_ASSIGNMENT_SPACES:
                    switch (c) {
                        case FORM_FEED:
                        case SPACES:
                        case TAB:
                        case CR:
                        case NL:
                            textCursor.next(); // skip
                            break;
                        case SINGLE_QUOTE:
                            tokenMode = MODE_TOKEN_SINGLE_QUOTED_VALUE;
                            charMode = MODE_CHAR;
                            b = new StringBuilder();
                            break;
                        case DOUBLE_QUOTE:
                            tokenMode = MODE_TOKEN_DOUBLE_QUOTED_VALUE;
                            charMode = MODE_CHAR;
                            b = new StringBuilder();
                            break;
                        default:
                            tokenMode = MODE_TOKEN_RAW_VALUE;
                            charMode = MODE_CHAR;
                            b = new StringBuilder();
                            break;
                    }
                    break;
                case MODE_TOKEN_RAW_VALUE:
                case MODE_TOKEN_SINGLE_QUOTED_VALUE:
                case MODE_TOKEN_DOUBLE_QUOTED_VALUE:
                    break;
                case MODE_TOKEN_QUOTED_VALUE_AFTER:
                    switch (c) {
                        case FORM_FEED:
                        case SPACES:
                        case TAB:
                        case CR:
                        case NL:
                            textCursor.next(); // skip
                            break;
                        default:
                            throw textCursor.lineInfo()
                                .invalidCharacterException()
                                .orElseThrow();
                    }
                    break;
                default:
                    throw new IllegalStateException("Invalid tokenMode: " + tokenMode);
            }

            switch (tokenMode) {
                case MODE_TOKEN_KEY:
                case MODE_TOKEN_RAW_VALUE:
                case MODE_TOKEN_SINGLE_QUOTED_VALUE:
                case MODE_TOKEN_DOUBLE_QUOTED_VALUE: {
                    switch (charMode) {
                        case MODE_CHAR:
                            switch (c) {
                                case CR:
                                case NL:
                                    if (MODE_TOKEN_RAW_VALUE == tokenMode) {
                                        environment = this.rawStringValue(
                                            environment,
                                            name,
                                            b,
                                            context
                                        );
                                        tokenMode = MODE_TOKEN_START_OF_LINE;
                                        name = null;
                                        b = null;
                                        break;
                                    } else {
                                        b.append(c);
                                    }
                                    break;
                                case BACKSLASH:
                                    charMode = MODE_CHAR_BACKSPACES_ESCAPING;
                                    break;
                                case SINGLE_QUOTE:
                                    if (MODE_TOKEN_SINGLE_QUOTED_VALUE == tokenMode && b.length() > 0) {
                                        environment = this.setEnvironmentValue(
                                            environment,
                                            name,
                                            //b.substring(1), // remove opening quote
                                            b.toString(),
                                            context
                                        );
                                        tokenMode = MODE_TOKEN_START_OF_LINE;
                                        b = null;
                                        name = null;
                                        break;
                                    }
                                    break;
                                case DOUBLE_QUOTE:
                                    if (MODE_TOKEN_DOUBLE_QUOTED_VALUE == tokenMode && b.length() > 0) {
                                        environment = this.setEnvironmentValue(
                                            environment,
                                            name,
                                            b.toString(),
                                            context
                                        );
                                        tokenMode = MODE_TOKEN_START_OF_LINE;
                                        b = null;
                                        name = null;
                                        break;
                                    }
                                    break;
                                default:
                                    b.append(c);
                                    break;
                            }
                            break;
                        case MODE_CHAR_BACKSPACES_ESCAPING:
                            switch (c) {
                                case 'b':
                                    b.append(BELL);
                                    charMode = MODE_CHAR;
                                    break;
                                case 'f':
                                    b.append(FORM_FEED);
                                    charMode = MODE_CHAR;
                                    break;
                                case 'n':
                                    b.append(NL);
                                    charMode = MODE_CHAR;
                                    break;
                                case 'r':
                                    b.append(CR);
                                    charMode = MODE_CHAR;
                                    break;
                                case 't':
                                    b.append(TAB);
                                    charMode = MODE_CHAR;
                                    break;
                                case 'u':
                                    charMode = MODE_CHAR_UNICODE_0;
                                    break;
                                case BACKSLASH:
                                    b.append(c);
                                    charMode = MODE_CHAR;
                                    break;
                                case CR:
                                    b.append(c);
                                    charMode = MODE_CHAR_BACKSPACES_ESCAPING_CR;
                                    break;
                                case NL:
                                    b.append(c);
                                    charMode = MODE_CHAR_BACKSPACES_ESCAPING_NL;
                                    break;
                                default:
                                    b.append(c);
                                    charMode = MODE_CHAR;
                                    break;
                            }
                            break;
                        case MODE_CHAR_BACKSPACES_ESCAPING_CR:
                            b.append(c);
                            charMode = MODE_CHAR;
                            break;
                        case MODE_CHAR_BACKSPACES_ESCAPING_NL:
                            b.append(c);
                            charMode = MODE_CHAR;
                            break;
                        case MODE_CHAR_UNICODE_0:
                            unicodeChar = nextUnicodeDigit(
                                c,
                                textCursor,
                                unicodeChar
                            );
                            charMode = MODE_CHAR_UNICODE_1;
                            break;
                        case MODE_CHAR_UNICODE_1:
                            unicodeChar = nextUnicodeDigit(
                                c,
                                textCursor,
                                unicodeChar
                            );
                            charMode = MODE_CHAR_UNICODE_2;
                            break;
                        case MODE_CHAR_UNICODE_2:
                            unicodeChar = nextUnicodeDigit(
                                c,
                                textCursor,
                                unicodeChar
                            );
                            charMode = MODE_CHAR_UNICODE_3;
                            break;
                        case MODE_CHAR_UNICODE_3:
                            b.append(
                                (char) nextUnicodeDigit(
                                    c,
                                    textCursor,
                                    unicodeChar
                                )
                            );
                            charMode = MODE_CHAR;
                            unicodeChar = 0;
                            break;
                        default:
                            throw new IllegalStateException("Invalid char mode " + charMode);
                    }
                    textCursor.next();
                }
                default:
                    break;
            }
        }

        switch (tokenMode) {
            case MODE_TOKEN_START_OF_LINE:
            case MODE_TOKEN_COMMENT:
            case MODE_TOKEN_COMMENT_CR:
            case MODE_TOKEN_SPACES: // space might be before key but file is not empty so must be an empty line
            case MODE_TOKEN_SPACES_CR:
            case MODE_TOKEN_QUOTED_VALUE_AFTER:
                // nop
                break;
            case MODE_TOKEN_KEY:
            case MODE_TOKEN_KEY_SPACES:
                throw new IllegalArgumentException("Missing value after name");
            case MODE_TOKEN_ASSIGNMENT_SPACES: // <-- empty value
            case MODE_TOKEN_RAW_VALUE:
                environment = this.rawStringValue(
                    environment,
                    name,
                    b,
                    context
                );
                break;
            case MODE_TOKEN_SINGLE_QUOTED_VALUE:
                throw new IllegalArgumentException("Missing terminating \'\'\'");
            case MODE_TOKEN_DOUBLE_QUOTED_VALUE:
                throw new IllegalArgumentException("Missing terminating \'\"\'");
            default:
                throw new IllegalArgumentException("Invalid token mode " + tokenMode);
        }

        return environment;
    }

    private EnvironmentValueName<?> parseEnvironmentValueName(final CharSequence text,
                                                              final TextCursorSavePoint start,
                                                              final C context) {
        try {
            return context.parseEnvironmentValueName(
                text.toString()
            );
        } catch (final InvalidCharacterException cause) {
            final TextCursorLineInfo lineInfo = start.lineInfo();

            // FIX column and line and then rethrow
            throw cause.setColumnAndLine(
                lineInfo.column() + cause.position() - 1,
                lineInfo.lineNumber()
            );
        }
    }

    private static int nextUnicodeDigit(final char c,
                                        final TextCursor text,
                                        final int unicode) {
        return unicode * 16 +
            digit(
                c,
                text
            );
    }

    private static int digit(final char c,
                             final TextCursor text) {
        final int value;

        switch (c) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                value = c - '0';
                break;
            case 'A':
            case 'B':
            case 'C':
            case 'D':
            case 'E':
            case 'F':
                value = 10 + c - 'A';
                break;
            case 'a':
            case 'b':
            case 'c':
            case 'd':
            case 'e':
            case 'f':
                value = 10 + c - 'a';
                break;
            default:
                throw new InvalidCharacterException(
                    text.text(),
                    text.lineInfo()
                        .textOffset()
                );
        }

        return value;
    }

    /**
     * Special case unquoted or raw values because they need to be trimmed of whitespace before and after.
     */
    private Environment rawStringValue(final Environment environment,
                                       final EnvironmentValueName<?> name,
                                       final StringBuilder text,
                                       final C context) {
        return this.setEnvironmentValue(
            environment,
            name,
            // will be null if there is no value after equals sign
            null == text ?
                "" :
                text.toString()
                    .trim(), // remove leading '='' and trim
            context
        );
    }

    private Environment setEnvironmentValue(final Environment environment,
                                            final EnvironmentValueName<?> name,
                                            final String text,
                                            final C context) {
        return environment.set(
            name,
            Cast.to(
                context.convertOrFail(
                    text,
                    name.type()
                )
            )
        );
    }

    @Override
    public String toString() {
        return TEXT + " to " + Environment.class.getSimpleName();
    }
}
