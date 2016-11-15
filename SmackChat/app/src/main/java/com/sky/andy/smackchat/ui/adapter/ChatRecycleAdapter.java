package com.sky.andy.smackchat.ui.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sky.andy.smackchat.Constants;
import com.sky.andy.smackchat.R;
import com.sky.andy.smackchat.bean.Messages;
import com.sky.andy.smackchat.utils.DateUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hcc on 16-11-1.
 * Company MingThink
 */

public class ChatRecycleAdapter extends RecyclerView.Adapter<ChatRecycleAdapter.ChatViewHolder> {

    private Context context;
    private ArrayList<Messages> messagesList;
    private LayoutInflater mLayoutInflater;

    /**
     * 音频播放器
     */
    private MediaPlayer mediaPlayer;

    public ChatRecycleAdapter(Context context, ArrayList<Messages> messagesList) {
        this.context = context;
        this.messagesList = messagesList;
        mLayoutInflater = LayoutInflater.from(context);
    }


    public void update(Messages message) {
        int idx = messagesList.indexOf(message);
        if (idx < 0) {
            messagesList.add(message);
        } else {
            messagesList.set(idx, message);
        }
        notifyDataSetChanged();
    }

    public void add(Messages message) {
        messagesList.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Messages messages = messagesList.get(position);
        int type = Constants.TYPE_INCOMING_TEXT;
        if (messages.isSend()) {
            type = Constants.TYPE_OUTGOING_TEXT;
        }
        return type;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == Constants.TYPE_OUTGOING_TEXT) {//发送出去的消息，自己发送的消息展示在界面右边
            itemView = mLayoutInflater.inflate(R.layout.chat_messgae_item_right_layout, parent, false);
        } else {
            itemView = mLayoutInflater.inflate(R.layout.chat_messgae_item_left_layout, parent, false);
        }

        ChatViewHolder chatViewHolder = new ChatViewHolder(itemView);

        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(final ChatViewHolder holder, int position) {
        final Messages messages = messagesList.get(position);

        if (isSameDayToPreviousPosition(messages.getDatetime())) {
            holder.tv_chat_msg_time.setVisibility(View.GONE);
        } else {
            holder.tv_chat_msg_time.setVisibility(View.VISIBLE);
        }
        holder.tv_chat_msg_time.setText(messages.getDatetime());
        holder.tv_chat_msg_username.setText(messages.getUsername());

        Glide.with(context)
                .load(messages.getFriend_avatar())
                .asBitmap()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.iv_chat_avatar);

        setMessageViewVisible(messages.getType(), holder);
        if (messages.getType().equals(Constants.MESSAGE_TYPE_TEXT)) {    //文字
            holder.tv_chat_msg_content_text.setText(messages.getContent());
        } else if (messages.getType().equals(Constants.MESSAGE_TYPE_IMAGE)) {   //图片
            Glide.with(context).load(messages.getContent())
                    .asBitmap()
                    .override(600, 200)
                    .centerCrop()
                    .into(holder.iv_chat_msg_content_image);
        } else if (messages.getType().equals(Constants.MESSAGE_TYPE_VOICE)) {   // 语音
            holder.iv_chat_msg_content_voice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    playVoice(holder.iv_chat_msg_content_voice, messages);
                }
            });
        }

    }


    /**
     * 根据类型显示响应的控件
     *
     * @param type
     */
    public void setMessageViewVisible(String type, ChatViewHolder holder) {
        if (type.equals(Constants.MESSAGE_TYPE_TEXT)) {
            holder.tv_chat_msg_content_text.setVisibility(View.VISIBLE);
            holder.iv_chat_msg_content_image.setVisibility(View.GONE);
            holder.iv_chat_msg_content_voice.setVisibility(View.GONE);

        } else if (type.equals(Constants.MESSAGE_TYPE_IMAGE) || type.equals("Sending file")) {
            holder.tv_chat_msg_content_text.setVisibility(View.GONE);
            holder.iv_chat_msg_content_image.setVisibility(View.VISIBLE);
            holder.iv_chat_msg_content_voice.setVisibility(View.GONE);
        } else if (type.equals(Constants.MESSAGE_TYPE_VOICE)) {
            holder.tv_chat_msg_content_text.setVisibility(View.GONE);
            holder.iv_chat_msg_content_image.setVisibility(View.GONE);
            holder.iv_chat_msg_content_voice.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return messagesList.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_chat_avatar)
        ImageView iv_chat_avatar;

        /**
         * 聊天时间
         */
        @BindView(R.id.tv_chat_msg_time)
        TextView tv_chat_msg_time;

        /**
         * 用户名称
         */
        @BindView(R.id.tv_chat_msg_username)
        TextView tv_chat_msg_username;

        /**
         * 聊天内容
         */
        @BindView(R.id.tv_chat_msg_content_text)
        TextView tv_chat_msg_content_text;

        /**
         * 图片内容
         */
        @BindView(R.id.iv_chat_msg_content_image)
        ImageView iv_chat_msg_content_image;

        /**
         * 语音内容
         */
        @BindView(R.id.iv_chat_msg_content_voice)
        ImageView iv_chat_msg_content_voice;

        /**
         * 发送过程图片
         */
        @BindView(R.id.iv_chat_msg_content_loading)
        ImageView iv_chat_msg_content_loading;

        public ChatViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

        }
    }


    private boolean isSameDayToPreviousPosition(String time) {
        // get previous item's date, for comparison
        try {
            long messageTime = DateUtil.parseDatetime(time).getTime();
            String messageTimeStr = DateUtil.formatDate(new Date(messageTime));
            String currentTimeStr = DateUtil.formatDate(new Date());
            if (messageTimeStr.equals(currentTimeStr)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * 播放语音信息
     *
     * @param iv
     * @param message
     */

    private void playVoice(final ImageView iv, final Messages message) {
        if (message.isSend()) {
            iv.setBackgroundResource(R.drawable.anim_chat_voice_right);
        } else {
            iv.setBackgroundResource(R.drawable.anim_chat_voice_left);
        }
        final AnimationDrawable animationDrawable = (AnimationDrawable) iv.getBackground();
        iv.post(new Runnable() {
            @Override
            public void run() {
                animationDrawable.start();
            }
        });
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {//点击播放，再次点击停止播放
            // 开始播放录音
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    animationDrawable.stop();
                    // 恢复语音消息图标背景
                    if (message.isSend()) {
                        iv.setBackgroundResource(R.drawable.gxu);
                    } else {
                        iv.setBackgroundResource(R.drawable.gxx);
                    }
                }
            });
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(message.getContent());
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            animationDrawable.stop();
            // 恢复语音消息图标背景
            if (message.isSend()) {
                iv.setBackgroundResource(R.drawable.gxu);
            } else {
                iv.setBackgroundResource(R.drawable.gxx);
            }
            if (mediaPlayer != null) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }
    }
}
