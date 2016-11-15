package com.sky.andy.smackchat.listener;

import android.content.Context;
import android.util.Log;

import com.sky.andy.smackchat.Constants;
import com.sky.andy.smackchat.bean.GetChatMessage;
import com.sky.andy.smackchat.bean.entry.ChatMessageModel;
import com.sky.andy.smackchat.bean.entry.ConversationModel;
import com.sky.andy.smackchat.manager.SmackManager;
import com.sky.andy.smackchat.utils.SdCardUtil;
import com.sky.andy.smackchat.utils.SystemUtils;
import com.sky.skyweight.SharePrefenceUtils;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;

import java.io.File;
import java.io.IOException;

import de.greenrobot.event.EventBus;

import static com.sky.andy.smackchat.db.SmackDataBaseHelper.getInstants;

/**
 * Created by hcc on 16-11-7.
 * Company MingThink
 */

public class FileTransListener implements FileTransferListener {

    private String fileDir;
    private Context mContext;

    public FileTransListener(Context mContext) {
        this.mContext = mContext;
        fileDir = SdCardUtil.getCacheDir(mContext);
    }


    @Override
    public void fileTransferRequest(FileTransferRequest request) {
        // Accept it
        IncomingFileTransfer transfer = request.accept();
        try {
            String type = request.getDescription();
            File file = new File(fileDir, request.getFileName());
            transfer.recieveFile(file);
            Log.e("hcc", "file-->>" + file.getAbsolutePath() + "\n type-->>"
                    + type + " \n " + request.getStreamID()
                    + "\n" + request.getMimeType() + "\n" +
                    request.getRequestor());
            String from = request.getRequestor();
            getSaveTransFelStatus(from, transfer, file, type);
        } catch (SmackException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收文件数据
     *
     * @param from
     * @param transfer
     * @param file
     */
    public void getSaveTransFelStatus(String from, final FileTransfer transfer, final File file, final String type) {
        String jid = SystemUtils.getInstance().spliceStr(from);
        String nickName = SmackManager.getInstance().getFriend(jid).getName();
        byte[] myImageByte = SmackManager.getInstance().getUserImage(jid);
        String body = file.getAbsolutePath();
        ChatMessageModel chatMessageModel = new ChatMessageModel();
        chatMessageModel.setmJid(jid);
        chatMessageModel.setmMessage(file.getAbsolutePath());
        chatMessageModel.setmType(type);
        chatMessageModel.setmIsSend(false);
        chatMessageModel.setHeadImage(myImageByte);
        chatMessageModel.setmTime(System.currentTimeMillis());
        chatMessageModel.setmUserName(nickName);
        getInstants().saveMessages(chatMessageModel);

        //保存更新ｃｏｎｖｅｒｓａｔｉｏｎ　表格
        /**
         * 处理conversation 对话
         */
        int unRead = 0;
        //判断当前是否在和发送过来的jid聊天 如果否则unRead 需要添加
        String currentUserJid = SmackManager.getInstance().getAccountJid();
        String currentJid = SharePrefenceUtils.readString(mContext, Constants.SP_NAME, Constants.SP_JID);
        Log.e("hcc", "SmackDataBaseHelper.getInstants().isAlreadyHave(jid)->>" + getInstants().isAlreadyHave(jid));
        if (getInstants().isAlreadyHave(jid)) {     // 如果纯在conversation 数据
            ConversationModel conversationModel = new ConversationModel();
            conversationModel.setC_jid(jid);
            conversationModel.setC_name(nickName);
            conversationModel.setC_head_image(myImageByte);
            conversationModel.setC_current_user_jid(currentUserJid);
            if (type.equals(Constants.MESSAGE_TYPE_IMAGE) || type.equals("Sending file")) {
                conversationModel.setC_last_message("[图片]");
            } else if (type.equals(Constants.MESSAGE_TYPE_VOICE)) {
                conversationModel.setC_last_message("[语音]");
            }
            conversationModel.setC_time(System.currentTimeMillis());
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
            conversationModel.setC_head_image(myImageByte);
            conversationModel.setC_current_user_jid(currentUserJid);
            if (type.equals(Constants.MESSAGE_TYPE_IMAGE) || type.equals("Sending file")) {
                conversationModel.setC_last_message("[图片]");
            } else if (type.equals(Constants.MESSAGE_TYPE_VOICE)) {
                conversationModel.setC_last_message("[语音]");
            }
            conversationModel.setC_time(System.currentTimeMillis());
            conversationModel.setC_unread(jid.equals(currentJid) ? 0 : 1);
            getInstants().saveConversation(conversationModel);
        }

        int totalUnReadCount = getInstants().getTotalUnReadCount();

        Log.e("hcc", "totalUnReadCount-->>" + totalUnReadCount);
        GetChatMessage getChatMessage = new GetChatMessage(body, jid, type, myImageByte);
        getChatMessage.setUnRead(totalUnReadCount);
        EventBus.getDefault().post(getChatMessage);
    }

}
