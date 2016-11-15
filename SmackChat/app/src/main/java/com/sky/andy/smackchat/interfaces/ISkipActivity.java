package com.sky.andy.smackchat.interfaces;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by hcc on 16-10-31.
 * Company MingThink
 */
public interface ISkipActivity {
    /**
     * 从当前activity跳到类toActivityCls指定的Activity，并不调用 activity's finish()
     *
     * @param toActivityCls
     */
    public void showActivity(Class<?> toActivityCls, Activity context);


    /**
     * 从当前activity启动指定Intent，多用于启动Android内部程序(利用Filter启动Activity)，并不调用 activity's finish()
     *
     * @param intent
     */
    public void showActivity(Intent intent, Activity context);

    /**
     * 从当前activity跳到类toActivityCls指定的Activity，并传递参数bundle，并不调用@param activity's finish()
     *
     * @param toActivityCls
     * @param bundle
     */
    public void showActivity(Class<?> toActivityCls, Bundle bundle, Activity context);

    /**
     * 从当前activity跳到类toActivityCls指定的Activity，并调用@param activity's finish()
     *
     * @param toActivityCls
     */
    public void skipActivity(Class<?> toActivityCls, Activity context);

    /**
     * 从当前activity启动指定Intent，并调用@param activity's finish()
     *
     * @param intent
     */
    public void skipActivity(Intent intent, Activity context);

    /**
     * 从当前activity跳到类toActivityCls指定的Activity，并传递参数bundle，并调用@param activity's finish()
     *
     * @param toActivityCls
     * @param bundle
     */
    public void skipActivity(Class<?> toActivityCls, Bundle bundle, Activity context);

    /**
     * 关闭该Activity
     */
    public void finishActivity(Activity context);
}
