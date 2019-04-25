//import 'dart:async';
//
//import 'package:flutter/services.dart';
//
//class MicroFlutterIflytek {
//  static const MethodChannel _channel =
//  const MethodChannel('micro_flutter_iflytek');
//
//  static Future<String> get platformVersion async {
//    final String version = await _channel.invokeMethod('recognizer');
//    return version;
//  }
//
//
//  static Future<String> get init async {
//    final String version = await _channel.invokeMethod('init');
//    print("MicroFlutterIflytek: init");
//    return version;
//  }
//
//}

library micro_flutter_iflytek;

export 'src/iflytek.dart';
//export 'src/iflytek_provider.dart';
