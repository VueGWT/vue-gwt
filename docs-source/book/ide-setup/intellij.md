# IntelliJ IDEA

## ✅ Vue GWT IntelliJ Plugin

First, install the [IntelliJ Vue GWT Plugin](https://plugins.jetbrains.com/plugin/10441-vue-gwt).
This will make IntelliJ re-process your Components templates when you save them.

You can find it in your IDE in `Preferences > Plugins > Browse Repositories... > Vue GWT`

## ✅ Enable Auto Build

By default IntelliJ doesn't support automatic annotation processing when the app is running.
But don't worry, enabling it is easy!

Go to `File -> Settings -> Build, Execution, Deployment -> Compiler` and enable “Make project automatically”

Open the Action window :
* Linux : `CTRL+SHIFT+A`
* MacOS : `SHIFT+COMMAND+A`
* Windows : `CTRL+ALT+SHIFT+/`

Enter `Registry...` and enable `compiler.automake.allow.when.app.running`.

![IntelliJ enable auto build when running](https://axellience.github.io/vue-gwt/resources/images/intellij-enable-autobuild-running.png)