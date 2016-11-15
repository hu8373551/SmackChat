package com.sky.andy.smackchat.mvp.presenter;

import android.content.Context;

import com.sky.andy.smackchat.base.BasePresenterImp;
import com.sky.andy.smackchat.mvp.model.FriendModelImp;
import com.sky.andy.smackchat.mvp.model.IFriendModel;
import com.sky.andy.smackchat.mvp.view.IFriendView;

/**
 * Created by hcc on 16-10-28.
 * Company MingThink
 */

public class FriendPresenterImp extends BasePresenterImp<IFriendView, Object> implements IFriendPresenter {

    private IFriendModel friendModel;

    /**
     * 构造方法
     *
     * @param view
     */
    public FriendPresenterImp(IFriendView view) {
        super(view);
        friendModel = new FriendModelImp();
    }

    @Override
    public void getFriendList(Context mContext,int requestTag) {
        onResume();
        beforeRequest(requestTag);
        friendModel.getFriendList(mContext, this, requestTag);
    }
}
