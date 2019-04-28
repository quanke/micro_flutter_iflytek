import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

/// Author: quanke (woquanke.com)
/// Date: 2019/4/22

class Iflytek {
  static const String _METHOD_INIT = 'init';

  static const String _METHOD_RECOGNIZER = "recognizer";
  static const String _METHOD_STOP_RECOGNIZER = "stopRecognizer";
  static const String _METHOD_ON_INIT = "onInit";
  static const String _METHOD_ON_BEGIN_OF_SPEECH = "onBeginOfSpeech";
  static const String _METHOD_ON_ERROR = "onError";
  static const String _METHOD_ON_END_OF_SPEECH = "onEndOfSpeech";
  static const String _METHOD_ON_RESULT = "onResult";
  static const String _METHOD_ON_VOLUME_CHANGED = "onVolumeChanged";
  static const String _METHOD_ON_ERROR_DIALOG = "onErrorDialog";
  static const String _METHOD_ON_RESULT_DIALOG = "onResultDialog";

  static const String _ARGUMENT_KEY_APP_ID = "appId";
  static const String _ARGUMENT_KEY_SHOW_DIALOG = "showDialog";
  static const String _ARGUMENT_KEY_CYCLIC = "cyclic";
  static const String _ARGUMENT_KEY_CYCLIC_DELAY_MILLIS = "cyclicDelayMillis";

  final StreamController _onInitStreamController = StreamController.broadcast();

//  final StreamController _onBeginOfSpeechStreamController =
//      StreamController.broadcast();
//
//  final StreamController _onErrorStreamController =
//      StreamController.broadcast();
//
//  final StreamController _onEndOfSpeechStreamController =
//      StreamController.broadcast();
//
//  final StreamController _onResultStreamController =
//      StreamController.broadcast();
//
//  final StreamController _onVolumeChangedStreamController =
//      StreamController.broadcast();
//
//  final StreamController _onErrorDialogStreamController =
//      StreamController.broadcast();
//
//  final StreamController _onResultDialogStreamController =
//      StreamController.broadcast();

  final MethodChannel _channel = const MethodChannel('micro_flutter_iflytek');

  ///初始化
  Future<void> init({
    @required String appId,
    bool showDialog = false,
  }) async {
//    _channel.setMethodCallHandler(_handleMethod);
    _channel.invokeMethod(_METHOD_INIT, <String, dynamic>{
      _ARGUMENT_KEY_APP_ID: appId,
      _ARGUMENT_KEY_SHOW_DIALOG: showDialog,
    });
  }
//
//  /// 识别
//  Future<void> recognizer({
//    bool cyclic = false,
//    int cyclicDelayMillis = 200,
//  }) async {
//    _channel.invokeMethod(_METHOD_RECOGNIZER, <String, dynamic>{
//      _ARGUMENT_KEY_CYCLIC: cyclic,
//      _ARGUMENT_KEY_CYCLIC_DELAY_MILLIS: cyclicDelayMillis,
//    });
//  }
//
//  /// 停止识别
//  Future<void> stopRecognizer() async {
//    _channel.invokeMethod(_METHOD_STOP_RECOGNIZER);
//  }
//
//  Future<dynamic> _handleMethod(MethodCall call) {
//    switch (call.method) {
//      case _METHOD_ON_INIT:
//        _onInitStreamController.add(call.arguments);
//        break;
//      case _METHOD_ON_BEGIN_OF_SPEECH:
//        _onBeginOfSpeechStreamController.add(call.arguments);
//        break;
//      case _METHOD_ON_ERROR:
//        _onErrorStreamController.add(call.arguments);
//        break;
//      case _METHOD_ON_END_OF_SPEECH:
//        _onEndOfSpeechStreamController.add(call.arguments);
//        break;
//      case _METHOD_ON_RESULT:
//        _onResultStreamController.add(call.arguments);
//        break;
//      case _METHOD_ON_VOLUME_CHANGED:
//        _onVolumeChangedStreamController.add(call.arguments);
//        break;
//      case _METHOD_ON_ERROR_DIALOG:
//        _onErrorDialogStreamController.add(call.arguments);
//        break;
//      case _METHOD_ON_RESULT_DIALOG:
//        _onResultDialogStreamController.add(call.arguments);
//        break;
//    }
//    return Future.value(true);
//  }
//
//  Stream onInit() {
//    return _onInitStreamController.stream;
//  }
//
//  Stream onBeginOfSpeech() {
//    return _onBeginOfSpeechStreamController.stream;
//  }
//
//  Stream onError() {
//    return _onErrorStreamController.stream;
//  }
//
//  Stream onEndOfSpeech() {
//    return _onEndOfSpeechStreamController.stream;
//  }
//
//  Stream onResult() {
//    return _onResultStreamController.stream;
//  }
//
//  Stream get onVolumeChanged => _onVolumeChangedStreamController.stream;
//
//  Stream onErrorDialog() {
//    return _onErrorDialogStreamController.stream;
//  }
//
//  Stream onResultDialog() {
//    return _onResultDialogStreamController.stream;
//  }
//
//  void dispose(
//      {onInit: true,
//      onBeginOfSpeech: true,
//      onError: true,
//      onEndOfSpeech: true,
//      onResult: true,
//      onVolumeChanged: true,
//      onErrorDialog: true,
//      onResultDialog: true}) {
//    if (onInit) {
//      _onInitStreamController.close();
//    }
//    if (onBeginOfSpeech) {
//      _onBeginOfSpeechStreamController.close();
//    }
//    if (onError) {
//      _onErrorStreamController.close();
//    }
//
//    if (onEndOfSpeech) {
//      _onEndOfSpeechStreamController.close();
//    }
//
//    if (onResult) {
//      _onResultStreamController.close();
//    }
//
//    if (onResultDialog) {
//      _onResultDialogStreamController.close();
//    }
//  }
}
