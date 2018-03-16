# Setup on Your Project

!INCLUDE "dependencies.md"

## ✅ Configure Maven

To add Vue GWT to your Maven project, simply add the following to your `pom.xml`:

```xml
<project>
    <dependencies>
        ...
        <dependency>
            <groupId>com.axellience</groupId>
            <artifactId>vue-gwt</artifactId>
            <version>1.0-beta-6</version>
        </dependency>
        <!-- Annotation Processors for Vue GWT -->
        <dependency>
            <groupId>com.axellience</groupId>
            <artifactId>vue-gwt-processors</artifactId>
            <version>1.0-beta-6</version>
			<optional>true</optional>
        </dependency>
    </dependencies>
</project>
```

<p class="warning-panel">
Starting beta-3, Vue GWT requires at least GWT 2.8.1 to work.
<p>

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
            <version>3.3</version>
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

Then add this profile, this is to expose templates to the annotation processor:

```xml
<profiles>
    <profile>
        <id>vue-gwt-resources</id>
        <activation>
            <activeByDefault>true</activeByDefault>
        </activation>
        <build>
            <resources>
                <resource>
                    <directory>src/main/java</directory>
                    <includes>
                        <include>**/*.html</include>
                    </includes>
                </resource>
            </resources>
        </build>
    </profile>
</profiles>
```

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

## ✅ Configure your IDE

### IntelliJ

You must [configure IntelliJ](ide-setup/intellij.md) to enable auto build when changing your Components Templates.

### Eclipse

You must [configure Eclipse](ide-setup/eclipse.md) to enable auto build when changing your Components Templates and Annotation Processing.

### NetBeans

[Feedback](https://gitter.im/Axellience/vue-gwt) for Vue GWT on NetBeans welcome.

## ✅ Configure Your GWT App

### Add the Module Dependency
You must add Vue GWT dependency in your module `.gwt.xml` file:

```xml
<inherits name='com.axellience.vuegwt.VueGWT'/>
```

## Init Vue GWT
In your application `EntryPoint` class, add this on top of the `onModuleLoad()` method:

```java
VueGWT.init();
```

This will inject Vue.js in your app, and init Vue GWT.

If you already have Vue.js included and you don't Vue GWT to inject it for you, you can call this instead:

```java
VueGWT.initWithoutVueLib();
```

Congratulation, you are good to go! **[Let's start using Vue GWT](introduction/README.md)**