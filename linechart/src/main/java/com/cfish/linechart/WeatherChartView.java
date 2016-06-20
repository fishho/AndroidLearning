package com.cfish.linechart;

        import android.content.Context;
        import android.content.res.TypedArray;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.DashPathEffect;
        import android.graphics.Paint;
        import android.graphics.Path;
        import android.util.AttributeSet;
        import android.view.View;

/**
 * Created by GKX100217 on 2016/6/20.
 */
public class WeatherChartView extends View {

    //x轴集合
    private float mAxis[] = new float[6];
    //白天y轴集合
    private float mYAxisDay[] = new float[6];
    //夜间y轴集合
    private float mYaxisNight[] = new float[6];
    //x,y轴集合数
    private static final int LENGTH = 6;
    //白天温度集合
    private int mTempDay[] = new int[6];
    //夜间温度集合
    private int mTempNight[] = new int[6];

    //控件高
    private  int mHeight;
    //字体大小
    private float mTextSize;
    //圆点半径
    private float mRadius;
    //今天的圆点半径
    private float mRadiusToday;
    //文字移动位置距离
    private float mTextSpace;
    //白天折线颜色
    private int mColorDay;
    //夜间折线颜色
    private int mColorNight;
    //屏幕密度
    private float mDensity;
    //控件边的空白空间
    private float mSpace;
    //线画笔
    private Paint mLinePaint;
    //点画笔
    private Paint mPointPaint;
    //文字画笔
    private Paint mTextPaint;


    public WeatherChartView(Context context) {
        this(context,null);
    }
    public WeatherChartView(Context context, AttributeSet attrs) {
        super(context,attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.WeatherChartView);
        float densityText = getResources().getDisplayMetrics().scaledDensity;
        mTextSize = array.getDimensionPixelSize(R.styleable.WeatherChartView_textSize,(int)(14*densityText));
        mColorDay = array.getColor(R.styleable.WeatherChartView_dayColor,getResources().getColor(R.color.colorAccent));
        mColorNight = array.getColor(R.styleable.WeatherChartView_nightColor,getResources().getColor(R.color.colorPrimary));
        int textColor = array.getColor(R.styleable.WeatherChartView_textColor, Color.WHITE);
        array.recycle();

        mDensity = getResources().getDisplayMetrics().density;
        mRadius = 3*mDensity;
        mRadiusToday = 5*mDensity;
        mSpace =  3*mDensity;
        mTextSpace = 10*mDensity;

        float strokeWidth = 2*mDensity;
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(strokeWidth);
        mLinePaint.setStyle(Paint.Style.STROKE);

        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mHeight == 0) {
            setHeightAndAxis();//设置控件高度，X轴集合
        }

        computeYAxisValues();
        drawChart(canvas, mColorDay, mTempDay, mYAxisDay, 0);
        drawChart(canvas, mColorNight, mTempNight, mYaxisNight,1);
    }

    private void computeYAxisValues() {
        int minTempDay = mTempDay[0];
        int maxTempDay = mTempDay[0];
        for (int item : mTempDay) {
            if (item <minTempDay) {
                minTempDay = item;
            }
            if (item >maxTempDay) {
                maxTempDay = item;
            }
        }

        int minTempNight = mTempNight[0];
        int maxTempNight = mTempNight[0];
        for (int item : mTempNight) {
            if (item < minTempNight) {
                minTempNight = item;
            }
            if (item > maxTempNight) {
                maxTempNight = item;
            }
        }

        //一天中的最低气温
        int minTemp  = minTempNight < minTempDay? minTempNight : minTempDay;
        //一天中的最高气温
        int maxTemp  = maxTempNight > maxTempDay? maxTempNight : maxTempDay;

        //温差
        float parts = maxTemp - minTemp;
        float length = mSpace + mTextSize + mTextSpace + mRadius;
        float yAxisHeight = mHeight - length*2;

        if (parts == 0) {
            for (int i = 0; i< LENGTH; i++) {
                mYAxisDay[i] = yAxisHeight/2 +length;
                mYaxisNight[i] = yAxisHeight/2 + length;
            }
        } else {
            float partValue = yAxisHeight / parts;
            for (int i = 0; i< LENGTH; i++) {
                mYAxisDay[i] = mHeight -partValue*(mTempDay[i] - minTemp) -length;
                mYaxisNight[i] = mHeight - partValue*(mTempNight[i] - minTemp) - length;
            }
        }
    }

    private void drawChart(Canvas canvas, int color, int temp[], float[] yAsix, int type) {
        mLinePaint.setColor(color);
        mPointPaint.setColor(color);

        int alpha1 = 102;
        int alpha2 = 255;
        for (int i = 0; i < LENGTH; i++) {
            if (i < LENGTH -1) {
                if (i == 0) {
                    mLinePaint.setAlpha(alpha1);
                    mLinePaint.setPathEffect(new DashPathEffect(new float[]{2*mDensity, 2*mDensity},0));

                    Path path  = new Path();
                    path.moveTo(mAxis[i],yAsix[i]);
                    path.lineTo(mAxis[i+1],yAsix[i+1]);
                    canvas.drawPath(path, mLinePaint);
                } else {
                    mLinePaint.setAlpha(alpha2);
                    mLinePaint.setPathEffect(null);
                    canvas.drawLine(mAxis[i],yAsix[i],mAxis[i+1],yAsix[i+1], mLinePaint);
                }
            }
            if (i != 1) {
                if (i == 0) {
                    mPointPaint.setAlpha(alpha1);
                    canvas.drawCircle(mAxis[i],yAsix[i],mRadius,mPointPaint);
                } else {
                    mPointPaint.setAlpha(alpha2);
                    canvas.drawCircle(mAxis[i], yAsix[i], mRadius, mPointPaint);
                }
            } else {
                mPointPaint.setAlpha(alpha2);
                canvas.drawCircle(mAxis[i], yAsix[i], mRadiusToday, mPointPaint);
            }

            if (i == 0) {
                mTextPaint.setAlpha(alpha1);
                drawText(canvas, mTextPaint, i, temp, yAsix, type);
            } else {
                mTextPaint.setAlpha(alpha2);
                drawText(canvas, mTextPaint, i, temp, yAsix, type);
            }
        }
    }


    private  void drawText(Canvas canvas, Paint textPaint, int i, int[] temp, float[] yAxis, int type) {
        switch (type) {
            case 0:
                canvas.drawText(temp[i] + "°",mAxis[i],yAxis[i] - mRadius - mTextSpace, textPaint);
                break;
            case 1:
                canvas.drawText(temp[i] + "°", mAxis[i], yAxis[i] + mTextSpace + mTextSize, textPaint);
                break;
        }
    }


    /**
     * 设置高度，X轴集合
     */
    public void setHeightAndAxis() {
        mHeight = getHeight();
        int width = getWidth();
        float w = width/12;
        mAxis[0] = w;
        mAxis[1] = w*3;
        mAxis[2] = w*5;
        mAxis[3] = w*7;
        mAxis[4] = w*9;
        mAxis[5] = w*11;
    }

    public void setTempDay(int[] tempDay) {
        mTempDay = tempDay;
    }

    public void setTempNight(int[] tempNight) {
        mTempNight = tempNight;
    }
}
