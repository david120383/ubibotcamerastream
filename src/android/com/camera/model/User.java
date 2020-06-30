// package com.kit.cordova.nativeLocation.com.camera.model;
package com.camera.model;

import android.content.SharedPreferences;

// 单例
public class User {
    private static User user = null;
    private String id;
    private String name;
    private String UID;
    private String account;
    private String password;

    public User(String id, String name, String UID, String account, String password) {
        this.id = id;
        this.name = name;
        this.UID = UID;
        this.account = account;
        this.password = password;
    }

    public String getID() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUID() {
        return this.UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
