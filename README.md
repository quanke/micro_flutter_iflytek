# micro_flutter_iflytek

A new Flutter plugin.



## ios

增加权限
````
应用需要在“Info.plist”中增加

<key>NSSpeechRecognitionUsageDescription</key>
<string>语音转文字</string>
<key>NSMicrophoneUsageDescription</key>
<string>麦克风</string>
````

参考[IOS 10 需要在info.plist文件中添加隐私权限配置](http://ask.dcloud.net.cn/article/931)

后台模式

```
应用需要在“Info.plist”中增加
<key>UIBackgroundModes</key>
	<array>
		<string>fetch</string>
		<string>remote-notification</string>
	</array>

```
参考[Xcode中的Info.plist字段列表详解](http://www.cocoachina.com/ios/20160922/17611.html)

## Getting Started

This project is a starting point for a Flutter
[plug-in package](https://flutter.io/developing-packages/),
a specialized package that includes platform-specific implementation code for
Android and/or iOS.

For help getting started with Flutter, view our 
[online documentation](https://flutter.io/docs), which offers tutorials, 
samples, guidance on mobile development, and a full API reference.
