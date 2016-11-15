package com.sky.andy.smackchat.mvp.model;

import android.content.Context;

import com.sky.andy.smackchat.base.IBaseRequestCallBack;

/**
 * Created by hcc on 16-10-31.
 * Company MingThink
 */

public interface IFriendModel <T>{
       void getFriendList(Context mContext, IBaseRequestCallBack<T> callBack, int requestTag);
}
