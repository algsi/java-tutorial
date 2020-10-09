# 更改Maven默认的Java版本

Maven默认的Java编译版本是1.5，如果我们要修改为我们按照的JDK版本的话，在POM文件中添加以下Maven插件就行：

```xml
<build>

    <plugins>
        <!-- java编译插件，使用本项目默认的JDK版本1.8 -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.7.0</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
                <encoding>UTF-8</encoding>
            </configuration>
        </plugin>
    </plugins>

</build>
```