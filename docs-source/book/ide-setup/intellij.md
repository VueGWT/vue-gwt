# IntelliJ IDEA

## ✅ Vue GWT IntelliJ Plugin

First, download the latest release from [Vue GWT IntelliJ Plugin](https://github.com/Axellience/vue-gwt-intellij-plugin/releases).
Then install it in IntelliJ by going to `Preferences > Plugins > Install Plugin from disk...` and selecting the downloaded ZIP.

This will make IntelliJ re-process your Components templates when you save them.

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