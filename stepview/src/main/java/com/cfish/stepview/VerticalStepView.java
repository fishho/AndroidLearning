package com.cfish.stepview;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by GKX100217 on 2016/6/29.
 */
public class VerticalStepView extends LinearLayout implements  VerticalStepViewIndicator.OnDrawIndicatorListener{

    private RelativeLayout mTextContainer;
    private VerticalStepViewIndicator mStepsViewIndicator;
    private List<String> mTexts;
    private int mCompletingPosition;
    private int mUnCompletedTextColor = ContextCompat.getColor(getContext(), R.color.uncompleted_text_color);
    private int mCompletedTextColor = ContextCompat.getColor(getContext(), android.R.color.white);

    public VerticalStepView(Context context) {
        this(context, null);
    }

    public VerticalStepView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalStepView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_vertical_stepsview, this);
        mStepsViewIndicator = (VerticalStepViewIndicator) rootView.findViewById(R.id.steps_indicator);
        mStepsViewIndicator.setOnDrawListener(this);
        mTextContainer = (RelativeLayout) rootView.findViewById(R.id.rl_text_container);
        mTextContainer.removeAllViews();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public VerticalStepView setStepViewTexts(List<String> texts) {
        mTexts = texts;
        mStepsViewIndicator.setStepNum(mTexts.size());
        return this;
    }

    public VerticalStepView setStepViewIndicatorCompletingPosition(int completingPosition) {
        mCompletingPosition = completingPosition;
        mStepsViewIndicator.setCompletingPosition(completingPosition);
        return this;
    }

    public VerticalStepView setStepViewUnCompletedTextColor(int unCompletedTextColor) {
        mUnCompletedTextColor = unCompletedTextColor;
        return this;
    }

    public VerticalStepView setStepViewCompletedTextColor(int completedTextColor) {
        this.mCompletedTextColor = completedTextColor;
        return this;
    }

    public VerticalStepView setStepsViewIndicatorUnCompletedLineColor(int unCompletedLineColor) {
        mStepsViewIndicator.setUnCompletedLineColor(unCompletedLineColor);
        return this;
    }

    public VerticalStepView setStepsViewIndicatorDefaultIcon(Drawable defaultIcon) {
        mStepsViewIndicator.setDefaultIcon(defaultIcon);
        return this;
    }

    public VerticalStepView setStepsViewIndicatorCompleteIcon(Drawable completeIcon)
    {
        mStepsViewIndicator.setCompleteIcon(completeIcon);
        return this;
    }

    public VerticalStepView setStepsViewIndicatorAttentionIcon(Drawable attentionIcon)
    {
        mStepsViewIndicator.setAttentionIcon(attentionIcon);
        return this;
    }

    public VerticalStepView reverseDraw(boolean isReverSe)
    {
        this.mStepsViewIndicator.reverseDraw(isReverSe);
        return this;
    }

    @Override
    public void ondrawIndicator() {
        List<Float> complectedXPosition = mStepsViewIndicator.getCircleCenterPointPositionList();
        if (mTexts != null) {
            for (int i = 0; i < mTexts.size(); i++) {
                TextView textView = new TextView(getContext());
                textView.setText(mTexts.get(i));
                textView.setY(complectedXPosition.get(i) - mStepsViewIndicator.getCircleRadius() / 2);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                if (i < mCompletingPosition) {
                    textView.setTypeface(null, Typeface.BOLD);
                    textView.setTextColor(mCompletedTextColor);
                } else {
                    textView.setTextColor(mUnCompletedTextColor);
                }
                mTextContainer.addView(textView);
            }
        }
    }
}
