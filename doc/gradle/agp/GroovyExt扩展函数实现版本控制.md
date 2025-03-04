# Groovy ext扩展函数

我们在使用Groovy语言构建项目的时候，抽取projectConfigure.gradle作为全局的变量控制，使用ext扩展函数来统一配置依赖，如下：

```groovy
ext {
    androidVersion = [
            compileSdk : 33,
            minSdk     : 19,
            targetSdk  : 33,
            versionCode: 10000,
            versionName: "1.0.0",
    ]

    plugin = [
    ]

    dependency = [
            //测试库
            junit                   : "junit:junit:4.13.2",
            androidxJunit           : "androidx.test.ext:junit:1.1.5",
            androidxEspressoCore    : "androidx.test.espresso:espresso-core:3.5.1",
            //官方依赖库
            androidxCoreKtx         : "androidx.core:core-ktx:1.7.0",
            androidxAppcompat       : "androidx.appcompat:appcompat:1.4.1",
            material                : "com.google.android.material:material:1.4.0",
            androidxActivity        : "androidx.activity:activity:1.4.0",
            androidxConstraintlayout: "androidx.constraintlayout:constraintlayout:2.1.1"
    ]
}
```



依赖写完之后，在根路径下的build.gradle文件的内容顶部添加

```
apply from:"projectConfigure.gradle"
```

然后在子工程里面就可以这样使用了

```groovy
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
}


android {
    compileSdk rootProject.ext.androidVersion.compileSdk

    defaultConfig {
        applicationId "cc.fastcv.codelab"
        minSdk rootProject.ext.androidVersion.minSdk
        targetSdk rootProject.ext.androidVersion.targetSdk
        versionCode rootProject.ext.androidVersion.versionCode
        versionName rootProject.ext.androidVersion.versionName

        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
    }

   ...
}

dependencies {
    implementation rootProject.ext.dependency.androidxCoreKtx
    implementation rootProject.ext.dependency.androidxAppcompat
    implementation rootProject.ext.dependency.material
    implementation rootProject.ext.dependency.androidxActivity
    implementation rootProject.ext.dependency.androidxConstraintlayout
}
```

此方式可以做到版本依赖，但是最大的缺点就是无法跟踪代码，想要找到上面示例代码中的rootProject.ext.dependency.androidxCoreKtx这个依赖，需要手动切到config.gradle去搜索查找，可读性很差。