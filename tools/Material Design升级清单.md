### Material Design升级清单
1. 自定义颜色基调
```
compile 'com.android.support:appcompat-v7:21.+'
```
```
<color name="testcolor1">#ff1a38ff</color>
<style name="TestTheme">
    <item name="colorPrimary">#ffff160e</item>
    <item name="colorPrimaryDark">#ffffff2c</item>
    <item name="android:textColorPrimary">#ff6cff50</item>
    <item name="android:windowBackground">@color/testcolor1</item>
    <!--<item name="navigationBarColor">#fffffcf3</item>-->
</style>
```

2. Palette特性
```
compile 'com.android.support:palette-v7:21.0.+'
```
使用过程：
- 从1张bitmap中提取一系列颜色样本Swatch（对于风景图，12-16最佳，对于人脸的图片，该值需要增加到24-32才是最佳的。该值的增加，会增加计算的时间，如果没有特殊需求，使用默认值16即可）
- 颜色样本按照色调可以分为：
	- 有活力的颜色. Palette.getVibrantSwatch()
	- 有活力的暗色. Palette.getDarkVibrantSwatch()
	- 有活力的亮色. Palette.getLightVibrantSwatch()
	- 柔和的颜色. Palette.getMutedSwatch()
	- 柔和的暗色. Palette.getDarkMutedSwatch()
	- 柔和的亮色. Palette.getLightMutedSwatch()
- 每个颜色样本包含一些独特功能的颜色：
	- getPopulation(): 返回被该样本代表的像素的总数
	- getRgb(): 返回一个 RGB 颜色值
	- getHsl(): 返回一个 HSL颜色值.
	- getBodyTextColor(): 返回一个适合做内容体颜色的颜色值
	- getTitleTextColor(): 返回一个适合做标题颜色的颜色值

参考：[android L Palette 实现原理和效果](http://www.2cto.com/kf/201501/373806.html)

3. 用ToolBar代替ActionBar
4. VectorDrawable 矢量图片：[兼容方案MrVector](https://github.com/telly/MrVector)

5. RecyclerView
```
compile 'com.android.support:appcompat-v7:21.+'
```

6. CardView
```
compile 'com.android.support:cardview-v7:21.0.0-rc1'
```
7. 过渡动画
[ActivityOptionsICS](https://github.com/tianzhijiexian/ActivityOptionsICS)
介绍文章：[用开源项目ActivityOptionsICS让ActivityOptions的动画实现兼容](http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0203/2400.html)

8. 阴影android:elevation
```
compile 'com.android.support:design:22.2.0'
```
参考： [一个Demo学会用Android兼容包新控件](http://www.open-open.com/lib/view/open1436519077255.html)

9. 动画
https://github.com/andkulikov/transitions-everywhere
- [Touch feedback（触摸反馈）](https://github.com/traex/RippleEffect)
- [Reveal effect（揭露效果）]()
- [Curved motion（曲线运动）]()
- [View state changes （视图状态改变）]()

10. Tinting view(着色视图)和Clipping view(裁剪视图)
[Android 5.0学习之Tinting和Clipping](http://blog.csdn.net/cym492224103/article/details/41786289)

11. 其它
[直接拿来用！十大Material Design开源项目](http://www.csdn.net/article/2014-11-21/2822753-material-design-libs/1)