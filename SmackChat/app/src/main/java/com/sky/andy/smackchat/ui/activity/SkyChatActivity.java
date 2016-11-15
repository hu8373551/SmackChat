package com.sky.andy.smackchat.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.sky.andy.smackchat.Constants;
import com.sky.andy.smackchat.R;
import com.sky.andy.smackchat.base.BaseActivity;
import com.sky.andy.smackchat.bean.GetChatMessage;
import com.sky.andy.smackchat.bean.Messages;
import com.sky.andy.smackchat.bean.RecorderBean;
import com.sky.andy.smackchat.bean.entry.ChatMessageModel;
import com.sky.andy.smackchat.bean.entry.ConversationModel;
import com.sky.andy.smackchat.db.SmackDataBaseHelper;
import com.sky.andy.smackchat.manager.SmackManager;
import com.sky.andy.smackchat.ui.adapter.ChatRecycleAdapter;
import com.sky.andy.smackchat.utils.BitmapUtil;
import com.sky.andy.smackchat.utils.DateUtil;
import com.sky.andy.smackchat.utils.FileUtil;
import com.sky.andy.smackchat.utils.SdCardUtil;
import com.sky.andy.smackchat.utils.SystemUtils;
import com.sky.andy.smackchat.utils.ValueUtil;
import com.sky.andy.smackchat.utils.view.ChatKeyboard;
import com.sky.skyweight.SharePrefenceUtils;
import com.sky.skyweight.topbar.TopBarViewWithLayout;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

import static com.sky.andy.smackchat.Constants.MESSAGE_TYPE_VOICE;
import static com.sky.andy.smackchat.db.SmackDataBaseHelper.getInstants;

/**
 * Created by hcc on 16-11-1.
 * Company MingThink
 */

public class SkyChatActivity extends BaseActivity implements ChatKeyboard.ChatKeyboardOperateListener {


    /**
     * 选择图片
     */
    private static final int REQUEST_CODE_GET_IMAGE = 1;
    /**
     * 拍照
     */
    private static final int REQUEST_CODE_TAKE_PHOTO = 2;


    /**
     * 文件存储目录
     */
    private String fileDir;


    /**
     * 聊天对象用户Jid
     */
    private String friendRosterUser;
    /**
     * 聊天对象昵称
     */
    private String friendNickname;
    /**
     * 当前自己昵称
     */
    private String currNickname;

    private ChatRecycleAdapter mChatRecycleAdapter;

    private ArrayList<Messages> messagesList = new ArrayList<>();

    private List<Message> chatMessageList = new ArrayList<>();

    /**
     * 当前用户的ｊｉｄ；
     */
    String currentUserJid;

    /**
     * 当前用户的头像
     */
    byte[] myImageByte;
    /**
     * 聊天jid
     */
    private String chatJid;

    /**
     * 好友的ｆｕｌｌＪＩＤ
     */
    private String fullJid;

    /**
     * 聊天属性
     */
    private Chat chat;


    @BindView(R.id.my_bar)
    TopBarViewWithLayout mMyBar;

    @BindView(R.id.ckb_chat_board)
    ChatKeyboard ckb_chat_board;

    @BindView(R.id.chat_view)
    RecyclerView chat_view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        fileDir = SdCardUtil.getCacheDir(this);
        EventBus.getDefault().register(this);


        friendRosterUser = getIntent().getStringExtra("user");
        friendNickname = getIntent().getStringExtra("nickname");
        currNickname = SmackManager.getInstance().getAccountName();
        fullJid = SmackManager.getInstance().getPresence(friendRosterUser).getFrom();
        chatJid = SmackManager.getInstance().getChatJidByUser(friendNickname);
        currentUserJid = SmackManager.getInstance().getAccountJid();
        myImageByte = SmackManager.getInstance().getUserImage(currentUserJid);
        Log.e("hcc", "fullJid-->>" + fullJid + "\n chatJid-->>" + chatJid + " \n friendRosterUser-->." + friendRosterUser);
        //保存当前对话的JID
        SharePrefenceUtils.write(this, Constants.SP_NAME, Constants.SP_JID, friendRosterUser);


//        Bitmap bitmap = SmackManager.getInstance().getUserImage(friendRosterUser);

        ButterKnife.bind(this);
        mMyBar.setTvTitle(friendRosterUser);
        mMyBar.setOnTopBarClickListener(new TopBarViewWithLayout.onTopBarClickListener() {
            @Override
            public void onClickLeftButton() {
                SystemUtils.getInstance().finishActivity(SkyChatActivity.this);
            }

            @Override
            public void onClickRightButton() {

            }
        });

        ckb_chat_board.setChatKeyboardOperateListener(this);
        ckb_chat_board.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    chat_view.smoothScrollToPosition(mChatRecycleAdapter.getItemCount());
                }
            }
        });


        // 创建聊天室
        chat = SmackManager.getInstance().createChat(friendRosterUser);

        // 设置未读条数为0
        getInstants().updateConversationUnRead(friendRosterUser, System.currentTimeMillis(), 0);
        messagesList = SmackDataBaseHelper.getInstants().findMessagesByJid(friendRosterUser);


        chat_view.setHasFixedSize(true);
        chat_view.setLayoutManager(new LinearLayoutManager(this));
        mChatRecycleAdapter = new ChatRecycleAdapter(this, messagesList);
//        chat_view.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));
        chat_view.setItemAnimator(new DefaultItemAnimator());
        chat_view.setAdapter(mChatRecycleAdapter);
        chat_view.smoothScrollToPosition(mChatRecycleAdapter.getItemCount());
        chat_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ckb_chat_board.hideKeyboard(SkyChatActivity.this);
                ckb_chat_board.hideLayout();
                return false;
            }
        });

    }

    /**
     * ********************************************消息处理******************************************
     */

    /**
     * 发送消息
     *
     * @param message
     */
    @Override
    public void send(final String message) {
        Log.e("hcc", "send--<<>>>" + message);
        if (ValueUtil.isEmpty(message)) {
            return;
        }
        new Thread() {
            public void run() {
                try {
                    chat.sendMessage(message);
                    Messages msg = new Messages(Constants.MESSAGE_TYPE_TEXT, currNickname,
                            DateUtil.formatDatetime(new Date()), true, myImageByte);
                    msg.setContent(message);
                    // 保存message
                    ChatMessageModel chatMessageModel = new ChatMessageModel();
                    chatMessageModel.setmJid(friendRosterUser);
                    chatMessageModel.setmMessage(message);
                    chatMessageModel.setmIsSend(msg.isSend());
                    chatMessageModel.setmType(msg.getType());
                    chatMessageModel.setmTime(new Date().getTime());
                    getInstants().saveMessages(chatMessageModel);
                    //更新conversation
                    ConversationModel conversationModel = new ConversationModel();
                    conversationModel.setC_jid(friendRosterUser);
                    conversationModel.setC_last_message(message);
                    conversationModel.setC_time(System.currentTimeMillis());
                    conversationModel.setC_unread(0);
                    getInstants().updateConversation(conversationModel);
                    setResult(0);
                    handler.obtainMessage(1, msg).sendToTarget();
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    @Override
    public void sendVoice(RecorderBean bean) {
        Log.e("hcc", "bean-->>" + bean.toString());
        sendFile(new File(bean.getFilaPath()), MESSAGE_TYPE_VOICE);
    }

    @Override
    public void recordStart() {

    }

    @Override
    public void functionClick(int index) {
        switch (index) {
            case 1://选择图片
                selectImage();
                break;
            case 2://拍照
                takePhoto();
                break;
        }
    }

//
//    ChatManagerListener chatManagerListener = new ChatManagerListener() {
//        @Override
//        public void chatCreated(Chat chat, boolean b) {
//            chat.addMessageListener(new ChatMessageListener() {
//                @Override
//                public void processMessage(Chat chat, Message message) {
//                    //接收到消息Message之后进行消息展示处理，这个地方可以处理所有人的消息
//                    Messages msg = new Messages(Messages.MESSAGE_TYPE_TEXT, friendNickname, DateUtil.formatDatetime(new Date()), false);
//                    if (message.getBody() != null) {
//                        msg.setContent(message.getBody());
//                        handler.obtainMessage(1, msg).sendToTarget();
//                    }
//                }
//            });
//        }
//    };


    @Subscribe(threadMode = ThreadMode.MainThread)
    public void helloEventBus(GetChatMessage getChatMessage) {
        Messages msg = new Messages(getChatMessage.getType(), friendNickname,
                DateUtil.formatDatetime(new Date()), false, getChatMessage.getFriend_avatar());
        if (getChatMessage.getMessage() != null && friendRosterUser.equals(getChatMessage.getFromUser())) {
            msg.setContent(getChatMessage.getMessage());
            handler.obtainMessage(1, msg).sendToTarget();
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mChatRecycleAdapter.add((Messages) msg.obj);
                    chat_view.smoothScrollToPosition(mChatRecycleAdapter.getItemCount());
                    break;
                case 2:
                    mChatRecycleAdapter.update((Messages) msg.obj);
                    chat_view.smoothScrollToPosition(mChatRecycleAdapter.getItemCount());
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("hcc", "onActivityResult");
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_TAKE_PHOTO) {//拍照成功
                Log.e("hcc", "result__ok");
                takePhotoSuccess();
            } else if (requestCode == REQUEST_CODE_GET_IMAGE) {//图片选择成功
                Uri dataUri = data.getData();
                if (dataUri != null) {
                    File file = FileUtil.uri2File(this, dataUri);
                    sendFile(file, Constants.MESSAGE_TYPE_IMAGE);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        //清除当前对话的JID
        SharePrefenceUtils.write(this, Constants.SP_NAME, Constants.SP_JID, "");
    }

    /**
     * 发送文件，图片,语音
     */
    public void sendFile(File file, String type) {
        OutgoingFileTransfer transfer = SmackManager.getInstance().getSendFileTransfer(fullJid);
        try {
            transfer.sendFile(file, String.valueOf(type));
            checkTransferStatus(transfer, file, type, true);
        } catch (SmackException e) {
            e.printStackTrace();
        }
    }

    public void checkTransferStatus(final OutgoingFileTransfer transfer, final File file, final String type, final boolean isSend) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    Messages msg = new Messages(type, currNickname, DateUtil.formatDatetime(new Date()), isSend, myImageByte);
                    msg.setContent(file.getAbsolutePath());
                    msg.setLoadState(0);
                    // 保存message
                    ChatMessageModel chatMessageModel = new ChatMessageModel();
                    chatMessageModel.setmJid(friendRosterUser);
                    chatMessageModel.setmMessage(msg.getContent());
                    chatMessageModel.setmIsSend(msg.isSend());
                    chatMessageModel.setmType(msg.getType());
                    chatMessageModel.setmTime(new Date().getTime());
                    getInstants().saveMessages(chatMessageModel);

                    //更新Ｃｏｎｖｅｒｓａｔｉｏｎ
                    ConversationModel conversationModel = new ConversationModel();
                    conversationModel.setC_jid(friendRosterUser);
                    if (type.equals(Constants.MESSAGE_TYPE_IMAGE) || type.equals("Sending file")) {
                        conversationModel.setC_last_message("[图片]");
                    } else if (type.equals(Constants.MESSAGE_TYPE_VOICE)) {
                        conversationModel.setC_last_message("[语音]");
                    }
                    conversationModel.setC_time(System.currentTimeMillis());
                    conversationModel.setC_unread(0);
                    getInstants().updateConversation(conversationModel);
                    setResult(0);

                    if (transfer.getProgress() < 1) {//传输开始
                        handler.obtainMessage(1, msg).sendToTarget();
                    }
                    while (!transfer.isDone()) {
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (FileTransfer.Status.complete.equals(transfer.getStatus())) {//传输完成
                        Log.e("hcc", "complete-->>>");
                        msg.setLoadState(1);
                        handler.obtainMessage(2, msg).sendToTarget();
                    } else if (FileTransfer.Status.cancelled.equals(transfer.getStatus())) {
                        Log.e("hcc", "cancelled-->>>");
                        //传输取消
                        msg.setLoadState(-1);
                        handler.obtainMessage(2, msg).sendToTarget();
                    } else if (FileTransfer.Status.error.equals(transfer.getStatus())) {
                        Log.e("hcc", "error-->>>");
                        //传输错误
                        msg.setLoadState(-1);
                        handler.obtainMessage(2, msg).sendToTarget();
                    } else if (FileTransfer.Status.refused.equals(transfer.getStatus())) {
                        Log.e("hcc", "refused-->>>");
                        //传输拒绝
                        msg.setLoadState(-1);
                        handler.obtainMessage(2, msg).sendToTarget();
                    }
//                    handler.obtainMessage(1, msg).sendToTarget();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private String picPath = "";

    /**
     * 拍照
     */
    public void takePhoto() {
        String dir = SdCardUtil.getSubOfRootDir(this, "maker_cacheFiles");
        File file = new File(dir, System.currentTimeMillis() + ".jpg");
        SdCardUtil.isExist(file);
        picPath = file.getAbsolutePath();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
    }

    /**
     * 从图库选择图片
     */
    public void selectImage() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"), REQUEST_CODE_GET_IMAGE);
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "选择图片"), REQUEST_CODE_GET_IMAGE);
        }
    }

    /**
     * 照片拍摄成功
     */
    public void takePhotoSuccess() {
        Bitmap bitmap = BitmapUtil.createBitmapWithFile(picPath, 640);
        BitmapUtil.createPictureWithBitmap(picPath, bitmap, 80);
        if (!bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        sendFile(new File(picPath), Constants.MESSAGE_TYPE_IMAGE);
    }
}
