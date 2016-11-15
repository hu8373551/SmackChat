package com.sky.andy.smackchat.base;

/**
 * 作者：Andy on 2016/9/14 14:35
 * <p/>
 * 代理的实现类
 * 邮箱：a1993628470@gmail.com
 */
public class BasePresenterImp<T extends IBaseView, V> implements IBasePresenter, IBaseRequestCallBack<V> {

    private final IBaseView iView;

    /**
     * 构造方法
     */
    public BasePresenterImp(T view) {
        this.iView = view;
    }

    @Override
    public void beforeRequest(int requestTag) {
        iView.showProgress(requestTag);
    }

    @Override
    public void requestSuccess(V callBack, int requestTag) {
        //回传数据给UI
        iView.loadDataSuccess(callBack, requestTag);
    }

    @Override
    public void requestError(Throwable e, int requestTag) {
        iView.loadDataError(e, requestTag);
    }

    @Override
    public void requestComplete(int requestTag) {
        iView.hideProgress(requestTag);
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }
}
