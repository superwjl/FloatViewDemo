package com.tik.floatviewdemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import java.lang.Override;
import java.lang.Runnable;
import java.lang.String;

/**
 * 自定义加速球
 */
public class CustomProgressView extends View {
    /**
     * 加速球宽高
     */
    private int mWidth = 200;
    private int mHeight = 200;
    /** 圆球画笔 */
    private Paint mCirclePaint;
    /** 进度画笔 */
    private Paint mProgressPaint;
    /** 文本画笔 */
    private Paint mTextPaint;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    /** 绘制路径 */
    private Path mPath = new Path();
    /** 目标进度 */
    private int mDstProgress = 80;
    /** 进度峰值 */
    private int mMaxProgress = 100;
    /** 当前进度 */
    private int mCurrProgress = 0;
    /** 波浪绘制次数 */
    private int mCount = 20;
    /** 是否单击状态 */
    private boolean isSingleTap = false;
    /** 是否动画结束 */
    private boolean isAnimationFinished = true;
    private GestureDetector mGestureDetector;
    private MyGestureDetectorListener myGestureDetectorListener;
    private Handler mHandler = new Handler();
    /** 涨水动画 */
    private RiseRunnalbe mRiseRunnable = new RiseRunnalbe();
    /** 波浪动画 */
    private WaveRunnable mWaveRunnable = new WaveRunnable();
    public CustomProgressView(Context context) {
        super(context);
        init();
    }

    public CustomProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(Color.argb(0xff, 0x3a, 0x8c, 0x6c));

        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setColor(Color.argb(0xff, 0x4e, 0xc9, 0x63));
        mProgressPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(25);

        mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        mGestureDetector = new GestureDetector(new MyGestureDetectorListener());
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
        setClickable(true);
    }

    class MyGestureDetectorListener extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            startDoubleTap();
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            startSingleTap();
            return super.onSingleTapConfirmed(e);
        }
    }

    protected void startDoubleTap(){
        if(isAnimationFinished) {
            mCurrProgress = 0;
            isAnimationFinished = false;
            isSingleTap = false;
            mHandler.postDelayed(mRiseRunnable, 50);
        }
    }

    protected void startSingleTap(){
        if(isAnimationFinished) {
            isAnimationFinished = false;
            isSingleTap = true;
            mHandler.postDelayed(mWaveRunnable, 50);
        }
    }

    class WaveRunnable implements Runnable{

        @Override
        public void run() {
            if(--mCount >= 0){
                invalidate();
                mHandler.postDelayed(mWaveRunnable, 200);
            }else{
                mHandler.removeCallbacks(mWaveRunnable);
                mCount = 20;
                isAnimationFinished = true;
            }
        }
    }

    class RiseRunnalbe implements Runnable{

        @Override
        public void run() {
            if(++mCurrProgress <= mDstProgress){
                invalidate();
                mHandler.postDelayed(mRiseRunnable, 50);
            }else{
                mHandler.removeCallbacks(mRiseRunnable);
                mCurrProgress = mDstProgress;
                isAnimationFinished = true;
//                isSingleTap = true;
//                mHandler.postDelayed(mWaveRunnable, 50);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle();
        mPath.reset();
        drawProgressFixed();
        if(!isSingleTap){//双击
            drawProgressRise();
        }else{//单击
            drawProgressWave();
        }

        mPath.close();
        mCanvas.drawPath(mPath, mProgressPaint);
        drawProgressText();
        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    /**
     * 绘制圆形
     */
    protected void drawCircle(){
        mCanvas.drawCircle(mWidth / 2, mHeight / 2, mWidth / 2, mCirclePaint);
    }

    /**
     * 绘制左右下三边
     */
    protected void drawProgressFixed(){
        float y = (1 - (float)mCurrProgress/mMaxProgress) * mHeight;
        mPath.moveTo(mWidth, y);
        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(0, mHeight);
        mPath.lineTo(0, y);
    }

    /**
     * 进度的增长路径
     */
    protected void drawProgressRise(){
        float d1 = 10;
        float offset = (1 - (float)mCurrProgress/mDstProgress) * 10;
        for (int i = 0; i < 5; i++){
            mPath.rQuadTo(d1, -offset, 2*d1, 0);
            mPath.rQuadTo(d1, offset, 2*d1, 0);
        }
    }


    /**
     * 进度的波浪路径
     */
    protected void drawProgressWave(){
        float offset = (float)mCount/50 * 20;
        if(mCount % 2 == 0){
            for (int i = 0; i < 5; i++){
                mPath.rQuadTo(20, -offset, 40, 0);
                mPath.rQuadTo(20, offset, 40, 0);
            }
        }else{
            for (int i = 0; i < 5; i++){
                mPath.rQuadTo(20, offset, 40, 0);
                mPath.rQuadTo(20, -offset, 40, 0);
            }
        }
    }

    /**
     * 绘制进度文本
     */
    protected void drawProgressText(){
        String textProgress = (int)(((float)mCurrProgress/mMaxProgress) * 100) + "%";
        float width = mTextPaint.measureText(textProgress);
        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        float baseline = mHeight/2 - (metrics.ascent + metrics.descent)/2;
        mCanvas.drawText(textProgress, mWidth / 2 - width / 2, baseline, mTextPaint);
    }
}
