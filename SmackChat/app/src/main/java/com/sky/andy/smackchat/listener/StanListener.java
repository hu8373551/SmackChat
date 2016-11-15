package com.sky.andy.smackchat.listener;

import android.content.Context;
import android.util.Log;

import com.sky.andy.smackchat.bean.AddFriendModel;

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
            String from = presence.getFrom();//发送方
            String to = presence.getTo();//接收方
//            Log.e("hcc", "from friends-->>" + from + "\n to-->>" + to);
            if (presence.getType().equals(Presence.Type.subscribe)) {
                System.out.println("收到添加请求！");
                Log.e("hcc", "request__add friends-->>");
                //发送广播传递发送方的JIDfrom及字符串
//                        acceptAdd = "收到添加请求！";
//                        Intent intent = new Intent();
//                        intent.putExtra("fromName", from);
//                        intent.putExtra("acceptAdd", acceptAdd);
//                        intent.setAction("com.example.eric_jqm_chat.AddFriendActivity");
//                        sendBroadcast(intent);
                EventBus.getDefault().post(new AddFriendModel(from));
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
