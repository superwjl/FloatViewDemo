package com.tik.floatviewdemo.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tik.floatviewdemo.manager.ActivityManager;

import java.lang.Override;import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 此方法在注入View之前调用 用于当前Activity独立功能的初始化
     */
    protected abstract void beforBindViews(Bundle savedInstanceState);

    /**
     * 此方法在注入View之后调用
     */
    protected abstract void afterBindViews(Bundle savedInstanceState);

    /**
     * 返回当前Activity对应的XML资源
     */
    protected abstract int getLayoutId();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().addActivity(this);
        beforBindViews(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        afterBindViews(savedInstanceState);

    }


}
