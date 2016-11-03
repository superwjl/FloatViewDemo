package com.tik.floatviewdemo.manager;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.tik.floatviewdemo.widget.FloatMenuView;
import com.tik.floatviewdemo.widget.FloatView;

import java.lang.Class;import java.lang.ClassNotFoundException;
import java.lang.IllegalAccessException;
import java.lang.InstantiationException;
import java.lang.Math;
import java.lang.NoSuchFieldException;
import java.lang.Object;
import java.lang.Override;
import java.lang.reflect.Field;

/**
 * 悬浮球/加速球管理类
 */
public class FloatViewManager {

    private Context mContext;
    private static FloatViewManager instance;
    private WindowManager mWindowManager;
    private FloatView mFloatView;
    private FloatMenuView mFloatMenuView;
    private WindowManager.LayoutParams params;
    private int screenWidth;
    private int screenHeight;
    /**
     * 悬浮球坐标
     */
    private int mFloatViewX = 0;
    private int mFloatViewY = 0;
    private View.OnTouchListener floatViewTouchListener = new View.OnTouchListener() {

        private float mStartX;
        private float mStartY;
        private float mDownX;
        private float mDownY;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    mStartX = event.getRawX();
                    mStartY = event.getRawY();
                    mDownX = event.getRawX();
                    mDownY = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float x = event.getRawX();
                    float y = event.getRawY();
                    float dx = x - mStartX;
                    float dy = y - mStartY;
                    params.x += dx;
                    params.y += dy;
                    mFloatView.setIsDragging(true);
                    mFloatViewX = params.x;
                    mFloatViewY = params.y;
                    mWindowManager.updateViewLayout(mFloatView, params);
                    mStartX = x;
                    mStartY = y;
                    break;
                case MotionEvent.ACTION_UP:
                    float endX = event.getRawX();
                    if(endX < screenWidth / 2){
                        params.x = 0;
                    }else{
                        params.x = screenWidth - mFloatView.getWidth();
                    }
                    mFloatView.setIsDragging(false);
                    mFloatViewX = params.x;
                    mWindowManager.updateViewLayout(mFloatView, params);
                    if(Math.abs(endX - mDownX) > 6){
                        return true;
                    }else {
                        return false;
                    }
            }
            return false;
        }
    };


    public FloatViewManager(final Context context) {
        this.mContext = context;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mFloatView = new FloatView(context);
        mFloatView.setOnTouchListener(floatViewTouchListener);
        mFloatView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindowManager.removeView(mFloatView);
                showFloatMenuView();
            }
        });
        getScreenParams();
        mFloatMenuView = new FloatMenuView(context);
        mFloatMenuView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWindowManager.removeView(mFloatMenuView);
                showFloatView();

            }
        });
    }

    public static FloatViewManager getInstance(Context context){
        if(instance == null){
            synchronized (FloatViewManager.class){
                if(instance == null){
                    instance = new FloatViewManager(context);
                }
            }
        }
        return instance;
    }

    public void getScreenParams(){
        Point point = new Point();
        mWindowManager.getDefaultDisplay().getSize(point);
        screenWidth = point.x;
        screenHeight = point.y;
    }

    public void showFloatView(){
        params = new WindowManager.LayoutParams();
        params.width = mFloatView.getWidth();
        params.height = mFloatView.getHeight();
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = mFloatViewX;
        params.y = mFloatViewY;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        params.format = PixelFormat.RGBA_8888;
        mWindowManager.addView(mFloatView, params);
    }

    public void showFloatMenuView(){
        params = new WindowManager.LayoutParams();
        params.width = screenWidth;
        params.height = screenHeight - getStatusHeight();
        params.gravity = Gravity.BOTTOM | Gravity.LEFT;
        params.x = 0;
        params.y = 0;
        params.type = WindowManager.LayoutParams.TYPE_PHONE;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        params.format = PixelFormat.RGBA_8888;
        mWindowManager.addView(mFloatMenuView, params);
        mFloatMenuView.startAnimation();
    }

    public int getStatusHeight(){
        int height = 0;
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object o = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = (int) field.get(o);
            height = mContext.getResources().getDimensionPixelSize(x);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return height;
    }

    public void removeFloatView(){
        if(mWindowManager != null && mFloatView != null){
            mWindowManager.removeView(mFloatView);
        }
    }

}
