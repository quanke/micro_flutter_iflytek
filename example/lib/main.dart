import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:micro_flutter_iflytek/micro_flutter_iflytek.dart';
import 'package:permission_handler/permission_handler.dart';

void main() => runApp(Home());

Iflytek _iflytek = Iflytek();

class Home extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _MyAppState();
  }

  Home() {
    _iflytek.init(appId: '5cc03524', showDialog: false);
  }
}

class _MyAppState extends State<Home> {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    super.initState();

    _iflytek.onBeginOfSpeech().listen(_onBeginOfSpeech);
    _iflytek.onInit().listen(_onInit);
    _iflytek.onResult().listen(_onResult);
//
//    _iflytek.onVolumeChanged
//        .listen(_onVolumeChanged, onDone: _onVolumeChangedDone);

    initPlatformState();
  }

  void _onInit(code) {
    print("_onResult---------------  ${code}");
  }

  void _onResult(resp) {
    print("_onResult--------------- ${resp}");
  }

  void _onVolumeChanged(resp) {
//    print("_onVolumeChanged--------------- ${resp}");
  }

  void _onVolumeChangedDone() {
//    print("_onVolumeChangedDone---------------");
  }

  void _onBeginOfSpeech(resp) {
    print("_onBeginOfSpeech ${resp}");
    setState(() {
      _platformVersion:
      "${resp}";
    });
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    Map<PermissionGroup, PermissionStatus> permissions =
        await PermissionHandler().requestPermissions([PermissionGroup.speech]);

    if (!mounted) return;
  }

  @override
  void dispose() {
    _iflytek.dispose();
    super.dispose();
  }

  void _beginIatRecognize() {
    try {
      print("_beginIatRecognize");

      _iflytek.recognizer(cyclic: true);
    } on PlatformException {}
  }

  void _stopIatRecognize() {
    try {
      print("_stopIatRecognize");

//      _iflytek.stopRecognizer();
    } on PlatformException {}
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Column(
            children: <Widget>[
              Text('Running on: $_platformVersion\n'),
              RaisedButton(
                child: Text('听我说！'),
                onPressed: _beginIatRecognize,
              ),
              RaisedButton(
                child: Text('不听了！'),
                onPressed: _stopIatRecognize,
              )
            ],
          ),
        ),
      ),
    );
  }
}
