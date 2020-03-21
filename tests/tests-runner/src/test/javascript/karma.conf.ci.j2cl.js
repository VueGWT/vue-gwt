const baseConfig = require('./karma.conf.js');

module.exports = function (config) {
  // Load base config
  baseConfig(config);

  config.set({
    files: [
      'src/test/javascript/components/**/*.js',
      {
        pattern: "../tests-app-wrapper-j2cl/target/vue-gwt-tests-app-wrapper-j2cl-*/**/*.j2cl.js",
        watched: false
      },
    ],
    singleRun: true,
    autoWatch: false
  });
};