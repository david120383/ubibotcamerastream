package com.tutk.sample.AVAPI;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingDeque;

import com.camera.model.User;
import com.decode.tools.BufferInfo;

import java.util.concurrent.BlockingDeque;

public class MainActivity extends Activity implements ClearData {

    private String UID = "GV4GRAS1S2XJY3F1111A";
    //    public static BlockingDeque<BufferInfo> bq;
    List<User> itemList = new ArrayList<>();
    User user;
    CameraAdapter adapter;
    String regularEx = "#";

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(MainActivity.this);

        initSaveOnClickListener();

        Intent intent = getIntent();
        String addflg = intent.getStringExtra("addflg");

        getUserInfo();
        if (addflg != null) {
            String name = intent.getStringExtra("name");
            String uid = intent.getStringExtra("uid");
            String account = intent.getStringExtra("account");
            String password = intent.getStringExtra("password");
            if (addflg.equals("1")) {
                addUserInfo(name, uid, account, password);
            } else {
                String index = intent.getStringExtra("listindexupdate");
                updateUserInfo(Integer.parseInt(index), name, uid, account, password);
            }
        } else {

        }
        initRecyclerView();
    }

    @Override
    public void clear(String key) {
        SharedPreferences userInfo = getSharedPreferences(UID, MODE_PRIVATE);
        String primarykey = userInfo.getString("primarykey", null);
        String[] primarykeyarray = null;
        String[] primarykeyarraynew = null;
        if (primarykey != null) {
            primarykeyarray = primarykey.split(regularEx);
            primarykeyarraynew = new String[primarykeyarray.length - 1];
            int arrayindex = 0;
            for (String idold : primarykeyarray) {
                if (idold.equals(key)) {
                } else {
                    primarykeyarraynew[arrayindex] = idold;
                    arrayindex += 1;
                }
            }
            SharedPreferences.Editor editor = userInfo.edit();//获取Editor
            editor.putString("primarykey", setSharedPreference(primarykeyarraynew));
            editor.remove("name" + key);
            editor.remove("uid" + key);
            editor.remove("account" + key);
            editor.remove("password" + key);
            editor.apply();
            initRecyclerView();
            adapter.notifyDataSetChanged();
        }
    }

    private void initSaveOnClickListener() {
        TextView save = (TextView) findViewById(R.id.add_camera);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initRecyclerView() {
        RecyclerView rc = findViewById(R.id.page_rec);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rc.setLayoutManager(linearLayoutManager);
//        adapter = new CameraAdapter(this, itemList, this);
        adapter = new CameraAdapter(this, itemList, this);
        rc.setAdapter(adapter);
    }

    /***
     * 更新摄像头数据
     * @param indexupdate 需要更新的itemList的index
     * @param name
     * @param uid
     * @param account
     * @param password
     */
    private void updateUserInfo(int indexupdate, String name, String uid, String account, String password) {
        String idupdate = itemList.get(indexupdate).getID();//需要更新的主键值
        SharedPreferences userInfo = getSharedPreferences(UID, MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();//获取Editor
        editor.putString("name" + idupdate, name);
        editor.putString("uid" + idupdate, uid);
        editor.putString("account" + idupdate, account);
        editor.putString("password" + idupdate, password);
        editor.apply();//提交修改
        itemList.get(indexupdate).setName(name);
        itemList.get(indexupdate).setUID(uid);
        itemList.get(indexupdate).setAccount(account);
        itemList.get(indexupdate).setPassword(password);
    }

    /***
     * 新增摄像头数据
     * @param name
     * @param uid
     * @param account
     * @param password
     * 摄像头数据存储在SharedPreferences的键值对中
     * 键值对primarykey存储主键信息，存储格式为【0#1#2#......N#】 N为主键
     * 键值对nameN存储摄像头名称信息，N为主键
     * 键值对uidN存储UID信息，N为主键
     * 键值对accountN存储账号信息，N为主键
     * 键值对passwordN存储密码信息，N为主键
     */
    private void addUserInfo(String name, String uid, String account, String password) {
        SharedPreferences userInfo = getSharedPreferences(UID, MODE_PRIVATE);
        String primarykey = userInfo.getString("primarykey", null);//主键字符串
        String key = "";
        String[] primarykeyarray = null;//添加数据前的主键数组
        String[] primarykeyarraynew = null;//添加数据后的主键数组
        if (primarykey == null || primarykey.equals("")) {
            key = "0";
            primarykeyarraynew = new String[1];
            primarykeyarraynew[0] = "0";
        } else {
            primarykeyarray = primarykey.split(regularEx);
            key = String.valueOf(primarykeyarray.length);
            primarykeyarraynew = new String[primarykeyarray.length + 1];
            for (int i = 0; i < primarykeyarray.length; i++) {
                primarykeyarraynew[i] = primarykeyarray[i];
            }
            primarykeyarraynew[primarykeyarray.length] = key;
        }
        SharedPreferences.Editor editor = userInfo.edit();//获取Editor
        editor.putString("primarykey", setSharedPreference(primarykeyarraynew));//得到Editor后，写入需要保存的数据
        editor.putString("name" + key, name);
        editor.putString("uid" + key, uid);
        editor.putString("account" + key, account);
        editor.putString("password" + key, password);
        editor.apply();//提交修改

        user = new User(key, name, uid, account, password);
        itemList.add(user);
    }

    private void getUserInfo() {
        SharedPreferences userInfo = getSharedPreferences(UID, MODE_PRIVATE);
        String primarykey = userInfo.getString("primarykey", null);
        if (primarykey != null && !primarykey.equals("")) {
            String[] primarykeyarray = primarykey.split(regularEx);
            for (String key : primarykeyarray) {
                String name = userInfo.getString("name" + key, null);
                String uid = userInfo.getString("uid" + key, null);
                String account = userInfo.getString("account" + key, null);
                String password = userInfo.getString("password" + key, null);
                user = new User(key, name, uid, account, password);
                itemList.add(user);
            }
        }
    }

    private void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String setSharedPreference(String[] values) {
        String str = "";
        if (values != null && values.length > 0) {
            for (String value : values) {
                str += value;
                str += regularEx;
            }
        }
        return str;
    }

}
