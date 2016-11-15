package com.sky.andy.smackchat.bean.entry;

/**
 * Created by hcc on 16-11-2.
 * Company MingThink
 */

public class ChatMessageModel {
    private String mJid;
    private String mMessage;
    private String mType;
    private Long mTime;
    private int mStatus;
    private String mUserName;
    private boolean mIsSend;
    private byte[] headImage;

    public byte[] getHeadImage() {
        return headImage;
    }

    public void setHeadImage(byte[] headImage) {
        this.headImage = headImage;
    }

    public boolean ismIsSend() {
        return mIsSend;
    }

    public void setmIsSend(boolean mIsSend) {
        this.mIsSend = mIsSend;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmJid() {
        return mJid;
    }

    public void setmJid(String mJid) {
        this.mJid = mJid;
    }

    public String getmMessage() {
        return mMessage;
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public Long getmTime() {
        return mTime;
    }

    public void setmTime(Long mTime) {
        this.mTime = mTime;
    }

    public int getmStatus() {
        return mStatus;
    }

    public void setmStatus(int mStatus) {
        this.mStatus = mStatus;
    }
}
