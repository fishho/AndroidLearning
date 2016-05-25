package com.cfish.androidlearning;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.widget.NumberPicker;
import android.widget.OverScroller;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by GKX100217 on 2016/5/25.
 */
public class Ruler extends View  {

    public static final int DEFAULT_LONG_LINE_HEIGHT = 38;
    public static final int DEFAULT_BOTTOM_PADDING = 8;
    public static final int DEFAULT_BG_COLOR = Color.parseColor("#FAE40B");
    public static final int DEFAULT_INDICATOR_HEIGHT = 18;
    public static final int DEFAULT_TEXT_SIZE = 17;
    public static final float DEFAULT_UINT = 10;
    public static final int DEFAULT_WIDTH_PER_UINT = 100;
    public static final int DEFAULT_LONG_LINE_STROKE_WIDTH = 2;
    public static final int DEFAULT_SHORT_LINE_STROKE_WIDTH = 1;

    private float density;
    private int width;
    private int height;
    //当前值
    private float currentValue;
    //起点值
    private float startValue;
    //终点值
    private float endValue;
    //两条长线之间的距离
    private float widthPerUnit;
    //两条短线之间的距离
    private float widthPerMicroUnit;
    //两条长线之间的数值
    private float unit;
    //短线单位
    private float microUnit;
    //短线的数量
    private int microUnitCount;
    //能够移动的最大值
    private float maxRightOffset;
    private float maxLeftOffset;

    private int bgColor;

    private float longLineHeight;
    private float shortLineHeight;

    private float bottomPadding;
    private float indicatorHeight;

    private float textSize;
    private int textColor;
    private int lineColor;

    private Paint bgPaint;
    private Paint indicatorPaint;
    private Paint linePaint;
    private Paint textPaint;
    private PaintFlagsDrawFilter pfdf;
    private float moveX;
    private VelocityTracker velocityTracker;
    private OverScroller scroller;

    //剩余偏移量
    private float offset;
    //最小速度
    private int minvelocity;

    public static final int MICRO_UNIT_ZERO = 0;
    public static final int MICRO_UNIT_ONE = 1;
    public static final int MICRO_UNIT_FIVE = 5;
    public static final int MICRO_UNIT_TEN = 10;
    private float originValue;

    @IntDef({MICRO_UNIT_ZERO, MICRO_UNIT_ONE, MICRO_UNIT_FIVE, MICRO_UNIT_TEN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MicroUnitMode {

    }
    private OnValueChangeListener listener;

    public interface OnValueChangeListener {
        void onValueChange(float value);
    }

    public Ruler(Context context) {
        this(context, null);
    }

    public Ruler(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Ruler(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        scroller = new OverScroller(context);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        density = getResources().getDisplayMetrics().density;

        longLineHeight = DEFAULT_LONG_LINE_HEIGHT * density;
        shortLineHeight = DEFAULT_LONG_LINE_HEIGHT / 2 * density;
        bottomPadding = DEFAULT_BOTTOM_PADDING * density;
        indicatorHeight = DEFAULT_INDICATOR_HEIGHT * density;
        textSize = DEFAULT_TEXT_SIZE * density;
        widthPerUnit = DEFAULT_WIDTH_PER_UINT * density;

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,R.styleable.Ruler);
        if (typedArray != null)  {
            bgColor = typedArray.getColor(R.styleable.Ruler_ruler_bg_color,DEFAULT_BG_COLOR);
            textSize = typedArray.getDimension(R.styleable.Ruler_ruler_text_size,DEFAULT_TEXT_SIZE);
            textColor = typedArray.getColor(R.styleable.Ruler_ruler_text_color,Color.WHITE);
            widthPerUnit = typedArray.getDimension(R.styleable.Ruler_ruler_width_per_uint, DEFAULT_WIDTH_PER_UINT);
            lineColor = typedArray.getColor(R.styleable.Ruler_ruler_line_color, Color.WHITE);
            typedArray.recycle();
        }

        minvelocity = ViewConfiguration.get(getContext()).getScaledMinimumFlingVelocity();
        initPaint();
    }

    /**
     * @param startValue 开始值
     * @param endValue 结束值
     * @param currentValue 保留以为小数
     * @param listener
     */
    public void initPaint(float startValue, float endValue, float currentValue, OnValueChangeListener listener) {
        init(startValue, endValue, currentValue, DEFAULT_UINT, MICRO_UNIT_ZERO, listener);
    }

    /**
     *
     * @param startValue 开始值
     * @param endValue 结束值
     * @param currentValue 保留一位小数
     * @param unit 单位
     * @param microUnitCount 小单位
     * @param listener
     */
    public void init(float startValue, float endValue, float currentValue, float unit, @MicroUnitMode int microUnitCount, OnValueChangeListener listener) {
        this.startValue = startValue;
        this.endValue = endValue;
        this.currentValue = currentValue;
        if (currentValue < startValue) {
            this.currentValue = startValue;
        }
        if (currentValue > endValue) {
            this.currentValue = endValue;
        }

        this.originValue = this.currentValue;
        this.unit = unit;
        this.microUnit = microUnitCount == MICRO_UNIT_ZERO? 0: unit/microUnitCount;
        this.microUnitCount = microUnitCount;
        this.listener = listener;
        calculate();
    }

    private void initPaint() {
        pfdf = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        bgPaint = new Paint();
        bgPaint.setColor(bgColor);

        indicatorPaint = new Paint();
        indicatorPaint.setColor(lineColor);

        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);

        linePaint = new Paint();
        linePaint.setColor(lineColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int measureMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int measureSize = View.MeasureSpec.getSize(widthMeasureSpec);
        int result = getSuggestedMinimumWidth();
        switch (measureMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = measureSize;
                break;
            default:
                break;
        }
        width = result;
        return result;
    }

    private int measureHeight(int heightMeasureSpec) {
        int measureMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int measreSize = View.MeasureSpec.getSize(heightMeasureSpec);
        int result = (int) ((bottomPadding + longLineHeight)*2);
        switch (measureMode) {
            case MeasureSpec.EXACTLY:
                result = Math.max(result, measreSize);
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(result, measreSize);
                break;
            default:
                break;
        }

        height = result;
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(pfdf);
        drawBg(canvas);
        drawIndicator(canvas);
        drawRuler(canvas);
    }

    private void drawBg(Canvas canvas) {
        canvas.drawRect(0, 0, width, height, bgPaint);
    }

    private void drawIndicator(Canvas canvas) {
        Path path = new Path();
        path.moveTo(width/2 - indicatorHeight/2, 0);
        path.lineTo(width/2, indicatorHeight/2);
        path.lineTo(width/2 + indicatorHeight/2, 0);
        canvas.drawPath(path, indicatorPaint);
    }

    private void drawRuler(Canvas canvas) {
        if (moveX < maxRightOffset) {
            moveX = maxRightOffset;
        }
        if (moveX > maxLeftOffset) {
            moveX = maxLeftOffset;
        }

        int halfCount = (int) (width/2/getBaseUnitWidth());
        float moveValue = (int) (moveX /getBaseUnitWidth())*getBaseUnit();
        currentValue = originValue - moveValue;
        //剩余偏移量
        offset = moveX - (int) (moveX /getBaseUnitWidth())*getBaseUnitWidth();

        for (int i = -halfCount -1; i <= halfCount; i++) {
            float value = ArithmeticUtil.addWithScale(currentValue,ArithmeticUtil.mulWithScale(i,getBaseUnit(),2),2);
            //只会出范围内的图形
            if (value >= startValue && value <= endValue) {
                //画长的刻度
                float startx = width/2 + offset + i*getBaseUnitWidth();
                if (startx >0 && startx < width) {
                    if (ArithmeticUtil.remainder(value, unit) == 0) {
                        drawLongLine(canvas, i, value);
                    } else {
                        //画短线
                        drawShortLine(canvas, i);
                    }
                } else {
                    //画长线
                    drawLongLine(canvas, i, value);
                }
            }
        }
        notifyValueChange();
    }

    private void notifyValueChange() {
        if (listener != null) {
            currentValue = ArithmeticUtil.round(currentValue,2);
            listener.onValueChange(currentValue);
        }
    }

    private void drawShortLine(Canvas canvas, int i) {
        linePaint.setStrokeWidth(DEFAULT_SHORT_LINE_STROKE_WIDTH*density);
        canvas.drawLine(width/2+offset+i*getBaseUnitWidth(), 0, width/2 +offset +i*getBaseUnitWidth(), 0+shortLineHeight, linePaint);
    }

    private void drawLongLine(Canvas canvas, int i, float value) {
        linePaint.setStrokeWidth(DEFAULT_SHORT_LINE_STROKE_WIDTH*density);
        //画长线
        canvas.drawLine(width/2+offset+i*getBaseUnitWidth(), 0, width/2+offset+i*getBaseUnitWidth(), 0+longLineHeight,linePaint);

        //画刻度
        canvas.drawText(String.valueOf(value),width/2+offset+i*getBaseUnitWidth()-textPaint.measureText(value+"")/2,getHeight()-bottomPadding-calcTextHeight(textPaint,value+""),textPaint);
    }

    private float getBaseUnitWidth() {
        if (microUnitCount != 0) {
            return widthPerMicroUnit;
        }
        return widthPerUnit;
    }

    private float getBaseUnit() {
        if (microUnitCount !=0) {
            return microUnit;
        }
        return unit;
    }

    private void calculate() {
        startValue = format(startValue);
        endValue = format(endValue);
        currentValue = format(currentValue);
        originValue = currentValue;
        if (unit == 0 ) {
            unit = DEFAULT_UINT;
        }
        if (microUnitCount != MICRO_UNIT_ZERO) {

            widthPerMicroUnit = ArithmeticUtil.div(widthPerUnit, microUnitCount, 2);
        }

        maxRightOffset = -1 *((endValue - originValue)* getBaseUnitWidth()/getBaseUnit());
        maxLeftOffset = ((originValue - startValue))* getBaseUnitWidth() / getBaseUnit();
        invalidate();
    }

    private float format(float value) {
        float result = value;
        if (getBaseUnit() < 0.1) {
            if (ArithmeticUtil.remainder(result, getBaseUnit()) != 0) {
                result += 0.01;
                result = format(result);
            }
        } else if (getBaseUnit() < 1) {
            if (ArithmeticUtil.remainder(result,getBaseUnit()) != 0) {
                result += 0.1;
                result = format(result);
            }
        } else if (getBaseUnit() < 10) {
            if (ArithmeticUtil.remainder(result, getBaseUnit()) != 0) {
                result += 1;
                result = format(result);
            }
        }
        return result;
    }


    private boolean isActionUp = false;
    private float mLastX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action  = event.getAction();
        float xPosition = event.getX();

        if (velocityTracker == null) {
            velocityTracker = velocityTracker.obtain();
        }
        velocityTracker.addMovement(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isActionUp = false;
                scroller.forceFinished(true);
                if (null != animator) {
                    animator.cancel();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                isActionUp = false;
                float off = xPosition - mLastX;
                if ((moveX <= maxLeftOffset) && off < 0 || (moveX >=maxLeftOffset) && off >0){

                } else {
                    moveX += off;
                    postInvalidate();
                 }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isActionUp = true;
                f = true;
                countVelocityTracker(event);
                return false;
            default:
                break;
        }
        mLastX = xPosition;
        return true;
    }


    private ValueAnimator animator;
    private boolean isCancel = false;

    private void startAnim() {
        isCancel  = false;
        float neededMoveX = ArithmeticUtil.mul(ArithmeticUtil.div(moveX, getBaseUnitWidth(),0),getBaseUnitWidth());
        animator = new ValueAnimator().ofFloat(moveX,neededMoveX);
        animator.setDuration(300);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (!isCancel) {
                    moveX = (Float) animation.getAnimatedValue();
                    postInvalidate();
                }
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                isCancel = true;
            }
        });
        animator.start();
    }
    private boolean f = true;

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            float off = scroller.getFinalX()- scroller.getCurrX();
            off = off * funcitonSpeed();
            if ((moveX < maxRightOffset) && off < 0) {
                moveX = maxRightOffset;
            } else  if ((moveX >= maxLeftOffset) && off >0) {
                moveX = maxLeftOffset;
            } else {
                moveX += off;
                if (scroller.isFinished()) {
                    startAnim();
                } else {
                    postInvalidate();
                    mLastX = scroller.getFinalX();
                }
            }
        } else {
            if (isActionUp && f) {
                startAnim();
                f = false;
            }
        }
    }

    //控制滑动速度
    private float funcitonSpeed() {
        return 0.2f;
    }

    private void countVelocityTracker(MotionEvent event) {
        velocityTracker.computeCurrentVelocity(1000, 3000);
        float xVelocity = velocityTracker.getXVelocity();
        if (Math.abs(xVelocity) > minvelocity) {
            scroller.fling(0, 0, (int)xVelocity, 0, Integer.MIN_VALUE,Integer.MAX_VALUE, 0, 0);
        }
    }

    private int calcTextHeight(Paint paint, String demoText) {
        Rect r = new Rect();
        paint.getTextBounds(demoText, 0, demoText.length(), r);
        return r.height();
    }

}
