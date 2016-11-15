package com.sky.andy.smackchat.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sky.skyweight.dialog.LoadingProDialog;

/**
 * Created by hcc on 16-10-27.
 * Company MingThink
 */

public class BaseActivity extends AppCompatActivity{
    private LoadingProDialog mDialog;
    private boolean flag = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void showProgressDialog() {
        if (mDialog != null) {
            mDialog = null;
        }
        mDialog = new LoadingProDialog(this);
        mDialog.isCancelable(flag);
        mDialog.show();
    }

    public void showProgressDialog(String msg, boolean flag) {
        if (mDialog != null) {
            mDialog = null;
        }
        if (msg != null) {
            mDialog = new LoadingProDialog(this, msg);
        } else {
            mDialog = new LoadingProDialog(this);
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
