# Maven 项目子模块执行顺序问题

项目模块层次：

```language
- tinyid
		- tinyid-base
		- tinyid-client
		- tinyid-server
```

tinyid 是所有模块的 root 模块，tinyid-base 是公共模块，被其他模块依赖。

maven install 或 package 时 ，执行警告报错：

```language
[INFO] ------------------< com.xavier.tinyid:tinyid-client >-------------------
[INFO] Building tinyid-client 1.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[WARNING] The POM for com.xavier.tinyid:tinyid-base:jar:1.0-SNAPSHOT is missing, no dependency information available
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  0.202 s
[INFO] Finished at: 2021-01-14T20:47:13+08:00
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal on project tinyid-client: Could not resolve dependencies for project com.xavier.tinyid:tinyid-client:jar:1.0-SNAPSHOT: Could not find artifact com.xavier.tinyid:tinyid-base:jar:1.0-SNAPSHOT -> [Help 1]
```

这是因为 maven 项目子模块在执行安装打包等相关操作的时候，没有先执行公共模块。即项目有一个公共模块 tinyid-base；当前需要执行的模块为 tinyid-client，那么应该先安装或者打包 tinyid-base，再执行 tinyid-client 相关的操作。

## 解决方案

对父模块（root）进行打包安装操作，会同时对它下面的所有子模块进行打包安装。针对上面的项目，父模块打包安装之后，在本地仓库可以找到三个模块打包安装之后的文件。

