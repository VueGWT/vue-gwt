const webpackConfig = require('./webpack.config');

module.exports = function (config) {
	config.set({
		basePath: "../../..",
		frameworks: ['mocha'],
		files: [
			'src/test/javascript/components/**/*.js',
			{
				pattern: "target/vue-gwt-tests-*/**/*.nocache.js",
				watched: false
			},
			{
				pattern: "target/vue-gwt-tests-*/**/*.cache.js",
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