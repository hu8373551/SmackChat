package com.sky.andy.smackchat.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

/**
 * Created by hcc on 16-11-2.
 * Company MingThink
 */

public class SmackDataBase {
    private Context mContext;
    private SQLiteDatabase mSQLiteDatabase;
    public static final String AUTOINCREMENT_ID = "id";
    private static SmackDataBase database = null;
    private static String TAG = "SmackDataBase";
    // 数据库名称常量
    private static final String DATABASE_NAME = "Smack.db";
    // 数据库版本常量
    private static final int DATABASE_VERSION = 1;

    //=============User Model =========================
    public static final String USER_INFO_TABLE_NAME = "user_info_table_name";
    public static final String U_JID = "u_jid";
    public static final String U_NICKNAME = "u_nickname";
    public static final String U_HEADER_IMG = "u_header_img";

    //============MessageModel=======================
    public static final String MESSAGE_TABLE_NAME = "message_table_name";
    public static final String M_JID = "m_jid";
    public static final String M_MESSAGE = "m_message";  // 聊天内容
    public static final String M_TYPE = "m_type";        // 数据类型，　文字，图片，语音
    public static final String M_USER_NAME = "m_user_name";    // 用户名称
    public static final String M_TIME = "m_time";   //  发送时间
    public static final String M_STATUS = "m_status"; // 状态,
    public static final String M_IS_SEND = "m_is_send"; // 接收/ 发送
    public static final String M_HEAD_IMAGE = "m_head_image"; //头像

    //==============联系对话列表 ==========================
    public static final String CONVERSATION_TABLE_NAME = "conversation_table_name";
    public static final String C_JID = "c_jid";
    public static final String C_NAME = "c_name";
    public static final String C_LAST_MESSAGE = "c_last_message";
    public static final String C_UNREAD = "c_unread";
    public static final String C_TIME = "c_time";
    public static final String C_HEAD_IMAGE = "c_head_image";
    public static final String C_CURRENT_USER_JID = "c_current_user_jid";

    //===============联系人表＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
    public static final String FRIEND_TABLE_NAME = "friend_table_name";
    public static final String F_JID = "f_jid";
    public static final String F_FULL_JID = "f_full_jid";
    public static final String F_NAME = "f_name";
    public static final String F_HEAD_IMAGE = "f_head_image";
    public static final String F_CURRENT_USER_JID = "f_current_user_jid";


    public SmackDataBase(Context context) {
        database = this;
        this.mContext = context;
        try {
            mSQLiteDatabase = mContext.openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null);
        } catch (SQLiteException se) {
            Log.e(TAG, se.toString());
        }
        // 创建或修改表结构
        createOrUpgradeTable();
    }

    /**
     * 创建或修改表结构
     */
    private void createOrUpgradeTable() {
        if (mSQLiteDatabase != null) {
            int version = mSQLiteDatabase.getVersion();
            if (version < 1) {
                // Message Table
                String createMessageTableSql = "CREATE TABLE " + MESSAGE_TABLE_NAME +
                        "(" + AUTOINCREMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        M_JID + " TEXT,"
                        + M_USER_NAME + " TEXT,"
                        + M_MESSAGE + " TEXT,"
                        + M_TYPE + " TEXT,"
                        + M_TIME + " LONG,"
                        + M_IS_SEND + " BOOL,"
                        + M_HEAD_IMAGE + " BLOB,"
                        + M_STATUS + " INTEGER)";

                mSQLiteDatabase.execSQL(createMessageTableSql);

                // Conversation Table
                String createConversationTableSql = "CREATE TABLE " + CONVERSATION_TABLE_NAME +
                        "(" + AUTOINCREMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        C_JID + " TEXT,"
                        + C_NAME + " TEXT,"
                        + C_LAST_MESSAGE + " TEXT,"
                        + C_CURRENT_USER_JID + " TEXT,"
                        + C_UNREAD + " INTEGER,"
                        + C_HEAD_IMAGE + " BLOB,"
                        + C_TIME + " LONG)";
                mSQLiteDatabase.execSQL(createConversationTableSql);

                // friend table
                String createFriendTableSql = "CREATE TABLE " + FRIEND_TABLE_NAME +
                        "(" + AUTOINCREMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        F_JID + " TEXT,"
                        + F_FULL_JID + " TEXT,"
                        + F_NAME + " TEXT,"
                        + F_CURRENT_USER_JID + " TEXT,"
                        + F_HEAD_IMAGE + " BLOB)";
                mSQLiteDatabase.execSQL(createFriendTableSql);
            }
            mSQLiteDatabase.setVersion(DATABASE_VERSION);
        }
    }

    public static SmackDataBase getInstance() {
        return database;
    }

    public SQLiteDatabase getSQLiteDatabase() {
        return mSQLiteDatabase;
    }

    public Cursor rawQuery(String sql) {
        Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
        return cursor;
    }

    public void execSQL(String sql) {
        mSQLiteDatabase.execSQL(sql);
    }

    public long insert(String table, ContentValues values) {
        return mSQLiteDatabase.insert(table, null, values);
    }

    public void closeSQLiteDatabase() {
        mSQLiteDatabase.close();
    }
}
