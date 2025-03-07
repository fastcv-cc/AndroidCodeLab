# AndroidCodeLab
此项目用于记录多年开发过程中的各种功能代码，包括但不限于功能开发测试、版本适配测试、依赖库封装测试等等。

这里装个逼，作为一个19年开始安卓开发的UI仔，也是工作了这么多年，前前后后也是接触了不少项目。小公司小项目干过，Framework外包也干过，面对全球的项目也干过，干了这么久，说实话，安卓不过如此（反正画画UI，解解bug）。

但是，时间久了也开始腻了，于是琢磨来、琢磨去，啥也没琢磨出来，笔记写了不少，也收藏了不少。前前后后也新建了好几个这样的工程。但是每次做到后面就发现，没意思了。用到的时候又要去重新找，哈哈哈。

最近突然灵光一现，好像可以把它们揉在一起，重新来一遍，把笔记-收藏-demo啥的整理一遍，再结合官网文档，做个大补丸。

然后，以此模式，开展我的大前端之路。

别问为啥要玩大前端，不玩不行了，这几年太卷了！！！，我只想活命，我有错吗？



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



# 项目内容



## libs模块

依赖库集合。自己实现/模仿（借鉴）的库的实现都在这里。

| 库               | 说明                     | 说明文档                                                     |
| ---------------- | ------------------------ | ------------------------------------------------------------ |
| Logger           | 日志打印库。             | [Logger库说明文档](./doc/libs/logger/Logger库说明文档.md)    |
| Local_data_store | 轻量级本地数据持久化库。 | [LocalDataStore库说明文档](./doc/libs/local_data_store/LocalDataStore库说明文档.md) |
|                  |                          |                                                              |





# 问题收集

整个项目维护开发遇到的问题分类收集。

| 问题分类 | 文档地址                                       |
| -------- | ---------------------------------------------- |
| 编译异常 | [编译异常](./doc/gradle/agp/编译异常问题集.md) |
|          |                                                |
|          |                                                |

