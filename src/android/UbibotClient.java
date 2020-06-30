package com.kit.cordova.ubibotcamerastream;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;

import com.camera.api.AVAPIsClient;
import com.camera.api.G711Code;
import com.camera.model.Audio;
import com.camera.model.SaveFrames;
import com.decode.tools.BufferInfo;
import com.tutk.IOTC.AVAPIs;
import com.tutk.IOTC.AVAPIstest;
import com.tutk.IOTC.IOTCAPIs;
import com.tutk.IOTC.St_AVClientStartInConfig;
import com.tutk.IOTC.St_AVClientStartOutConfig;

import java.util.Arrays;
import java.util.concurrent.BlockingDeque;

public class UbibotClient {

    private static int avIndex = -1; // avClientStart的返回值
    /**
     * 修改视频清晰度的常量
     */
    public static byte nowQuality = -1;
    public static final byte AVIOCTRL_QUALITY_HIGH = 0x02;  // 640*480, 10fps, 256kbps  超清
    public static final byte AVIOCTRL_QUALITY_MIDDLE = 0x03;// 320*240, 15fps, 256kbps  高清
    public static final byte AVIOCTRL_QUALITY_LOW = 0x04;   // 320*240, 10fps, 128kbps  标清
    public static final byte AVIOCTRL_QUALITY_MIN = 0x05;   // 160*120, 10fps, 64kbps   流畅
    private static final int IOTYPE_USER_IPCAM_SETSTREAMCTRL_REQ = 0x0320;
    public static boolean bolCheck = false;

//     public static void start4(CallbackContext callbackContext){
// //         ServerManager server_manager = new ServerManager();
// //         server_manager.Start(8800,callbackContext);
// // //         while (true) {
// // //         }
//     }
//
//     public static void start3( CallbackContext callbackContext){
// //         int count = Integer.parseInt(message);
// //         for(int j =0; j < 10; j++){
//         int j = 0;
//         while (true) {
//             j += 1;
// //                 callbackContext.success("abba:" + message);
//             PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, "test index:" + Integer.toString(j) );
//             pluginResult.setKeepCallback(true); // keep callback
//             callbackContext.sendPluginResult(pluginResult);
// //             System.out.println("test index:" + Integer.toString(j));
//             if( j == 10){
//                 break;
//             }
//             try {
// //                 Thread.sleep(30);
//                 Thread.currentThread().sleep(2000);
// //                 continue;
//             } catch (InterruptedException e) {
//                 System.out.println(e.getMessage());
//                 break;
//             }
//         }
//     }
//
//     public static void start2(String uid, String account_or_identity, String password_or_token, CallbackContext callbackContext){
// //     public static void start2(String uid, String account_or_identity, String password_or_token, BlockingDeque bq, Handler handler, CallbackContext callbackContext){
//         try{
// //             mySendMessage( "STEP 1.1 ",callbackContext);
// //             try{
//                 int ret = IOTCAPIs.IOTC_Initialize2(0);
// //                 mySendMessage("STEP 1.2 " + Integer.toString(ret),callbackContext);
// //             }catch(Exception e){
// //                 mySendMessage("Exception STEP 1.21 + "+ e.getMessage(),callbackContext);
// //             }
// //             mySendMessage("STEP 1.3 ",callbackContext);
// //             mySendMessage( "STEP 1.2 "+ Integer.toString(ret),callbackContext);
//             if (ret != IOTCAPIs.IOTC_ER_NoERROR) {
//                 IOTCAPIs.IOTC_DeInitialize();
//                 mySendMessage("IOTCAPIs_Device exit...!!",callbackContext);
//                 return;
//             }
// //             mySendMessage( "STEP 2 ",callbackContext);
//
//             // alloc 3 sessions for video and two-way audio
//             AVAPIs.avInitialize(3);
//
//             int sid = IOTCAPIs.IOTC_Get_SessionID();
//             if (sid < 0) {
//                 AVAPIs.avDeInitialize();
//                 IOTCAPIs.IOTC_DeInitialize();
//                 mySendMessage("IOTC_Get_SessionID error code " + sid,callbackContext);
//                 return;
//             }
// //             mySendMessage("STEP 3 ",callbackContext);
//             ret = IOTCAPIs.IOTC_Connect_ByUID_Parallel(uid, sid);
//
//             int srvType = 0;
//             int bResend = 0;
//             St_AVClientStartInConfig av_client_in_config = new St_AVClientStartInConfig();
//             St_AVClientStartOutConfig av_client_out_config = new St_AVClientStartOutConfig();
//
//             av_client_in_config.iotc_session_id = sid;
//             av_client_in_config.iotc_channel_id = 0;
//             av_client_in_config.timeout_sec = 20;
//             av_client_in_config.account_or_identity = account_or_identity;
//             av_client_in_config.password_or_token = password_or_token;
//             av_client_in_config.resend = 1;
//     //        av_client_in_config.security_mode = 1; //enable DTLS
//             av_client_in_config.security_mode = 0; //enable DTLS
//             av_client_in_config.auth_type = 0;
//
//             avIndex = AVAPIs.avClientStartEx(av_client_in_config, av_client_out_config);
//             bResend = av_client_out_config.resend;
//             srvType = av_client_out_config.server_type;
// //             mySendMessage("STEP 4 ",callbackContext);
//             if (avIndex < 0) {
//                 IOTCAPIs.IOTC_Session_Close(sid);
//                 AVAPIs.avDeInitialize();
//                 IOTCAPIs.IOTC_DeInitialize();
//                 mySendMessage("avIndex<0:" + Integer.toString(avIndex),callbackContext);
//                 return;
//             }
// //             mySendMessage("STEP 5 ",callbackContext);
//             if (startIpcamStream(avIndex,callbackContext)) {
// //                 Thread videoThread = new Thread(new VideoThread(avIndex, bq, handler), "Video Thread");
//                 Thread videoThread = new Thread(new VideoThread(avIndex, callbackContext), "Video Thread");
//                 videoThread.start();//启动VideoThread线程
//                 try {
//
// //                     mySendMessage("STEP 6 ",callbackContext);
//                     //邀请VideoThread线程先执行，本线程先暂停执行
//                     //等待VideoThread线程执行完后，主线程再接着往下执行
//                     videoThread.join();
//                 } catch (InterruptedException e) {
//                     AVAPIs.avClientStop(avIndex);
//                     IOTCAPIs.IOTC_Session_Close(sid);
//                     AVAPIs.avDeInitialize();
//                     IOTCAPIs.IOTC_DeInitialize();
//                     mySendMessage("InterruptedException failed :" + e.getMessage(),callbackContext);
//                     return;
//                 }
//             }else{
//                 mySendMessage("-77",callbackContext);
//             }
//
// //             mySendMessage("STEP 7 ",callbackContext);
//             AVAPIs.avClientStop(avIndex);
//             IOTCAPIs.IOTC_Session_Close(sid);
//             AVAPIs.avDeInitialize();
//             IOTCAPIs.IOTC_DeInitialize();
//         }catch(Exception ule){
//             mySendMessage("Exception STEP 1.31 ",callbackContext);
//         }
// //         mySendMessage("STEP 1.4 ",callbackContext);
//     }

    public static void stop(){
        bolCheck = true;
    }

    public static void start(String uid, String account_or_identity, String password_or_token, CallbackContext callbackContext) {
        bolCheck = false;
//         BlockingDeque bq;
//         Handler handler;
//         public static void start(String uid, String account_or_identity, String password_or_token, BlockingDeque bq, Handler handler, CallbackContext callbackContext) {
//             mySendMessage( "STEP 1 ",callbackContext);
//         try{
//             mySendMessage( "STEP 1.1 ",callbackContext);
//             int ret = -99;
//             try{
//                 ret = IOTCAPIs.IOTC_Initialize2(0);
//             }catch(Exception e){
//                 mySendMessage( "Exception 1.2 + "+ e.getMessage(),callbackContext);
//             }
            int ret = IOTCAPIs.IOTC_Initialize2(0);
//             mySendMessage( "STEP 1.2 "+ Integer.toString(ret),callbackContext);
            if (ret != IOTCAPIs.IOTC_ER_NoERROR) {
                IOTCAPIs.IOTC_DeInitialize();
                mySendMessage("IOTCAPIs_Device failed...!!",callbackContext);
                return;
            }
//             mySendMessage( "STEP 2 ",callbackContext);

            // alloc 3 sessions for video and two-way audio
            AVAPIs.avInitialize(3);

            int sid = IOTCAPIs.IOTC_Get_SessionID();
            if (sid < 0) {
                AVAPIs.avDeInitialize();
                IOTCAPIs.IOTC_DeInitialize();
                mySendMessage("IOTC_Get_SessionID failed : " + sid,callbackContext);
                return;
            }
//             mySendMessage("STEP 3 ",callbackContext);
            ret = IOTCAPIs.IOTC_Connect_ByUID_Parallel(uid, sid);

            int srvType = 0;
            int bResend = 0;
            St_AVClientStartInConfig av_client_in_config = new St_AVClientStartInConfig();
            St_AVClientStartOutConfig av_client_out_config = new St_AVClientStartOutConfig();

            av_client_in_config.iotc_session_id = sid;
            av_client_in_config.iotc_channel_id = 0;
            av_client_in_config.timeout_sec = 20;
            av_client_in_config.account_or_identity = account_or_identity;
            av_client_in_config.password_or_token = password_or_token;
            av_client_in_config.resend = 1;
    //        av_client_in_config.security_mode = 1; //enable DTLS
            av_client_in_config.security_mode = 0; //enable DTLS
            av_client_in_config.auth_type = 0;

            avIndex = AVAPIs.avClientStartEx(av_client_in_config, av_client_out_config);
            bResend = av_client_out_config.resend;
            srvType = av_client_out_config.server_type;
//             mySendMessage("STEP 4 ",callbackContext);
            if (avIndex < 0) {
                IOTCAPIs.IOTC_Session_Close(sid);
                AVAPIs.avDeInitialize();
                IOTCAPIs.IOTC_DeInitialize();
                mySendMessage("av client start failed :" + Integer.toString(avIndex),callbackContext);
                return;
            }
//             mySendMessage("STEP 5 ",callbackContext);
            if (startIpcamStream(avIndex,callbackContext)) {
//                 Thread videoThread = new Thread(new VideoThread(avIndex, bq, handler), "Video Thread");
                Thread videoThread = new Thread(new VideoThread(avIndex, callbackContext), "Video Thread");
                videoThread.start();//启动VideoThread线程
                try {

//                     mySendMessage("STEP 6 ",callbackContext);
                    //邀请VideoThread线程先执行，本线程先暂停执行
                    //等待VideoThread线程执行完后，主线程再接着往下执行
                    videoThread.join();
                } catch (InterruptedException e) {
                    AVAPIs.avClientStop(avIndex);
                    IOTCAPIs.IOTC_Session_Close(sid);
                    AVAPIs.avDeInitialize();
                    IOTCAPIs.IOTC_DeInitialize();
                    mySendMessage("InterruptedException failed :" + e.getMessage(),callbackContext);
                    return;
                }
            }else{
                mySendMessage("start_ipcam_stream failed",callbackContext);
            }

//             mySendMessage("STEP 7 ",callbackContext);
            AVAPIs.avClientStop(avIndex);
            IOTCAPIs.IOTC_Session_Close(sid);
            AVAPIs.avDeInitialize();
            IOTCAPIs.IOTC_DeInitialize();
            mySendMessage("stop get stream",callbackContext);
//         }catch(Exception e){
//             mySendMessage("Exception:" + e.getMessage(),callbackContext);
//         }
    }

    public static boolean startIpcamStream(int avIndex,CallbackContext callbackContext) {
//     public static boolean startIpcamStream(int avIndex, Handler handler,CallbackContext callbackContext) {
        AVAPIs av = new AVAPIs();
        int ret = av.avSendIOCtrl(avIndex, AVAPIs.IOTYPE_INNER_SND_DATA_DELAY,
                new byte[2], 2);
        if (ret < 0) {
//            System.out.printf("start_ipcam_stream failed[%d]\n", ret);
//             mySendMessage("start_ipcam_stream failed:" + ret,callbackContext);
            return false;
        }

        // This IOTYPE constant and its corrsponsing data structure is defined in
        // Sample/Linux/Sample_AVAPIs/AVIOCTRLDEFs.h
        //
        int IOTYPE_USER_IPCAM_START = 0x1FF;
        ret = av.avSendIOCtrl(avIndex, IOTYPE_USER_IPCAM_START,
                new byte[8], 8);
        if (ret < 0) {
//            System.out.printf("start_ipcam_stream failed[%d]\n", ret);
//             mySendMessage("start_ipcam_stream failed:" + ret,callbackContext);
            return false;
        }

        int IOTYPE_USER_IPCAM_AUDIOSTART = 0x300;
        ret = av.avSendIOCtrl(avIndex, IOTYPE_USER_IPCAM_AUDIOSTART,
                new byte[8], 8);
        if (ret < 0) {
//            System.out.printf("start_ipcam_stream failed[%d]\n", ret);
//             mySendMessage("start_ipcam_stream failed:" + ret,callbackContext);
            return false;
        }

//        byte[] result = AVAPIsClient.SMsgAVIoctrlSetStreamCtrlReq.parseContent(avIndex, AVIOCTRL_QUALITY_MIN);
//        ret = av.avSendIOCtrl(avIndex, IOTYPE_USER_IPCAM_SETSTREAMCTRL_REQ,
//                result, 8);
//        if (ret < 0) {
//            System.out.printf("切换流畅失败 [%d]\n", ret);
//            return false;
//        }

        return true;
    }

    /**
     * 修改视频清晰度
     *
     * @param qualityNum qualityNum 0 - 3 依次为 流畅 吧标清 高清 超清
     */
    public static void setQuality(int qualityNum) {


        AVAPIs av = new AVAPIs();
        switch (qualityNum) {
            // 流畅
            case 0:
                if (nowQuality != AVIOCTRL_QUALITY_MIN) {
                    System.out.println("视频切换为流畅");
                    byte[] result = AVAPIsClient.SMsgAVIoctrlSetStreamCtrlReq.parseContent(avIndex, AVIOCTRL_QUALITY_MIN);
                    int ret = av.avSendIOCtrl(avIndex, IOTYPE_USER_IPCAM_SETSTREAMCTRL_REQ,
                            result, 8);
                    if (ret < 0) {
                        System.out.printf("切换流畅失败 [%d]\n", ret);
                    }
                    nowQuality = AVIOCTRL_QUALITY_MIN;
                }

                break;
            // 标清
            case 1:
                if (nowQuality != AVIOCTRL_QUALITY_LOW) {
                    System.out.println("视频切换为标清");
                    byte[] result = AVAPIsClient.SMsgAVIoctrlSetStreamCtrlReq.parseContent(avIndex, AVIOCTRL_QUALITY_LOW);
                    int ret = av.avSendIOCtrl(avIndex, IOTYPE_USER_IPCAM_SETSTREAMCTRL_REQ,
                            result, 8);
                    if (ret < 0) {
                        System.out.printf("切换标清失败 [%d]\n", ret);
                    }
                    nowQuality = AVIOCTRL_QUALITY_LOW;
                }
                break;
            // 高清
            case 2:
                if (nowQuality != AVIOCTRL_QUALITY_MIDDLE) {
                    System.out.println("视频切换为高清");
                    byte[] result = AVAPIsClient.SMsgAVIoctrlSetStreamCtrlReq.parseContent(avIndex, AVIOCTRL_QUALITY_MIDDLE);
                    int ret = av.avSendIOCtrl(avIndex, IOTYPE_USER_IPCAM_SETSTREAMCTRL_REQ,
                            result, 8);
                    if (ret < 0) {
                        System.out.printf("切换高清失败 [%d]\n", ret);
                    }
                    nowQuality = AVIOCTRL_QUALITY_MIDDLE;
                }
                break;
            case 3:
                if (nowQuality != AVIOCTRL_QUALITY_HIGH) {
                    System.out.println("视频切换为超清");
                    byte[] result = AVAPIsClient.SMsgAVIoctrlSetStreamCtrlReq.parseContent(avIndex, AVIOCTRL_QUALITY_HIGH);
                    int ret = av.avSendIOCtrl(avIndex, IOTYPE_USER_IPCAM_SETSTREAMCTRL_REQ,
                            result, 8);
                    if (ret < 0) {
                        System.out.printf("切换超清失败 [%d]\n", ret);
                    }
                    nowQuality = AVIOCTRL_QUALITY_HIGH;
                }
                break;
            default:
                System.out.println("切换失败");
                break;
        }
    }

    public static class VideoThread implements Runnable {
        //        static final int VIDEO_BUF_SIZE = 100000;// 预计视频buf大小
        static final int VIDEO_BUF_SIZE = 500000;// 预计视频buf大小
        static final int FRAME_INFO_SIZE = 16;// 帧信息大小

        private int avIndex; // 需要传入的avIndex
        private CallbackContext callbackContext;
//         private BlockingDeque bq;
//         private Handler handler;
        public static boolean startReceive = false;

        public VideoThread(int avIndex,CallbackContext callbackContext) {
//         public VideoThread(int avIndex, BlockingDeque bq, Handler handler) {
            this.avIndex = avIndex;
            this.callbackContext = callbackContext;
//             this.bq = bq;
//             this.handler = handler;
        }

        @Override
        public void run() {
            AVAPIs av = new AVAPIs();
            byte[] frameInfo = new byte[FRAME_INFO_SIZE];
            int[] outBufSize = new int[1];
            int[] outFrameSize = new int[1];
            int[] outFrmInfoBufSize = new int[1];
            SaveFrames saveFrames = new SaveFrames();
            byte[] videoBuffer = null;
            while (true) {
                if(bolCheck == true){
                    mySendMessage("break",callbackContext);
                    break;
                }
                try {
                    videoBuffer = new byte[VIDEO_BUF_SIZE];  // 用来存取视频帧
                } catch (Exception e) {
                    videoBuffer = null;
                    continue;
                }
                int[] frameNumber = new int[1];
                // 返回结果为接收视频videoBuffer的实际长度
                int ret = av.avRecvFrameData2(avIndex, videoBuffer,
                        VIDEO_BUF_SIZE, outBufSize, outFrameSize,
                        frameInfo, FRAME_INFO_SIZE,
                        outFrmInfoBufSize, frameNumber);

                if (ret == AVAPIs.AV_ER_DATA_NOREADY) {//The data is not ready for receiving yet.
                    try {
                        Thread.sleep(30);
                        continue;
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                } else if (ret == AVAPIs.AV_ER_LOSED_THIS_FRAME) {//The whole frame is lost during receiving
                    System.out.printf("[%s] Lost video frame number[%d]\n",
                            Thread.currentThread().getName(), frameNumber[0]);
                    continue;
                } else if (ret == AVAPIs.AV_ER_INCOMPLETE_FRAME) {//Some parts of a frame are lost during receiving
                    System.out.printf("[%s] Incomplete video frame number[%d]\n",
                            Thread.currentThread().getName(), frameNumber[0]);
                    continue;
                } else if (ret == AVAPIs.AV_ER_SESSION_CLOSE_BY_REMOTE) {//The remote site already closes the IOTC session. Please call IOTC_Session_Close() to release local IOTC session resource
                    System.out.printf("[%s] AV_ER_SESSION_CLOSE_BY_REMOTE\n",
                            Thread.currentThread().getName());
                    break;
                } else if (ret == AVAPIs.AV_ER_REMOTE_TIMEOUT_DISCONNECT) {//This IOTC session is disconnected because remote site has no any response after a specified timeout expires.
                    System.out.printf("[%s] AV_ER_REMOTE_TIMEOUT_DISCONNECT\n",
                            Thread.currentThread().getName());
                    break;
                } else if (ret == AVAPIs.AV_ER_INVALID_SID) {//The IOTC session of specified AV channel is not valid
                    System.out.printf("[%s] Session cant be used anymore\n",
                            Thread.currentThread().getName());
                    break;
                }

                if (ret == AVAPIs.AV_ER_BUFPARA_MAXSIZE_INSUFF) {//The buffer to receive frame is too small to store one frame
//                     mySendMessage(handler, -5, System.currentTimeMillis() + "AV_ER_BUFPARA_MAXSIZE_INSUFF:" + videoBuffer[4] + "|" + videoBuffer[5] + "|" + videoBuffer[6]);
                }

                // Now the data is ready in videoBuffer[0 ... ret - 1]
                // Do something here
                //----------------------把videobuffer信息加入阻塞队列----------------------
                mySendMessage(videoBuffer,callbackContext);
//                 try {
//                     BufferInfo bi = new BufferInfo(outFrameSize[0], videoBuffer);
//                     bq.offer(bi);
//                 } catch (Exception e) {
//                 }
//                 //---------------------------------------------------------------------
//                 if (startReceive) {
//                     saveFrames.saveFrames(videoBuffer, frameInfo, ret);
//                 } else {
//                     saveFrames.stopReceive();
//                 }
//                }
                videoBuffer = null;
            }
        }
    }

    private static void mySendMessage(String str,CallbackContext callbackContext) {
        //String
        //JSONArray
        //JSONObject
        //int
        //float
        //boolean
        //byte[]
        //List<PluginResult>
        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, str);
        pluginResult.setKeepCallback(true); // keep callback
        callbackContext.sendPluginResult(pluginResult);
    }
    private static void mySendMessage(byte[] bytes,CallbackContext callbackContext) {
        PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, bytes);
        pluginResult.setKeepCallback(true); // keep callback
        callbackContext.sendPluginResult(pluginResult);
    }
}
