package com.sky.andy.smackchat.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sky.andy.smackchat.R;
import com.sky.andy.smackchat.bean.entry.FriendEntryModel;
import com.sky.andy.smackchat.manager.SmackManager;

import org.jivesoftware.smack.packet.Presence;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hcc on 16-10-31.
 * Company MingThink
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private Context context;
    private LayoutInflater mLayoutInflater;
    private ArrayList<FriendEntryModel> mFriends = new ArrayList<>();
    private OnItemListener mOnItemListener;

    public interface OnItemListener {
        void OnItemClick(int position);

        void OnItemLongClick();
    }

    public void setOnItemListener(OnItemListener mOnItemListener) {
        this.mOnItemListener = mOnItemListener;
    }

    public RecyclerAdapter(Context context, ArrayList<FriendEntryModel> mFriends) {
        this.mFriends = mFriends;
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setFriends(ArrayList<FriendEntryModel> mFriends) {
        this.mFriends = mFriends;
        this.notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(mLayoutInflater.inflate(R.layout.recycler_friend_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        FriendEntryModel friendEntryModel = mFriends.get(position);
        Log.e("hcc", "friendEntryModel-->>>" + friendEntryModel.getImageHead());
        Presence presence = SmackManager.getInstance().getPresence(friendEntryModel.getJid());

        Glide.with(context)
                .load(friendEntryModel.getImageHead())
                .centerCrop()
                .into(holder.head_image);

//        if (friendEntryModel.getImageHead() != null) {
//            ByteArrayInputStream bais = new ByteArrayInputStream(
//                    friendEntryModel.getImageHead());
//            Bitmap bitmap = BitmapFactory.decodeStream(bais);
//            holder.head_image.setImageBitmap(bitmap);
//        }
        Log.e("hcc", "friends-->>" + friendEntryModel.getName() + "\n user " + friendEntryModel.getJid()
                + "\n presence.getFrom()" + presence.getFrom());

        holder.fridendName.setText(friendEntryModel.getJid());
        if (presence.isAvailable()) {
            holder.userStatus.setText("[在线]");
        } else {
            holder.userStatus.setText("[离线]");
        }
        holder.linear_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemListener.OnItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.e("hcc", "mFriends-->>" + mFriends.size());
        return mFriends.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.linear_layout)
        RelativeLayout linear_layout;
        @BindView(R.id.head_image)
        ImageView head_image;
        @BindView(R.id.text_friend_name)
        TextView fridendName;
        @BindView(R.id.user_status)
        TextView userStatus;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


}
