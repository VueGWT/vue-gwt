/**
 * Process the Vue.js dev runtime and output lines of java
 * to be put in VueLibDevInjector
 */

const fs = require('fs');

const JAVA_STRING_LIMIT = 65535;

fs.readFile('vue.runtime.js', 'utf8', function(err, data) {
    if (err) throw err;
    data = data.replace(/\\/g, "\\\\").replace(/\n/g, "\\n").replace(/"/g, "\\\"");

    console.log(`StringBuilder builder = new StringBuilder();`)
    while (data.length > JAVA_STRING_LIMIT) {
        addToStringBuilder(data.substring(0, JAVA_STRING_LIMIT));
        data = data.substring(JAVA_STRING_LIMIT);
    }
    addToStringBuilder(data);
});

function addToStringBuilder(string) {
    console.log(`builder.append("${string}");`);
}
