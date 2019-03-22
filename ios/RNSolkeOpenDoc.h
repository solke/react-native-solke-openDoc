
#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif
#import <Foundation/Foundation.h>
#import <QuickLook/QuickLook.h>

@interface RNSolkeOpenDoc : NSObject <RCTBridgeModule, QLPreviewControllerDelegate, QLPreviewControllerDataSource, QLPreviewItem>
@property (strong, nonatomic) NSURL* fileUrl;
@property (readonly) NSURL* previewItemURL;
@end
  
