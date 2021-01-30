# Maven 基础

## 1. 介绍

### 1.1. 什么是项目构建

项目构建是一个项目从编写源码到编译、测试、运行、打包、部署、运行到过程。

#### 1.1.1. Maven 项目构建过程

Maven 将项目构建的过程进行标准化，每个阶段使用一个命令完成。

1. 清理（clean）
2. 编译（compile）
3. 测试（test）
4. 报告（report）
5. 打包（package）
6. 部署（deploy）

## 2. Maven 项目工程目录约定

使用 Maven 创建的工程我们称它为 Maven 工程，Maven 工程具有一定的目录规范，如下：

- `src/main/java`：存放项目的 `.java` 文件；
- `src/main/resources`：存放项目资源文件，如 Spring 的配置文件；
- `src/test/java`：存放所有单元测试 `.java` 文件，如 JUnit 测试类；
- `src/test/resources`：存放测试资源文件；
- target：项目输出位置，编译后的 class 文件会输出到此目录；
- pom.xml：Maven 项目的核心配置文件。

