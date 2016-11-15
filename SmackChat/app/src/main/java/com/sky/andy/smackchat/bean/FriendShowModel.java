package com.sky.andy.smackchat.bean;

import org.jivesoftware.smack.roster.RosterEntry;

/**
 * Created by hcc on 16-11-9.
 * Company MingThink
 */

public class FriendShowModel {
    private RosterEntry rosterEntry;
    private byte[] imageByte;

    public RosterEntry getRosterEntry() {
        return rosterEntry;
    }

    public void setRosterEntry(RosterEntry rosterEntry) {
        this.rosterEntry = rosterEntry;
    }

    public byte[] getImageByte() {
        return imageByte;
    }

    public void setImageByte(byte[] imageByte) {
        this.imageByte = imageByte;
    }
}
