package com.sky.andy.smackchat.listener;

import android.content.Context;
import android.util.Log;

import com.sky.andy.smackchat.Constants;
import com.sky.andy.smackchat.bean.GetChatMessage;
import com.sky.andy.smackchat.bean.entry.ChatMessageModel;
import com.sky.andy.smackchat.bean.entry.ConversationModel;
import com.sky.andy.smackchat.manager.SmackManager;
import com.sky.andy.smackchat.utils.SystemUtils;
import com.sky.skyweight.SharePrefenceUtils;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

import de.greenrobot.event.EventBus;

import static com.sky.andy.smackchat.db.SmackDataBaseHelper.getInstants;


/**
 * Created by hcc on 16-11-1.
 * Company MingThink
 */

public class ChatManagerListener implements org.jivesoftware.smack.chat.ChatManagerListener {

    private Context context;

    public ChatManagerListener(Context context) {
        this.context = context;
    }

    @Override
    public void chatCreated(Chat chat, boolean b) {
        chat.addMessageListener(new ChatMessageListener() {
                                    @Override
                                    public void processMessage(Chat chat, Message message) {

                                        //接收到消息Message之后进行消息展示处理，这个地方可以处理所有人的消息
                                        String body = message.getBody();
                                        String from = message.getFrom();
                                        Log.e("hcc", "from-->>" + from);
                                        String jid = SystemUtils.getInstance().spliceStr(from);
                                        String nickName = SmackManager.getInstance().getFriend(jid).getName();
                                        byte[] myImageByte = SmackManager.getInstance().getUserImage(jid);
                                        Log.e("hcc", "body-->>>" + body + "\n" + " name-->>" + jid + "\n nickName-->>" + nickName);
                                        if (body != null) {
                                            /**
                                             * 处理保存接收数据
                                             */

                                            ChatMessageModel chatMessageModel = new ChatMessageModel();
                                            chatMessageModel.setmJid(jid);
                                            chatMessageModel.setmMessage(body);
                                            chatMessageModel.setmType(Constants.MESSAGE_TYPE_TEXT);
                                            chatMessageModel.setmIsSend(false);
                                            chatMessageModel.setHeadImage(myImageByte);
                                            chatMessageModel.setmTime(System.currentTimeMillis());
                                            chatMessageModel.setmUserName(nickName);
                                            getInstants().saveMessages(chatMessageModel);


                                            /**
                                             * 处理conversation 对话
                                             */
                                            int unRead = 0;
                                            //判断当前是否在和发送过来的jid聊天 如果否则unRead 需要添加
                                            String currentUserJid = SmackManager.getInstance().getAccountJid();
                                            String currentJid = SharePrefenceUtils.readString(context, Constants.SP_NAME, Constants.SP_JID);
                                            Log.e("hcc", "SmackDataBaseHelper.getInstants().isAlreadyHave(jid)->>" + getInstants().isAlreadyHave(jid));
                                            if (getInstants().isAlreadyHave(jid)) {     // 如果纯在conversation 数据
                                                ConversationModel conversationModel = new ConversationModel();
                                                conversationModel.setC_jid(jid);
                                                conversationModel.setC_name(nickName);
                                                conversationModel.setC_last_message(body);
                                                conversationModel.setC_current_user_jid(currentUserJid);
                                                conversationModel.setC_time(System.currentTimeMillis());
                                                conversationModel.setC_head_image(myImageByte);
                                                unRead = getInstants().unReadCount(jid);
                                                if (!jid.equals(currentJid)) {
                                                    unRead++;
                                                    conversationModel.setC_unread(unRead);
                                                }
                                                getInstants().updateConversation(conversationModel);
                                            } else {
                                                ConversationModel conversationModel = new ConversationModel();
                                                conversationModel.setC_jid(jid);
                                                conversationModel.setC_name(nickName);
                                                conversationModel.setC_last_message(body);
                                                conversationModel.setC_time(System.currentTimeMillis());
                                                conversationModel.setC_unread(jid.equals(currentJid) ? 0 : 1);
                                                conversationModel.setC_current_user_jid(currentUserJid);
                                                conversationModel.setC_head_image(myImageByte);
                                                getInstants().saveConversation(conversationModel);
                                            }
                                            int totalUnReadCount = getInstants().getTotalUnReadCount();
                                            Log.e("hcc", "totalUnReadCount-->>" + totalUnReadCount);
                                            GetChatMessage getChatMessage = new GetChatMessage(body, jid, Constants.MESSAGE_TYPE_TEXT, myImageByte);
                                            getChatMessage.setUnRead(totalUnReadCount);
                                            EventBus.getDefault().post(getChatMessage);
                                        }
                                    }
                                }
        );
    }
}

