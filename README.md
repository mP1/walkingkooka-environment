[![Build Status](https://github.com/mP1/walkingkooka-environment/actions/workflows/build.yaml/badge.svg)](https://github.com/mP1/walkingkooka-environment/actions/workflows/build.yaml/badge.svg)
[![Coverage Status](https://coveralls.io/repos/github/mP1/walkingkooka-environment/badge.svg)](https://coveralls.io/github/mP1/walkingkooka-environment)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/mP1/walkingkooka-environment.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-environment/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/mP1/walkingkooka-environment.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-environment/alerts/)
![](https://tokei.rs/b1/github/mP1/walkingkooka-environment)
[![J2CL compatible](https://img.shields.io/badge/J2CL-compatible-brightgreen.svg)](https://github.com/mP1/j2cl-central)

# walkingkooka-environment

Support for providing a scoped / context with targetted environment values.

This is intended primarily to support custom values to a component(s) such as a [plugin](https://github.com/mP1/walkingkooka-plugin).

- Secret values such as a username and password to a component.
- Non secret values such as an ip-address.

### [EnvironmentValueName](https://github.com/mP1/walkingkooka-environment/blob/master/src/main/java/walkingkooka/environment/EnvironmentValueName.java)

- [indentation](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/text/Indentation.java): Indentation mostly used for "printing" multi-line structure like json.
- [lineEnding](https://github.com/mP1/walkingkooka/blob/master/src/main/java/walkingkooka/text/LineEnding.java): The current line-ending used by functions when printing lines of text
- locale: The current locale for the current user, eg "en-AU".
- now: The current date/time`java.time.LocalDateTime`
- timeOffset: An offset that is used to adjust the current system time to match the locality of the current user.
- [user](https://github.com/mP1/walkingkooka-net/blob/master/src/main/java/walkingkooka/net/email/EmailAddress.java): The email address identifying the current user

Within a spreadsheet terminal session, additional values are available. The list below is not exhaustive but a short list of examples.

- [spreadsheetId](https://github.com/mP1/walkingkooka-spreadsheet/blob/master/src/main/java/walkingkooka/spreadsheet/meta/SpreadsheetId.java): Identifies the default spreadsheetId assumed for unqualified cell references within expressions.
- converters: The [Converter](https://github.com/mP1/walkingkooka-convert/blob/master/src/main/java/walkingkooka/convert/Converter.java) used when executing expressions.
- dateParser: One or more [Parser](https://github.com/mP1/walkingkooka-spreadsheet/blob/master/src/main/java/walkingkooka/spreadsheet/parser/SpreadsheetParser.java) used to parse dates within expressions.
- dateTimeOffset: Used to select the date equivalent to a value of zero.
- dateTimeParser: One or more [Parser](https://github.com/mP1/walkingkooka-spreadsheet/blob/master/src/main/java/walkingkooka/spreadsheet/parser/SpreadsheetParser.java) used to parse date/times within expressions.
- decimalNumberDigitCount: The default number of digits for numbers within expressions.
- decimalNumberSymbols: CSV list containing belonging to [DecimalNumberSymbol](https://github.com/mP1/walkingkooka-math/blob/master/src/main/java/walkingkooka/math/DecimalNumberSymbols.java) used when parsing and formatting numbers.
- defaultYear: The default user assumed for dates without a year.
- functions: Csv of available [Function](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/ExpressionFunction.java) for expressions.
- numberParser: One or more [Parser](https://github.com/mP1/walkingkooka-spreadsheet/blob/master/src/main/java/walkingkooka/spreadsheet/parser/SpreadsheetParser.java) used to parse numbers within expressions.
- precision: The number of digits of precision for decimal numbers.
- serverUrl: The [AbsoluteUrl](https://github.com/mP1/walkingkooka-net/blob/master/src/main/java/walkingkooka/net/AbsoluteUrl.java) for the host server.
- timeParser: One or more [Parser](https://github.com/mP1/walkingkooka-spreadsheet/blob/master/src/main/java/walkingkooka/spreadsheet/parser/SpreadsheetParser.java) used to parse times within expressions.
- twoDigitYear: The century component used for two digit years within a date.
- valueSeparator: The value separator character used to demark a list of values.

### [Converters](https://github.com/mP1/walkingkooka-convert/blob/master/src/main/java/walkingkooka/convert/Converter.java)

A collection of converters that are particularly useful within expressions and support passing environment values.

- [EnvironmentConverterStringToEnvironmentValueName](https://github.com/mP1/walkingkooka-environment/tree/master/src/main/java/walkingkooka/environment/convert/EnvironmentConverterStringToEnvironmentValueName.java)

