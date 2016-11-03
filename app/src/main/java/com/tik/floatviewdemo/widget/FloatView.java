package com.tik.floatviewdemo.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.tik.floatviewdemo.R;

import java.lang.Override;
import java.lang.String;

/**
 * 悬浮球
 */
public class FloatView extends View{

    private int mWidth = 100;
    private int mHeight = 100;
    private Paint mCirclePaint;
    private Paint mTextPaint;
    private String mText = "50%";
    private boolean isDragging = false;
    private Bitmap mBmpBg;

    public FloatView(Context context) {
        super(context);
        init();
    }

    public FloatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FloatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mCirclePaint = new Paint();
//        mCirclePaint.setColor(Color.parseColor("#aa888888"));
        mCirclePaint.setColor(Color.argb(0xaa, 0x88, 0x88, 0x88));
        mCirclePaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setTextSize(25);
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);

        Bitmap src = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        mBmpBg = Bitmap.createScaledBitmap(src, mWidth, mHeight, true);
    }

    public void setIsDragging(boolean isDragging){
        this.isDragging = isDragging;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isDragging){
            canvas.drawBitmap(mBmpBg, 0, 0, null);
        }else{
            canvas.drawCircle(mWidth/2, mHeight/2, mWidth/2, mCirclePaint);
            float textWidth = mTextPaint.measureText(mText);
            float x = mWidth/2 - textWidth/2;
            Paint.FontMetrics fm = mTextPaint.getFontMetrics();
            float dy = -(fm.ascent + fm.descent)/2;
            float y = mHeight/2 + dy;
            canvas.drawText(mText, x, y, mTextPaint);
        }
    }

}
