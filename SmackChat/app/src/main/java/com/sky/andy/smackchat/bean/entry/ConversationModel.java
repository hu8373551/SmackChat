package com.sky.andy.smackchat.bean.entry;

/**
 * Created by hcc on 16-11-3.
 * Company MingThink
 */

public class ConversationModel {
    private String c_jid;
    private String c_name;
    private String c_last_message;
    private int c_unread;
    private long c_time;
    private byte[] c_head_image;
    private String c_current_user_jid;

    public String getC_current_user_jid() {
        return c_current_user_jid;
    }

    public void setC_current_user_jid(String c_current_user_jid) {
        this.c_current_user_jid = c_current_user_jid;
    }

    public byte[] getC_head_image() {
        return c_head_image;
    }

    public void setC_head_image(byte[] c_head_image) {
        this.c_head_image = c_head_image;
    }

    public String getC_jid() {
        return c_jid;
    }

    public void setC_jid(String c_jid) {
        this.c_jid = c_jid;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getC_last_message() {
        return c_last_message;
    }

    public void setC_last_message(String c_last_message) {
        this.c_last_message = c_last_message;
    }

    public int getC_unread() {
        return c_unread;
    }

    public void setC_unread(int c_unread) {
        this.c_unread = c_unread;
    }

    public long getC_time() {
        return c_time;
    }

    public void setC_time(long c_time) {
        this.c_time = c_time;
    }
}
