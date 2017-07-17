{% raw %}
<script src="https://unpkg.com/vue/dist/vue.js"></script>
<script>
    var basePath = "/";
    if (window.location.hostname === "axellience.github.io")
    	basePath = "/vue-gwt/";
    
    var script = document.createElement("script");
    script.src = basePath + "resources/scripts/VueGwtExamples.nocache.js";
    document.head.appendChild(script);
</script>
<script>
	window.FullJsComponent = Vue.extend({
		template: "<div>We Come In Peace From the JS World.</div>"
	});
	window.FullJsWithMethodsComponent = Vue.extend({
		template: "<div>My Value: {{ value }}. My Value x2: {{ multiplyBy2(value) }}</div>",
        data: function () {
            return {
            	value: 10
            }
		},
        methods: {
			multiplyBy2: function (value) {
                return value * 2;
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
</script>
{% endraw %}