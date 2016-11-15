package com.sky.andy.smackchat.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sky.andy.smackchat.R;
import com.sky.andy.smackchat.base.BaseActivity;
import com.sky.andy.smackchat.bean.SearchFriend;
import com.sky.andy.smackchat.manager.SmackManager;
import com.sky.andy.smackchat.ui.adapter.SearchFriendAdapter;
import com.sky.andy.smackchat.utils.SystemUtils;
import com.sky.andy.smackchat.utils.view.DividerItemDecoration;
import com.sky.skyweight.topbar.TopBarViewWithLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hcc on 16-11-8.
 * Company MingThink
 */

public class SkyAddFriendsActivity extends BaseActivity {


    private SearchFriendAdapter mRecyclerAdapter;
    private ArrayList<SearchFriend> mSearchFriends=new ArrayList<>();

    @BindView(R.id.my_bar)
    TopBarViewWithLayout mMyBar;
    @BindView(R.id.btn_searchfriend)
    Button searchBtn;
    @BindView(R.id.edit_addfriend)
    EditText friendName;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private String friendStr;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        ButterKnife.bind(this);


        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerAdapter = new SearchFriendAdapter(this, mSearchFriends);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mRecyclerAdapter);


        mMyBar.setOnTopBarClickListener(new TopBarViewWithLayout.onTopBarClickListener() {
            @Override
            public void onClickLeftButton() {
                SystemUtils.getInstance().finishActivity(SkyAddFriendsActivity.this);
            }

            @Override
            public void onClickRightButton() {

            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("hcc", "onClick--->>>");
                ArrayList<SearchFriend> searchFriends = SmackManager.getInstance().searchUser(friendName.getText().toString().trim());
                mRecyclerAdapter.setSearchFriend(searchFriends);

            }
        });

    }
}
