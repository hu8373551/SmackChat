package com.sky.andy.smackchat;

/**
 * Created by hcc on 16-11-3.
 * Company MingThink
 */

public class Constants {

    /**
     * 文本消息类型
     */
    public static final String MESSAGE_TYPE_TEXT = "0";
    /**
     * 图片消息类型
     */
    public static final String MESSAGE_TYPE_IMAGE = "1";
    /**
     * 语音消息类型
     */
    public static final String MESSAGE_TYPE_VOICE = "2";


    public static final int TYPE_INCOMING_TEXT = 1;
    public static final int TYPE_OUTGOING_TEXT = 2;
    public static final int TYPE_INCOMING_VOID = 3;
    public static final int TYPE_OUTGOING_VOID = 4;
    public static final int TYPE_INCOMING_IMAGE = 5;
    public static final int TYPE_OUTGOING_IMAGE = 6;

    //===============SharedPreferences=====================

    public static final String SP_NAME = "sp_name";
    public static final String SP_JID = "sp_jid";
    public static final String SP_ALREADY_LOAD_FRIEND = "sp_already_load_friend";
    public static final int ALREADY_LOAD_FRIENDS = 1001;
    public static final String SP_USER_INFO = "sp_user_info";
    public static final String SP_USER_NICKNAME = "sp_user_nickname";
    public static final String SP_USER_JID = "sp_user_jid";
    public static final String SP_USER_HEADER_IMAGE = "sp_user_header_image";


    // ================exception string=====================
    public static final String FRIENDNUN = "friendNun";  //没有朋友


}
