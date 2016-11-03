package com.tik.floatviewdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.tik.floatviewdemo.manager.FloatViewManager;

import java.lang.Override;

public class FloatViewService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FloatViewManager.getInstance(this).showFloatView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        FloatViewManager.getInstance(this).removeFloatView();
    }
}
