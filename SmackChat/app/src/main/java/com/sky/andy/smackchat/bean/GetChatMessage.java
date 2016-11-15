package com.sky.andy.smackchat.bean;

/**
 * Created by hcc on 16-11-3.
 * Company MingThink
 * 接收的消息
 */

public class GetChatMessage {

    private String message;
    private String fromUser;
    private String type;
    private byte[] friend_avatar;
    private int unRead;

    public int getUnRead() {
        return unRead;
    }

    public void setUnRead(int unRead) {
        this.unRead = unRead;
    }

    public byte[] getFriend_avatar() {
        return friend_avatar;
    }

    public void setFriend_avatar(byte[] friend_avatar) {
        this.friend_avatar = friend_avatar;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public GetChatMessage() {

    }

    public GetChatMessage(String message, String fromUser, String type, byte[] friend_avatar) {
        this.message = message;
        this.fromUser = fromUser;
        this.type = type;
        this.friend_avatar = friend_avatar;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }


    @Override
    public String toString() {
        return "GetChatMessage{" +
                "message='" + message + '\'' +
                ", fromUser='" + fromUser + '\'' +
                '}';
    }
}
