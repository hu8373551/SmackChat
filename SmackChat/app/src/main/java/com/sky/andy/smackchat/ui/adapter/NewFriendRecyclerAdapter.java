package com.sky.andy.smackchat.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sky.andy.smackchat.Constants;
import com.sky.andy.smackchat.R;
import com.sky.andy.smackchat.bean.entry.NewFriendModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hcc on 16-11-16.
 * Company MingThink
 */

public class NewFriendRecyclerAdapter extends RecyclerView.Adapter<NewFriendRecyclerAdapter.NewFriendHoldView> {

    private ArrayList<NewFriendModel> newFriendModels = new ArrayList<>();
    private Context context;
    private LayoutInflater mLayoutInflater;
    private OnItemListener mOnItemListener;

    public interface OnItemListener {
        void OnItemClick(int position);

        void OnItemLongClick();
    }


    public void setOnItemListener(OnItemListener mOnItemListener) {
        this.mOnItemListener = mOnItemListener;
    }


    public NewFriendRecyclerAdapter(Context context, ArrayList<NewFriendModel> newFriendModels) {
        this.context = context;
        this.newFriendModels = newFriendModels;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public void setData(ArrayList<NewFriendModel> newFriendModels) {
        this.newFriendModels = newFriendModels;
        this.notifyDataSetChanged();
    }

    @Override
    public NewFriendHoldView onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mLayoutInflater.inflate(R.layout.recycler_new_friend_item, parent, false);

        return new NewFriendHoldView(view);
    }

    @Override
    public void onBindViewHolder(NewFriendHoldView holder, final int position) {
        NewFriendModel newFriendModel = newFriendModels.get(position);
        Glide.with(context).load(newFriendModel.getN_head_image())
                .asBitmap()
                .centerCrop()
                .placeholder(R.drawable.icon_chat_image)
                .into(holder.head_image);
        holder.friend_name.setText(newFriendModel.getN_name());
        holder.friend_info.setText(newFriendModel.getN_info());
        if (newFriendModel.getN_status() == Constants.N_ACCECPT) {
            holder.btn_new_friend.setText("接受");
            holder.btn_new_friend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemListener.OnItemClick(position);
                }
            });
        } else if (newFriendModel.getN_status() == Constants.N_ALREADY_ADD) {
            holder.btn_new_friend.setText("已添加");
            holder.btn_new_friend.setTextColor(ContextCompat.getColor(context, R.color.color_40000000));
            holder.btn_new_friend.setBackgroundResource(R.color.white);
        } else if (newFriendModel.getN_status() == Constants.N_ALREADY_SEND) {
            holder.btn_new_friend.setText("已发送");
            holder.btn_new_friend.setTextColor(ContextCompat.getColor(context, R.color.color_40000000));
            holder.btn_new_friend.setBackgroundResource(R.color.white);
        }
    }

    @Override
    public int getItemCount() {
        return newFriendModels.size();
    }


    public class NewFriendHoldView extends RecyclerView.ViewHolder {

        @BindView(R.id.head_image)
        ImageView head_image;
        @BindView(R.id.friend_name)
        TextView friend_name;
        @BindView(R.id.friend_info)
        TextView friend_info;
        @BindView(R.id.btn_new_friend)
        TextView btn_new_friend;

        public NewFriendHoldView(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
