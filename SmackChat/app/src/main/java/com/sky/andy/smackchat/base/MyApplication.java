package com.sky.andy.smackchat.base;

import android.app.Application;

import com.sky.andy.smackchat.db.SmackDataBase;

/**
 * Created by hcc on 16-11-3.
 * Company MingThink
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        new SmackDataBase(this);
    }
}
