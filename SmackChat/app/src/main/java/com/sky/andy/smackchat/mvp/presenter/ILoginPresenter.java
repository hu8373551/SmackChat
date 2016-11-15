package com.sky.andy.smackchat.mvp.presenter;

import android.content.Context;

/**
 * Created by hcc on 16-10-28.
 * Company MingThink
 */

public interface ILoginPresenter {
    void login(Context context, String userName, String passWord, int requestTag);
}
