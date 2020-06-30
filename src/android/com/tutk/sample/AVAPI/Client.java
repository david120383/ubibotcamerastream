package com.tutk.sample.AVAPI;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.camera.api.G711Code;
import com.camera.model.Audio;
import com.decode.tools.BufferInfo;
import com.camera.model.SaveFrames;

import java.util.Arrays;
import java.util.concurrent.BlockingDeque;

import com.tutk.IOTC.IOTCAPIs;
import com.tutk.IOTC.AVAPIs;
import com.tutk.IOTC.St_AVClientStartInConfig;
import com.tutk.IOTC.St_AVClientStartOutConfig;

public class Client {
//    public static void start(String uid, Handler handler) {
//
//        System.out.println("StreamClient start...");
//        mySendMessage(handler, 0, "StreamClient start...");
//
//        int ret = IOTCAPIs.IOTC_Initialize2(0);
//        System.out.printf("IOTC_Initialize2() ret = %d\n", ret);
//        mySendMessage(handler, 0, "IOTC_Initialize2() ret = " + Integer.toString(ret));
//        if (ret != IOTCAPIs.IOTC_ER_NoERROR) {
//            System.out.printf("IOTCAPIs_Device exit...!!\n");
//            mySendMessage(handler, 0, "IOTCAPIs_Device exit...!!\n");
//            return;
//        }
//
//        // alloc 3 sessions for video and two-way audio
//        AVAPIs.avInitialize(3);
//
//        int sid = IOTCAPIs.IOTC_Get_SessionID();
//        if (sid < 0) {
//            System.out.printf("IOTC_Get_SessionID error code [%d]\n", sid);
//            mySendMessage(handler, 0, "IOTC_Get_SessionID error code " + Integer.toString(sid));
//            return;
//        }
//        ret = IOTCAPIs.IOTC_Connect_ByUID_Parallel(uid, sid);
//        System.out.printf("Step 2: call IOTC_Connect_ByUID_Parallel(%s).......\n", uid);
//        mySendMessage(handler, 0, "Step 2: call IOTC_Connect_ByUID_Parallel " + (uid));
//
//        int srvType = 0;
//        int bResend = 0;
//        int avIndex = 0;
//        St_AVClientStartInConfig av_client_in_config = new St_AVClientStartInConfig();
//        St_AVClientStartOutConfig av_client_out_config = new St_AVClientStartOutConfig();
//
//        av_client_in_config.iotc_session_id = sid;
//        av_client_in_config.iotc_channel_id = 0;
//        av_client_in_config.timeout_sec = 20;
//        av_client_in_config.account_or_identity = "admin";
//        av_client_in_config.password_or_token = "ipc12345";
//        av_client_in_config.resend = 1;
////        av_client_in_config.security_mode = 1; //enable DTLS
//        av_client_in_config.security_mode = 0; //enable DTLS
//        av_client_in_config.auth_type = 0;
//
//        avIndex = AVAPIs.avClientStartEx(av_client_in_config, av_client_out_config);
//        bResend = av_client_out_config.resend;
//        srvType = av_client_out_config.server_type;
//        System.out.printf("Step 2: call avClientStartEx(%d).......\n", avIndex);
//        mySendMessage(handler, 0, "Step 2: call avClientStartEx " + Integer.toString(avIndex));
//
//        if (avIndex < 0) {
//            System.out.printf("avClientStartEx failed[%d]\n", avIndex);
//            mySendMessage(handler, 0, "avClientStartEx failed " + Integer.toString(avIndex));
//            return;
//        }
//
//        if (startIpcamStream(avIndex)) {
//            Thread videoThread = new Thread(new VideoThread(avIndex, handler),
//                    "Video Thread");
//            Thread audioThread = new Thread(new AudioThread(avIndex, handler),
//                    "Audio Thread");
//            videoThread.start();
//            audioThread.start();
//            try {
//                videoThread.join();
//            } catch (InterruptedException e) {
//                System.out.println(e.getMessage());
//                return;
//            }
//            try {
//                audioThread.join();
//            } catch (InterruptedException e) {
//                System.out.println(e.getMessage());
//                return;
//            }
//        }
//
//        AVAPIs.avClientStop(avIndex);
//        System.out.printf("avClientStop OK\n");
//        mySendMessage(handler, 0, "avClientStop OK...");
//        IOTCAPIs.IOTC_Session_Close(sid);
//        System.out.printf("IOTC_Session_Close OK\n");
//        mySendMessage(handler, 0, "IOTC_Session_Close OK");
//        AVAPIs.avDeInitialize();
//        IOTCAPIs.IOTC_DeInitialize();
//        System.out.printf("StreamClient exit...\n");
//        mySendMessage(handler, 0, "StreamClient exit...");
//    }

    public void start(String uid, BlockingDeque bq) {
//        public static void start(String uid, BlockingDeque bq) {
        System.out.println("StreamClient start...");
//        mySendMessage(handler, 0, "StreamClient start...");

        int ret = IOTCAPIs.IOTC_Initialize2(0);
        System.out.printf("IOTC_Initialize2() ret = %d\n", ret);
//        mySendMessage(handler, 0, "IOTC_Initialize2() ret = " + Integer.toString(ret));
        if (ret != IOTCAPIs.IOTC_ER_NoERROR) {
            System.out.printf("IOTCAPIs_Device exit...!!\n");
//            mySendMessage(handler, 0, "IOTCAPIs_Device exit...!!\n");
            return;
        }

        // alloc 3 sessions for video and two-way audio
        AVAPIs.avInitialize(3);

        int sid = IOTCAPIs.IOTC_Get_SessionID();
        if (sid < 0) {
            System.out.printf("IOTC_Get_SessionID error code [%d]\n", sid);
//            mySendMessage(handler, 0, "IOTC_Get_SessionID error code " + Integer.toString(sid));
            return;
        }
        ret = IOTCAPIs.IOTC_Connect_ByUID_Parallel(uid, sid);
        System.out.printf("Step 2: call IOTC_Connect_ByUID_Parallel(%s).......\n", uid);
//        mySendMessage(handler, 0, "Step 2: call IOTC_Connect_ByUID_Parallel " + (uid));

        int srvType = 0;
        int bResend = 0;
        int avIndex = 0;
        St_AVClientStartInConfig av_client_in_config = new St_AVClientStartInConfig();
        St_AVClientStartOutConfig av_client_out_config = new St_AVClientStartOutConfig();

        av_client_in_config.iotc_session_id = sid;
        av_client_in_config.iotc_channel_id = 0;
        av_client_in_config.timeout_sec = 20;
        av_client_in_config.account_or_identity = "admin";
        av_client_in_config.password_or_token = "ipc12345";
        av_client_in_config.resend = 1;
//        av_client_in_config.security_mode = 1; //enable DTLS
        av_client_in_config.security_mode = 0; //enable DTLS
        av_client_in_config.auth_type = 0;

        avIndex = AVAPIs.avClientStartEx(av_client_in_config, av_client_out_config);
        bResend = av_client_out_config.resend;
        srvType = av_client_out_config.server_type;
        System.out.printf("Step 2: call avClientStartEx(%d).......\n", avIndex);
//        mySendMessage(handler, 0, "Step 2: call avClientStartEx " + Integer.toString(avIndex));

        if (avIndex < 0) {
            System.out.printf("avClientStartEx failed[%d]\n", avIndex);
//            mySendMessage(handler, 0, "avClientStartEx failed " + Integer.toString(avIndex));
            return;
        }

        if (startIpcamStream(avIndex)) {
//            Thread videoThread = new Thread(new VideoThread(avIndex, handler), "Video Thread");
            Thread videoThread = new Thread(new VideoThread(avIndex, bq), "Video Thread");
//            Thread audioThread = new Thread(new AudioThread(avIndex, handler), "Audio Thread");
//            Thread audioThread = new Thread(new AudioThread(avIndex), "Audio Thread");
            videoThread.start();
//            audioThread.start();
            try {
                videoThread.join();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
                return;
            }
//            try {
//                audioThread.join();
//            } catch (InterruptedException e) {
//                System.out.println(e.getMessage());
//                return;
//            }
        }

        AVAPIs.avClientStop(avIndex);
        System.out.printf("avClientStop OK\n");
//        mySendMessage(handler, 0, "avClientStop OK...");
        IOTCAPIs.IOTC_Session_Close(sid);
        System.out.printf("IOTC_Session_Close OK\n");
//        mySendMessage(handler, 0, "IOTC_Session_Close OK");
        AVAPIs.avDeInitialize();
        IOTCAPIs.IOTC_DeInitialize();
        System.out.printf("StreamClient exit...\n");
//        mySendMessage(handler, 0, "StreamClient exit...");
    }

    public static boolean startIpcamStream(int avIndex) {
        AVAPIs av = new AVAPIs();
        int ret = av.avSendIOCtrl(avIndex, AVAPIs.IOTYPE_INNER_SND_DATA_DELAY,
                new byte[2], 2);
        if (ret < 0) {
            System.out.printf("start_ipcam_stream failed[%d]\n", ret);
            return false;
        }

        // This IOTYPE constant and its corrsponsing data structure is defined in
        // Sample/Linux/Sample_AVAPIs/AVIOCTRLDEFs.h
        //
        int IOTYPE_USER_IPCAM_START = 0x1FF;
        ret = av.avSendIOCtrl(avIndex, IOTYPE_USER_IPCAM_START,
                new byte[8], 8);
        if (ret < 0) {
            System.out.printf("start_ipcam_stream failed[%d]\n", ret);
            return false;
        }

        int IOTYPE_USER_IPCAM_AUDIOSTART = 0x300;
        ret = av.avSendIOCtrl(avIndex, IOTYPE_USER_IPCAM_AUDIOSTART,
                new byte[8], 8);
        if (ret < 0) {
            System.out.printf("start_ipcam_stream failed[%d]\n", ret);
            return false;
        }

        return true;
    }

    public class VideoThread implements Runnable {
        //    public static class VideoThread implements Runnable {
        //https://github.com/aa82579920/CameraWithTUTK/blob/162d68bdbe63970c729c3071e890b13e106621e3/app/src/main/java/com/camera/camerawithtutk/VideoThread.java
        static final int VIDEO_BUF_SIZE = 100000;// 预计视频buf大小
        //        static final int VIDEO_BUF_SIZE = 50000;// 预计视频buf大小
        static final int FRAME_INFO_SIZE = 16;// 帧信息大小

        private int avIndex; // 需要传入的avIndex
        private BlockingDeque bq;
        private Handler handler;
        //        public static boolean startReceive = false;
        public boolean startReceive = false;

        public VideoThread(int avIndex, BlockingDeque bq) {
            this.avIndex = avIndex;
            this.bq = bq;
        }

        public VideoThread(int avIndex, Handler handler) {
            this.avIndex = avIndex;
            this.handler = handler;
            mySendMessage(this.handler, 0, "Video Thread step 1...");
        }

        @Override
        public void run() {
            System.out.printf("[%s] Start\n", Thread.currentThread().getName());
            AVAPIs av = new AVAPIs();
            byte[] frameInfo = new byte[FRAME_INFO_SIZE];
            int[] outBufSize = new int[1];
            int[] outFrameSize = new int[1];
            int[] outFrmInfoBufSize = new int[1];
            SaveFrames saveFrames = new SaveFrames();
            byte[] videoBuffer = null;
            while (true) {
                videoBuffer = null;
                videoBuffer = new byte[VIDEO_BUF_SIZE];  // 用来存取视频帧
                int[] frameNumber = new int[1];
                // 返回结果为接收视频videoBuffer的实际长度
                int ret = av.avRecvFrameData2(avIndex, videoBuffer,
                        VIDEO_BUF_SIZE, outBufSize, outFrameSize,
                        frameInfo, FRAME_INFO_SIZE,
                        outFrmInfoBufSize, frameNumber);

                if (ret == AVAPIs.AV_ER_DATA_NOREADY) {
                    try {
                        Thread.sleep(30);
                        continue;
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                } else if (ret == AVAPIs.AV_ER_LOSED_THIS_FRAME) {
                    System.out.printf("[%s] Lost video frame number[%d]\n",
                            Thread.currentThread().getName(), frameNumber[0]);
                    continue;
                } else if (ret == AVAPIs.AV_ER_INCOMPLETE_FRAME) {
                    System.out.printf("[%s] Incomplete video frame number[%d]\n",
                            Thread.currentThread().getName(), frameNumber[0]);
                    continue;
                } else if (ret == AVAPIs.AV_ER_SESSION_CLOSE_BY_REMOTE) {
                    System.out.printf("[%s] AV_ER_SESSION_CLOSE_BY_REMOTE\n",
                            Thread.currentThread().getName());
                    break;
                } else if (ret == AVAPIs.AV_ER_REMOTE_TIMEOUT_DISCONNECT) {
                    System.out.printf("[%s] AV_ER_REMOTE_TIMEOUT_DISCONNECT\n",
                            Thread.currentThread().getName());
                    break;
                } else if (ret == AVAPIs.AV_ER_INVALID_SID) {
                    System.out.printf("[%s] Session cant be used anymore\n",
                            Thread.currentThread().getName());
                    break;
                }
                // Now the data is ready in videoBuffer[0 ... ret - 1]
                // Do something here
                //----------------------把videobuffer信息加入阻塞队列----------------------
                try {
                    BufferInfo bi = new BufferInfo(outFrameSize[0], videoBuffer);
                    bq.offer(bi);
//                    System.out.println("BufferInfoBufferInfoBufferInfoBufferInfoBufferInfoBufferInfoBufferInfoBufferInfoBufferInfoBufferInfo");
//                    System.out.printf("BufferInfoBufferInfoBufferInfoBufferInfoBufferInfoBufferInfoBufferInfoBufferInfoBufferInfoBufferInfo");
                } catch (Exception e) {
//                    e.printStackTrace();
                }
                //---------------------------------------------------------------------
                if (startReceive) {
                    saveFrames.saveFrames(videoBuffer, frameInfo, ret);
                } else {
                    saveFrames.stopReceive();
                }
                videoBuffer = null;
//                System.gc();
            }
            System.out.printf("[%s] Exit\n", Thread.currentThread().getName());
        }
    }

    public static class AudioThread implements Runnable {
        private AudioTrack mAudioTrack;
        //音频流类型
        private static final int mStreamType = AudioManager.STREAM_MUSIC;
        //指定采样率 （MediaRecoder 的采样率通常是8000Hz AAC的通常是44100Hz。 设置采样率为44100，目前为常用的采样率，官方文档表示这个值可以兼容所有的设置）
        private static final int mSampleRateInHz = 8000;
        //指定捕获音频的声道数目。在AudioFormat类中指定用于此的常量
        private static final int mChannelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO; //单声道
        //指定音频量化位数 ,在AudioFormaat类中指定了以下各种可能的常量。通常我们选择ENCODING_PCM_16BIT和ENCODING_PCM_8BIT PCM代表的是脉冲编码调制，它实际上是原始音频样本。
        //因此可以设置每个样本的分辨率为16位或者8位，16位将占用更多的空间和处理能力,表示的音频也更加接近真实。
        private static final int mAudioFormat = AudioFormat.ENCODING_PCM_16BIT;
        //指定缓冲区大小。调用AudioRecord类的getMinBufferSize方法可以获得。
        private int mMinBufferSize;
        //STREAM的意思是由用户在应用程序通过write方式把数据一次一次得写到audiotrack中。这个和我们在socket中发送数据一样，
        // 应用层从某个地方获取数据，例如通过编解码得到PCM数据，然后write到audiotrack。
        private static int mMode = AudioTrack.MODE_STREAM;

        static final int AUDIO_BUF_SIZE = 1024;
        static final int FRAME_INFO_SIZE = 16;

        private int avIndex;
        //        private BlockingDeque bq;
        private Handler handler;

        public AudioThread(int avIndex) {
            this.avIndex = avIndex;
            initData();
        }

        public AudioThread(int avIndex, Handler handler) {
            this.avIndex = avIndex;
            this.handler = handler;
        }

        private void initData() {
            //根据采样率，采样精度，单双声道来得到frame的大小。
            mMinBufferSize = AudioTrack.getMinBufferSize(mSampleRateInHz, mChannelConfig, mAudioFormat);//计算最小缓冲区
            //注意，按照数字音频的知识，这个算出来的是一秒钟buffer的大小。
            //创建AudioTrack
            mAudioTrack = new AudioTrack(mStreamType, mSampleRateInHz, mChannelConfig,
                    mAudioFormat, mMinBufferSize, mMode);
        }

        @Override
        public void run() {
            System.out.printf("[%s] Start\n",
                    Thread.currentThread().getName());

            AVAPIs av = new AVAPIs();
            byte[] frameInfo = new byte[FRAME_INFO_SIZE];
            byte[] audioBuffer = new byte[AUDIO_BUF_SIZE];
            Audio audio = new Audio();
            while (true) {
                int ret = av.avCheckAudioBuf(avIndex);

                if (ret < 0) {
                    // Same error codes as below
                    System.out.printf("[%s] avCheckAudioBuf() failed: %d\n",
                            Thread.currentThread().getName(), ret);
                    break;
                } else if (ret < 3) {
                    try {
                        Thread.sleep(120);
                        continue;
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                        break;
                    }
                }

                int[] frameNumber = new int[1];
                ret = av.avRecvAudioData(avIndex, audioBuffer,
                        AUDIO_BUF_SIZE, frameInfo, FRAME_INFO_SIZE,
                        frameNumber);

                if (ret == AVAPIs.AV_ER_SESSION_CLOSE_BY_REMOTE) {
                    System.out.printf("[%s] AV_ER_SESSION_CLOSE_BY_REMOTE\n",
                            Thread.currentThread().getName());
                    break;
                } else if (ret == AVAPIs.AV_ER_REMOTE_TIMEOUT_DISCONNECT) {
                    System.out.printf("[%s] AV_ER_REMOTE_TIMEOUT_DISCONNECT\n",
                            Thread.currentThread().getName());
                    break;
                } else if (ret == AVAPIs.AV_ER_INVALID_SID) {
                    System.out.printf("[%s] Session cant be used anymore\n",
                            Thread.currentThread().getName());
                    break;
                } else if (ret == AVAPIs.AV_ER_LOSED_THIS_FRAME) {
                    //System.out.printf("[%s] Audio frame losed\n",
                    //        Thread.currentThread().getName());
                    continue;
                }

                mySendMessage(this.handler, 88, audioBuffer);
                System.out.printf("[%s] Now the data is ready in audioBuffer[0 ... ret - 1] Do something here\n",
                        Thread.currentThread().getName());
                // Now the data is ready in audioBuffer[0 ... ret - 1]
                // Do something here
                //audio.getNewFrame(AudioThread.convertG711ToPCM(audioBuffer, ret, audio.getNewPCMBuf()));
                if (ret > 0) {
                    audio.getNewFrame(G711Code.G711aDecoder(new short[ret], audioBuffer, ret));
                    Log.d("audio1", Arrays.toString(G711Code.G711aDecoder(new short[ret], audioBuffer, ret)));
                    if (audio.getLength() == 10) {
                        System.out.println(audio);
                    }
                    if (mAudioTrack.getState() == AudioTrack.STATE_UNINITIALIZED) {
                        initData();
                    }
                    mAudioTrack.play();
                    mAudioTrack.write(G711Code.G711aDecoder(new short[ret], audioBuffer, ret), 0, ret);
                }
            }
            if (mAudioTrack != null) {
                if (mAudioTrack.getState() == AudioRecord.STATE_INITIALIZED) {//初始化成功
                    mAudioTrack.stop();//停止播放
                }
                if (mAudioTrack != null) {
                    mAudioTrack.release();//释放audioTrack资源
                }
            }

            System.out.printf("[%s] Exit\n",
                    Thread.currentThread().getName());
        }
    }

    public static void mySendMessage(Handler handler, int what, Object obj) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = obj;
        handler.sendMessage(msg);
    }

//    public static byte[] copyByteData(byte[] bArr, int i, int i2) {
//        if (i < 0 || i2 + i > bArr.length) {
//            return null;
//        }
//        byte[] bArr2 = new byte[i2];
//        System.arraycopy(bArr, i, bArr2, 0, i2);
//        return bArr2;
//    }

}
