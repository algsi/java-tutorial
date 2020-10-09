# Maven打包鲸依赖以包含进去

maven在进行项目打包（maven install）时并不会将项目依赖的包也打进去，但有时，我们可能需要将有些依赖也一同打进JAR包中去。

解决方法：使用插件maven-assembly-plugin，如下。

```xml
<!--Maven 打包集成插件-->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-assembly-plugin</artifactId>
    <executions>
        <execution>
            <id>make-assembly</id>
            <!-- bind to the packaging phase -->
            <phase>package</phase>
            <goals>
                <goal>single</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <descriptorRefs>
            <!-- 将依赖一起打包到 JAR -->
            <descriptorRef>jar-with-dependencies</descriptorRef>
        </descriptorRefs>
        <archive>
            <!--Main class in mainfest make a executable jar-->
            <manifest>
                <mainClass>com.vintage.kafkastorm.KafkaStorm</mainClass>
            </manifest>
        </archive>
    </configuration>
</plugin>
```

通过上面的配置，我们还可以指定Jar包的主类，因此将该Jar包变成一个可执行文件。

但也有时候我们只是希望将一些依赖打包进去，而另一些依赖并不打包进去。这种情况下，我们就需要在每一个依赖中指定`scope`属性，即依赖的作用域。

