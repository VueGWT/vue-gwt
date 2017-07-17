# Setup on Your Project

## ✅ Configure Maven

Vue GWT uses Maven.

### Add the Dependency
Add Vue GWT to your project `pom.xml`:

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

### Add Annotation Processing Configuration
In the `plugins` section of your `pom.xml`:
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

## ✅ Configure `JsInterop`
Vue GWT relies heavily on `JsInterop`.
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

## ✅ Add Vue.js Library to Your Index.html

In your GWT app index.html add the following tag in the &lt;head&gt; section:

```html
<!-- Vue JS Framework -->
<script src="https://unpkg.com/vue/dist/vue.js"></script>
```

## ✅ IDE Configuration

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

You are good to go! **[Let's start using Vue GWT](introduction/README.md)**