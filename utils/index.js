const processNashornTemplateCompiler = require('./nashorn/process-nashorn-template-compiler'),
	processVueDevRuntime = require('./vue-runtime/process-vue-dev-runtime'),
	processVueRuntime = require('./vue-runtime/process-vue-runtime'),
	fs = require('fs'),
	path = require('path');

const OUT = path.join(__dirname, 'out');
if (!fs.existsSync(OUT)){
	fs.mkdirSync(OUT);
}

processNashornTemplateCompiler();
processVueDevRuntime();
processVueRuntime();