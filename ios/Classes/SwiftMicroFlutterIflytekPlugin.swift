import Flutter
import UIKit
import IFlyMSC/IFlyMSC.h

public class SwiftMicroFlutterIflytekPlugin: NSObject, FlutterPlugin {
    
    let  METHOD_INIT = "init"
    let  METHOD_RECOGNIZER = "recognizer"
    let  METHOD_STOP_RECOGNIZER = "stopRecognizer"
    
    let  METHOD_ON_INIT = "onInit"
    let  METHOD_ON_BEGIN_OF_SPEECH = "onBeginOfSpeech"
    let  METHOD_ON_ERROR = "onError"
    let  METHOD_ON_END_OF_SPEECH = "onEndOfSpeech"
    let  METHOD_ON_RESULT = "onResult"
    let  METHOD_ON_VOLUME_CHANGED = "onVolumeChanged"
    let  METHOD_ON_ERROR_DIALOG = "onErrorDialog"
    let  METHOD_ON_RESULT_DIALOG = "onResultDialog"
    
    let  ARGUMENT_KEY_APP_ID = "appId"
    let  ARGUMENT_KEY_SHOW_DIALOG = "showDialog"
    let  ARGUMENT_KEY_CYCLIC = "cyclic"
    let  ARGUMENT_KEY_CYCLIC_DELAY_MILLIS = "cyclicDelayMillis"
    
    let registrar:FlutterPluginRegistrar
    let channel:FlutterMethodChannel
    
    
    
    init(registrar:FlutterPluginRegistrar,channel:FlutterMethodChannel) {
        self.registrar = registrar
        self.channel = channel
    }
    
    public static func register(with registrar: FlutterPluginRegistrar) {
        let channel = FlutterMethodChannel(name: "micro_flutter_iflytek", binaryMessenger: registrar.messenger())
        let instance = SwiftMicroFlutterIflytekPlugin(registrar: registrar, channel: channel)
        
        registrar.addMethodCallDelegate(instance, channel: channel)
    }
    
    public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
        
        if(METHOD_INIT.elementsEqual(call.method)){
            print("init")
            IFlySpeechUtility.
            result("init")
        }
        
        result("iOS " + UIDevice.current.systemVersion)
    }
}
