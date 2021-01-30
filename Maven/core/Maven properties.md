# Maven properites 标签的使用

Properties are the last required piece to understand POM basics. Maven properties are value placeholders, like properties in Ant. Their values are accessible anywhere within a POM by using the notation `${X}`, where X is the property. Or they can be used by plugins as default values, for example:

```xml
<project>
  ...
  <properties>
    <maven.compiler.source>1.8</maven.compiler.source>
    <maven.compiler.target>1.8</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
  </properties>
  ...
</project>
```

They come in five different styles:

1. `env.X`:  Prefixing a variable with "env." will return the shell's environment variable. For example, `${env.PATH}` contains the PATH environment variable.

   *Note:* While environment variables themselves are case-insensitive on Windows, lookup of properties is case-sensitive. In other words, while the Windows shell returns the same value for `%PATH%` and `%Path%`, Maven distinguishes between `${env.PATH}` and `${env.Path}`. **The names of environment variables are normalized to all upper-case** for the sake of reliability.

2. `project.x`: A dot (.) notated path in the POM will contain the corresponding element's value. For example: `<project><version>1.0</version></project>` is accessible via `${project.version}`.

3. `settings.x`: A dot (.) notated path in the `settings.xml` will contain the corresponding element's value. For example: `<settings><offline>false</offline></settings>` is accessible via `${settings.offline}`.

4. Java System Properties: All properties accessible via `java.lang.System.getProperties()` are available as POM properties, such as `${java.home}`.

5. `x`: Set within a `<properties />` element in the POM. The value of `<properties><someVar>value</someVar></properties>` may be used as `${someVar}`.

## 内置变量

Maven 有一些内置变量，这些内置变量只要我们声明了，就可以被 Maven plugin 使用作为相应的默认值。我们也可以在 POM 文件中访问。

### project.x

- `${project.version}`：这可以在必须编写文字版本的位置使用，否则，特别是如果您在多模块构建模块间依赖关系。
- `${project.basedir}`：这引用了 module/project 的根文件夹（当前pom.xml文件所在的位置），还可以简化的写法：${basedir}。
- `${project.build.directory}`：这表示默认的 target 文件夹。
- `${project.build.outputDirectory}`：默认情况下表示 target/classes 文件夹。
- `${project.build.testOutputDirectory}`：这表示默认的 target/test-classes 文件夹。
- `${project.build.sourceDirectory}`：这表示默认情况下 src/main/java 文件夹。
- `${project.build.testSourceDirectory}`：这表示默认情况下 src/test/java 文件夹。
- `${project.build.finalName}`：默认情况下定义为 `${project.artifactId}-${project.version}`。
- `${project.build.sourceEncoding}`：表示项目编译源码使用的编码方式。
- `${project.reporting.outputEncoding}`：表示项目打包源码使用的编码方式。

## 自定义变量

当我们要在 POM 文件描述项目依赖其他组件的版本时，使用最多的就是在 properties 文件中自定义变量。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <developers>
        <developer>
            <id>algsi</id>
            <name>Xavier Li</name>
            <email>vintage.algs@gmail.com</email>
        </developer>
    </developers>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
        <spring.version>5.0.6.RELEASE</spring.version>
        <spring.boot.version>2.0.2.RELEASE</spring.boot.version>
        <junit.version>4.12</junit.version>
        <maven.compiler.version>3.8.0</maven.compiler.version>
        <mybatis.version>3.4.0</mybatis.version>
        <perf4j.version>0.9.16</perf4j.version>
        <curator.version>2.6.0</curator.version>
        <slf4j.version>1.7.5</slf4j.version>
        <druid.version>1.1.0</druid.version>
        <jackson-databind.version>2.9.6</jackson-databind.version>
        <mysql-connector-java.version>5.1.38</mysql-connector-java.version>
        <commons-io.version>2.4</commons-io.version>
        <log4j.version>1.2.17</log4j.version>
        <mybatis-spring.version>1.3.0</mybatis-spring.version>
        <guava.version>18.0</guava.version>
        <fastjson.version>1.2.58</fastjson.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>druid</artifactId>
                <version>${druid.version}</version>
            </dependency>
            <!-- zk -->
            <dependency>
                <groupId>org.apache.curator</groupId>
                <artifactId>curator-recipes</artifactId>
                <version>${curator.version}</version>
                <exclusions>
                    <exclusion>
                        <artifactId>log4j</artifactId>
                        <groupId>log4j</groupId>
                    </exclusion>
                </exclusions>
            </dependency>
            <dependency>
                <groupId>commons-io</groupId>
                <artifactId>commons-io</artifactId>
                <version>${commons-io.version}</version>
            </dependency>
            <dependency>
                <groupId>org.mybatis</groupId>
                <artifactId>mybatis</artifactId>
                <version>${mybatis.version}</version>
            </dependency>
            <dependency>
                <groupId>org.mybatis</groupId>
                <artifactId>mybatis-spring</artifactId>
                <version>${mybatis-spring.version}</version>
            </dependency>
            <!-- spring -->
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-web</artifactId>
                <version>${spring.boot.version}</version>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-freemarker</artifactId>
                <version>${spring.boot.version}</version>
                <exclusions>
                    <exclusion>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-test</artifactId>
                <version>${spring.boot.version}</version>
                <exclusions>
                    <exclusion>
                        <groupId>org.springframework.boot</groupId>
                        <artifactId>spring-boot-starter</artifactId>
                    </exclusion>
                </exclusions>
            </dependency>
            <!-- logging -->
            <dependency>
                <groupId>org.slf4j</groupId>
                <artifactId>slf4j-api</artifactId>
                <version>${slf4j.version}</version>
            </dependency>
            <dependency>
                <groupId>log4j</groupId>
                <artifactId>log4j</artifactId>
                <version>${log4j.version}</version>
            </dependency>
            <!-- slf4j-log4j12 compile dependencies log4j:log4j and org.slf4j:slf4j-api -->
            <dependency>
                <groupId>org.slf4j</groupId>
                <artifactId>slf4j-log4j12</artifactId>
                <version>${slf4j.version}</version>
            </dependency>
            <!-- test -->
            <dependency>
                <groupId>junit</groupId>
                <artifactId>junit</artifactId>
                <version>${junit.version}</version>
            </dependency>

            <!-- mysql connector -->
            <dependency>
                <groupId>mysql</groupId>
                <artifactId>mysql-connector-java</artifactId>
                <version>${mysql-connector-java.version}</version>
            </dependency>

            <dependency>
                <groupId>com.google.guava</groupId>
                <artifactId>guava</artifactId>
                <version>${guava.version}</version>
            </dependency>

            <dependency>
                <groupId>com.alibaba</groupId>
                <artifactId>fastjson</artifactId>
                <version>${fastjson.version}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <finalName>leaf</finalName>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>${maven.compiler.version}</version>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>

</project>
```

## Reference

- [POM Reference](http://maven.apache.org/pom.html#Properties)
