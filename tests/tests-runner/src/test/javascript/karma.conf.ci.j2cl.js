const baseConfig = require('./karma.conf.js');

module.exports = function (config) {
  // Load base config
  baseConfig(config);

  config.set({
    files: [
      'src/test/javascript/components/**/*.js',
      {
        pattern: "../tests-app-wrapper-j2cl/target/vue-gwt-tests-app-wrapper-j2cl-*/VueGwtTestsAppJ2CL.js",
        watched: false
      },
    ],
    // NOT WORKING IN J2CL for now!
    exclude: [
      '**/collections.spec.js',
      '**/inheritance.spec.js'
    ],
    singleRun: true,
    autoWatch: false
  });
};