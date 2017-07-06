{% raw %}
<script src="https://unpkg.com/vue/dist/vue.js"></script>
<script>
    var basePath = "/";
    if (window.location.hostname === "axellience.github.io")
    	basePath = "/vue-gwt/";
    
    var script = document.createElement("script");
    script.src = basePath + "dependencies/scripts/VueGwtExamples.nocache.js";
    document.head.appendChild(script);
</script>
{% endraw %}