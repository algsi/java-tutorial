# maven plugins: maven-compiler-plugin

maven-compiler-plugin 插件用来设置 maven 编译的 JDK 版本。maven3 默认用 JDK1.5，maven2 默认用 JDK1.3。

如果我们要修改为指定的 JDK 版本的话，在POM文件中添加该插件就行：

```xml
<build>
    <plugins>
        <!-- java编译插件，使用本项目默认的JDK版本1.8 -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.7.0</version>
            <configuration>
                <source>1.8</source> <!-- 源代码使用的JDK版本 -->
                <target>1.8</target> <!-- 需要生成的目标class文件的编译版本 -->
                <encoding>UTF-8</encoding> <!-- 字符集编码 -->
            </configuration>
        </plugin>
    </plugins>
</build>
```