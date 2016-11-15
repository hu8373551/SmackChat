package com.sky.andy.smackchat.mvp.presenter;

import android.content.Context;

import com.sky.andy.smackchat.base.BasePresenterImp;
import com.sky.andy.smackchat.mvp.model.ILoginModel;
import com.sky.andy.smackchat.mvp.model.LoginModelImp;
import com.sky.andy.smackchat.mvp.view.ILoginView;

/**
 * Created by hcc on 16-10-28.
 * Company MingThink
 */

public class LoginPresenterImp extends BasePresenterImp<ILoginView, Object> implements ILoginPresenter {

    private ILoginModel loginModel;

    /**
     * 构造方法
     *
     * @param view
     */
    public LoginPresenterImp(ILoginView view) {
        super(view);
        loginModel = new LoginModelImp();
    }

    @Override
    public void login(Context context, String userName, String passWord, int requestTag) {
        onResume();
        beforeRequest(requestTag);
        loginModel.login(context, userName, passWord, this, requestTag);
    }
}
