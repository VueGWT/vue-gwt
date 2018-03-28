# Eclipse

## ✅ Vue GWT Eclipse Plugin

First, install the [Vue GWT Eclipse Plugin](https://marketplace.eclipse.org/content/vue-gwt).
This will make Eclipse re-process your Components templates when you save them.

To install, just drag and drop the following button on your Eclipse window:

[![Drag to your running Eclipse* workspace. *Requires Eclipse Marketplace Client](https://marketplace.eclipse.org/sites/all/themes/solstice/public/images/marketplace/btn-install.png)](http://marketplace.eclipse.org/marketplace-client-intro?mpc_install=3923910 "Drag to your running Eclipse* workspace. *Requires Eclipse Marketplace Client")

## ✅ Make Eclipse find your Templates

Because of the `resources` block in the `pom.xml`, Eclipse automatically adds a rule to ignore files in `src/main/java`.
This will throw an error that your templates cannot be found.

To fix this, the easiest way is placing the `resources` block from your pom.xml in a profile that will only be enabled when not in Eclipse.

So remove this block that you added in the project setup:
```xml
<resources>
    <resource>
        <directory>src/main/java</directory>
    </resource>
</resources>
```

And add this block instead:

```xml
<profiles>
	<profile>
		<id>vue-gwt-resources</id>
		<activation>
			<property>
				<name>!m2e.version</name>
			</property>
		</activation>
		<build>
			<resources>
				<resource>
					<directory>src/main/java</directory>
				</resource>
			</resources>
		</build>
	</profile>
</profiles>
```

<p class="warning-panel">
Adding this profile will disable your default Maven profiles if you have any.
So you will have to add -PmyDefaultProfile when compiling in command line.
<p>

## ✅ Annotation Processing

We then need to enable Annotation Processing on Eclipse.
First install the `m2e-apt` plugin:
[https://marketplace.eclipse.org/content/m2e-apt](https://marketplace.eclipse.org/content/m2e-apt).

Then you need to enable annotation processing for your project in the `m2e-apt` project settings:

![Enabling Annotation processing in Eclipse](https://axellience.github.io/vue-gwt/resources/images/eclipse-enable-annotation-processing.png)


