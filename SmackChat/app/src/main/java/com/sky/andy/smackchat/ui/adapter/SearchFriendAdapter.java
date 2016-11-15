package com.sky.andy.smackchat.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sky.andy.smackchat.R;
import com.sky.andy.smackchat.bean.SearchFriend;
import com.sky.andy.smackchat.manager.SmackManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Andy on 2016/11/8.
 */

public class SearchFriendAdapter extends RecyclerView.Adapter<SearchFriendAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<SearchFriend> searchFriends = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public SearchFriendAdapter(Context mContext, ArrayList<SearchFriend> searchFriends) {
        this.mContext = mContext;
        this.searchFriends = searchFriends;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    public void setSearchFriend(ArrayList<SearchFriend> searchFriends) {
        this.searchFriends = searchFriends;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.recycler_search_friend_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final SearchFriend searchFriend = searchFriends.get(position);
        holder.searchUserNameTv.setText(searchFriend.getUserName());
        holder.searchResponse.setText(searchFriend.getName());
        holder.imageAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //执行添加好友
                SmackManager.getInstance().addFriend(searchFriend.getUserName(), searchFriend.getName(), null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchFriends.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.img_seachFriend)
        ImageView imgSearch;
        @BindView(R.id.text_searchFriend)
        TextView searchUserNameTv;
        @BindView(R.id.text_response)
        TextView searchResponse;
        @BindView(R.id.img_addFriend)
        ImageView imageAddFriend;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
