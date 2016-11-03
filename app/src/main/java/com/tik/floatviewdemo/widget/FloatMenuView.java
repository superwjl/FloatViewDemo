package com.tik.floatviewdemo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.tik.floatviewdemo.R;

import java.lang.Override;

/**
 * 加速球菜单视图
 */
public class FloatMenuView extends LinearLayout {
    private LinearLayout mLinearLayout;
    private CustomProgressView mCustomProgressView;
    private TranslateAnimation mEnterTranslateAnimation;
    public FloatMenuView(Context context) {
        super(context);
        init();
    }

    public FloatMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FloatMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init(){
        View view = View.inflate(getContext(), R.layout.float_menu_view, null);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.uill_layout);
        mCustomProgressView = (CustomProgressView) view.findViewById(R.id.customProgressView);
        mEnterTranslateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0);
        mEnterTranslateAnimation.setDuration(200);
        mEnterTranslateAnimation.setFillAfter(true);
        mEnterTranslateAnimation.setInterpolator(new LinearInterpolator());
        mLinearLayout.setAnimation(mEnterTranslateAnimation);
        addView(view);
    }

    public void startAnimation(){
        mLinearLayout.clearAnimation();
        mLinearLayout.setAnimation(mEnterTranslateAnimation);
        mEnterTranslateAnimation.start();
        mEnterTranslateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mCustomProgressView.startDoubleTap();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}
