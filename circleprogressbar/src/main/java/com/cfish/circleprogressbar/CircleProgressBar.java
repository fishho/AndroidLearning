package com.cfish.circleprogressbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by GKX100217 on 2016/6/3.
 */
public class CircleProgressBar extends View {
    /**
     * 默认宽度
     */
    private static final int DEFAULTWIDTH = 100;
    /**
     * 默认高度
     */
    private static final int DEFAULTHEIGHT = 100;
    //外层圆圈的线条宽度
    private int stroke = 7;
    //外层圆圈的线条颜色
    private int circleColor = Color.BLACK;
    //内外圆圈之前的间距
    private int padding = 20;
    //内层实心圆的颜色
    private int sweepColor = Color.RED;
    //开始绘制的角度
    private int startAngle = -90;
    //已经绘制的角度
    private int sweepAngle = 0;
    //每次增长的度数
    private int sweepStep = 1;

    private Paint paint;

    private RectF rectF;

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint =  new Paint();
        paint.setAntiAlias(true);

        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressBar);
        circleColor = t.getColor(R.styleable.CircleProgressBar_circleColor, this.circleColor);
        sweepColor = t.getColor(R.styleable.CircleProgressBar_sweepColor, this.sweepColor);
        startAngle = t.getInt(R.styleable.CircleProgressBar_startAngle, this.startAngle);
        sweepStep = t.getInt(R.styleable.CircleProgressBar_sweepStep, this.sweepStep);
        stroke = (int) t.getDimension(R.styleable.CircleProgressBar_stroke,stroke);
        padding = (int) t.getDimension(R.styleable.CircleProgressBar_my_padding, padding);
        t.recycle();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paint.setColor(circleColor);
        paint.setStrokeWidth(stroke);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(getWidth()/2, getHeight()/2, (float) (getWidth()/2 - Math.ceil(stroke/2.0)), paint);

        paint.setColor(sweepColor);
        paint.setStyle(Paint.Style.FILL);
        //把扇形画在矩形中
        rectF = new RectF(padding, padding, getWidth()- padding, getHeight() - padding);
        canvas.drawArc(rectF, startAngle, sweepAngle, true, paint);
        sweepAngle += sweepStep;
        sweepAngle = sweepAngle > 360 ? 0: sweepAngle;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                widthSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULTWIDTH, getResources().getDisplayMetrics());
                break;
        }

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                heightSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULTHEIGHT, getResources().getDisplayMetrics());
                break;
        }
        widthSize = heightSize = Math.min(widthSize, heightSize);
        setMeasuredDimension(widthSize, heightSize);
    }
}
