package com.sky.andy.smackchat.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.sky.andy.smackchat.interfaces.IShowToastMessage;
import com.sky.andy.smackchat.interfaces.ISkipActivity;
import com.sky.skyweight.dialog.LoadingProDialog;

/**
 * Created by hcc on 16-10-28.
 * Company MingThink
 */

public class SystemUtils implements IShowToastMessage, ISkipActivity {
    private static SystemUtils systemUtils;

    public static SystemUtils getInstance() {
        if (systemUtils == null) {
            systemUtils = new SystemUtils();
        }
        return systemUtils;
    }


    @Override
    public void showLongToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLongToast(Context context, int msgId) {
        Toast.makeText(context, msgId, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showShortToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showShortToast(Context context, int msgId) {
        Toast.makeText(context, msgId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(Context context, String message, int duration) {
        Toast.makeText(context, message, duration).show();
    }

    @Override
    public void showToast(Context context, int msgId, int duration) {
        Toast.makeText(context, msgId, duration).show();
    }


    /**
     * *******************************************activity 跳转相关*******************************
     */

    @Override
    public void showActivity(Class<?> toActivityCls, Activity context) {
        Intent intent = new Intent(context, toActivityCls);
        context.startActivity(intent);
        applyActivityAnim(context);
    }

    @Override
    public void showActivity(Intent intent, Activity context) {
        context.startActivity(intent);
        applyActivityAnim(context);
    }

    @Override
    public void showActivity(Class<?> toActivityCls, Bundle bundle, Activity context) {
        Intent intent = new Intent(context, toActivityCls);
        intent.putExtras(bundle);
        context.startActivity(intent);
        applyActivityAnim(context);
    }

    @Override
    public void skipActivity(Class<?> toActivityCls, Activity context) {
        Intent intent = new Intent(context, toActivityCls);
        context.startActivity(intent);
        finishActivity(context);
    }

    @Override
    public void skipActivity(Intent intent, Activity context) {
        context.startActivity(intent);
        finishActivity(context);
    }

    @Override
    public void skipActivity(Class<?> toActivityCls, Bundle bundle, Activity context) {
        Intent intent = new Intent(context, toActivityCls);
        intent.putExtras(bundle);
        context.startActivity(intent);
        finishActivity(context);
    }

    /**
     * Activity之间应用调转动画
     */
    public void applyActivityAnim(Activity context) {
        context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void finishActivity(Activity context) {
        context.finish();
        applyActivityAnim(context);
    }


    public String spliceStr(String formUser) {
        return formUser.split("/")[0];
    }


    //************************************dialog**************************88
    private LoadingProDialog mDialog;
    private boolean flag = true;

    public void showProgressDialog(Context mContext) {
        if (mDialog != null) {
            mDialog = null;
        }
        mDialog = new LoadingProDialog(mContext);
        mDialog.isCancelable(flag);
        mDialog.show();
    }

    public void showProgressDialog(Context mContext,String msg, boolean flag) {
        if (mDialog != null) {
            mDialog = null;
        }
        if (msg != null) {
            mDialog = new LoadingProDialog(mContext, msg);
        } else {
            mDialog = new LoadingProDialog(mContext);
        }
        mDialog.isCancelable(flag);
        mDialog.show();
    }

    public void dismissProgressDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

}
