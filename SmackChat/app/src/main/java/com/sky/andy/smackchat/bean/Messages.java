package com.sky.andy.smackchat.bean;

import java.util.UUID;

/**
 * Created by hcc on 16-11-1.
 * Company MingThink
 */

public class Messages {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private String uuid;

    public String getUuid() {
        return uuid;
    }


    /**
     * 消息内容
     */
    private String content;
    /**
     * 消息类型，0：文本，1：图片，2：语音
     */
    private String type;
    /**
     * 消息发送人
     */
    private String username;
    /**
     * 消息发送接收的时间
     */
    private String datetime;
    /**
     * 当前消息是否是自己发出的
     */
    private boolean isSend;
    /**
     * 接收的图片或语音路径
     */
    private String filePath;
    /**
     * 文件加载状态,0:加载开始，1：加载成功，-1：加载失败
     */
    private int loadState = 0;

    // 头像
    private byte[] friend_avatar;

    public Messages() {
    }

    public Messages(String type, String username, String datetime, boolean isSend, byte[] friend_avatar) {
        super();
        this.type = type;
        this.username = username;
        this.datetime = datetime;
        this.isSend = isSend;
        this.uuid = UUID.randomUUID().toString();
        this.friend_avatar = friend_avatar;
    }


    public byte[] getFriend_avatar() {
        return friend_avatar;
    }

    public void setFriend_avatar(byte[] friend_avatar) {
        this.friend_avatar = friend_avatar;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getLoadState() {
        return loadState;
    }

    public void setLoadState(int loadState) {
        this.loadState = loadState;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o instanceof Messages) {
            return uuid.equals(((Messages) o).uuid);
        }
        return false;
    }

}
