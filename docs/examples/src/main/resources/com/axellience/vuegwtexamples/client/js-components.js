window.FullJsComponent = Vue.extend({
  render: function () {
    with (this) {
      return _c('div', [_v("I Come In Peace From the JS World.")])
    }
  }
});

window.FullJsWithMethodsComponent = Vue.extend({
  data: function () {
    return {
      value: 10
    }
  },
  methods: {
    multiplyBy2: function (value) {
      return value * 2;
    }
  },
  render: function () {
    with (this) {
      return _c('div', [_v("My Value: " + _s(value) + ". My Value x2: " +
          _s(multiplyBy2(value)))])
    }
  }
});

window.ParentJsComponent = Vue.extend({
  data: function () {
    return {
      parentMessage: "This is a message from the Parent"
    };
  },
  methods: {
    parentMultiplyBy2: function (value) {
      return value * 2;
    }
  },
  computed: {
    parentComputed: function () {
      return "Computed Message | " + this.parentMessage;
    }
  }
});

Vue.component("pass-values-js", {
  props: ["myInt",
    "myBool",
    "myDouble",
    "myFloat",
    "myLong",
    "wrappedInteger",
    "wrappedBoolean",
    "wrappedDouble",
    "wrappedFloat",
    "wrappedLong"
  ],
  render: function () {
    with (this) {
      return _c('ul', [_c('li', [_v("int: " + _s(myInt + 5) +
          " | Integer: " + _s(wrappedInteger + 5))]), _c('li', [
        _v("boolean: " + _s(!myBool) + " | Boolean: " +
            _s(!wrappedBoolean))
      ]), _c('li', [_v("double: " + _s(myDouble + 5) +
          " | Double: " + _s(wrappedDouble + 5))]), _c('li', [
        _v("long: " + _s(myLong + 5) + " | Long: " + _s(
            wrappedLong + 5))
      ]), _c('li', [_v("float: " + _s(myFloat + 0.5) +
          " | Float: " + _s(wrappedFloat + 0.5))])])
    }
  }
});