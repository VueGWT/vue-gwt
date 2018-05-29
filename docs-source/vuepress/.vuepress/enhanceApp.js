export default ({
  Vue, // the version of Vue being used in the VuePress app
  options, // the options for the root Vue instance
  router, // the router instance for the app
  siteData // site metadata
}) => {
  const oldMounted = options.mounted;
  options.mounted = function () {
    if (oldMounted)
      oldMounted();

    const gwtScript = document.createElement("script");
    gwtScript.src = "/scripts/VueGwtExamples.nocache.js";
    document.body.appendChild(gwtScript);
  };

  router.afterHooks.push(function () {
    if (window.VueGwtExamplesService) {
      VueGwtExamplesService.initExamples();
    }
  })
}