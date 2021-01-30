# maven command

## package

```bash
(base) ➜  leaf-core mvn clean package -DskipTests
[INFO] Scanning for projects...
[INFO]
[INFO] ---------------------< com.xavier.leaf:leaf-core >----------------------
[INFO] Building leaf-core 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO]
[INFO] --- maven-clean-plugin:2.5:clean (default-clean) @ leaf-core ---
[INFO] Deleting /Users/xavier/Java/normal-workspace/leaf/leaf-core/target
[INFO]
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ leaf-core ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Copying 1 resource
[INFO]
[INFO] --- maven-compiler-plugin:3.8.0:compile (default-compile) @ leaf-core ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 17 source files to /Users/xavier/Java/normal-workspace/leaf/leaf-core/target/classes
[INFO]
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ leaf-core ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Copying 2 resources
[INFO]
[INFO] --- maven-compiler-plugin:3.8.0:testCompile (default-testCompile) @ leaf-core ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 1 source file to /Users/xavier/Java/normal-workspace/leaf/leaf-core/target/test-classes
[INFO]
[INFO] --- maven-surefire-plugin:2.12.4:test (default-test) @ leaf-core ---
[INFO] Tests are skipped.
[INFO]
[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ leaf-core ---
[INFO] Building jar: /Users/xavier/Java/normal-workspace/leaf/leaf-core/target/leaf.jar
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  2.580 s
[INFO] Finished at: 2020-11-16T22:07:16+08:00
[INFO] ------------------------------------------------------------------------
```

mvn clean package:

1. clean: maven-clean-plugin:2.5:clean (default-clean)
2. resources: maven-resources-plugin:2.6:resources (default-resources)
3. compile: maven-compiler-plugin:3.8.0:compile (default-compile)
4. testResources: maven-resources-plugin:2.6:testResources (default-testResources)
5. testCompile: maven-compiler-plugin:3.8.0:testCompile (default-testCompile)
6. test: maven-surefire-plugin:2.12.4:test (default-test)
7. jar（打包）: maven-jar-plugin:2.4:jar (default-jar)

## install

```bash
(base) ➜  leaf-core mvn clean install -DskipTests
[INFO] Scanning for projects...
[INFO] 
[INFO] ---------------------< com.xavier.leaf:leaf-core >----------------------
[INFO] Building leaf-core 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
Downloading from aliyunmaven: https://maven.aliyun.com/repository/public/org/apache/maven/plugins/maven-install-plugin/2.4/maven-install-plugin-2.4.pom
Downloaded from aliyunmaven: https://maven.aliyun.com/repository/public/org/apache/maven/plugins/maven-install-plugin/2.4/maven-install-plugin-2.4.pom (0 B at 0 B/s)
Downloading from aliyunmaven: https://maven.aliyun.com/repository/public/org/apache/maven/plugins/maven-install-plugin/2.4/maven-install-plugin-2.4.jar
Downloaded from aliyunmaven: https://maven.aliyun.com/repository/public/org/apache/maven/plugins/maven-install-plugin/2.4/maven-install-plugin-2.4.jar (0 B at 0 B/s)
[INFO]
[INFO] --- maven-clean-plugin:2.5:clean (default-clean) @ leaf-core ---
[INFO] Deleting /Users/xavier/Java/normal-workspace/leaf/leaf-core/target
[INFO]
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ leaf-core ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Copying 1 resource
[INFO]
[INFO] --- maven-compiler-plugin:3.8.0:compile (default-compile) @ leaf-core ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 17 source files to /Users/xavier/Java/normal-workspace/leaf/leaf-core/target/classes
[INFO]
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ leaf-core ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Copying 2 resources
[INFO]
[INFO] --- maven-compiler-plugin:3.8.0:testCompile (default-testCompile) @ leaf-core ---
[INFO] Changes detected - recompiling the module!
[INFO] Compiling 1 source file to /Users/xavier/Java/normal-workspace/leaf/leaf-core/target/test-classes
[INFO]
[INFO] --- maven-surefire-plugin:2.12.4:test (default-test) @ leaf-core ---
[INFO] Tests are skipped.
[INFO]
[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ leaf-core ---
[INFO] Building jar: /Users/xavier/Java/normal-workspace/leaf/leaf-core/target/leaf.jar
[INFO]
[INFO] --- maven-install-plugin:2.4:install (default-install) @ leaf-core ---
Downloading from aliyunmaven: https://maven.aliyun.com/repository/public/org/codehaus/plexus/plexus-utils/3.0.5/plexus-utils-3.0.5.pom
Downloaded from aliyunmaven: https://maven.aliyun.com/repository/public/org/codehaus/plexus/plexus-utils/3.0.5/plexus-utils-3.0.5.pom (0 B at 0 B/s)
Downloading from aliyunmaven: https://maven.aliyun.com/repository/public/org/codehaus/plexus/plexus/3.1/plexus-3.1.pom
Downloaded from aliyunmaven: https://maven.aliyun.com/repository/public/org/codehaus/plexus/plexus/3.1/plexus-3.1.pom (0 B at 0 B/s)
Downloading from aliyunmaven: https://maven.aliyun.com/repository/public/org/codehaus/plexus/plexus-digest/1.0/plexus-digest-1.0.pom
Downloaded from aliyunmaven: https://maven.aliyun.com/repository/public/org/codehaus/plexus/plexus-digest/1.0/plexus-digest-1.0.pom (0 B at 0 B/s)
Downloading from aliyunmaven: https://maven.aliyun.com/repository/public/org/codehaus/plexus/plexus-components/1.1.7/plexus-components-1.1.7.pom
Downloaded from aliyunmaven: https://maven.aliyun.com/repository/public/org/codehaus/plexus/plexus-components/1.1.7/plexus-components-1.1.7.pom (0 B at 0 B/s)
Downloading from aliyunmaven: https://maven.aliyun.com/repository/public/org/codehaus/plexus/plexus/1.0.8/plexus-1.0.8.pom
Downloaded from aliyunmaven: https://maven.aliyun.com/repository/public/org/codehaus/plexus/plexus/1.0.8/plexus-1.0.8.pom (0 B at 0 B/s)
Downloading from aliyunmaven: https://maven.aliyun.com/repository/public/org/codehaus/plexus/plexus-container-default/1.0-alpha-8/plexus-container-default-1.0-alpha-8.pom
Downloaded from aliyunmaven: https://maven.aliyun.com/repository/public/org/codehaus/plexus/plexus-container-default/1.0-alpha-8/plexus-container-default-1.0-alpha-8.pom (0 B at 0 B/s)
Downloading from aliyunmaven: https://maven.aliyun.com/repository/public/org/codehaus/plexus/plexus-utils/3.0.5/plexus-utils-3.0.5.jar
Downloading from aliyunmaven: https://maven.aliyun.com/repository/public/org/codehaus/plexus/plexus-digest/1.0/plexus-digest-1.0.jar
Downloaded from aliyunmaven: https://maven.aliyun.com/repository/public/org/codehaus/plexus/plexus-utils/3.0.5/plexus-utils-3.0.5.jar (0 B at 0 B/s)
Downloaded from aliyunmaven: https://maven.aliyun.com/repository/public/org/codehaus/plexus/plexus-digest/1.0/plexus-digest-1.0.jar (0 B at 0 B/s)
[INFO] Installing /Users/xavier/Java/normal-workspace/leaf/leaf-core/target/leaf.jar to /Users/xavier/Java/maven-repo/common/com/xavier/leaf/leaf-core/1.0-SNAPSHOT/leaf-core-1.0-SNAPSHOT.jar
[INFO] Installing /Users/xavier/Java/normal-workspace/leaf/leaf-core/pom.xml to /Users/xavier/Java/maven-repo/common/com/xavier/leaf/leaf-core/1.0-SNAPSHOT/leaf-core-1.0-SNAPSHOT.pom
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  2.677 s
[INFO] Finished at: 2020-11-16T22:22:50+08:00
[INFO] ------------------------------------------------------------------------
```

mvn clean package依次执行了clean、resources、compile、testResources、testCompile、test、jar(打包)等７个阶段。
mvn clean install依次执行了clean、resources、compile、testResources、testCompile、test、jar(打包)、install等8个阶段。
mvn clean deploy依次执行了clean、resources、compile、testResources、testCompile、test、jar(打包)、install、deploy等９个阶段。

由上面的分析可知主要区别如下，

- package 命令完成了项目编译、单元测试、打包功能，但没有把打好的可执行Jar包（War包或其它形式的包）布署到本地 maven 仓库和远程 maven 私服仓库
- install 命令完成了项目编译、单元测试、打包功能，同时把打好的可执行Jar包（War包或其它形式的包）布署到本地 maven 仓库，但没有布署到远程 maven 私服仓库
- deploy 命令完成了项目编译、单元测试、打包功能，同时把打好的可执行Jar包（War包或其它形式的包）布署到本地 maven 仓库和远程 maven 私服仓库。
