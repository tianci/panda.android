# panda.android
## 目标
提供一个完善的Android App应用开发库。其特质为 提升开发效率、缩短开发工期、降低维护成本。

## 引入方式
1. 在项目的`build.gradle`中添加maven url。
```gradle

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:2.1.0'
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.7'
        classpath 'com.oguzbabaoglu:butterfork-plugin:1.0.0'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
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

apply plugin: 'com.neenbedankt.android-apt'
apply plugin: 'com.oguzbabaoglu.butterfork-plugin'

dependencies {
    compile 'panda.android:lib:0.1.+@aar'
    compile 'com.android.support:support-v4:23.+'
    compile 'com.android.support:appcompat-v7:23.+'
    compile 'com.jakewharton:butterknife:7.0.1'
    compile 'de.greenrobot:eventbus:2.4.0'
    compile 'com.google.code.gson:gson:2.2.4'
    compile 'com.readystatesoftware.systembartint:systembartint:1.0.3'
    compile 'com.umeng.analytics:analytics:latest.integration'
    compile('com.weiwangcn.betterspinner:library:1.1.0') {
        exclude group: 'com.android.support', module: 'appcompat-v7'
    }
    compile 'net.danlew:android.joda:2.9.1'
    compile 'com.oguzbabaoglu:butterfork-binder:1.0.0'
    apt 'com.oguzbabaoglu:butterfork-compiler:1.0.0'
    compile 'com.github.chrisbanes:PhotoView:1.2.6'

    compile 'com.nineoldandroids:library:2.4.0'
    compile 'cn.bingoogolapple:bga-refreshlayout:1.1.0@aar'
    compile 'com.android.support:recyclerview-v7:23.0.+'
}
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
1. 按照AndroidStudio创建项目

2. 修改全局gradle属性配置文件——**./gradle.properties**
```bash
ANDROID_BUILD_MIN_SDK_VERSION=15
ANDROID_BUILD_TARGET_SDK_VERSION=23
ANDROID_BUILD_TOOLS_VERSION=23.0.3
ANDROID_BUILD_SDK_VERSION=23
PROJECT_DIRECTORY=DeepSea

#### sonar配置
sonar_host_url=http://sonar.freetek.cc
sonar_login=
sonar_password=
sonar_sourceEncoding=UTF-8
```

3. 修改全局的gradle配置
```bash
// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        jcenter()
        mavenCentral()
        maven { url "http://plugins.gradle.org/m2/" }
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:2.1.2'
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.7'
        classpath 'com.oguzbabaoglu:butterfork-plugin:1.0.0'
        classpath(
                //使用`./gradlew sonarqube` 打开
//                'org.sonarqube.gradle:gradle-sonarqube-plugin:1.0',
//                'gradle.plugin.de.weltn24:sonarqube-conventions:2.0.0'
        )
    }
}

allprojects {
    repositories {
        jcenter()
        maven { url "http://dl.bintray.com/populov/maven" }
        // 这句是重点，重点，重点
//            maven { url "file:///Users/shitianci/work/Lab/mvn-repo" }
        maven { url "https://github.com/tianci-panda/mvn-repo/raw/master/" }
        maven { url "https://jitpack.io" }
        maven { url "http://plugins.gradle.org/m2/"}
    }
}

task clean(type: Delete) {
    delete rootProject.buildDir
}
```

4. 修改app gradle属性配置文件——**./app/gradle.properties**
```bash
APP_ID=<包名>
MODULE_NAME=<模块名>
VERSION_NAME=<版本名>
VERSION_CODE=<版本号>
```

5. 修改app下的gradle配置为
```bash
apply plugin: 'com.android.application'
apply plugin: 'com.neenbedankt.android-apt'
apply plugin: 'com.oguzbabaoglu.butterfork-plugin'

android {
    compileSdkVersion Integer.parseInt(project.ANDROID_BUILD_SDK_VERSION)
    buildToolsVersion project.ANDROID_BUILD_TOOLS_VERSION

    //解决导入友盟分享异常com.android.build.api.transform.TransformException: com.android.builder.packaging.DuplicateFileException:
    //    Duplicate files copied in APK META-INF/NOTICE
    packagingOptions {
        exclude 'META-INF/DEPENDENCIES'
        exclude 'META-INF/NOTICE'
        exclude 'META-INF/LICENSE'
        exclude 'META-INF/LICENSE.txt'
        exclude 'META-INF/NOTICE.txt'
    }

    defaultConfig {
        applicationId project.APP_ID
        minSdkVersion Integer.parseInt(project.ANDROID_BUILD_MIN_SDK_VERSION)
        targetSdkVersion Integer.parseInt(project.ANDROID_BUILD_TARGET_SDK_VERSION)
        versionCode Integer.parseInt(project.VERSION_CODE)
        versionName project.VERSION_NAME
    }

    signingConfigs {
        myConfig {
            storeFile file("<keystore文件>")
            storePassword "<storePassword>"
            keyAlias "<alias>"
            keyPassword "<keyPassword>"
        }
    }

    lintOptions {
        abortOnError false
    }

    buildTypes {
        debug {
            minifyEnabled false
            signingConfig signingConfigs.myConfig
            debuggable true
        }
        release {
            minifyEnabled false
            proguardFiles getDefaultProguardFile('proguard-android.txt')
            //proguardFile "build/intermediates/exploded/-aar/" + project.PROJECT_DIRECTORY + "/PandaAndroidLib/unspecified/proguard.txt"
            signingConfig signingConfigs.myConfig
        }
    }
}

dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    compile 'panda.android:lib:0.1.2016052801@aar'
    compile 'com.android.support:support-v4:23.+'
    compile 'com.android.support:appcompat-v7:23.+'
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
    compile 'com.oguzbabaoglu:butterfork-binder:1.0.0'
    apt 'com.oguzbabaoglu:butterfork-compiler:1.0.0'
    compile 'com.github.chrisbanes:PhotoView:1.2.6'
    compile 'com.nineoldandroids:library:2.4.0'
    compile 'cn.bingoogolapple:bga-refreshlayout:1.1.0@aar'
    compile 'com.android.support:recyclerview-v7:23.0.+'
    compile 'com.chanven.lib:cptr:1.0.0'
}

/**
 * 参考：
 * 1. http://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner+for+Gradle#AnalyzingwithSonarQubeScannerforGradle-Globalconfigurationsettings
 * 2. https://github.com/WeltN24/gradle-sonarqube-conventions-plugin
 */
//apply plugin: 'org.sonarqube'
//apply plugin: 'de.weltn24.sonarqube-conventions'
//sonarqube {
//    properties {
//        property "sonar.host.url", project.sonar_host_url
//        property "sonar.login", project.sonar_login
//        property "sonar.password", project.sonar_password
//        property "sonar.password", project.sonar_password
//        property "sonar.sourceEncoding", project.sonar_sourceEncoding
//        property 'sonar.projectName', project.MODULE_NAME
//        property 'sonar.projectVersion', project.VERSION_NAME+":"+Integer.parseInt(project.VERSION_CODE)
//        property 'sonar.sources', "src/main/java"
//    }
//}
```





