{% raw %}
<script src="https://unpkg.com/vue/dist/vue.js"></script>
<script>
    var basePath = "/";
    if (window.location.hostname === "axellience.github.io")
    	basePath = "/vue-gwt/";
    
    var script = document.createElement("script");
    script.src = basePath + "dependencies/scripts/VueGwtExamples.nocache.js";
    document.head.appendChild(script);
    
    var style = document.createElement("link");
    style.rel = "stylesheet";
    style.href = basePath + "dependencies/style/custom-style.css";
    document.head.appendChild(style);
</script>
{% endraw %}