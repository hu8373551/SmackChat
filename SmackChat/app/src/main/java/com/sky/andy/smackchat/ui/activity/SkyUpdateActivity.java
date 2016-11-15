package com.sky.andy.smackchat.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.sky.andy.smackchat.Constants;
import com.sky.andy.smackchat.R;
import com.sky.andy.smackchat.base.BaseActivity;
import com.sky.andy.smackchat.bean.UserInfo;
import com.sky.andy.smackchat.manager.SmackManager;
import com.sky.skyweight.SharePrefenceUtils;
import com.sky.skyweight.dialog.LoadingProDialog;
import com.sky.skyweight.topbar.TopBarViewWithLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hcc on 16-11-14.
 * Company MingThink
 */

public class SkyUpdateActivity extends BaseActivity implements TopBarViewWithLayout.onTopBarClickListener {

    @BindView(R.id.my_bar)
    TopBarViewWithLayout my_bar;

    @BindView(R.id.ed_name)
    EditText ed_name;

    private LoadingProDialog loadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_layout);
        ButterKnife.bind(this);
        loadingDialog = new LoadingProDialog(this);
        my_bar.setTvTitle("昵称");
        my_bar.setRightText("保存");
        my_bar.setOnTopBarClickListener(this);

        String nickName = getIntent().getExtras().getString("nickName");
        if (nickName != null) {
            ed_name.setText(nickName);
        }
    }

    @Override
    public void onClickLeftButton() {
        finish();
    }

    @Override
    public void onClickRightButton() {
        //异步处理保存用户数据
        doUpdateUser();

        loadingDialog.show();

    }

    public void doUpdateUser() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                // 处理登陆事件
                try {
                    SharePrefenceUtils.write(SkyUpdateActivity.this, Constants.SP_USER_INFO, Constants.SP_USER_NICKNAME, ed_name.getText().toString().trim());
                    UserInfo userInfo = new UserInfo();
                    userInfo.setBareJid(SharePrefenceUtils.readString(SkyUpdateActivity.this, Constants.SP_USER_INFO, Constants.SP_USER_JID));
                    userInfo.setUserName(ed_name.getText().toString().trim());
                    SmackManager.getInstance().saveUserNickName(userInfo);
                    e.onNext(200);
                } catch (Exception ex) {
                    Log.e("hcc", "ex-->>>" + ex);
                    e.onNext(400);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Integer value) {
                        if (value == 200) {
                            loadingDialog.dismiss();
                            Intent data = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("nickName", ed_name.getText().toString().trim());
                            data.putExtras(bundle);
                            setResult(RESULT_OK, data);
                            SkyUpdateActivity.this.finish();
                        }
                        if (value == 400) {
                            loadingDialog.dismiss();
                            Toast.makeText(SkyUpdateActivity.this, "保存失败，请重新保存", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

}
