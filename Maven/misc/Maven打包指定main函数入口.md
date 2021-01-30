# Maven打包指定main函数入口

如果我们在用Maven打 Jar 包的时候指定了 main class，那么这个 jar 包就成为了一个可执行的 Jar 包。

要做到指定 main 函数入口，需要在 pom.xml 中添加以下插件：

```xml
<build>
    <plugins>
        <plugin>
            <artifactId>maven-assembly-plugin</artifactId>
            <configuration>
                <appendAssemblyId>false</appendAssemblyId>
                <descriptorRefs>
                    <descriptorRef>jar-with-dependencies</descriptorRef>
                </descriptorRefs>
                <archive>
                    <manifest>
                        <!-- specify the main class -->
                        <mainClass>com.xavier.WordCountDriver</mainClass>
                    </manifest>
                </archive>
            </configuration>
            <executions>
                <execution>
                    <id>make-assembly</id>
                    <phase>package</phase>
                    <goals>
                        <goal>assembly</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

然后执行命令编译：`mvn clean package`

打包之后，在 MANIFEST.MF 文件中看到 `Main-Class: com.xavier.WordCountDriver` 你的 main class 就说明成功了。

运行：`java -jar XXX.jar`

