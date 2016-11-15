package com.sky.andy.smackchat.bean;

/**
 * Created by hcc on 16-11-9.
 * Company MingThink
 */

public class UserInfo {
    private String bareJid;
    private String userName;
    private byte[] avatar;

    public String getBareJid() {
        return bareJid;
    }

    public void setBareJid(String bareJid) {
        this.bareJid = bareJid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }
}
