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
public class VerticalStepViewIndicator extends View {

    private int defaultStepIndicatorNum = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getResources().getDisplayMetrics());
    private float mCompletedLineHeight;
    private float mCircleRadius;

    private Drawable mCompleteIcon;//完成的默认图片    definition default completed icon
    private Drawable mAttentionIcon;//正在进行的默认图片     definition default underway icon
    private Drawable mDefaultIcon;//默认的背景图  definition default unCompleted icon
    private float mCenterX;//该View的X轴的中间位置
    private float mLeftY;
    private float mRightY;

    private int mStepNum = 0;//当前有几步流程
    private float mLinePadding; //两条连线之间的间距

    private List<Float> mCircleCenterPositions; //所有圆的圆心点位置集合
    private Paint mUnCompletedPaint; //未完成paint;
    private Paint mCompletedPaint; //已完成paint;
    private int mUncompletedLineColor = ContextCompat.getColor(getContext(), R.color.uncompleted_color);
    private int mCompletedLineColor = Color.WHITE;
    private PathEffect mEffects;

    private int mCompletingPostion; //正在进行的position
    private Path mPath;

    private OnDrawIndicatorListener mOnDrawListener;
    private Rect mRect;
    private int mHeight;
    private boolean mIsReverseDraw;

    public void setOnDrawListener(OnDrawIndicatorListener ondrawListener) {
        mOnDrawListener = ondrawListener;
    }

    public float getCircleRadius() {
        return mCircleRadius;
    }

    public VerticalStepViewIndicator(Context context) {
        this(context, null);
    }

    public VerticalStepViewIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalStepViewIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPath = new Path();
        mEffects = new DashPathEffect(new float[]{8, 8, 8, 8}, 1);

        mCircleCenterPositions = new ArrayList<>();

        mUnCompletedPaint = new Paint();
        mCompletedPaint = new Paint();
        mUnCompletedPaint.setAntiAlias(true);
        mUnCompletedPaint.setColor(mUncompletedLineColor);
        mUnCompletedPaint.setStyle(Paint.Style.STROKE);
        mUnCompletedPaint.setStrokeWidth(2);

        mCompletedPaint.setAntiAlias(true);
        mCompletedPaint.setColor(mCompletedLineColor);
        mCompletedPaint.setStyle(Paint.Style.STROKE);
        mCompletedPaint.setStrokeWidth(2);

        mUnCompletedPaint.setPathEffect(mEffects);
        mCompletedPaint.setStyle(Paint.Style.FILL);

        //已经完成线的宽高 set mCompletedLineHeight
        mCompletedLineHeight = 0.05f * defaultStepIndicatorNum;
        //圆的半径  set mCircleRadius
        mCircleRadius = 0.28f * defaultStepIndicatorNum;
        //线与线之间的间距    set mLinePadding
        mLinePadding = 0.85f * defaultStepIndicatorNum;
        mCompleteIcon = ContextCompat.getDrawable(getContext(), R.drawable.complted);//已经完成的icon
        mAttentionIcon = ContextCompat.getDrawable(getContext(), R.drawable.attention);//正在进行的icon
        mDefaultIcon = ContextCompat.getDrawable(getContext(), R.drawable.default_icon);//未完成的icon

        mIsReverseDraw = true;//default draw
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = defaultStepIndicatorNum;
        mHeight = 0 ;
        if (mStepNum > 0) {
            mHeight = (int) (getPaddingTop() + getPaddingBottom() + mCircleRadius * 2 * mStepNum + (mStepNum - 1) * mLinePadding);
        }
        if(MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(widthMeasureSpec))
        {
            width = Math.min(width, MeasureSpec.getSize(widthMeasureSpec));
        }
        if(MeasureSpec.UNSPECIFIED != MeasureSpec.getMode(heightMeasureSpec))
        {
            mHeight = MeasureSpec.getSize(heightMeasureSpec);
        }
        setMeasuredDimension(width, mHeight);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = getWidth() / 2;
        mLeftY = mCenterX - (mCompletedLineHeight / 2);
        mRightY = mCenterX + (mCompletedLineHeight / 2);

        for(int i = 0; i < mStepNum; i++)
        {
            //reverse draw VerticalStepViewIndicator
            if(mIsReverseDraw)
            {
                mCircleCenterPositions.add(mHeight - (mCircleRadius + i * mCircleRadius * 2 + i * mLinePadding));
            } else
            {
                mCircleCenterPositions.add(mCircleRadius + i * mCircleRadius * 2 + i * mLinePadding);
            }
        }
        /**
         * set listener
         */
        if(mOnDrawListener != null)
        {
            mOnDrawListener.ondrawIndicator();
        }
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        if(mOnDrawListener != null)
        {
            mOnDrawListener.ondrawIndicator();
        }
        mUnCompletedPaint.setColor(mUncompletedLineColor);
        mCompletedPaint.setColor(mCompletedLineColor);

        //-----------------------画线-------draw line-----------------------------------------------
        for(int i = 0; i < mCircleCenterPositions.size() - 1; i++)
        {
            //前一个ComplectedXPosition
            final float preComplectedXPosition = mCircleCenterPositions.get(i);
            //后一个ComplectedXPosition
            final float afterComplectedXPosition = mCircleCenterPositions.get(i + 1);

            if(i < mCompletingPostion)//判断在完成之前的所有点
            {
                //判断在完成之前的所有点，画完成的线，这里是矩形,很细的矩形，类似线，为了做区分，好看些
                if(mIsReverseDraw)
                {
                    canvas.drawRect(mLeftY, afterComplectedXPosition + mCircleRadius - 10, mRightY, preComplectedXPosition - mCircleRadius + 10, mCompletedPaint);
                } else
                {
                    canvas.drawRect(mLeftY, preComplectedXPosition + mCircleRadius - 10, mRightY, afterComplectedXPosition - mCircleRadius + 10, mCompletedPaint);
                }
            } else
            {
                if(mIsReverseDraw)
                {
                    mPath.moveTo(mCenterX, afterComplectedXPosition + mCircleRadius);
                    mPath.lineTo(mCenterX, preComplectedXPosition - mCircleRadius);
                    canvas.drawPath(mPath, mUnCompletedPaint);
                } else
                {
                    mPath.moveTo(mCenterX, preComplectedXPosition + mCircleRadius);
                    mPath.lineTo(mCenterX, afterComplectedXPosition - mCircleRadius);
                    canvas.drawPath(mPath, mUnCompletedPaint);
                }

            }
        }
        //-----------------------画线-------draw line-----------------------------------------------

        //-----------------------画图标-----draw icon-----------------------------------------------
        for(int i = 0; i < mCircleCenterPositions.size(); i++)
        {
            final float currentComplectedXPosition = mCircleCenterPositions.get(i);
            mRect = new Rect((int) (mCenterX - mCircleRadius), (int) (currentComplectedXPosition - mCircleRadius), (int) (mCenterX + mCircleRadius), (int) (currentComplectedXPosition + mCircleRadius));
            if(i < mCompletingPostion)
            {
                mCompleteIcon.setBounds(mRect);
                mCompleteIcon.draw(canvas);
            } else if(i == mCompletingPostion && mCircleCenterPositions.size() != 1)
            {
                mCompletedPaint.setColor(Color.WHITE);
                canvas.drawCircle(mCenterX, currentComplectedXPosition, mCircleRadius * 1.1f, mCompletedPaint);
                mAttentionIcon.setBounds(mRect);
                mAttentionIcon.draw(canvas);
            } else
            {
                mDefaultIcon.setBounds(mRect);
                mDefaultIcon.draw(canvas);
            }
        }
        //-----------------------画图标-----draw icon-----------------------------------------------
    }


    /**
     * 得到所有圆点所在的位置
     *
     * @return
     */
    public List<Float> getCircleCenterPointPositionList()
    {
        return mCircleCenterPositions;
    }

    /**
     * 设置流程步数
     *
     * @param stepNum 流程步数
     */
    public void setStepNum(int stepNum)
    {
        this.mStepNum = stepNum;
        invalidate();
    }

    /**
     * 设置正在进行position
     *
     * @param complectingPosition
     */
    public void setCompletingPosition(int complectingPosition)
    {
        this.mCompletingPostion = complectingPosition;
        invalidate();
    }

    /**
     * 设置未完成线的颜色
     *
     * @param unCompletedLineColor
     */
    public void setUnCompletedLineColor(int unCompletedLineColor)
    {
        this.mUncompletedLineColor = unCompletedLineColor;
    }

    /**
     * 设置已完成线的颜色
     *
     * @param completedLineColor
     */
    public void setCompletedLineColor(int completedLineColor)
    {
        this.mCompletedLineColor = completedLineColor;
    }

    /**
     * is reverse draw 是否倒序画
     */
    public void reverseDraw(boolean isReverseDraw)
    {
        this.mIsReverseDraw = isReverseDraw;
        invalidate();
    }

    /**
     * 设置默认图片
     *
     * @param defaultIcon
     */
    public void setDefaultIcon(Drawable defaultIcon)
    {
        this.mDefaultIcon = defaultIcon;
    }

    /**
     * 设置已完成图片
     *
     * @param completeIcon
     */
    public void setCompleteIcon(Drawable completeIcon)
    {
        this.mCompleteIcon = completeIcon;
    }

    /**
     * 设置正在进行中的图片
     *
     * @param attentionIcon
     */
    public void setAttentionIcon(Drawable attentionIcon)
    {
        this.mAttentionIcon = attentionIcon;
    }

    public interface OnDrawIndicatorListener {
        void ondrawIndicator();
    }

}
