const webpackConfig = require('./webpack.config');

module.exports = function (config) {
  config.set({
    basePath: "../../..",
    frameworks: ['mocha'],
    exclude: ['karma.conf*.js'],
    reporters: ['spec'],
    port: 9877,
    browsers: ['ChromeHeadless'],
    preprocessors: {
      'src/test/javascript/components/**/*.js': ['webpack']
    },
    webpack: webpackConfig
  });
};