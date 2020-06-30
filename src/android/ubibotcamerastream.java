package com.kit.cordova.ubibotcamerastream;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.PluginResult;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
// import org.apache.cordova.api.Plugin;
// import org.apache.cordova.api.PluginResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

// import com.kit.cordova.nativeLocation.com.camera.api.AVAPIsClient;
// import com.kit.cordova.nativeLocation.com.decode.MediaCodecDecoder;
// import com.kit.cordova.nativeLocation.com.decode.tools.BufferInfo;
import com.camera.api.AVAPIsClient;
import com.decode.MediaCodecDecoder;
import com.decode.tools.BufferInfo;

/**
 * This class echoes a string called from JavaScript.
 */
public class ubibotcamerastream extends CordovaPlugin {

    private String UID;
    private String account;
    private String pwd;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("getversion")) {
            String message = "1.0.2";
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, message);
            callbackContext.sendPluginResult(pluginResult);
            return true;
        }
        if (action.equals("stop")) {
             UbibotClient.stop();
             String message = args.getString(0);
             PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, message);
             callbackContext.sendPluginResult(pluginResult);
             return true;
         }
        if (action.equals("start")){
            String str = args.getString(0);
            String[] strArr = str.split("\\|");
            UID = strArr[0];
            account = strArr[1];
            pwd = strArr[2];
            cordova.getThreadPool().execute(new Runnable(){
                public void run(){
                    UbibotClient.start(UID,account,pwd,callbackContext);
                }
            });
            return true;
        }
        return false;
    }
}
