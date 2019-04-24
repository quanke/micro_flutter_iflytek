#import "MicroFlutterIflytekPlugin.h"
#import <micro_flutter_iflytek/micro_flutter_iflytek-Swift.h>

@implementation MicroFlutterIflytekPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftMicroFlutterIflytekPlugin registerWithRegistrar:registrar];
}
@end
