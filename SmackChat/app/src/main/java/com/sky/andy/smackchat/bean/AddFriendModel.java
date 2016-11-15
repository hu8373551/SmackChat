package com.sky.andy.smackchat.bean;

/**
 * Created by hcc on 16-11-8.
 * Company MingThink
 */

public class AddFriendModel {
    public String fromName;

    public AddFriendModel(String fromName) {
        this.fromName = fromName;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }
}
