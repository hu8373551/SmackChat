package com.sky.andy.smackchat.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sky.andy.smackchat.R;
import com.sky.andy.smackchat.bean.entry.FriendEntryModel;
import com.sky.andy.smackchat.mvp.presenter.FriendPresenterImp;
import com.sky.andy.smackchat.mvp.presenter.IFriendPresenter;
import com.sky.andy.smackchat.mvp.view.IFriendView;
import com.sky.andy.smackchat.ui.activity.SkyChatActivity;
import com.sky.andy.smackchat.ui.adapter.RecyclerAdapter;
import com.sky.andy.smackchat.utils.SystemUtils;
import com.sky.andy.smackchat.utils.view.DividerItemDecoration;
import com.sky.skyweight.tablayout.BaseFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by hcc on 16-10-31.
 * Company MingThink
 */

public class SkyConnectFragment extends BaseFragment implements IFriendView {
    private RecyclerAdapter mRecyclerAdapter;
    private ArrayList<FriendEntryModel> mFriends = new ArrayList<>();

    private IFriendPresenter mIFriendPresenter;
    private int RequestMsg = 101;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIFriendPresenter = new FriendPresenterImp(this);
        EventBus.getDefault().register(this);

    }

    @Override
    public void fetchData() {
        mIFriendPresenter.getFriendList(getContext(), RequestMsg);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connect, container, false);

        ButterKnife.bind(this, view);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerAdapter = new RecyclerAdapter(getContext(), mFriends);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), RecyclerView.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.setOnItemListener(new RecyclerAdapter.OnItemListener() {
            @Override
            public void OnItemClick(int position) {
                // TODO 执行点击事件
                Intent intent = new Intent(getContext(), SkyChatActivity.class);
                Log.e("hcc", "user-->>" + mFriends.get(position).getJid());
                intent.putExtra("user", mFriends.get(position).getJid());
                intent.putExtra("nickname", mFriends.get(position).getName());
                SystemUtils.getInstance().showActivity(intent, getActivity());
            }

            @Override
            public void OnItemLongClick() {
                // TODO somethings
            }
        });
        return view;
    }

    @Override
    public void toast(String msg, int requestTag) {

    }

    @Override
    public void showProgress(int requestTag) {
        SystemUtils.getInstance().showProgressDialog(getContext());
    }

    @Override
    public void hideProgress(int requestTag) {
        SystemUtils.getInstance().dismissProgressDialog();
    }

    @Override
    public void loadDataSuccess(Object data, int requestTag) {
        this.mFriends = (ArrayList<FriendEntryModel>) data;
        Log.e("hcc", "friends-->>>" + mFriends);
        mRecyclerAdapter.setFriends(mFriends);
        SystemUtils.getInstance().showShortToast(getContext(), "获取用户列表");
    }

    @Override
    public void loadDataError(Throwable e, int requestTag) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void reflashView(String str) {
        Log.e("hcc", "reflashView-->>");
        if (str.equals("updateView")) {
            mRecyclerAdapter.notifyDataSetChanged();
        }
    }


}
