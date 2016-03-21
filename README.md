# panda.android
## 目标
提供一个完善的Android App应用开发库。其特质为 提升开发效率、缩短开发工期、降低维护成本。

## 引入方式
1. 在项目的`build.gradle`中添加maven url。
```gradle
allprojects {
    repositories {
        jcenter()
        // 这句是重点，重点，重点
        maven { url "https://github.com/tianci-panda/mvn-repo/raw/master/" }
    }
}
```

2. 在应用模块的`build.gradle`中添加库的引用
```gradle
    compile 'panda.android:lib:0.1.2016032102@aar'
    compile 'com.android.support:support-v4:22.+'
    compile 'com.android.support:appcompat-v7:22.+'
    compile 'com.jakewharton:butterknife:7.0.1'
    compile 'de.greenrobot:eventbus:2.4.0'
    compile 'com.google.code.gson:gson:2.2.4'
    compile 'com.readystatesoftware.systembartint:systembartint:1.0.3'
    compile 'com.umeng.analytics:analytics:latest.integration'
    compile 'in.srain.cube:ultra-ptr:1.0.11'
    compile('com.weiwangcn.betterspinner:library:1.1.0') {
        exclude group: 'com.android.support', module: 'appcompat-v7'
    }
    compile 'net.danlew:android.joda:2.9.1'
```


## 知识沉淀
[财富自由·技术·Android](http://www.jianshu.com/notebooks/1357264/latest)