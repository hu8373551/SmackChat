package com.sky.andy.smackchat.base;

/***
 * 请求数据回调
 * Presenter 用于接受model获取、加载数据后的回调
 */
public interface IBaseRequestCallBack<T> {

    /**
     * 开始请求前
     *
     * @param requestTag
     */
    void beforeRequest(int requestTag);


    /**
     * 请求成功
     *
     * @param callBack 根据业务返回相应的数据
     */
    void requestSuccess(T callBack, int requestTag);

    /**
     * 请求失败
     *
     * @param e 失败的原因
     */
    void requestError(Throwable e, int requestTag);


    /**
     * 请求结束
     */
    void requestComplete(int requestTag);

}
