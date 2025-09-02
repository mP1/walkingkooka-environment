[![Build Status](https://github.com/mP1/walkingkooka-environment/actions/workflows/build.yaml/badge.svg)](https://github.com/mP1/walkingkooka-environment/actions/workflows/build.yaml/badge.svg)
[![Coverage Status](https://coveralls.io/repos/github/mP1/walkingkooka-environment/badge.svg?branch=master)](https://coveralls.io/repos/github/mP1/walkingkooka-environment?branch=master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/mP1/walkingkooka-environment.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-environment/context:java)
[![Total alerts](https://img.shields.io/lgtm/alerts/g/mP1/walkingkooka-environment.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/mP1/walkingkooka-environment/alerts/)
![](https://tokei.rs/b1/github/mP1/walkingkooka-environment)
[![J2CL compatible](https://img.shields.io/badge/J2CL-compatible-brightgreen.svg)](https://github.com/mP1/j2cl-central)

# walkingkooka-environment
Support for providing a scoped / context with targetted environment values.

This is intended primarily to support custom values to a component(s) such as a [plugin](https://github.com/mP1/walkingkooka-plugin).

- Secret values such as a username and password to a component.
- Non secret values such as an address.
- A [EnvironmentValueName](https://github.com/mP1/walkingkooka-environment/blob/master/src/main/java/walkingkooka/environment/EnvironmentValueName.java) 
 supports supplying a default to a function parameter from a environment context.

### [Converters](https://github.com/mP1/walkingkooka-convert/blob/master/src/main/java/walkingkooka/convert/Converter.java)

A collection of converters that are particularly useful within expressions and support passing environment values.

- [EnvironmentConverterStringToEnvironmentValueName](https://github.com/mP1/walkingkooka-environment/tree/master/src/main/java/walkingkooka/environment/convert/EnvironmentConverterStringToEnvironmentValueName.java)


### [Functions](https://github.com/mP1/walkingkooka-tree/blob/master/src/main/java/walkingkooka/tree/expression/function/ExpressionFunction.java)

Functions that will be useful with terminal session or other similar general environment queries.

- [getEnv](https://github.com/mP1/walkingkooka-environment/tree/master/src/main/java/walkingkooka/environment/expression/function/EnvironmentExpressionFunctionGetEnv.java)
- [getLocale](https://github.com/mP1/walkingkooka-environment/tree/master/src/main/java/walkingkooka/environment/expression/function/EnvironmentExpressionFunctionGetLocale.java)
- [getUser](https://github.com/mP1/walkingkooka-environment/tree/master/src/main/java/walkingkooka/environment/expression/function/EnvironmentExpressionFunctionGetUser.java)
- [setEnv](https://github.com/mP1/walkingkooka-environment/tree/master/src/main/java/walkingkooka/environment/expression/function/EnvironmentExpressionFunctionSetEnv.java)
- [setLocale](https://github.com/mP1/walkingkooka-environment/tree/master/src/main/java/walkingkooka/environment/expression/function/EnvironmentExpressionFunctionSetLocale.java)
- [removeEnv](https://github.com/mP1/walkingkooka-environment/tree/master/src/main/java/walkingkooka/environment/expression/function/EnvironmentExpressionFunctionRemoveEnv.java)
