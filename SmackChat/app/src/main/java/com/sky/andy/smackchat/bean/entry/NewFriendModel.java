package com.sky.andy.smackchat.bean.entry;

import java.util.Arrays;

/**
 * Created by hcc on 16-11-15.
 * Company MingThink
 */

public class NewFriendModel {
    private String n_jid;
    private String n_full_jid;
    private byte[] n_head_image;
    private String n_name;
    private String n_info;
    private int n_status;
    private String n_current_jid;

    public String getN_info() {
        return n_info;
    }

    public void setN_info(String n_info) {
        this.n_info = n_info;
    }

    public String getN_current_jid() {
        return n_current_jid;
    }

    public void setN_current_jid(String n_current_jid) {
        this.n_current_jid = n_current_jid;
    }

    public String getN_jid() {
        return n_jid;
    }

    public void setN_jid(String n_jid) {
        this.n_jid = n_jid;
    }

    public String getN_full_jid() {
        return n_full_jid;
    }

    public void setN_full_jid(String n_full_jid) {
        this.n_full_jid = n_full_jid;
    }

    public byte[] getN_head_image() {
        return n_head_image;
    }

    public void setN_head_image(byte[] n_head_image) {
        this.n_head_image = n_head_image;
    }

    public String getN_name() {
        return n_name;
    }

    public void setN_name(String n_name) {
        this.n_name = n_name;
    }

    public int getN_status() {
        return n_status;
    }

    public void setN_status(int n_status) {
        this.n_status = n_status;
    }

    @Override
    public String toString() {
        return "NewFriendModel{" +
                "n_jid='" + n_jid + '\'' +
                ", n_full_jid='" + n_full_jid + '\'' +
                ", n_head_image=" + Arrays.toString(n_head_image) +
                ", n_name='" + n_name + '\'' +
                ", n_info='" + n_info + '\'' +
                ", n_status=" + n_status +
                ", n_current_jid='" + n_current_jid + '\'' +
                '}';
    }
}
