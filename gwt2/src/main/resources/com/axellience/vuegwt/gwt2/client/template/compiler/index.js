(function(context) {
	'use strict';
	context.process = {
		env: {
			NODE_ENV: "production"
		}
	};

	context.compile = require('vue-template-compiler').compile;
})(this);