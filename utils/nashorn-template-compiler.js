(function () {
  'use strict';
  global.process = {
    env: {
      NODE_ENV: "production"
    }
  };

  global.compile = require('vue-template-compiler').compile;
})();