package com.sky.andy.smackchat.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.sky.andy.smackchat.R;
import com.sky.andy.smackchat.base.BaseActivity;
import com.sky.andy.smackchat.listener.ChatManagerListener;
import com.sky.andy.smackchat.listener.FileTransListener;
import com.sky.andy.smackchat.manager.SmackManager;
import com.sky.andy.smackchat.mvp.presenter.ILoginPresenter;
import com.sky.andy.smackchat.mvp.presenter.LoginPresenterImp;
import com.sky.andy.smackchat.mvp.view.ILoginView;
import com.sky.andy.smackchat.utils.SystemUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hcc on 16-10-28.
 * Company MingThink
 */

public class SkyLoginActivity extends BaseActivity implements ILoginView {
    private String userNameStr;
    private String passwordStr;
    private final int RequestMsg = 0;

    private ILoginPresenter loginPresenter;


    @BindView(R.id.userNameEd)
    EditText UserNameEd;

    @BindView(R.id.passwordEd)
    EditText PassWordEd;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginPresenter = new LoginPresenterImp(this);
        ButterKnife.bind(this);
        UserNameEd.setText("hucc");
        PassWordEd.setText("hucc");


    }

    @OnClick(R.id.loginBtn)
    public void login() {
        if (UserNameEd.getText().toString().equals("")) {
            Toast.makeText(this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }

        if (PassWordEd.getText().toString().equals("")) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }

        userNameStr = UserNameEd.getText().toString();
        passwordStr = PassWordEd.getText().toString();
        loginPresenter.login(this,userNameStr, passwordStr, RequestMsg);
    }

    @Override
    public void toast(String msg, int requestTag) {

    }

    @Override
    public void showProgress(int requestTag) {
        showProgressDialog();
    }

    @Override
    public void hideProgress(int requestTag) {
        dismissProgressDialog();
    }

    @Override
    public void loadDataSuccess(Object data, int requestTag) {
        Log.e("hcc", "data-->>" + (Integer) data);
        SmackManager.getInstance().getChatManager().addChatListener(new ChatManagerListener(getApplicationContext()));
        SmackManager.getInstance().addFileTransferListener(new FileTransListener(getApplicationContext()));

        SmackManager.getInstance().getUserStatus();
        SmackManager.getInstance().filterFriend(this);
        SystemUtils.getInstance().showShortToast(this, R.string.login_success);
        //进入聊天列表
        SystemUtils.getInstance().skipActivity(SkyMainActivity.class, this);


    }

    @Override
    public void loadDataError(Throwable e, int requestTag) {
        Log.e("hcc", "data-->>" + e);
        SystemUtils.getInstance().showShortToast(this, R.string.login_fail);
        dismissProgressDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("hcc","login__des");
    }

}
