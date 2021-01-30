# maven plugins: maven-assembly-plugin

在 Maven 中，主要有 3 个插件可以用来打包： 

- maven-jar-plugin：默认的打包插件，用来打普通的 project JAR 包；
- maven-shade-plugin：用来打可执行JAR包，也就是所谓的 fat JAR 包；
- maven-assembly-plugin：支持自定义的打包结构，也可以定制依赖项等。

我们日常使用的以 maven-assembly-plugin 为最多，因为项目中往往可能有很多 shell 脚本、SQL 脚本、properties 配置项及 XML 配置项等，采用 assembly 插件可以让输出的结构清晰而标准化。要使用该插件，就在项目 pom 文件中加入以下内容。

