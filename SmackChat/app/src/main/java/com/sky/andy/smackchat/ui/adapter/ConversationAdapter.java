package com.sky.andy.smackchat.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sky.andy.smackchat.R;
import com.sky.andy.smackchat.bean.entry.ConversationModel;
import com.sky.andy.smackchat.utils.DateUtil;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hcc on 16-11-3.
 * Company MingThink
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHold> {
    private Context context;
    private ArrayList<ConversationModel> conversationModels = new ArrayList<>();
    private LayoutInflater mLayoutInflater;


    private OnItemListener mOnItemListener;

    public interface OnItemListener {
        void OnItemClick(int position);

        void OnItemLongClick();
    }

    public void setOnItemListener(OnItemListener mOnItemListener) {
        this.mOnItemListener = mOnItemListener;
    }


    public ConversationAdapter(Context context, ArrayList<ConversationModel> conversationModels) {
        this.context = context;
        this.conversationModels = conversationModels;
        mLayoutInflater = LayoutInflater.from(context);
    }


    public void setConversation(ArrayList<ConversationModel> conversationModels) {
        this.conversationModels = conversationModels;
        notifyDataSetChanged();
    }

    @Override
    public ConversationViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.recycler_conversation_item, parent, false);
        return new ConversationViewHold(view);
    }

    @Override
    public void onBindViewHolder(ConversationViewHold holder, final int position) {
        ConversationModel conversationModel = conversationModels.get(position);
        holder.friendName.setText(conversationModel.getC_name());
        holder.lastMessage.setText(conversationModel.getC_last_message());
        holder.tvTime.setText(DateUtil.formatTime(new Date(conversationModel.getC_time())));
        if (conversationModel.getC_unread() == 0) {
            holder.tvUnReadCount.setVisibility(View.GONE);
        } else {
            holder.tvUnReadCount.setVisibility(View.VISIBLE);
        }
        Glide.with(context)
                .load(conversationModel.getC_head_image())
                .centerCrop()
                .into(holder.headImg);
        holder.tvUnReadCount.setText(conversationModel.getC_unread() + "");
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemListener.OnItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return conversationModels.size();
    }

    public class ConversationViewHold extends RecyclerView.ViewHolder {

        @BindView(R.id.relative_layout)
        RelativeLayout relativeLayout;
        @BindView(R.id.head_image)
        ImageView headImg;
        @BindView(R.id.friend_name)
        TextView friendName;
        @BindView(R.id.last_message)
        TextView lastMessage;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_unread_count)
        TextView tvUnReadCount;

        public ConversationViewHold(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
