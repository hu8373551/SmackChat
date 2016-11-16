package com.sky.andy.smackchat.mvp.model;

import android.content.Context;
import android.util.Log;

import com.sky.andy.smackchat.Constants;
import com.sky.andy.smackchat.base.IBaseRequestCallBack;
import com.sky.andy.smackchat.bean.entry.NewFriendModel;
import com.sky.andy.smackchat.db.SmackDataBaseHelper;
import com.sky.skyweight.SharePrefenceUtils;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hcc on 16-11-16.
 * Company MingThink
 */

public class NewFriendsModelImp implements INewFriendModel {

    @Override
    public void getNewFriends(final Context context, final IBaseRequestCallBack callBack, final int requestTag) {
        Observable.create(new ObservableOnSubscribe<ArrayList<NewFriendModel>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<NewFriendModel>> e) throws Exception {
                // TODO 处理读取数据库事件
                String currentUserJid = SharePrefenceUtils.readString(context, Constants.SP_USER_INFO, Constants.SP_USER_JID);
                ArrayList<NewFriendModel> newFriendModels = SmackDataBaseHelper.getInstants().findNewFriends(currentUserJid);
                Log.e("hcc", "v-->>" + newFriendModels.size());
                e.onNext(newFriendModels);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<NewFriendModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<NewFriendModel> value) {
                        callBack.requestSuccess(value, requestTag);

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
