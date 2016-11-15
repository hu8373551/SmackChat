package com.sky.andy.smackchat.mvp.model;

import android.content.Context;
import android.util.Log;

import com.sky.andy.smackchat.Constants;
import com.sky.andy.smackchat.base.IBaseRequestCallBack;
import com.sky.andy.smackchat.manager.SmackManager;
import com.sky.skyweight.SharePrefenceUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hcc on 16-10-28.
 * Company MingThink
 */

public class LoginModelImp implements ILoginModel {


    @Override
    public void login(final Context mContext, final String userName, final String passWord, final IBaseRequestCallBack callBack, final int requestTag) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                // 处理登陆事件

                boolean isLogined = SmackManager.getInstance().isLogined();
                Log.e("hcc","isLogined-->>"+isLogined);
                if (isLogined) {
                    SmackManager.getInstance().disconnect();
                }
                boolean loginSuccess = SmackManager.getInstance().login(userName, passWord);
                if (loginSuccess) {
                    //保存用户数据到ｓｈａｒｅＰｒｅｆｅｒｅｎｃｅｓ　中
                    String currentUserJid = SmackManager.getInstance().getAccountJid();
                    String currentUserNickName = SmackManager.getInstance().getAccountName();
                    String currentUserHeaderImg = SharePrefenceUtils.bytesToHexString(SmackManager.getInstance().getUserImage(currentUserJid));
                    SharePrefenceUtils.write(mContext, Constants.SP_USER_INFO, Constants.SP_USER_JID, currentUserJid);
                    SharePrefenceUtils.write(mContext, Constants.SP_USER_INFO, Constants.SP_USER_NICKNAME, currentUserNickName);
                    SharePrefenceUtils.write(mContext, Constants.SP_USER_INFO, Constants.SP_USER_HEADER_IMAGE, currentUserHeaderImg);
                    e.onNext(200);
                    e.onComplete();
                } else {
                    e.onNext(400);
                }
//                } else {
//                    e.onNext(401);
//                }

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer value) {
                        if (value == 200) {
                            callBack.requestSuccess(value, requestTag);
                        }
                        if (value == 400) {
                            callBack.requestError(new Exception("fail"), requestTag);
                        }

                        if (value == 401) {
                            callBack.requestError(new Exception("logined"), requestTag);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.requestError(new Exception("fail"), requestTag);
                    }

                    @Override
                    public void onComplete() {
                        callBack.requestComplete(requestTag);
                    }
                });
    }
}
