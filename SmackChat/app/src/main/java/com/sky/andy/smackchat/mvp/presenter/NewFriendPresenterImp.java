package com.sky.andy.smackchat.mvp.presenter;

import android.content.Context;

import com.sky.andy.smackchat.base.BasePresenterImp;
import com.sky.andy.smackchat.mvp.model.INewFriendModel;
import com.sky.andy.smackchat.mvp.model.NewFriendsModelImp;
import com.sky.andy.smackchat.mvp.view.INewFriendView;

/**
 * Created by hcc on 16-11-16.
 * Company MingThink
 */

public class NewFriendPresenterImp extends BasePresenterImp<INewFriendView, Object> implements INewFriendPresenter {

    private INewFriendModel newFriendModel;


    public NewFriendPresenterImp(INewFriendView newFriendView) {
        super(newFriendView);
        this.newFriendModel = new NewFriendsModelImp();
    }


    @Override
    public void getNewFriends(Context context, int requestTag) {
        onResume();
        beforeRequest(requestTag);
        newFriendModel.getNewFriends(context, this, requestTag);
    }
}
