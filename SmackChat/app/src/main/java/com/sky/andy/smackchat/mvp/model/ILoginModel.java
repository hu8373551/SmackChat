package com.sky.andy.smackchat.mvp.model;

import android.content.Context;

import com.sky.andy.smackchat.base.IBaseRequestCallBack;

/**
 * Created by hcc on 16-10-28.
 * Company MingThink
 */

public interface ILoginModel<T> {
    void login(Context context,String userName, String passWord, IBaseRequestCallBack<T> callBack, int requestTag);
}
