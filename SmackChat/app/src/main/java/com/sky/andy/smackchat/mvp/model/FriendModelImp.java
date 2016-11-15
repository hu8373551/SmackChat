package com.sky.andy.smackchat.mvp.model;

import android.content.Context;

import com.sky.andy.smackchat.Constants;
import com.sky.andy.smackchat.base.IBaseRequestCallBack;
import com.sky.andy.smackchat.bean.entry.FriendEntryModel;
import com.sky.andy.smackchat.db.SmackDataBaseHelper;
import com.sky.andy.smackchat.manager.SmackManager;
import com.sky.skyweight.SharePrefenceUtils;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.sky.andy.smackchat.db.SmackDataBaseHelper.getInstants;

/**
 * Created by hcc on 16-10-31.
 * Company MingThink
 */

public class FriendModelImp implements IFriendModel {
    @Override
    public void getFriendList(Context mContext, final IBaseRequestCallBack callBack, final int requestTag) {

        if (SharePrefenceUtils.readString(mContext,
                Constants.SP_NAME,
                Constants.SP_ALREADY_LOAD_FRIEND, "").equals(SmackManager.getInstance().getAccountJid())) {
            loadDBFriends(callBack, requestTag);
        } else {
            loadNetFriends(mContext, callBack, requestTag);
            //保存表示已经通过网络请求获取朋友数据
        }
    }

    public void loadNetFriends(final Context mContext, final IBaseRequestCallBack callBack, final int requestTag) {
        Observable.create(new ObservableOnSubscribe<ArrayList<FriendEntryModel>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<FriendEntryModel>> e) throws Exception {
                // 处理登陆事件
                Set<RosterEntry> friends = SmackManager.getInstance().getAllFriends();
                ArrayList<FriendEntryModel> list = new ArrayList<>();
                String currentUserJid = SmackManager.getInstance().getAccountJid();
                for (RosterEntry friend : friends) {
                    byte[] myImageByte = SmackManager.getInstance().getUserImage(friend.getUser());
                    FriendEntryModel friendEntryModel = new FriendEntryModel();
                    friendEntryModel.setJid(friend.getUser());
                    friendEntryModel.setName(friend.getName());
                    friendEntryModel.setFullJid(SmackManager.getInstance().getPresence(friend.getUser()).getFrom());
                    friendEntryModel.setImageHead(myImageByte);
                    friendEntryModel.setCurrentUserJid(currentUserJid);
                    if (SmackDataBaseHelper.getInstants().isAlreadyFriend(friend.getUser(),currentUserJid)) {
                        getInstants().updateFriend(friendEntryModel);
                        list.add(friendEntryModel);
                    } else {
                        getInstants().saveFriends(friendEntryModel);
                        list.add(friendEntryModel);
                    }
                }
                if (friends != null) {
                    e.onNext(list);
                    e.onComplete();
                } else {
                    e.onNext(null);
                }

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<FriendEntryModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<FriendEntryModel> value) {
                        if (value != null) {
                            callBack.requestSuccess(value, requestTag);
                            SharePrefenceUtils.write(mContext, Constants.SP_NAME,
                                    Constants.SP_ALREADY_LOAD_FRIEND, SmackManager.getInstance().getAccountJid());
                        } else {
                            callBack.requestError(new Exception(Constants.FRIENDNUN), requestTag);
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


    public void loadDBFriends(final IBaseRequestCallBack callBack, final int requestTag) {
        Observable.create(new ObservableOnSubscribe<ArrayList<FriendEntryModel>>() {
            @Override
            public void subscribe(ObservableEmitter<ArrayList<FriendEntryModel>> e) throws Exception {
                String currentUserJid = SmackManager.getInstance().getAccountJid();
                ArrayList<FriendEntryModel> friendEntryModels = SmackDataBaseHelper.getInstants().findAllFriends(currentUserJid);
                if (friendEntryModels != null) {
                    e.onNext(friendEntryModels);
                    e.onComplete();
                } else {
                    e.onNext(null);
                }

            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ArrayList<FriendEntryModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<FriendEntryModel> value) {
                        if (value != null) {
                            callBack.requestSuccess(value, requestTag);
                        } else {
                            callBack.requestError(new Exception(Constants.FRIENDNUN), requestTag);
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
