module.exports = {
	output: {
		path: 'src/test/javascript/components/',
		filename: '[name].js'
	},
	resolve: {
		extensions: ['.js']
	},
	module: {
		rules: [
			{
				test: /\.js$/,
				loader: 'babel-loader',
				exclude: /node_modules/
			}
		]
	}
};