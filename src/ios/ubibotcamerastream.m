/********* ubibotcamerastream.m Cordova Plugin Implementation *******/

#import <Cordova/CDV.h>
#import "IOTCAPIs.h"
#import "AVAPIs.h"
#import "AVIOCTRLDEFs.h"
#import "AVFRAMEINFO.h"
#import <sys/time.h>
#import <pthread.h>

#define AUDIO_BUF_SIZE    1024
#define VIDEO_BUF_SIZE    500000

@interface ubibotcamerastream : CDVPlugin {
  // Member variables go here.
}

- (void)start:(CDVInvokedUrlCommand*)command;
- (void)stop:(CDVInvokedUrlCommand*)command;
@end

@implementation ubibotcamerastream

NSString *mUID;
NSString *mAccount ;
NSString *mPassword;
bool bolCheck = false;
static ubibotcamerastream* native;
static NSString *_callbackID;

- (void)stop:(CDVInvokedUrlCommand*)command{
    bolCheck = true;
    CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"stop"];
    [pluginResult setKeepCallbackAsBool:YES];
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)start:(CDVInvokedUrlCommand*)command{
    bolCheck = false;
    NSArray *arguments = [command.arguments objectAtIndex:0];
    if(arguments != nil && arguments.count == 3){
        native = self;
        _callbackID = command.callbackId;
        NSString *UID = arguments[0];
        NSString *account = arguments[1];
        NSString *password = arguments[2];
        NSLog(@"STEP 1");
        [self start:UID account:account password:password];
        NSLog(@"STEP 2");
    }else{
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"arguments is error"];
        [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }
}

- (pthread_t)start:(NSString *)UID account:(NSString *)account password:(NSString *)password {
    bolCheck = false;
    //    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(receiveVideoThread:) name:@"stopstop" object:nil];
//    NSLog(@"start UID:%@", UID);
//    NSLog(@"start account:%@", account);
//    NSLog(@"start password:%@", password);
    mUID = UID;
    mAccount = account;
    mPassword = password;
    pthread_t main_thread;
    NSLog(@"STEP 3");
    pthread_create(&main_thread, NULL, &start_main, NULL);
    NSLog(@"STEP 4");
    pthread_detach(main_thread);//即主线程与子线程分离，子线程结束后，资源自动回收。
    NSLog(@"STEP 5");
    return main_thread;
}

void *start_main () {
    NSString *UID = mUID ;
    NSString *Account = mAccount ;
    NSString *Password = mPassword ;
//    NSLog(@"start_main2 UID:%@", UID);
//    NSLog(@"start_main2 account:%@", Account);
//    NSLog(@"start_main2 password:%@", Password);
    int ret, SID;
    ret = IOTC_Initialize(0, "46.137.188.54", "122.226.84.253", "m2.iotcplatform.com", "m5.iotcplatform.com");
    NSLog(@"IOTC_Initialize() ret = %d", ret);
    if (ret != IOTC_ER_NoERROR) {
        NSLog(@"IOTCAPIs exit...");
        IOTC_DeInitialize();
        NSString *message = [[NSString alloc]initWithFormat:@"IOTC_Initialize|%d|IOTCAPIs exit...", ret];
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:message];
        [native.commandDelegate sendPluginResult:pluginResult callbackId:_callbackID];
        //        [[NSNotificationCenter defaultCenter] postNotificationName:@"error" object:message];
        return NULL;
    }
    // alloc 4 sessions for video and two-way audio
    avInitialize(4);
    SID = IOTC_Get_SessionID();
    ret = IOTC_Connect_ByUID_Parallel((char *)[UID UTF8String], SID);

    printf("Step 2: call IOTC_Connect_ByUID_Parallel(%s) ret(%d).......\n", [UID UTF8String], ret);
    struct st_SInfo Sinfo;
    ret = IOTC_Session_Check(SID, &Sinfo);

    if (ret >= 0){
        if(Sinfo.Mode == 0)
            printf("Device is from %s:%d[%s] Mode=P2P\n",Sinfo.RemoteIP, Sinfo.RemotePort, Sinfo.UID);
        else if (Sinfo.Mode == 1)
            printf("Device is from %s:%d[%s] Mode=RLY\n",Sinfo.RemoteIP, Sinfo.RemotePort, Sinfo.UID);
        else if (Sinfo.Mode == 2)
            printf("Device is from %s:%d[%s] Mode=LAN\n",Sinfo.RemoteIP, Sinfo.RemotePort, Sinfo.UID);
    }else{
        NSLog(@"IOTCAPIs exit...");
        IOTC_Session_Close(SID);
        IOTC_DeInitialize();
        NSString *message = [[NSString alloc]initWithFormat:@"IOTC_Connect_ByUID_Parallel|%d|IOTCAPIs exit...", ret];
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:message];
        [native.commandDelegate sendPluginResult:pluginResult callbackId:_callbackID];
        //        [[NSNotificationCenter defaultCenter] postNotificationName:@"error" object:message];
        return NULL;
    }

    unsigned int srvType;
    int avIndex = avClientStart(SID, (char *)[Account UTF8String], (char *)[Password UTF8String], 20000, &srvType, 0);
    printf("Step 3: call avClientStart(%d).......\n", avIndex);

    if(avIndex < 0){
        //-14 The specified IOTC session ID is not valid
        printf("avClientStart failed[%d]\n", avIndex);
        IOTC_Session_Close(SID);
        IOTC_DeInitialize();
        NSString *message = [[NSString alloc]initWithFormat:@"avClientStart|%d|avClientStart failed[%d]", avIndex, avIndex];
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:message];
        [native.commandDelegate sendPluginResult:pluginResult callbackId:_callbackID];
        //        [[NSNotificationCenter defaultCenter] postNotificationName:@"error" object:message];
        return NULL;
    }

    printf("Step 4: start_ipcam_stream\n");
    if (start_ipcam_stream(avIndex)>0) {
        printf("Step 5: start_ipcam_stream\n");
        pthread_t ThreadVideo_ID;
        pthread_create(&ThreadVideo_ID, NULL, &thread_ReceiveVideo, (void *)&avIndex);
        pthread_join(ThreadVideo_ID, NULL);//即是子线程合入主线程，主线程阻塞等待子线程结束，然后回收子线程资源。
    }else{
        printf("Step 6: start_ipcam_stream\n");
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:@"start_ipcam_stream_failed"];
        [native.commandDelegate sendPluginResult:pluginResult callbackId:_callbackID];
    }
    avClientStop(avIndex);
    NSLog(@"avClientStop OK");
    IOTC_Session_Close(SID);
    NSLog(@"IOTC_Session_Close OK");
    avDeInitialize();
    IOTC_DeInitialize();
    NSLog(@"StreamClient exit...");
    pthread_exit(0);
//    return nil;
}

void *thread_ReceiveVideo(void *arg){
    int avIndex = *(int *)arg;
    char *buf = malloc(VIDEO_BUF_SIZE);
    unsigned int frmNo;
    int ret;
    FRAMEINFO_t frameInfo;

    int pActualFrameSize[] = {0};
    int pExpectedFameSize[] = {0};
    int pActualFrameInfoSize[] = {0};

    while (1)
    {
        if(bolCheck == true){
            break;
        }
        ret = avRecvFrameData2(avIndex, buf, VIDEO_BUF_SIZE, pActualFrameSize, pExpectedFameSize, (char *)&frameInfo, sizeof(FRAMEINFO_t), pActualFrameInfoSize, &frmNo);
        if (ret > 0)
        {
            dispatch_async(dispatch_get_main_queue(), ^{
                NSData *dic = [NSData dataWithBytes:buf length:ret];
                NSDictionary *dict = @{@"data":[NSData dataWithBytes:buf length:ret],
                                       @"timestamp":[NSNumber numberWithUnsignedInt:frameInfo.timestamp]};
                CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArrayBuffer:dic];
                [pluginResult setKeepCallbackAsBool:YES];
                [native.commandDelegate sendPluginResult:pluginResult callbackId:_callbackID];
//                [[NSNotificationCenter defaultCenter] postNotificationName:@"client" object:dict];
            });
            usleep(30000);
        }
        else if(ret == AV_ER_DATA_NOREADY) {
            //-20012 The data is not ready for receiving yet.
            usleep(10000);
            continue;
        }else if(ret == AV_ER_LOSED_THIS_FRAME){
            //-20014
            NSLog(@"Lost video frame NO[%d]", frmNo);
            continue;
        }else if(ret == AV_ER_INCOMPLETE_FRAME){
            //-20013
            NSLog(@"Incomplete video frame NO[%d]", frmNo);
            continue;
        }else if(ret == AV_ER_SESSION_CLOSE_BY_REMOTE){
            //-20015
            NSLog(@"[thread_ReceiveVideo] AV_ER_SESSION_CLOSE_BY_REMOTE");
            break;
        }else if(ret == AV_ER_REMOTE_TIMEOUT_DISCONNECT){
            //-20016
            NSLog(@"[thread_ReceiveVideo] AV_ER_REMOTE_TIMEOUT_DISCONNECT");
            break;
        }else if(ret == IOTC_ER_INVALID_SID){
            //-14
            NSLog(@"[thread_ReceiveVideo] Session cant be used anymore");
            break;
        }
    }
    free(buf);
    NSLog(@"[thread_ReceiveVideo] thread exit");
    return 0;
}

int start_ipcam_stream (int avIndex) {
    int ret;
    unsigned short val = 0;
    if ((ret = avSendIOCtrl(avIndex, IOTYPE_INNER_SND_DATA_DELAY, (char *)&val, sizeof(unsigned short)) < 0))
    {
        //        NSLog(@"start_ipcam_stream_failed[%d]", ret);
        //        NSString *message = [[NSString alloc]initWithFormat:@"start_ipcam_stream|%d|start_ipcam_stream_failed[%d]", ret, ret];
        //        [[NSNotificationCenter defaultCenter] postNotificationName:@"error" object:message];
        return 0;
    }
    SMsgAVIoctrlAVStream ioMsg;
    memset(&ioMsg, 0, sizeof(SMsgAVIoctrlAVStream));
    if ((ret = avSendIOCtrl(avIndex, IOTYPE_USER_IPCAM_START, (char *)&ioMsg, sizeof(SMsgAVIoctrlAVStream)) < 0))
    {
        //        NSLog(@"start_ipcam_stream_failed[%d]", ret);
        //        NSString *message = [[NSString alloc]initWithFormat:@"start_ipcam_stream|%d|start_ipcam_stream_failed[%d]", ret, ret];
        //        [[NSNotificationCenter defaultCenter] postNotificationName:@"error" object:message];
        return 0;
    }
    if ((ret = avSendIOCtrl(avIndex, IOTYPE_USER_IPCAM_AUDIOSTART, (char *)&ioMsg, sizeof(SMsgAVIoctrlAVStream)) < 0))
    {
        //        NSLog(@"start_ipcam_stream_failed[%d]", ret);
        //        NSString *message = [[NSString alloc]initWithFormat:@"start_ipcam_stream|%d|start_ipcam_stream_failed[%d]", ret, ret];
        //        [[NSNotificationCenter defaultCenter] postNotificationName:@"error" object:message];
        return 0;
    }
    return 1;
}

@end
