package com.sky.andy.smackchat.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sky.andy.smackchat.bean.Messages;
import com.sky.andy.smackchat.bean.entry.ChatMessageModel;
import com.sky.andy.smackchat.bean.entry.ConversationModel;
import com.sky.andy.smackchat.bean.entry.FriendEntryModel;
import com.sky.andy.smackchat.utils.DateUtil;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by hcc on 16-11-2.
 * Company MingThink
 */

public class SmackDataBaseHelper {
    private static SmackDataBaseHelper mSmackDataBaseHelper;


    public static SmackDataBaseHelper getInstants() {
        if (mSmackDataBaseHelper == null) {
            mSmackDataBaseHelper = new SmackDataBaseHelper();
        }
        return mSmackDataBaseHelper;
    }


    //=========================Message===============================

    /**
     * 保存或更新message数据库
     *
     * @param mChatMessageModel
     */
    public void saveMessages(ChatMessageModel mChatMessageModel) {
        ContentValues values = new ContentValues();
        values.put(SmackDataBase.M_JID, mChatMessageModel.getmJid());
        values.put(SmackDataBase.M_MESSAGE, mChatMessageModel.getmMessage());
        values.put(SmackDataBase.M_STATUS, mChatMessageModel.getmStatus());
        values.put(SmackDataBase.M_TIME, mChatMessageModel.getmTime());
        values.put(SmackDataBase.M_TYPE, mChatMessageModel.getmType());
        values.put(SmackDataBase.M_IS_SEND, mChatMessageModel.ismIsSend());
        values.put(SmackDataBase.M_USER_NAME, mChatMessageModel.getmUserName());
        values.put(SmackDataBase.M_HEAD_IMAGE, mChatMessageModel.getHeadImage());
        SQLiteDatabase db = SmackDataBase.getInstance().getSQLiteDatabase();
        db.insert(SmackDataBase.MESSAGE_TABLE_NAME, null, values);
    }


    /**
     * 通过M_JID查找聊天记录
     */

    public ArrayList<Messages> findMessagesByJid(String Jid) {
        ArrayList<Messages> messagesList = new ArrayList<>();
        String sql = "select * from " + SmackDataBase.MESSAGE_TABLE_NAME + " where " + SmackDataBase.M_JID + " = '"
                + Jid + "'";
        Cursor c = SmackDataBase.getInstance().rawQuery(sql);
        while (c.moveToNext()) {
            Messages msgs = new Messages();
            msgs.setContent(c.getString(c.getColumnIndex(SmackDataBase.M_MESSAGE)));
            msgs.setDatetime(DateUtil.formatDatetime(new Date(c.getLong(c.getColumnIndex(SmackDataBase.M_TIME)))));
            msgs.setType(c.getString(c.getColumnIndex(SmackDataBase.M_TYPE)));
            msgs.setFriend_avatar(c.getBlob(c.getColumnIndex(SmackDataBase.M_HEAD_IMAGE)));
            msgs.setSend(c.getInt(c.getColumnIndex(SmackDataBase.M_IS_SEND)) == 1);
            msgs.setUsername(c.getString(c.getColumnIndex(SmackDataBase.M_USER_NAME)));
            messagesList.add(msgs);
        }
        c.close();
        return messagesList;
    }


    //=======================conversation =========================

    /**
     * 保存聊天列表
     *
     * @param mConversationModel
     */
    public void saveConversation(ConversationModel mConversationModel) {
        ContentValues values = new ContentValues();
        values.put(SmackDataBase.C_JID, mConversationModel.getC_jid());
        values.put(SmackDataBase.C_LAST_MESSAGE, mConversationModel.getC_last_message());
        values.put(SmackDataBase.C_NAME, mConversationModel.getC_name());
        values.put(SmackDataBase.C_UNREAD, mConversationModel.getC_unread());
        values.put(SmackDataBase.C_TIME, mConversationModel.getC_time());
        values.put(SmackDataBase.C_HEAD_IMAGE, mConversationModel.getC_head_image());
        values.put(SmackDataBase.C_CURRENT_USER_JID, mConversationModel.getC_current_user_jid());
        SQLiteDatabase db = SmackDataBase.getInstance().getSQLiteDatabase();
        //  判断是进行保存还是进行更新
        db.insert(SmackDataBase.CONVERSATION_TABLE_NAME, null, values);
        Log.e("hcc", "保存完毕");
    }

    /**
     * 判断是否存在 conversation 数据
     *
     * @param jid
     * @return
     */
    public boolean isAlreadyHave(String jid) {
        String sql_jid = null;
        String sql = "select * from " + SmackDataBase.CONVERSATION_TABLE_NAME + " where " + SmackDataBase.C_JID + " = '"
                + jid + "'";
        Cursor c = SmackDataBase.getInstance().rawQuery(sql);
        if (c.moveToFirst()) {
            sql_jid = c.getString(c.getColumnIndex(SmackDataBase.C_JID));
        }
        return sql_jid != null;
    }


    /**
     * 更新conversation对话数据
     *
     * @param mConversationModel
     */
    public void updateConversation(ConversationModel mConversationModel) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = SmackDataBase.getInstance().getSQLiteDatabase();
        values.put(SmackDataBase.C_LAST_MESSAGE, mConversationModel.getC_last_message());
        values.put(SmackDataBase.C_TIME, mConversationModel.getC_time());
        values.put(SmackDataBase.C_UNREAD, mConversationModel.getC_unread());
        values.put(SmackDataBase.C_HEAD_IMAGE, mConversationModel.getC_head_image());
        values.put(SmackDataBase.C_CURRENT_USER_JID, mConversationModel.getC_current_user_jid());
        db.update(SmackDataBase.CONVERSATION_TABLE_NAME, values, SmackDataBase.C_JID + "=?", new String[]{String.valueOf
                (mConversationModel.getC_jid())});
    }

    public void updateConversationUnRead(String jid, long time, int unRead) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = SmackDataBase.getInstance().getSQLiteDatabase();
        values.put(SmackDataBase.C_TIME, time);
        values.put(SmackDataBase.C_UNREAD, unRead);
        db.update(SmackDataBase.CONVERSATION_TABLE_NAME, values, SmackDataBase.C_JID + "=?", new String[]{String.valueOf
                (jid)});
    }


    /**
     * 查询未读条数
     *
     * @param jid
     * @return
     */
    public int unReadCount(String jid) {
        int count = 0;
        String sql = "select " + SmackDataBase.C_UNREAD + " from " + SmackDataBase.CONVERSATION_TABLE_NAME + " where " + SmackDataBase.C_JID + " = '"
                + jid + "'";
        Cursor c = SmackDataBase.getInstance().rawQuery(sql);
        if (c.moveToFirst()) {
            count = c.getInt(c.getColumnIndex(SmackDataBase.C_UNREAD));
        }
        c.close();
        return count;
    }


    /**
     * 查找联系列表降序排列
     *
     * @return
     */
    public ArrayList<ConversationModel> findAllConversation(String currentUserJid) {
        ArrayList<ConversationModel> conversationModels = new ArrayList<>();
        String sql = "select * from " + SmackDataBase.CONVERSATION_TABLE_NAME
                + " where " + SmackDataBase.C_CURRENT_USER_JID + " = '"
                + currentUserJid + "' order by " + SmackDataBase.C_TIME + " DESC ";
        Cursor c = SmackDataBase.getInstance().rawQuery(sql);
        while (c.moveToNext()) {
            ConversationModel conversationModel = new ConversationModel();
            conversationModel.setC_jid(c.getString(c.getColumnIndex(SmackDataBase.C_JID)));
            conversationModel.setC_last_message(c.getString(c.getColumnIndex(SmackDataBase.C_LAST_MESSAGE)));
            conversationModel.setC_name(c.getString(c.getColumnIndex(SmackDataBase.C_NAME)));
            conversationModel.setC_time(c.getLong(c.getColumnIndex(SmackDataBase.C_TIME)));
            conversationModel.setC_unread(c.getInt(c.getColumnIndex(SmackDataBase.C_UNREAD)));
            conversationModel.setC_head_image(c.getBlob(c.getColumnIndex(SmackDataBase.C_HEAD_IMAGE)));
            conversationModel.setC_current_user_jid(c.getString(c.getColumnIndex(SmackDataBase.C_CURRENT_USER_JID)));
            conversationModels.add(conversationModel);
        }
        c.close();
        return conversationModels;
    }

    /**
     * 获取未读数据总数
     *
     * @return
     */
    public int getTotalUnReadCount() {
        int count = 0;
        String sql = " select sum(" + SmackDataBase.C_UNREAD + ") from " + SmackDataBase.CONVERSATION_TABLE_NAME;
        Cursor c = SmackDataBase.getInstance().rawQuery(sql);
        if (c.moveToFirst()) {
            count = c.getInt(0);
        }
        c.close();
        return count;
    }


    //======================Friend ==========================

    /**
     * 保存朋友列表
     *
     * @param friendEntryModel
     */
    public void saveFriends(FriendEntryModel friendEntryModel) {
        ContentValues values = new ContentValues();
        values.put(SmackDataBase.F_JID, friendEntryModel.getJid());
        values.put(SmackDataBase.F_FULL_JID, friendEntryModel.getFullJid());
        values.put(SmackDataBase.F_NAME, friendEntryModel.getName());
        values.put(SmackDataBase.F_HEAD_IMAGE, friendEntryModel.getImageHead());
        values.put(SmackDataBase.F_CURRENT_USER_JID, friendEntryModel.getCurrentUserJid());
        SQLiteDatabase db = SmackDataBase.getInstance().getSQLiteDatabase();
        //  判断是进行保存还是进行更新
        db.insert(SmackDataBase.FRIEND_TABLE_NAME, null, values);
        Log.e("hcc", "保存完毕");
    }


    /**
     * 判断是否存在 Friend 数据
     *
     * @param jid
     * @return
     */
    public boolean isAlreadyFriend(String jid, String currentJid) {
        String sql_jid = null;
        String sql = "select * from " + SmackDataBase.FRIEND_TABLE_NAME + " where " + SmackDataBase.F_JID + " = '"
                + jid + "' and " + SmackDataBase.F_CURRENT_USER_JID + " = '" + currentJid + "'";
        Cursor c = SmackDataBase.getInstance().rawQuery(sql);
        if (c.moveToFirst()) {
            sql_jid = c.getString(c.getColumnIndex(SmackDataBase.F_JID));
        }
        return sql_jid != null;
    }


    /**
     * 更新conversation对话数据
     *
     * @param friendEntryModel
     */
    public void updateFriend(FriendEntryModel friendEntryModel) {
        ContentValues values = new ContentValues();
        SQLiteDatabase db = SmackDataBase.getInstance().getSQLiteDatabase();
        values.put(SmackDataBase.F_JID, friendEntryModel.getJid());
        values.put(SmackDataBase.F_FULL_JID, friendEntryModel.getFullJid());
        values.put(SmackDataBase.F_NAME, friendEntryModel.getName());
        values.put(SmackDataBase.F_HEAD_IMAGE, friendEntryModel.getImageHead());
        values.put(SmackDataBase.F_CURRENT_USER_JID, friendEntryModel.getCurrentUserJid());
        db.update(SmackDataBase.FRIEND_TABLE_NAME, values, SmackDataBase.F_JID + "=?", new String[]{String.valueOf
                (friendEntryModel.getJid())});
    }

    /**
     * 查找联系列表降序排列
     *
     * @return
     */
    public ArrayList<FriendEntryModel> findAllFriends(String currentUserJid) {
        ArrayList<FriendEntryModel> friendEntryModels = new ArrayList<>();
        String sql = "select * from " + SmackDataBase.FRIEND_TABLE_NAME
                + " where " + SmackDataBase.F_CURRENT_USER_JID
                + " = '" + currentUserJid + "'";
        Cursor c = SmackDataBase.getInstance().rawQuery(sql);
        while (c.moveToNext()) {
            FriendEntryModel friendEntryModel = new FriendEntryModel();
            friendEntryModel.setJid(c.getString(c.getColumnIndex(SmackDataBase.F_JID)));
            friendEntryModel.setFullJid(c.getString(c.getColumnIndex(SmackDataBase.F_FULL_JID)));
            friendEntryModel.setName(c.getString(c.getColumnIndex(SmackDataBase.F_NAME)));
            friendEntryModel.setImageHead(c.getBlob(c.getColumnIndex(SmackDataBase.F_HEAD_IMAGE)));
            friendEntryModel.setCurrentUserJid(c.getString(c.getColumnIndex(SmackDataBase.F_CURRENT_USER_JID)));
            friendEntryModels.add(friendEntryModel);
        }
        c.close();
        return friendEntryModels;
    }


}
