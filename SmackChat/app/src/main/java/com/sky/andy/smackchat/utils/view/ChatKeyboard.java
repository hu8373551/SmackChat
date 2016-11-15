package com.sky.andy.smackchat.utils.view;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sky.andy.smackchat.R;
import com.sky.andy.smackchat.bean.RecorderBean;
import com.sky.andy.smackchat.utils.ValueUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.sky.andy.smackchat.R.id.cb_chat_more_toolbox;


public class ChatKeyboard extends RelativeLayout implements
        SoftKeyboardStateHelper.SoftKeyboardStateListener {
    private Context context;

    /**
     * 软键盘监听助手
     */
    private SoftKeyboardStateHelper mKeyboardHelper;
    /**
     * 发送表情开关按钮
     */
    @BindView(R.id.cb_chat_face_toolbox)
    CheckBox mCbFace;
    /**
     * 发送更多类型消息开关按钮
     */
    @BindView(cb_chat_more_toolbox)
    CheckBox mCbMore;

    /**
     * 文本消息输入框
     */
    @BindView(R.id.et_chat_message_toolbox)
    EditText mEtMessage;
    /**
     * 发送文本消息按钮
     */
    @BindView(R.id.btn_chat_send_txt)
    Button mBtnSendTxt;
    /**
     * 发送语音消息按钮
     */
    @BindView(R.id.cb_chat_send_voice)
    CheckBox mCbSendVoice;
    /**
     * 当前是否为正在录音界面
     */
    private boolean isRecordVoice = false;
    /**
     * 录音界面
     */
    @BindView(R.id.rl_chat_record_voice_layout)
    RelativeLayout mRecordVoiceLayout;
    /**
     * 录音控件
     */
    @BindView(R.id.psv_record)
    AudioRecoderButton mSpeakRecord;

    /**
     * 聊天操作监听
     */
    private ChatKeyboardOperateListener listener;
    /**
     * 选择更多聊天功能布局
     */
    @BindView(R.id.rl_chat_keyboard_more)
    RelativeLayout mChatMoreLayout;
    /**
     * 选择图片
     */
    @BindView(R.id.ll_chat_more_images)
    LinearLayout mChatMoreImage;
    /**
     * 拍照
     */
    @BindView(R.id.ll_chat_more_photo)
    LinearLayout mChatMorePhoto;

    public ChatKeyboard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public ChatKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChatKeyboard(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View root = View.inflate(context, R.layout.chat_key_board_layout, null);
        this.addView(root);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initKeyboardHelper();
        this.initWidget();
    }

    private void initKeyboardHelper() {
        mKeyboardHelper = new SoftKeyboardStateHelper(((Activity) getContext())
                .getWindow().getDecorView());
        mKeyboardHelper.addSoftKeyboardStateListener(this);
    }


    public void hideLayout() {
        isChatMoreClick = false;
        isRecordVoice = false;
        mRecordVoiceLayout.setVisibility(View.GONE);
        mChatMoreLayout.setVisibility(View.GONE);
    }

    /**
     * 初始化相关控件
     */
    private void initWidget() {
        ButterKnife.bind(this);
        mSpeakRecord.setOnAudioFinishRecorderListener(mRecordListener);
        mEtMessage.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    hideLayout();
                }
            }
        });
        mEtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = mEtMessage.getText().toString();
                if (ValueUtil.isEmpty(text)) {//切换语音输入与文本输入
//                    mCbSendVoice.setVisibility(View.VISIBLE);
                    mBtnSendTxt.setVisibility(View.GONE);
                    mCbMore.setVisibility(VISIBLE);
                } else {
//                    mCbSendVoice.setVisibility(View.GONE);
                    mBtnSendTxt.setVisibility(View.VISIBLE);
                    mCbMore.setVisibility(GONE);
                }
            }
        });
    }

    /**
     * 发送文本消息
     *
     * @param v
     */
    @OnClick(R.id.btn_chat_send_txt)
    public void sendTxtClick(View v) {
        if (listener != null) {
            String msg = mEtMessage.getText().toString();
            if (msg == null || msg.length() == 0) {
                return;
            }
            listener.send(msg);
            mEtMessage.setText("");
        }
    }

    /**
     * 发送语音消息
     *
     * @param v
     */
    @OnClick(R.id.cb_chat_send_voice)
    public void sendVoiceClick(View v) {
        mSpeakRecord.requestFocus();
        isRecordVoice = !isRecordVoice;
//        changeLayout(1, isRecordVoice);
        if (isRecordVoice) {
            mSpeakRecord.setVisibility(VISIBLE);
            mEtMessage.setVisibility(GONE);
        } else {
            mSpeakRecord.setVisibility(GONE);
            mEtMessage.setVisibility(VISIBLE);
        }
    }

    /**
     * Chat more功能是否选中
     */
    private boolean isChatMoreClick = false;

    /**
     * 展示more功能按钮选择
     *
     * @param v
     */
    @OnClick(R.id.cb_chat_more_toolbox)
    public void chatMoreClick(View v) {
        Log.e("hcc", "qqqq__-onclick");
//        mCbMore.setFocusable(true);
//        mCbMore.setFocusableInTouchMode(true);
//        mCbMore.requestFocus();
        isChatMoreClick = !isChatMoreClick;
        changeLayout(2, isChatMoreClick);
    }

    /**
     * 更多功能
     *
     * @param v
     */
    @OnClick({R.id.ll_chat_more_images, R.id.ll_chat_more_photo})
    public void chatMoreItemClick(View v) {
        switch (v.getId()) {
            case R.id.ll_chat_more_images://选择图片
                if (listener != null) {
                    listener.functionClick(1);
                }
                break;
            case R.id.ll_chat_more_photo:
                if (listener != null) {
                    listener.functionClick(2);
                }
                break;
        }
    }

    /**
     * 切换布局文件
     *
     * @param funFlag 功能代码
     * @param isShow  是否显示
     */
    private void changeLayout(int funFlag, boolean isShow) {
        Log.e("hcc", "ishow-->>>" + isShow);
        if (isShow == false) {
            mEtMessage.setFocusable(true);
            mEtMessage.setFocusableInTouchMode(true);
            mEtMessage.requestFocus();
//            isChatMoreClick = false;
            showKeyboard(context);
//    		mCbSendVoice.setBackgroundResource(R.drawable.chat_keyboard_start_record_voice_bg_selector);
            mEtMessage.setVisibility(View.VISIBLE);
            mRecordVoiceLayout.setVisibility(View.GONE);
            mChatMoreLayout.setVisibility(View.GONE);
            return;
        }
        hideKeyboard(context);
        switch (funFlag) {
            case 1://语音
                //延迟一会显示，避免出现某一时刻视图与键盘同时显示
                postDelayed(new Runnable() {
                    public void run() {
//						mCbSendVoice.setBackgroundResource(R.drawable.chat_keyboard_start_txt_bg_selector);
                        mEtMessage.setVisibility(View.INVISIBLE);
                        mRecordVoiceLayout.setVisibility(View.VISIBLE);
                        mChatMoreLayout.setVisibility(View.GONE);
                    }
                }, 50);
                break;
            case 2://more
                //延迟一会显示，避免出现某一时刻视图与键盘同时显示
                postDelayed(new Runnable() {
                    public void run() {
//						mCbSendVoice.setBackgroundResource(R.drawable.chat_keyboard_start_record_voice_bg_selector);
                        mEtMessage.setVisibility(View.VISIBLE);
                        mRecordVoiceLayout.setVisibility(View.GONE);
                        mChatMoreLayout.setVisibility(View.VISIBLE);
                    }
                }, 50);
                break;
        }
    }

    @Override
    public void onSoftKeyboardOpened(int keyboardHeightInPx) {
//		mCbSendVoice.setBackgroundResource(R.drawable.chat_keyboard_start_record_voice_bg_selector);
        mEtMessage.setVisibility(View.VISIBLE);
        mRecordVoiceLayout.setVisibility(View.GONE);
        mChatMoreLayout.setVisibility(View.GONE);
        isChatMoreClick = false;
        isRecordVoice = false;
    }

    @Override
    public void onSoftKeyboardClosed() {
        isChatMoreClick = true;
        isRecordVoice = true;
    }

    /**
     * 隐藏软键盘
     */
    public void hideKeyboard(Context context) {
        Activity activity = (Activity) context;
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm.isActive() && activity.getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(activity.getCurrentFocus()
                        .getWindowToken(), 0);
            }
        }
    }

    /**
     * 显示软键盘
     */
    public static void showKeyboard(Context context) {
        Activity activity = (Activity) context;
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInputFromInputMethod(activity.getCurrentFocus()
                    .getWindowToken(), 0);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public ChatKeyboardOperateListener getChatKeyboardOperateListener() {
        return listener;
    }

    public void setChatKeyboardOperateListener(ChatKeyboardOperateListener listener) {
        this.listener = listener;
    }

    /**
     * 录音过程中的监听
     */
    private AudioRecoderButton.OnAudioFinishRecorderListener mRecordListener = new AudioRecoderButton.OnAudioFinishRecorderListener() {

        @Override
        public void onFinish(RecorderBean bean) {
            if (listener != null) {
                listener.sendVoice(bean);
            }
        }
    };

    /**
     * 聊天操作监听接口
     *
     * @auther: hyl
     * @time: 2015-10-28下午4:39:06
     */
    public interface ChatKeyboardOperateListener {
        /**
         * 发送文本消息接口
         *
         * @param message
         */
        public void send(String message);

        /**
         * 录音完成，发送语音文件，UI线程
         *
         * @param bean
         */
        public void sendVoice(RecorderBean bean);

        /**
         * 开始录音，UI线程
         */
        public void recordStart();

        /**
         * 点击触发的功能
         *
         * @param index 从1开始，按照展示顺序进行索引
         */
        public void functionClick(int index);
    }
}
