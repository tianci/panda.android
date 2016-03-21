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

## 如何为PandaAndroidLib贡献代码

### 初级贡献者：new pull request
1. 注册git账号。
2. fork [panda.android](https://github.com/tianci/panda.android) ，下载到本地，记下本地地址，比如：`/Users/shitianci/work/Lab/panda.android`
3. 本地修改，同步到自己的repo后。创建pull request，发送给panda.android。


### 中级贡献者：Collaborators
- 将git账号发送给Panda，成为项目的Collaborators。
- 直接在**panda.android**的dev分支下开发即可。


### 高级贡献者：同步维护aar包的编译和发布
- 取得[mvn-repo](https://github.com/tianci-panda/mvn-repo/)的修改权限。
- clone **mvn-repo**到本机。
- 找到PandaAndroidLib下的gradle.properties，进行发布配置。
```
# **mvn-repo**的本机目录
aar.deployPath=/Users/shitianci/work/Lab/mvn-repo
# VersionCode的格式为 发布当天的yyyyMMDD+自增长的两位数
VERSION_CODE=2016032102
# VersionName的格式为 大版本.小版本.VersionCode
VERSION_NAME=0.1.2016032102
```
- 执行`./gradlew :PandaAndroidDemo:uploadArchives`
- 将生成文件同步到**mvn-repo**即可。
