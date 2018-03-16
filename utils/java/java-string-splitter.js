const JAVA_STRING_LIMIT = 65535;

class JavaStringSplitter {
	splitStringForJava(data, stringField) {
		data = data.replace(/\\/g, "\\\\").replace(/\n/g, "\\n").replace(/"/g, "\\\"");

		if (data.length < JAVA_STRING_LIMIT) {
			return `${stringField} = "${data}";`;
		}

		let result = "StringBuilder builder = new StringBuilder();\n";
		while (data.length > JAVA_STRING_LIMIT) {
			result += `builder.append("${data.substring(0, JAVA_STRING_LIMIT)}");\n`;
			data = data.substring(JAVA_STRING_LIMIT);
		}
		result += `builder.append("${data}");\n`;
		result += `${stringField} = builder.toString();`;

		return result;
	}
}

module.exports = new JavaStringSplitter();