package com.sky.andy.smackchat.bean.entry;

/**
 * Created by hcc on 16-11-9.
 * Company MingThink
 */

public class FriendEntryModel {
    private String jid;
    private String fullJid;
    private String name;
    private byte[] imageHead;
    private String currentUserJid;

    public String getJid() {
        return jid;
    }

    public void setJid(String jid) {
        this.jid = jid;
    }

    public String getFullJid() {
        return fullJid;
    }

    public void setFullJid(String fullJid) {
        this.fullJid = fullJid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImageHead() {
        return imageHead;
    }

    public void setImageHead(byte[] imageHead) {
        this.imageHead = imageHead;
    }

    public String getCurrentUserJid() {
        return currentUserJid;
    }

    public void setCurrentUserJid(String currentUserJid) {
        this.currentUserJid = currentUserJid;
    }
}
