package com.sky.andy.smackchat.listener;

import android.content.Context;
import android.util.Log;

import com.sky.andy.smackchat.Constants;
import com.sky.andy.smackchat.bean.entry.NewFriendModel;
import com.sky.andy.smackchat.db.SmackDataBaseHelper;
import com.sky.andy.smackchat.manager.SmackManager;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;

import de.greenrobot.event.EventBus;

/**
 * Created by hcc on 16-11-8.
 * Company MingThink
 */

public class StanListener implements StanzaListener {

    private Context mContext;

    public StanListener(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void processPacket(Stanza stanza) throws SmackException.NotConnectedException {
        if (stanza instanceof Presence) {
            Presence presence = (Presence) stanza;
            final String from = presence.getFrom();//发送方
            String to = presence.getTo();//接收方
            Log.e("hcc", "from friends-->>" + from + "\n to-->>" + to);
            if (presence.getType().equals(Presence.Type.subscribe)) {
                Log.e("hcc", "fullJid--processPacket>>" + SmackManager.getInstance().getPresence(from).getFrom());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO  收到添加请求, 保存新朋友数据
                        NewFriendModel newFriendModel = new NewFriendModel();
                        newFriendModel.setN_jid(from);
                        newFriendModel.setN_full_jid(SmackManager.getInstance().getPresence(from).getFrom());
                        newFriendModel.setN_current_jid(SmackManager.getInstance().getAccountJid());
                        Log.e("hcc", "SmackManager.getInstance().getFriend(from).getName() -->>>" + SmackManager.getInstance().getFriend(from));
                        if (SmackManager.getInstance().getFriend(from) != null ||
                                !SmackManager.getInstance().getFriend(from).equals("")) {
                            String name = null;
                            if (from != null) {
                                //裁剪JID得到对方用户名
                                name = from.substring(0, from.indexOf("@"));
                            }
                            newFriendModel.setN_name(name);
                        }
//                        else {
//                            newFriendModel.setN_name(SmackManager.getInstance().getFriend(from).getName());
//                        }
                        newFriendModel.setN_head_image(SmackManager.getInstance().getUserImage(from));
                        newFriendModel.setN_status(Constants.N_ACCECPT);
                        newFriendModel.setN_info("对方请求添加您为好友");

                        if (SmackDataBaseHelper.getInstants().isHaveNewFriend(from, SmackManager.getInstance().getAccountJid())) {
                            SmackDataBaseHelper.getInstants().updateNewFriend(newFriendModel);
                        } else {
                            SmackDataBaseHelper.getInstants().saveNewFriend(newFriendModel);
                        }
//                        EventBus.getDefault().post(new AddFriendModel(from));
                    }
                }).start();

            } else if (presence.getType().equals(
                    Presence.Type.subscribed)) {
                //发送广播传递response字符串
//                        response = "恭喜，对方同意添加好友！";
//                        Intent intent = new Intent();
//                        intent.putExtra("response", response);
//                        intent.setAction("com.example.eric_jqm_chat.AddFriendActivity");
//                        sendBroadcast(intent);
                EventBus.getDefault().post("");
            } else if (presence.getType().equals(
                    Presence.Type.unsubscribe)) {
//                            //发送广播传递response字符串
//                            response = "抱歉，对方拒绝添加好友，将你从好友列表移除！";
//                            Intent intent = new Intent();
//                            intent.putExtra("response", response);
//                            intent.setAction("com.example.eric_jqm_chat.AddFriendActivity");
//                            sendBroadcast(intent);
            } else if (presence.getType().equals(
                    Presence.Type.unsubscribed)) {
                Log.e("hcc", "删除好友");
            } else if (presence.getType().equals(
                    Presence.Type.unavailable)) {
                Log.e("hcc", "好友下线！" + from);
                EventBus.getDefault().post("");
            } else if (presence.getType().equals(Presence.Type.available)) {
                Log.e("hcc", "好友在线－－＞＞" + from);
                EventBus.getDefault().post("");
            }
        }

    }
}
