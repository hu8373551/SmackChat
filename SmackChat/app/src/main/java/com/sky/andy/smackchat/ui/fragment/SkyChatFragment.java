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
import com.sky.andy.smackchat.bean.GetChatMessage;
import com.sky.andy.smackchat.bean.entry.ConversationModel;
import com.sky.andy.smackchat.mvp.presenter.ConversationPresenter;
import com.sky.andy.smackchat.mvp.presenter.IConversationPresenter;
import com.sky.andy.smackchat.mvp.view.IConversationView;
import com.sky.andy.smackchat.ui.activity.SkyChatActivity;
import com.sky.andy.smackchat.ui.adapter.ConversationAdapter;
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

public class SkyChatFragment extends BaseFragment implements IConversationView {

    private ArrayList<ConversationModel> conversationModels = new ArrayList<>();
    private ConversationAdapter mConversationAdapter;
    private IConversationPresenter mConversationPresenter;
    private int RequestTag = 1002;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    public void fetchData() {
        mConversationPresenter.getConversations(RequestTag);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        mConversationPresenter = new ConversationPresenter(this);
        ButterKnife.bind(this, view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mConversationAdapter = new ConversationAdapter(getContext(), conversationModels);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), RecyclerView.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mConversationAdapter);
        mConversationAdapter.setOnItemListener(new ConversationAdapter.OnItemListener() {
            @Override
            public void OnItemClick(int position) {
                // TODO 执行点击事件
                Intent intent = new Intent(getContext(), SkyChatActivity.class);
                intent.putExtra("user", conversationModels.get(position).getC_jid());
                intent.putExtra("nickname", conversationModels.get(position).getC_name());
//                SystemUtils.getInstance().showActivity(intent, getActivity());
                startActivityForResult(intent, 0);
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

    }

    @Override
    public void hideProgress(int requestTag) {

    }

    @Override
    public void loadDataSuccess(Object data, int requestTag) {
        this.conversationModels = (ArrayList<ConversationModel>) data;
        Log.e("hcc", "friends-->>>" + conversationModels);
        mConversationAdapter.setConversation(conversationModels);
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
    public void reflashConversation(GetChatMessage message) {
        mConversationPresenter.getConversations(RequestTag);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 根据上面发送过去的请求吗来区别
        switch (requestCode) {
            case 0:
                mConversationPresenter.getConversations(RequestTag);
                break;
            default:
                break;
        }

    }
}
