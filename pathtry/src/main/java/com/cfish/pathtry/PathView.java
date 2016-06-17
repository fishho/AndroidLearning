package com.cfish.pathtry;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;

/**
 * Created by GKX100217 on 2016/6/17.
 */
public class PathView extends View {
    public static final String TAG = PathView.class.getSimpleName();
    private Paint mPaint = new Paint();
    private Path mPath = new Path();
    private int mWidth;
    private int mHeight;

    public PathView(Context context) {
        super(context);
    }

    /**
     * 初始化画笔，设置画笔类型，抗锯齿
     */
    private void initPaint() {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG,"onDraw");
        initPaint();
        drawAxis(canvas);

        drawArc(canvas);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG,"onSizeChanged"+w+h);
        mWidth = w;
        mHeight = h;
    }

    /**
     * @param canvas
     */
    private void drawAxis(Canvas canvas) {
        Log.d(TAG,"drawAxis");
        canvas.translate(mWidth/2, mHeight/2);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(10);
        //画原点
        canvas.drawPoint(0, 0, mPaint);
        //坐标轴四个端点
        canvas.drawPoints(new float[]{mWidth/2*0.8f,0,0,mHeight/2*0.8f,
                -mWidth/2*0.8f,0,0,-mHeight/2*0.8f},mPaint);
        //增加轴线与箭头
        mPaint.setStrokeWidth(1);
        mPath.moveTo(-mWidth/2*0.8f,0);//移动path起点到(-mWidth/2*0.8f,0)
        mPath.lineTo(mWidth/2*0.8f,0);//直线终点为(mWidth/2*0.8f,0)
        //y轴线
        mPath.moveTo(0,-mHeight/2*0.8f);//移动path起点到(0,-mHeight/2*0.8f)
        mPath.lineTo(0,mHeight/2*0.8f);//直线终点为(0,mHeight/2*0.8f)
        //x箭头
        mPath.moveTo(mWidth/2*0.8f*0.95f,-mWidth/2*0.8f*0.05f);
        mPath.lineTo(mWidth/2*0.8f,0);
        mPath.lineTo(mWidth/2*0.8f*0.95f,mWidth/2*0.8f*0.05f);
        //y箭头
        mPath.moveTo(mWidth/2*0.8f*0.05f,mHeight/2*0.8f-mWidth/2*0.8f*0.05f);
        mPath.lineTo(0,mHeight/2*0.8f);
        mPath.lineTo(-mWidth/2*0.8f*0.05f,mHeight/2*0.8f-mWidth/2*0.8f*0.05f);
        //文字
        mPaint.setTextSize(20);
        canvas.drawText("X",mWidth/2*0.8f*0.95f,mWidth/2*0.8f*0.1f,mPaint);
        canvas.drawText("Y",mWidth/2*0.8f*0.1f,mHeight/2*0.8f*0.95f,mPaint);
        Log.d(TAG,"drawPath");
        canvas.drawPath(mPath, mPaint);
    }

    private void drawArc(Canvas canvas) {
        // addarc 左上
        float r = Math.min(mWidth,mHeight)*0.6f/2;
        RectF mRectF = new RectF(-r,-r,0,0);
        mPath.addArc(mRectF,-60,180);//从-60度起，扫过180度
        canvas.drawPath(mPath, mPaint);

        // arcto 左下
        mPath.moveTo(0,0);
        RectF mRectFd = new RectF(-r,0,0,r);
        mPath.arcTo(mRectFd,-60,180);//从-60度起，扫过180度
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(2);
        canvas.drawPath(mPath, mPaint);
    }
}
