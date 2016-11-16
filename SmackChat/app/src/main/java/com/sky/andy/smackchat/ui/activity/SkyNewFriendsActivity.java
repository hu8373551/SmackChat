package com.sky.andy.smackchat.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.sky.andy.smackchat.Constants;
import com.sky.andy.smackchat.R;
import com.sky.andy.smackchat.base.BaseActivity;
import com.sky.andy.smackchat.bean.entry.FriendEntryModel;
import com.sky.andy.smackchat.bean.entry.NewFriendModel;
import com.sky.andy.smackchat.db.SmackDataBaseHelper;
import com.sky.andy.smackchat.manager.SmackManager;
import com.sky.andy.smackchat.mvp.presenter.INewFriendPresenter;
import com.sky.andy.smackchat.mvp.presenter.NewFriendPresenterImp;
import com.sky.andy.smackchat.mvp.view.INewFriendView;
import com.sky.andy.smackchat.ui.adapter.NewFriendRecyclerAdapter;
import com.sky.andy.smackchat.utils.view.DividerItemDecoration;
import com.sky.skyweight.topbar.TopBarViewWithLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by hcc on 16-11-16.
 * Company MingThink
 */

public class SkyNewFriendsActivity extends BaseActivity implements TopBarViewWithLayout.onTopBarClickListener, INewFriendView {

    private ArrayList<NewFriendModel> newFriendModels = new ArrayList<>();
    private NewFriendRecyclerAdapter mNewFriendRecyclerAdapter;

    private INewFriendPresenter newFriendPresenter;

    private int REQUEST_CODE = 0x100;
    @BindView(R.id.my_bar)
    TopBarViewWithLayout my_bar;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friends);
        ButterKnife.bind(this);
        newFriendPresenter = new NewFriendPresenterImp(this);
        newFriendPresenter.getNewFriends(this, REQUEST_CODE);
        my_bar.setTvTitle("新的朋友");
        my_bar.setRightText("添加朋友");
        my_bar.setLeftImageRecource(R.drawable.back_btn);
        my_bar.setOnTopBarClickListener(this);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mNewFriendRecyclerAdapter = new NewFriendRecyclerAdapter(this, newFriendModels);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, RecyclerView.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mNewFriendRecyclerAdapter);

        mNewFriendRecyclerAdapter.setOnItemListener(new NewFriendRecyclerAdapter.OnItemListener() {
            @Override
            public void OnItemClick(int position) {
                //TODO 点击同意添加用户
                final NewFriendModel newFriendModel = newFriendModels.get(position);
                SmackManager.getInstance().acceptFriend(newFriendModel.getN_jid(), newFriendModel.getN_name());
                newFriendModel.setN_status(Constants.N_ALREADY_ADD);
                // TODO 此处有关数据库处理最好放到线程中处理
                SmackDataBaseHelper.getInstants().updateNewFriendStatus(newFriendModel);
                mNewFriendRecyclerAdapter.notifyDataSetChanged();
                Log.e("hcc","newFriendModel tostring-->>"+newFriendModel.toString());
                // 保存朋友到数据库中;
                FriendEntryModel friendEntryModel = new FriendEntryModel();
                friendEntryModel.setJid(newFriendModel.getN_jid());
                friendEntryModel.setFullJid(newFriendModel.getN_full_jid());
                friendEntryModel.setName(newFriendModel.getN_name());
                friendEntryModel.setCurrentUserJid(newFriendModel.getN_current_jid());
                friendEntryModel.setImageHead(newFriendModel.getN_head_image());

                if (SmackDataBaseHelper.getInstants().isAlreadyFriend(newFriendModel.getN_jid(), newFriendModel.getN_current_jid())) {
                    SmackDataBaseHelper.getInstants().updateFriend(friendEntryModel);
                } else {
                    SmackDataBaseHelper.getInstants().saveFriends(friendEntryModel);
                }
                EventBus.getDefault().post("");
            }

            @Override
            public void OnItemLongClick() {

            }
        });

    }


    @Override
    public void onClickLeftButton() {
        this.finish();
    }

    @Override
    public void onClickRightButton() {

    }

    @Override
    public void toast(String msg, int requestTag) {

    }

    @Override
    public void showProgress(int requestTag) {

    }

    @Override
    public void hideProgress(int requestTag) {

    }

    @Override
    public void loadDataSuccess(Object data, int requestTag) {
        newFriendModels = (ArrayList<NewFriendModel>) data;
        Log.e("hcc", "loadDataSuccess-->>" + newFriendModels.size());
        mNewFriendRecyclerAdapter.setData(newFriendModels);
    }

    @Override
    public void loadDataError(Throwable e, int requestTag) {

    }
}
