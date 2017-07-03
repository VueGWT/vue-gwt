# Setup on your project

## ✅ Get Vue GWT

Vue GWT uses Maven.
For now there is no Maven repository.

You must clone the source on your computer and mvn install them:

```bash
git clone https://github.com/Axellience/vue-gwt.git
cd vue-gwt
mvn clean install
```

## ✅ Configure Maven

### Add the dependency
Add Vue GWT to your project `pom.xml`:

```xml
<properties>
    <vue-gwt.version>1.2-SNAPSHOT</vue-gwt.version>
</properties>
<dependency>
    <groupId>com.axellience</groupId>
    <artifactId>vue-gwt</artifactId>
    <version>${vue-gwt.version}</version>
</dependency>
```

### Add Annotation processing configuration
In the `plugins` section:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <!-- version is important to have java annotation processing correctly handled -->
    <version>3.3</version><!--$NO-MVN-MAN-VER$-->
    <configuration>
        <compilerArgument>-parameters</compilerArgument>
        <testCompilerArgument>-parameters</testCompilerArgument>
        <useIncrementalCompilation>false</useIncrementalCompilation>
        <source>1.8</source>
        <target>1.8</target>
    </configuration>
</plugin>
```

This is useful for Eclipse:
```xml
<pluginManagement>
    <plugins>
        <!--This plugin's configuration is used to store Eclipse m2e settings only. It has no influence on the Maven build itself.-->
        <plugin>
            <groupId>org.eclipse.m2e</groupId>
            <artifactId>lifecycle-mapping</artifactId>
            <version>1.0.0</version>
            <configuration>
                <lifecycleMappingMetadata>
                    <pluginExecutions>
                        <pluginExecution>
                            <pluginExecutionFilter>
                                <groupId>
                                    org.codehaus.mojo
                                </groupId>
                                <artifactId>
                                    gwt-maven-plugin
                                </artifactId>
                                <versionRange>
                                    [2.7.0,)
                                </versionRange>
                                <goals>
                                    <goal>compile</goal>
                                </goals>
                            </pluginExecutionFilter>
                            <action>
                                <ignore></ignore>
                            </action>
                        </pluginExecution>
                    </pluginExecutions>
                </lifecycleMappingMetadata>
            </configuration>
        </plugin>
    </plugins>
</pluginManagement>
```

## ✅ Configure JsInterop
Vue GWT relies heavily on JsInterop.
You must enable it in SuperDevMode and Maven.

### SuperDevMode
For SuperDevMode, simply add this flag to your devMode parameters:

`-generateJsInteropExports`

### Maven
For Maven, if you use [GWT Maven Plugin](https://gwt-maven-plugin.github.io/gwt-maven-plugin/), add the following in your `pom.xml`:

```xml
<plugins>
    <!-- Mojo's Maven Plugin for GWT -->
    <plugin>
        ...
        <configuration>
            ...
            <generateJsInteropExports>true</generateJsInteropExports>
        </configuration>
    </plugin>
</plugins>
```

## ✅ Add Vue.js library to your index.html

In your GWT app index.html add the following tag in the &lt;head&gt; section:

```html
<!-- Vue JS Framework -->
<script src="https://unpkg.com/vue/dist/vue.js"></script>
```

## IDE Configuration

### Eclipse
Just import your project as a Maven project.

### IntelliJ IDEA
By default IntelliJ doesn't support automatic compilation on file change.
But don't worry, enabling it is easy!

Go to `File -> Settings -> Build, Execution, Deployment -> Compiler` and enable “Make project automatically”

Open the Action window :
* Linux : `CTRL+SHIFT+A`
* Mac OSX : `SHIFT+COMMAND+A`
* Windows : `CTRL+ALT+SHIFT+/`

Enter `Registry...` and enable `compiler.automake.allow.when.app.running`

You are good to go!