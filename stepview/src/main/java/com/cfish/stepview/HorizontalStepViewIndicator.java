package com.cfish.stepview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GKX100217 on 2016/6/29.
 */
public class HorizontalStepViewIndicator extends View {

    private int defaultStepIndicatorNum = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
    private float mCompletedLineHeight;
    private float mCircleRadius;

    private Drawable mCompleteIcon; //完成的默认图标
    private Drawable mAttentionIcon; //正在进行的默认图标
    private Drawable mDefaultIcon; //默认的背景图标

    private float mCenterY;
    private float mLeftY; //左上方的Y位置
    private float mRightY; //右下方的Y位置

    private int mStepNum = 0; //当前有几步流程
    private float mLinePadding; //两条连线之间的距离

    private List<Float> mCircleCenterPositions; //所有圆的圆心点位置的集合
    private Paint mUncompletedPaint; //未完成Paint
    private Paint mCompletedPaint; //完成Paint
    private int mUnCompletedLineColor = ContextCompat.getColor(getContext(),R.color.uncompleted_color);
    private int mCompletedLineColor = Color.WHITE;
    private PathEffect mEffects;

    private int mCompletingPosition; //正在进行的position
    private Path mPath;

    private OnDrawIndicatorListener mOnDrawListener;

    public void setOnDrawListener(OnDrawIndicatorListener onDrawListener) {
        mOnDrawListener = onDrawListener;
    }

    public float getCircleRadius() {
        return mCircleRadius;
    }

    public HorizontalStepViewIndicator(Context context) {
        this(context, null);
    }

    public HorizontalStepViewIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalStepViewIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPath = new Path();
        mEffects = new DashPathEffect(new float[]{8, 8, 8, 8}, 1);

        mCircleCenterPositions = new ArrayList<>();

        mUncompletedPaint = new Paint();
        mCompletedPaint = new Paint();
        mUncompletedPaint.setAntiAlias(true);
        mUncompletedPaint.setColor(mUnCompletedLineColor);
        mUncompletedPaint.setStyle(Paint.Style.STROKE);
        mUncompletedPaint.setStrokeWidth(2);

        mCompletedPaint.setAntiAlias(true);
        mCompletedPaint.setColor(mCompletedLineColor);
        mCompletedPaint.setStyle(Paint.Style.STROKE);//mark
        mCompletedPaint.setStrokeWidth(2);

        mUncompletedPaint.setPathEffect(mEffects);
        mCompletedPaint.setStyle(Paint.Style.FILL); //mark

        //已经完成线的宽高
        mCompletedLineHeight = 0.05f * defaultStepIndicatorNum; //mark what is default..
        //圆的半径
        mCircleRadius = 0.28f * defaultStepIndicatorNum;
        //线与线之间的距离
        mLinePadding = 0.85f * defaultStepIndicatorNum;

        //是否可以自己用画笔画45度，60度
        mCompleteIcon = ContextCompat.getDrawable(getContext(), R.drawable.complted);
        mAttentionIcon = ContextCompat.getDrawable(getContext(), R.drawable.attention);
        mDefaultIcon = ContextCompat.getDrawable(getContext(),R.drawable.default_icon);

    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = defaultStepIndicatorNum * 2;
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }
        int height = defaultStepIndicatorNum;
        if (MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec)) {
            height = Math.min(height, MeasureSpec.getSize(heightMeasureSpec));
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //为了该view绘制的线和圆在该View垂直居中
        mCenterY = 0.5f * getHeight();
        mLeftY = mCenterY - mCompletedLineHeight / 2;
        mRightY = mCenterY + mCompletedLineHeight /2;

        for (int i = 0; i< mStepNum; i++) {
            float paddingLeft = (getWidth() - mStepNum* mCircleRadius*2 - (mStepNum - 1)*mLinePadding) / 2;
            mCircleCenterPositions.add(paddingLeft+mCircleRadius+ i*mCircleRadius *2+i*mLinePadding);

        }
        if (mOnDrawListener != null) {
            mOnDrawListener.ondrawIndicator();
        }
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mOnDrawListener != null) {
            mOnDrawListener.ondrawIndicator();
        }
        mUncompletedPaint.setColor(mUnCompletedLineColor);
        mCompletedPaint.setColor(mCompletedLineColor);

        //draw line
        for (int i = 0; i < mCircleCenterPositions.size() -1; i++) {
            final float preCompletedXPosition = mCircleCenterPositions.get(i);
            final float afterCompletedXPosition = mCircleCenterPositions.get(i+1);

            if (i < mCompletingPosition) {
                canvas.drawRect(preCompletedXPosition + mCircleRadius- 10, mLeftY, afterCompletedXPosition -mCircleRadius +10,
                        mRightY, mCompletedPaint);
            } else {
                mPath.moveTo(preCompletedXPosition+mCircleRadius, mCenterY);
                mPath.lineTo(afterCompletedXPosition-mCircleRadius, mCenterY);
                canvas.drawPath(mPath, mUncompletedPaint);
            }
        }

        //draw icon
        for (int i = 0; i < mCircleCenterPositions.size(); i++) {
            final float currentCompletedXPosition = mCircleCenterPositions.get(i);
            Rect rect = new Rect((int)(currentCompletedXPosition-mCircleRadius),(int)(mCenterY- mCircleRadius),
                    (int)(currentCompletedXPosition+mCircleRadius),(int)(mCenterY+mCircleRadius));
            if (i < mCompletingPosition) {
                mCompleteIcon.setBounds(rect);
                mCompleteIcon.draw(canvas);
            } else if (i == mCompletingPosition && mCircleCenterPositions.size() !=1) {
                mCompletedPaint.setColor(Color.WHITE);
                canvas.drawCircle(currentCompletedXPosition, mCenterY, mCircleRadius*1.1f,mCompletedPaint);
                mAttentionIcon.setBounds(rect);
                mAttentionIcon.draw(canvas);
            } else {
               mDefaultIcon.setBounds(rect);
                mDefaultIcon.draw(canvas);
            }
        }
    }

    public List<Float> getCircleCenterPositions() {
        return mCircleCenterPositions;
    }

    public void setStepNum(int stepNum) {
        this.mStepNum = stepNum;
        invalidate();
    }

    public void setCompletingPosition(int completingPosition) {
        this.mCompletingPosition = completingPosition;
        invalidate();
    }

    public void setUnCompletedLineColor(int unCompletedLineColor) {
        this.mUnCompletedLineColor = unCompletedLineColor;
    }

    public void setCompletedLineColor(int completedLineColor) {
        this.mCompletedLineColor = completedLineColor;
    }

    public void setDefaultIcon(Drawable defaultIcon) {
        this.mDefaultIcon = defaultIcon;
    }

    public void setCompleteIcon(Drawable completedIcon) {
        this.mCompleteIcon = completedIcon;
    }

    public void setAttentionIcon(Drawable attentionIcon) {
        this.mAttentionIcon = attentionIcon;
    }

    public interface  OnDrawIndicatorListener {
        void ondrawIndicator();
    }
}
