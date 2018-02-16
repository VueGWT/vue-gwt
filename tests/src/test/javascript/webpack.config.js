module.exports = {
	output: {
		path: 'src/main/java/com/axellience/vuegwt/tests/client/components/',
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