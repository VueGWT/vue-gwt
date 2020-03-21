# Contributing guide

## Testing

### How to run tests locally

Ensure you have Maven installed.
Then:

```shell script
mvn clean install
cd tests
mvn clean test
```

### Test architecture

To test the library we use [karma](https://karma-runner.github.io/latest/index.html), [mocha](https://mochajs.org/) and [chai](https://www.chaijs.com/).

The tests suite is composed of 4 Maven modules:

- `tests-app`: The source of all the components used for testing.
- `tests-app-wrapper-gwt2`: A simple GWT2 entry point that import `tests-app` and build a GWT2 js app with it. 
- `tests-app-wrapper-j2cl`: A simple J2CL entry point that import `tests-app` and build a J2CL js app with it.
- `tests-runner`: Contains all the JS tests that runs using the built tests component. 

The flow for GWT2 goes like this:

- The `tests-app` module containing the test components is built as a `gwt-lib` using [gwt-maven-plugin](https://github.com/tbroyer/gwt-maven-plugin).
- `tests-app-wrapper-gwt2` imports `tests-app` and build a `gwt-app` also using [gwt-maven-plugin](https://github.com/tbroyer/gwt-maven-plugin). The target JS files are stored in `tests-app-wrapper-gwt2/target/vue-gwt-tests-app-wrapper-gwt2-XX-SNAPSHOT/VueGwtTestsAppGwt2/*.js`.
- `tests-runner` runs `tests-runner/src/test/javascript/karma.conf.ci.gwt2.js` with Karma using [frontend-maven-plugin](https://github.com/eirslett/frontend-maven-plugin). This loads the GWT2 output JS and the JS tests in Chrome and runs all the tests against the built components.

The flow for J2CL goes like this:

- The `tests-app` module containing the test components is built as a `gwt-lib` using [gwt-maven-plugin](https://github.com/tbroyer/gwt-maven-plugin), this ends up just being a jar with the source and generated sources.
- `tests-app-wrapper-j2cl` imports `tests-app` and build a J2CL app using [j2cl-maven-plugin](https://github.com/Vertispan/j2clmavenplugin). The target JS files are stored in `tests-app-wrapper-j2cl/target/vue-gwt-tests-app-wrapper-j2cl-XX-SNAPSHOT/VueGwtTests.j2cl.js`.
- `tests-runner` runs `tests-runner/src/test/javascript/karma.conf.ci.j2cl.js` with Karma using [frontend-maven-plugin](https://github.com/eirslett/frontend-maven-plugin). This loads the J2CL output JS and the JS tests in Chrome and runs all the tests against the built components.
