package com.sky.andy.smackchat.mvp.model;

import android.util.Log;

import com.sky.andy.smackchat.base.IBaseRequestCallBack;
import com.sky.andy.smackchat.bean.entry.ConversationModel;
import com.sky.andy.smackchat.db.SmackDataBaseHelper;
import com.sky.andy.smackchat.manager.SmackManager;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by hcc on 16-11-3.
 * Company MingThink
 */

public class ConversationModelImp implements IConversationModel {

    @Override
    public void getConversations(final IBaseRequestCallBack callBack, final int requestTag) {
        Observable.create(new ObservableOnSubscribe<ArrayList<ConversationModel>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<ConversationModel>> e) throws Exception {
                // 处理登陆事件
                String currentUserJid = SmackManager.getInstance().getAccountJid();
                ArrayList<ConversationModel> conversationModels =
                        SmackDataBaseHelper.getInstants().findAllConversation(currentUserJid);
                Log.e("hcc","conversationModels-->>"+conversationModels.size());
                if (conversationModels != null) {
                    e.onNext(conversationModels);
                    e.onComplete();
                } else {
                    e.onNext(null);
                }

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<ConversationModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<ConversationModel> value) {
                        if (value != null) {
                            callBack.requestSuccess(value, requestTag);
                        } else {
                            callBack.requestError(new Exception("fail"), requestTag);
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
