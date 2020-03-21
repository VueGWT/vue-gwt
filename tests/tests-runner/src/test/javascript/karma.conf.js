const webpackConfig = require('./webpack.config');

module.exports = function (config) {
  config.set({
    basePath: "../../..",
    frameworks: ['mocha'],
    files: [
      'src/test/javascript/components/**/*.js',
      {
        pattern: "../tests-app-wrapper-gwt2/target/vue-gwt-tests-app-wrapper-gwt2-*/**/*.nocache.js",
        watched: false
      },
      {
        pattern: "../tests-app-wrapper-gwt2/target/vue-gwt-tests-app-wrapper-gwt2-*/**/*.cache.js",
        watched: false,
        included: false
      }
    ],
    exclude: ['karma.conf*.js'],
    reporters: ['spec'],
    port: 9877,
    browsers: ['ChromeHeadless'],
    singleRun: false,
    autoWatch: true,
    preprocessors: {
      'src/test/javascript/components/**/*.js': ['webpack']
    },
    webpack: webpackConfig
  });
};