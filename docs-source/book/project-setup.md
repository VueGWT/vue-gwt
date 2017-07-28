# Setup on Your Project

## ✅ Configure Maven

To add Vue GWT to your Maven project, simply add the following to your `pom.xml`:

```xml
<project>
    <properties>
        ...
        <vue-gwt.version>0.1-SNAPSHOT</vue-gwt.version>
    </properties>
    
    <dependencies>
        ...
        <dependency>
            <groupId>com.axellience</groupId>
            <artifactId>vue-gwt</artifactId>
            <version>${vue-gwt.version}</version>
        </dependency>
    </dependencies>
    
    <!-- For now we only have SNAPSHOT releases, so you need to add the SonaType repository to get them -->
    <repositories>
        ...
        <repository>
            <id>ossrh</id>
            <url>https://oss.sonatype.org/content/repositories/snapshots</url>
        </repository>
    </repositories>
</project>
```

## ✅ Annotation Processing Configuration

Vue GWT uses Java Annotation processing under the hood, this requires a little configuration.

First, in the `plugins` section of your Maven `pom.xml` add the following `plugin`:

```xml
<project>
    <plugins>
        ...
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
    </plugins>
</project>
```

### Eclipse
If you use Eclipse, you need the following to your `pom.xml` to enable automatic annotation processing:

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

### IntelliJ IDEA
By default IntelliJ doesn't support automatic annotation processing when the app is running.
But don't worry, enabling it is easy!

Go to `File -> Settings -> Build, Execution, Deployment -> Compiler` and enable “Make project automatically”

Open the Action window :
* Linux : `CTRL+SHIFT+A`
* MacOS : `SHIFT+COMMAND+A`
* Windows : `CTRL+ALT+SHIFT+/`

Enter `Registry...` and enable `compiler.automake.allow.when.app.running`.

## ✅ Configure `JsInterop`

Vue GWT relies heavily on GWT `JsInterop`.
It is not enabled by default in GWT 2.8, so you must enable it in your SuperDevMode and Maven configurations.

### SuperDevMode
For SuperDevMode, simply add this flag to your devMode parameters:

`-generateJsInteropExports`

### Maven
For Maven, if you use [GWT Maven Plugin](https://gwt-maven-plugin.github.io/gwt-maven-plugin/), add the following to your `pom.xml`:

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

## ✅ Configure Your GWT App

### Add the Module Dependency
You must add Vue GWT dependency in your module `.gwt.xml` file:

```xml
<inherits name='com.axellience.vuegwt.VueGwt'/>
```

## Init Vue GWT
In your application `EntryPoint` class, add this on top of the `onModuleLoad()` method:

```java
VueGWT.initWithVueLib();
```

This will inject Vue.js in your app, and init Vue GWT.

If you already have Vue.js included and you don't Vue GWT to inject it for you, you can call this instead:

```java
VueGWT.init();
```

Congratulation, you are good to go! **[Let's start using Vue GWT](introduction/README.md)**