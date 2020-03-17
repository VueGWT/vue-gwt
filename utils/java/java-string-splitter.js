const JAVA_STRING_LIMIT = 65535;
const JAVA_STRING_LIMIT_CHUCK = JAVA_STRING_LIMIT * 0.5;

const escapeData = (data) => data.replace(/\\/g, "\\\\").replace(/\n/g,
    "\\n").replace(/"/g,
    "\\\"");

class JavaStringSplitter {
  splitStringForJava(data, stringField) {
    if (data.length < JAVA_STRING_LIMIT_CHUCK) {
      return `${stringField} = "${escapeData(data)}";`;
    }

    let result = "StringBuilder builder = new StringBuilder();\n";
    while (data.length > JAVA_STRING_LIMIT_CHUCK) {
      result += `builder.append("${escapeData(data.substring(0,
          JAVA_STRING_LIMIT_CHUCK))}");\n`;
      data = data.substring(JAVA_STRING_LIMIT_CHUCK);
    }
    result += `builder.append("${escapeData(data)}");\n`;
    result += `${stringField} = builder.toString();`;

    return result;
  }
}

module.exports = new JavaStringSplitter();