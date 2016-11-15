package com.sky.andy.smackchat.mvp.presenter;

import com.sky.andy.smackchat.base.BasePresenterImp;
import com.sky.andy.smackchat.mvp.model.ConversationModelImp;
import com.sky.andy.smackchat.mvp.model.IConversationModel;
import com.sky.andy.smackchat.mvp.view.IConversationView;

/**
 * Created by hcc on 16-11-3.
 * Company MingThink
 */

public class ConversationPresenter extends BasePresenterImp<IConversationView, Object> implements IConversationPresenter {

    private IConversationModel mConversationModel;


    /**
     * 构造方法
     *
     * @param view
     */
    public ConversationPresenter(IConversationView view) {
        super(view);
        this.mConversationModel = new ConversationModelImp();
    }

    @Override
    public void getConversations(int requestTag) {
        beforeRequest(requestTag);
        mConversationModel.getConversations(this, requestTag);
    }
}
