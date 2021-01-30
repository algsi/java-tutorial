# Maven Scope

Maven的依赖项是有作用域的。

A依赖B，需要在A的pom.xml文件中添加B的坐标，添加坐标时需要指定依赖范围，依赖范围包括：

- compile：编译范围，指A在编译时依赖B，此范围为默认依赖范围。编译范围的依赖会用在编译、测试、运行，由于运行时需要所以编译范围的依赖会被打包。
- provided：provided依赖只有在当JDK或者一个容器已经提供该依赖之后才使用，provide依赖在编译和测试时需要，在运行时不需要，比如：servlet api被Tomcat容器提供。
- runtime：runtime依赖在运行和测试系统时需要，但在编译的时候不需要。比如：JDBC的驱动包。
- test：test范围依赖在编译和运行时不需要，它们只有在测试编译和测试运行时需可用，比如：junit。由于运行时不需要，所以test范围依赖不会被打包。
- system：system范围依赖与provided类似，但是你必须显示地提供一个对于本地系统中JAR文件的路径，需要指定systemPath磁盘路径，system依赖不推荐使用。

下表是一个总结：

| 依赖范围 | 对于编译classpath有效 | 对于测试classpath有效 | 对于运行时classpath有效 | 例子 |
| :------: | :-------------------: | :-------------------: | :---------------------: | :--: |
| compile  |           Y           |           Y           |            Y            |      |
|   test   |           -           |           Y           |            -            |      |
| provided |           Y           |           Y           |            -            |      |
| runtime  |           -           |           Y           |            Y            |      |
|  system  |           Y           |           Y           |            -            |      |

示例：

```xml
<!--storm-->
<dependency>
    <groupId>org.apache.storm</groupId>
    <artifactId>storm-core</artifactId>
    <version>0.9.3</version>
    <scope>provided</scope>
</dependency>
<!--storm-kafka-->
<dependency>
    <groupId>org.apache.storm</groupId>
    <artifactId>storm-kafka</artifactId>
    <version>0.9.3</version>
    <scope>compile</scope>
</dependency>
```

