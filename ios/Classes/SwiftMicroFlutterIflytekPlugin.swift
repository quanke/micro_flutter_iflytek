import Flutter
import UIKit

public class SwiftMicroFlutterIflytekPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "micro_flutter_iflytek", binaryMessenger: registrar.messenger())
    let instance = SwiftMicroFlutterIflytekPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result("iOS " + UIDevice.current.systemVersion)
  }
}
