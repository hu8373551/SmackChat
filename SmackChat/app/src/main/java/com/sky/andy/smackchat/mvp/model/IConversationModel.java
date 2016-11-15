package com.sky.andy.smackchat.mvp.model;

import com.sky.andy.smackchat.base.IBaseRequestCallBack;

/**
 * Created by hcc on 16-11-3.
 * Company MingThink
 */

public interface IConversationModel<T> {
    void getConversations(IBaseRequestCallBack<T> callBack, int requestTag);
}
