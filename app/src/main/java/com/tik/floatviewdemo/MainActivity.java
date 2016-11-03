package com.tik.floatviewdemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.tik.floatviewdemo.base.BaseActivity;
import com.tik.floatviewdemo.manager.ActivityManager;
import com.tik.floatviewdemo.service.FloatViewService;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.show_float)
    Button mBtnShowFloat;

    @BindView(R.id.hide_float)
    Button mBtnHideFloat;

    @OnClick({R.id.show_float, R.id.hide_float})
    void onClick(View v){
        switch (v.getId()){
            case R.id.show_float:
                Intent intent = new Intent(this, FloatViewService.class);
                startService(intent);
                break;
            case R.id.hide_float:
                Intent intent2 = new Intent(this, FloatViewService.class);
                stopService(intent2);
                break;
        }

    }


    @Override
    protected void beforBindViews(Bundle savedInstanceState) {

    }

    @Override
    protected void afterBindViews(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().finishActivity(this);
    }
}
