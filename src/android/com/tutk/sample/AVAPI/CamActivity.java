package com.tutk.sample.AVAPI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.camera.api.AVAPIsClient;
import com.decode.MediaCodecDecoder;
import com.decode.tools.BufferInfo;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class CamActivity extends Activity {
    //    static final String UID = "866J62HYSHPRYJ19111A";
    //    static final String UID = "XRZSUNKGSHLKX1LD111A";
    public static BlockingDeque<BufferInfo> bq;
    private MediaCodecDecoder mediaCodecDecoder; //解码器
    private SurfaceView surfaceViewDecode; // 视频播放绑定的surface
    private TextView tvMsg;
    private TextView tvName;
    private TextView definition;
    private Thread t1;
    private Handler handler;
    private String password = "";
    private String account = "";
    private String uid = "";
    private String name = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        uid = intent.getStringExtra("uid");
        account = intent.getStringExtra("account");
        password = intent.getStringExtra("password");

        tvMsg = findViewById(R.id.tv_msg1);
        tvName = findViewById(R.id.tv_canemraname);
        tvName.setText(name);
        definition = findViewById(R.id.camera_definition);
        bq = new LinkedBlockingDeque<>();

        initMediaCodecDecoder();//实例化解码器
        initSurfaceViewDecode();//SurfaceView绘制完成后 配置解码器并启动

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == -3 && Integer.parseInt(msg.obj.toString()) == -20010) {
                    tvMsg.setText(getTime() + " :设备离线");
                } else {
                    tvMsg.setText(getTime() + " :" + msg.obj.toString());
                }
            }
        };

//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        int heapSize = manager.getMemoryClass();
//        tvMsg.setText(getTime() + "|ACTIVITY_SERVICE:" + heapSize + "MB");

        ImageView tv = (ImageView) findViewById(R.id.tv_startclient);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvMsg.setText(getTime() + "|" + "start Thread");
                (new Thread() {
                    public void run() {
//                        UbibotClient.start(CamActivity.this.UID, bq, handler);
                        UbibotClient.start(uid, account, password, bq, handler);
                    }
                }).start();
            }
        });
        definition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPopWindow(v);
            }
        });

        // 此线程从阻塞队列poll buffer信息并送入解码器
        t1 = new Thread() {
            public void run() {
                BufferInfo temp;
                while (true) {
                    // 响应中断
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Java技术栈线程被中断，程序退出。");
                        return;
                    }
                    try {
                        temp = bq.poll(3000, TimeUnit.MILLISECONDS);
                        if (temp == null) {
                            continue;
                        }
//                        String strMsg = "";
//                        byte[] videoBuffer = temp.buffer;
//                        if (videoBuffer[4] == 0x00) {
////                            strMsg = "0x00";
//                            temp = null;
//                        }
//                        if (temp != null) {
//                            mySendMessage(handler, 0, getTime() + "|" + videoBuffer[4] + "|" + temp.len);
                        mySendMessage(handler, 0, temp.len);
                        // 向解码器输入buffer
                        mediaCodecDecoder.input(temp.buffer, temp.len, System.nanoTime() / 1000);
                        mediaCodecDecoder.output();
//                        }
                    } catch (Exception e) {
                        mySendMessage(handler, -6, getTime() + "|Exception:" + e.getMessage());
                    }
                }
            }
        };
        t1.start();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void initPopWindow(View view) {
        View view1 = LayoutInflater.from(this).inflate(R.layout.definition_window, null);
        TextView superclear = view1.findViewById(R.id.superclear);
        TextView highclear = view1.findViewById(R.id.highclear);
        TextView standard = view1.findViewById(R.id.standardclear);
        TextView fluency = view1.findViewById(R.id.fluency);
        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        final PopupWindow popWindow = new PopupWindow(view1,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popWindow.setAnimationStyle(R.anim.window_pop);  //设置加载动画
        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //要为popWindow设置一个背景才有效
        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAsDropDown(view, -6, -330);
        //设置点击事件
        superclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVAPIsClient.setQuality(3);
                popWindow.dismiss();
                definition.setText("超清");
//                bq.clear();
                mediaCodecDecoder.setQuanlity(3);
            }
        });
        highclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVAPIsClient.setQuality(2);
                popWindow.dismiss();
                definition.setText("高清");
//                bq.clear();
                mediaCodecDecoder.setQuanlity(2);
            }
        });
        standard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVAPIsClient.setQuality(1);
                popWindow.dismiss();
                definition.setText("标清");
//                bq.clear();
                mediaCodecDecoder.setQuanlity(1);
            }
        });
        fluency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AVAPIsClient.setQuality(0);
                popWindow.dismiss();
                definition.setText("流畅");
//                bq.clear();
                mediaCodecDecoder.setQuanlity(0);
            }
        });
    }

    private void mySendMessage(Handler handler, int what, Object obj) {
        Message msg = new Message();
        msg.what = what;
        msg.obj = obj;
        handler.sendMessage(msg);
    }

    private void initMediaCodecDecoder() {
        mediaCodecDecoder = new MediaCodecDecoder();//实例化解码器

        try {
            mediaCodecDecoder.init();// 初始化
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initSurfaceViewDecode() {
        surfaceViewDecode = findViewById(R.id.camera_video);// 绑定surfaceview
        // surfaceView绘制完成后 配置解码器并启动
        surfaceViewDecode.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                mediaCodecDecoder.configure(surfaceViewDecode.getHolder().getSurface());// 配置解码器
                mediaCodecDecoder.start();// 启动解码器
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                mediaCodecDecoder.release();
            }
        });
    }

    private String getTime() {
        Time t = new Time(); // or Time t=new Time("GMT+8"); 加上Time Zone资料。
        t.setToNow(); // 取得系统时间。
        int year = t.year;
        int month = t.month + 1;
        int day = t.monthDay;
        int hour = t.hour; // 0-23
        int minute = t.minute;
        int second = t.second;
        return String.format("%04d", year) +
                String.format("%02d", month) +
                String.format("%02d", day) +
                String.format("%02d", hour) +
                String.format("%02d", minute) +
                String.format("%02d", second);

    }

}
