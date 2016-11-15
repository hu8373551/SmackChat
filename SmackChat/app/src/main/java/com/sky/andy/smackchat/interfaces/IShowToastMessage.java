package com.sky.andy.smackchat.interfaces;

import android.content.Context;

/**
 * 吐司消息显示接口
 */
public interface IShowToastMessage {
    /**
     * 显示一个长时间的吐司提示
     *
     * @param message 提示内容
     */
    public void showLongToast(Context context, String message);

    /**
     * 显示一个长时间的吐司提示
     *
     * @param msgId 提示内容的资源ID
     */
    public void showLongToast(Context context, int msgId);

    /**
     * 显示一个短时间的吐司提示
     *
     * @param message 提示内容
     */
    public void showShortToast(Context context, String message);

    /**
     * 显示一个短时间的吐司提示
     *
     * @param msgId 提示内容的资源ID
     */
    public void showShortToast(Context context, int msgId);

    /**
     * 显示一个自定义时长的吐司提示
     *
     * @param message  提示内容
     * @param duration 吐司提示显示的时间
     */
    public void showToast(Context context, String message, int duration);

    /**
     * 显示一个自定义时长的吐司提示
     *
     * @param msgId    提示内容的资源ID
     * @param duration 吐司提示显示的时间
     */
    public void showToast(Context context, int msgId, int duration);
}
