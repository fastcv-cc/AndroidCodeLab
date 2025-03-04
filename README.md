# AndroidCodeLab
此项目用于记录多年开发过程中的各种功能代码，包括但不限于功能开发测试、版本适配测试、依赖库封装测试等等。



# 项目配置

[AGP、KGP及相关版本说明](./doc/gradle/agp/AGP-KGP及相关版本说明.md)

## 2025/03/04
新建此项目，版本配置为：

- Gradle版本为7.5-all

```
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-7.5-all.zip
```

- Java版本使用的Java11
- AGP7.4.2
- KGP版本为1.6.21



依赖与应用版本控制使用的是[GroovyExt扩展函数](./doc/gradle/agp/GroovyExt扩展函数实现版本控制.md)的方式进行管理的。

