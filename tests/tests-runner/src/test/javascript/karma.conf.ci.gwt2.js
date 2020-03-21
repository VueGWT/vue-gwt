const baseConfig = require('./karma.conf.js');

module.exports = function (config) {
  // Load base config
  baseConfig(config);

  // Override base config
  config.set({
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
    singleRun: true,
    autoWatch: false
  });
};