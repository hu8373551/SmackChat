package com.sky.andy.smackchat.mvp.model;

import android.content.Context;

import com.sky.andy.smackchat.base.IBaseRequestCallBack;

/**
 * Created by hcc on 16-11-16.
 * Company MingThink
 */

public interface INewFriendModel<T> {
    void getNewFriends(Context context, IBaseRequestCallBack<T> callBack, int requestTag);
}
