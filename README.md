# panda.android
## 目标
提供一个完善的Android App应用开发库。其特质为 提升开发效率、缩短开发工期、降低维护成本。

## 引入方式
1. 在项目的`build.gradle`中添加maven url。
```gradle

buildscript {
    repositories {
        jcenter()
        mavenCentral()
    }
    dependencies {
        classpath 'com.jakewharton:butterknife-gradle-plugin:8.5.1'
    }
}

allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
        // 这句是重点，重点，重点
        maven { url "https://github.com/tianci-panda/mvn-repo/raw/master/" }
    }
}
```

2. 在应用模块的`build.gradle`中添加库的引用
```gradle
apply plugin: 'com.jakewharton.butterknife'

dependencies {
    api 'panda.android:lib:0.1.<具体版本号>@aar'
}
```

详情参考：[[Android流水化开发]工序1：初始化项目框架](https://www.jianshu.com/p/1d0afecab1fb)


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
- clone **mvn-repo** 到本机。
- 找到PandaAndroidLib下的gradle.properties，进行发布配置。
```
# **mvn-repo**的本机目录
aar.deployPath=/Users/shitianci/work/Lab/mvn-repo
# VersionCode的格式为 发布当天的yyyyMMDD+自增长的两位数
VERSION_CODE=2016032102
# VersionName的格式为 大版本.小版本.VersionCode （VersionCode会自动拼接上去）
VERSION_NAME=0.1
```
- 执行`./gradlew :PandaAndroidLib:uploadArchives` windsws执行`gradlew :PandaAndroidLib:uploadArchives`
- 将生成文件同步到**mvn-repo**即可。


## 分支管理
- 主分支master：存放release的版本。每个版本都对应一个tag，并在[mvn-repo](https://github.com/tianci-panda/mvn-repo/)发布。
- 开发分支dev：日常开发活动分支。可以用来生成代码的最新隔夜版本（nightly）
- 临时性分支：用于应对一些特定目的的版本开发。属于临时性需要，使用完以后，应该删除，使得代码库的常设分支始终只有Master和Develop。
	- 功能（feature）分支
	- 预发布（release）分支
	- 修补bug（fixbug）分支

>具体理解参考[阮一峰：Git分支管理策略](http://blog.jobbole.com/23398/)


## 如何初始化一个基于PandaAndroidLib的项目
参考：[[Android流水化开发]工序1：初始化项目框架](http://www.jianshu.com/p/1d0afecab1fb)


## 主要更新日志
### 2016年
总共更新17次。核心内容沉淀在[孚睿科技·ANDROID知识总结](http://www.jianshu.com/collection/cf785fcdcaea)中，支持 海南3G v3.1；机务段v1.3；深海电商；成长记忆；借易贷{1.0、2.0、3.0}、ACCA、时间农夫、蜘蛛社群 等8款产品。

主要更新内容为：
1. 建立[流水化开发](http://www.jianshu.com/p/3d2ac2818136)的理念，持续积累。
2. 用maven构建产物，方便Gradle方式引用库。
3. 基于[CommonPullToRefresh](https://github.com/Chanven/CommonPullToRefresh)封装出简易的下拉刷新框架。
4. 基于[litehttp](https://github.com/litesuits/android-lite-http)封装BaseRepositoryCollection的方法，一行访问网络，获取常规的json和file数据。
5. 将网络接口错误码处理移动到BaseApp里面，统一处理。
