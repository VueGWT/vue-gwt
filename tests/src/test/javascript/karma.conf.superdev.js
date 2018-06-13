const baseConfig = require('./karma.conf.js');

module.exports = function (config) {
  // Load base config
  baseConfig(config);

  config.set({
    files: [
      'src/test/javascript/components/**/*.js',
      "http://127.0.0.1:8888/VueGwtTests/VueGwtTests.nocache.js"
    ],
    crossOriginAttribute: false
  });
};