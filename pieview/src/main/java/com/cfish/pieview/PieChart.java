package com.cfish.pieview;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by GKX100217 on 2016/6/7.
 */
public class PieChart extends View {
    private Paint mPaint = new Paint();
    private int mWidth;
    private int mHeight;
    private ArrayList<PieData> mPieData = new ArrayList<>();
    private float mStartAngle = 0;
    private RectF rectF = new RectF();
    private RectF rectFTra = new RectF();
    private RectF rectFIn = new RectF();
    private float r, rTra, rWhite;

    private RectF rectFF = new RectF();
    private RectF rectFTraF = new RectF();
    private RectF rectFWhite = new RectF();
    private float rF, rTraF, rWhiteF;

    private ValueAnimator animator;
    private float animatedValue;
    private long animatorDuration = 5000;
    private TimeInterpolator timeInterpolator = new AccelerateDecelerateInterpolator();
    private boolean animatedFlag = true;

    private boolean touchFlag = true;
    private float[] pieAngles;
    private int angleId;

    private double offsetScaleRadius = 1.1;
    private double widthScaleRadius = 0.8;
    private double radiusScaleTransparent = 0.5;
    private double radiusScaleInside = 0.43;

    private int percentTextSize = 45;
    private int centerTextSize = 60;

    private int centerTextColor = Color.BLACK;
    private int percentTextColor = Color.WHITE;
    private int percentDecimal = 0;
    private String name = "PieChart";
    private Point mPoint = new Point();
    private  float minAngle = 30;//小于此角度，只在点击时才显示文字

    private Path outPath = new Path();
    private Path midPath = new Path();
    private Path inPath = new Path();
    private Path outMidPath = new Path();
    private Path midInPath = new Path();

    private int stringId = 0;
    private boolean percentFlag = true;

    public PieChart(Context context) {
        this(context, null);
    }

    public PieChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

//    public PieChart(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        init(context, attrs, defStyleAttr, defStyleAttr);
//    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d("PieChart","onMeasure");
        int width = measureDimension(widthMeasureSpec);
        int height = measureDimension(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d("PieChart","onSizeChanged");
        mWidth = w;
        mHeight = h;

        r = (float) (Math.min(mWidth, mHeight)/2*widthScaleRadius); //饼状图半径
        rectF.left = -r;
        rectF.top = -r;
        rectF.right = r;
        rectF.bottom = r;

        //透明圆弧
        rTra = (float) (r*radiusScaleTransparent);
        rectFTra.left = -rTra;
        rectFTra.top = -rTra;
        rectFTra.right = rTra;
        rectFTra.bottom = rTra;

        //白色圆
        rWhite = (float) (r*radiusScaleInside);
        rectFIn.left = -rWhite;
        rectFIn.top = -rWhite;
        rectFIn.right = rWhite;
        rectFIn.bottom = rWhite;

        //浮出圆环
        rF = (float) (Math.min(mWidth,mHeight)/2*widthScaleRadius*offsetScaleRadius);
        rectFF.left = -rF;
        rectFF.top = -rF;
        rectFF.right = rF;
        rectFF.bottom = rF;

        //透明圆弧
        rTraF = (float) (rF*radiusScaleTransparent);
        rectFTraF.left = -rTraF;
        rectFTraF.top = -rTraF;
        rectFTraF.right = rTraF;
        rectFTraF.bottom = rTraF;

        //白色扇形
        rWhiteF = (float) (rF*radiusScaleInside);
        rectFWhite.left = -rWhiteF;
        rectFWhite.top = -rWhiteF;
        rectFWhite.right = rWhiteF;
        rectFWhite.bottom = rWhiteF;

        if (animatedFlag) {
            initAnimator(animatorDuration);
        } else {
            animatedValue = 360f;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("PieChart","onDraw");
        if (mPieData == null) {
            return;
        }
        float currentStartAngle = 0;
        canvas.translate(mWidth/2, mHeight/2); //画布原点移到中心位置

        canvas.save();
        canvas.rotate(mStartAngle);
        float drawAngle;
        for (int i = 0; i<mPieData.size(); i++){
            PieData pie = mPieData.get(i);
            if (Math.min(pie.getAngle()-1, animatedValue- currentStartAngle) >= 0) {
                drawAngle = Math.min(pie.getAngle()-1,animatedValue-currentStartAngle);
            } else {
                drawAngle = 0;
            }
            if (i== angleId) {
                drawArc(canvas,currentStartAngle,drawAngle,pie,rF,rTraF,rWhiteF,rectFF,rectFTraF,rectFWhite,mPaint);
            } else {
                drawArc(canvas,currentStartAngle,drawAngle,pie,r,rTra,rWhite,rectF,rectFTra,rectFIn,mPaint);
            }
            currentStartAngle += pie.getAngle();
        }
        canvas.restore();

        currentStartAngle = mStartAngle;
        //添加文字
        for (int i=0; i<mPieData.size(); i++) {
            PieData  pie = mPieData.get(i);
            mPaint.setColor(percentTextColor);
            mPaint.setTextSize(percentTextSize);
            mPaint.setTextAlign(Paint.Align.CENTER);
            NumberFormat numberFormat = NumberFormat.getPercentInstance();
            numberFormat.setMinimumFractionDigits(percentDecimal);

            int textPathX;
            int textPathY;

            if (animatedValue>pieAngles[i]-pie.getAngle()/2) {
                if (i == angleId) {
                    textPathX = (int) (Math.cos(Math.toRadians(currentStartAngle + (pie.getAngle() / 2))) * (rF + rTraF) / 2);
                    textPathY = (int) (Math.sin(Math.toRadians(currentStartAngle + (pie.getAngle() / 2))) * (rF + rTraF) / 2);
                    mPoint.x = textPathX;
                    mPoint.y = textPathY;
                    String[] strings = new String[]{pie.getName() + "", numberFormat.format(pie.getPercentage()) + ""};
                    if (strings.length == 2)
                        textCenter(strings, mPaint, canvas, mPoint, Paint.Align.CENTER);
                } else {
                    if (pie.getAngle() > minAngle) {
                        textPathX = (int) (Math.cos(Math.toRadians(currentStartAngle + (pie.getAngle() / 2))) * (r + rTra) / 2);
                        textPathY = (int) (Math.sin(Math.toRadians(currentStartAngle + (pie.getAngle() / 2))) * (r + rTra) / 2);
                        mPoint.x = textPathX;
                        mPoint.y = textPathY;
                        String[] strings = new String[]{numberFormat.format(pie.getPercentage()) + ""};
                        if (strings.length == 1)
                            textCenter(strings, mPaint, canvas, mPoint, Paint.Align.CENTER);
                    }
                }
                currentStartAngle += pie.getAngle();
            }
        }
        mPaint.setColor(centerTextColor);
        mPaint.setTextSize(centerTextSize);
        mPaint.setTextAlign(Paint.Align.CENTER);

        mPoint.x = 0;
        mPoint.y = 0;

        String[] strings = new String[]{name+""};
        if (strings.length == 1) {
            textCenter(strings, mPaint, canvas, mPoint, Paint.Align.CENTER);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (touchFlag && mPieData.size()>0){
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    float x = event.getX()-(mWidth/2);
                    float y = event.getY()-(mHeight/2);
                    float touchAngle = 0;
                    if (x<0&&y<0) {
                        touchAngle += 180;
                    } else if (y<0 && x>0) {
                        touchAngle += 360;
                    } else if (y>0 && x<0) {
                        touchAngle += 180;
                    }
                    touchAngle += Math.toDegrees(Math.atan(y/x));
                    touchAngle = touchAngle- mStartAngle;
                    if (touchAngle <0 ){
                        touchAngle = touchAngle +360;
                    }
                    float touchRadius = (float) Math.sqrt(y*y+x*x);
                    if (rTra < touchRadius && touchRadius > r) {
                        angleId = -Arrays.binarySearch(pieAngles,(touchAngle)) -1;
                        invalidate();
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    angleId = -1;
                    invalidate();
                    return true;
            }
        }
        return super.onTouchEvent(event);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.PieChart, defStyleAttr,defStyleRes);
        int n = array.getIndexCount();
        for (int i = 0; i<n; i++) {
            switch (i) {
                case R.styleable.PieChart_name:
                    name = array.getString(i);
                    break;
                case R.styleable.PieChart_percentDecimal:
                    percentDecimal = array.getInt(i, percentDecimal);
                    break;
                case R.styleable.PieChart_textSize:
                    percentTextSize = array.getDimensionPixelSize(i, percentTextSize);
                    break;
            }
        }
        array.recycle();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    private void initDate(ArrayList<PieData> mPieData) {
        float dataMax = 0;
        if (mPieData == null || mPieData.size() ==0) {
            Log.d("PieChart","data empty");
            return;
        }
        pieAngles = new float[mPieData.size()];
        float sumValue = 0;
        for (int i = 0; i <mPieData.size(); i++) {
            PieData pie = mPieData.get(i);
            sumValue += pie.getValue();
            Log.d("PieChart",sumValue+"");
        }

        float sumAngle = 0;
        for (int i =0; i<mPieData.size(); i++) {
            PieData pie = mPieData.get(i);
            float percentage = pie.getValue()/sumValue;
            float angle = percentage*360;
            pie.setPercentage(percentage);
            pie.setAngle(angle);
            sumAngle += angle;
            pieAngles[i] = sumAngle;

            NumberFormat numberFormat = NumberFormat.getPercentInstance();
            numberFormat.setMinimumFractionDigits(percentDecimal);
            if (dataMax < textWidth(numberFormat.format(pie.getPercentage()), percentTextSize,mPaint)) {
                stringId = i;
            }
        }
        angleId = -1;
    }

    private void initAnimator(long duration) {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
            animator.start();
        }else {
            animator = ValueAnimator.ofFloat(0, 360).setDuration(duration);
            animator.setInterpolator(timeInterpolator);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(){
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    animatedValue = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            animator.start();
        }
    }
    private int measureDimension(int measureSpec) {
        int size = measureWrap(mPaint);

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                size = measureWrap(mPaint);
                break;
            case MeasureSpec.EXACTLY:
                size = specSize;
                break;
            case MeasureSpec.AT_MOST:
                size = Math.min(specSize, measureWrap(mPaint));
                break;
            default:
                size = measureWrap(mPaint);
                break;
        }
        return size;
    }

    private int measureWrap(Paint paint) {
        float wrapSize;
        if (mPieData != null && mPieData.size() > 1) {
            NumberFormat numberFormat = NumberFormat.getPercentInstance();
            numberFormat.setMinimumFractionDigits(percentDecimal);
            paint.setTextSize(percentTextSize);
            float percentWidth = paint.measureText(numberFormat.format(mPieData.get(stringId).getPercentage())+ "");
            paint.setTextSize(centerTextSize);
            float nameWidth = paint.measureText(name +"");
            wrapSize = (percentWidth*4 + nameWidth*2)*(float)offsetScaleRadius;
            Log.d("PieChart",percentWidth+":"+nameWidth+":"+wrapSize);
        }else {
            wrapSize = 0;
        }
        return (int) wrapSize;
    }

    private void drawArc(Canvas canvas, float currentStartAngle, float drawAngle, PieData pie,
                         float outR, float midR, float inR, RectF outRectF, RectF midRectF, RectF inRectF,Paint paint){
        outPath.lineTo(outR*(float) Math.cos(Math.toRadians(currentStartAngle)),outR*(float) Math.sin(Math.toRadians(currentStartAngle)));
        outPath.arcTo(outRectF,currentStartAngle,drawAngle);
        midPath.lineTo(midR*(float) Math.cos(Math.toRadians(currentStartAngle)),midR*(float) Math.sin(Math.toRadians(currentStartAngle)));
        midPath.arcTo(midRectF,currentStartAngle,drawAngle);
        inPath.lineTo(inR*(float) Math.cos(Math.toRadians(currentStartAngle)),inR*(float) Math.sin(Math.toRadians(currentStartAngle)));
        inPath.arcTo(inRectF,currentStartAngle,drawAngle);
        outMidPath.op(outPath,midPath, Path.Op.DIFFERENCE);
        midInPath.op(midPath,inPath, Path.Op.DIFFERENCE);
        paint.setColor(pie.getColor());
        canvas.drawPath(outMidPath,paint);
        paint.setAlpha(0x80);//设置透明度
        canvas.drawPath(midInPath,paint);
        outPath.reset();
        midPath.reset();
        inPath.reset();
        outMidPath.reset();
        midInPath.reset();
    }

    private void drawText(Canvas canvas, PieData pie, float currentStartAngle, NumberFormat numberFormat,
                          boolean flag) {
        int textPathX = (int) (Math.cos(Math.toRadians(currentStartAngle + (pie.getAngle()/2)))*(r+rTra)/2);
        int textPathY = (int) (Math.sin(Math.toRadians(currentStartAngle + (pie.getAngle()/2)))*(r+rTra)/2);
        mPoint.x = textPathX;
        mPoint.y = textPathY;
        String[] strings;
        if (flag) {
            strings = new String[] {pie.getName()+"", numberFormat.format(pie.getPercentage())+""};

        }else {
            strings = new String[] {numberFormat.format(pie.getPercentage()) +""};
        }
        textCenter(strings, mPaint, canvas, mPoint, Paint.Align.CENTER);
    }

    private void textCenter(String[] strings, Paint paint, Canvas canvas, Point point,Paint.Align align) {
        paint.setTextAlign(align);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();

        float top = fontMetrics.top;
        float bottom = fontMetrics.bottom;
        int length = strings.length;
        float total = (length-1)*(-top+bottom)+(-fontMetrics.ascent+fontMetrics.descent);
        float offset = total/2-bottom;
        for (int i = 0; i < length; i++) {
            float yAxis = -(length - i -1)*(-top +bottom) + offset;
            canvas.drawText(strings[i], point.x, point.y+ yAxis, paint);
        }
    }

    private float textWidth(String string, int size, Paint paint) {
        paint.setTextSize(size);
        return paint.measureText(string+"");
    }

    /**
     * 设置初始角度
     * @param mStartAngle 初始角度
     */
    public void setStartAngle(float mStartAngle) {
        while (mStartAngle < 0 ){
            mStartAngle = mStartAngle+360;
        }
        while (mStartAngle >360){
            mStartAngle = mStartAngle-360;
        }
        this.mStartAngle = mStartAngle;
    }

    /**
     * 设置数据
     * @param mPieData 数据
     */
    public void setPieData(ArrayList<PieData> mPieData) {
        if (mPieData == null) {
            return;
        }
        this.mPieData = mPieData;
        Log.d("PieChart","setPieData");
        initDate(mPieData);
    }

    /**
     * 是否显示点触效果
     * @param touchFlag
     */
    public void setTouchFlag(boolean touchFlag) {
        this.touchFlag = touchFlag;
    }

    /**
     * 设置绘制圆环的动画时间
     * @param animatorDuration
     */
    public void setAnimatorDuration(long animatorDuration) {
        this.animatorDuration = animatorDuration;
    }


    /**
     * 设置偏移扇形与原扇形的半径比例
     * @param offsetScaleRadius
     */
    public void setOffsetScaleRadius(double offsetScaleRadius) {
        this.offsetScaleRadius = offsetScaleRadius;
    }

    /**
     * 业主圆环外层圆的半径与视图的宽度比
     * @param widthScaleRadius
     */
    public void setmWidthScaleRadius(double widthScaleRadius) {
        this.widthScaleRadius = widthScaleRadius;
    }

    /**
     * 设置透明圆环的半径与外圆环的半径比
     * @param radiusScaleTransparent
     */
    public void setRadiusScaleTransparent(double radiusScaleTransparent) {
        this.radiusScaleTransparent = radiusScaleTransparent;
    }


    /**
     * 设置内部圆与外部园的半径比
     * @param radiusScaleInside
     */
    public void setRadiusScaleInside(double radiusScaleInside) {
        this.radiusScaleInside = radiusScaleInside;
    }

    public void setPercentTextSize(int percentTextSize) {
        this.percentTextSize = percentTextSize;
    }

    public void setPercentTextColor(int percentTextColor) {
        this.percentTextColor = percentTextColor;
    }


    /**
     * 设置中心文字画笔大小
     * @param centerTextSize 中心文字画笔大小
     */
    public void setCenterTextSize(int centerTextSize) {
        this.centerTextSize = centerTextSize;
    }

    /**
     * 设置中心文字颜色
     * @param centerTextColor 中心文字颜色
     */
    public void setCenterTextColor(int centerTextColor) {
        this.centerTextColor = centerTextColor;
    }


    /**
     * 设置动画类型
     * @param timeInterpolator 动画类型
     */
    public void setTimeInterpolator(TimeInterpolator timeInterpolator) {
        this.timeInterpolator = timeInterpolator;
    }


    /**
     * 设置百分比小数位
     * @param percentDecimal
     */
    public void setPercentDecimal(int percentDecimal) {
        this.percentDecimal = percentDecimal;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMinAngle(float minAngle) {
        this.minAngle = minAngle;
    }

    /**
     * 是否开启动画
     * @param animatedFlag 默认为true,开启
     */
    public void setAnimatedFlag(boolean animatedFlag) {
        this.animatedFlag = animatedFlag;
    }

    /**
     * 设置旋转角度
     * @param animatedValue 默认由动画控制
     */
    public void setAnimatedValue (float animatedValue) {
        this.animatedValue = animatedValue;
    }
}
