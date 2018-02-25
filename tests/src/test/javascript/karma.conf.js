const webpackConfig = require('./webpack.config');

module.exports = function (config) {
	config.set({
		basePath: "../../..",
		frameworks: ['mocha'],
		files: [
			'src/test/javascript/components/**/*.js',
			{
				pattern: "target/vue-gwt-tests-1.0-beta-7-SNAPSHOT/**/*.nocache.js",
				watched: false
			},
			{
				pattern: "target/vue-gwt-tests-1.0-beta-7-SNAPSHOT/**/*.cache.js",
				watched: false,
				included: false
			}
		],
		exclude: ['karma.conf*.js'],
		reporters: ['spec'],
		port: 9876,
		browsers: ['ChromeHeadless'],
		singleRun: false,
		autoWatch: true,
		preprocessors: {
			'src/test/javascript/components/**/*.js': ['webpack']
		},
		webpack: webpackConfig
	});
};