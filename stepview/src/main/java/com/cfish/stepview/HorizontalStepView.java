package com.cfish.stepview;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
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
public class HorizontalStepView extends LinearLayout implements HorizontalStepViewIndicator.OnDrawIndicatorListener {

    private RelativeLayout mTextContainer;
    private HorizontalStepViewIndicator mStepsViewIndicator;
    private List<String>  mTexts;
    private int mCompletingPosition;
    private int mUnCompletedTextColor = ContextCompat.getColor(getContext(), R.color.uncompleted_text_color);
    private int mCompletedTextColor = ContextCompat.getColor(getContext(), android.R.color.white);

    public HorizontalStepView(Context context) {
        this(context, null);
    }

    public HorizontalStepView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalStepView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.widget_horizontal_stepsview,this);
        mStepsViewIndicator = (HorizontalStepViewIndicator) rootView.findViewById(R.id.steps_indicator);
        mStepsViewIndicator.setOnDrawListener(this);
        mTextContainer = (RelativeLayout) rootView.findViewById(R.id.rl_text_container);
        mTextContainer.removeAllViews();
    }

    public HorizontalStepView setStepViewTexts(List<String> texts) {
        mTexts = texts;
        mStepsViewIndicator.setStepNum(mTexts.size());
        return this;
    }

    public HorizontalStepView setStepViewIndicatorCompletingPosition(int completingPosition) {
        mCompletingPosition = completingPosition;
        mStepsViewIndicator.setCompletingPosition(completingPosition);
        return this;
    }

    public HorizontalStepView setStepViewUnCompletedTextColor(int unCompletedTextColor) {
        mUnCompletedTextColor = unCompletedTextColor;
        return this;
    }

    public HorizontalStepView setStepViewCompletedTextColor(int completedTextColor) {
        this.mCompletedTextColor = completedTextColor;
        return this;
    }

    public HorizontalStepView setStepsViewIndicatorUnCompletedLineColor(int unCompletedLineColor)
    {
        mStepsViewIndicator.setUnCompletedLineColor(unCompletedLineColor);
        return this;
    }

    public HorizontalStepView setStepsViewIndicatorCompletedLineColor(int completedLineColor)
    {
        mStepsViewIndicator.setCompletedLineColor(completedLineColor);
        return this;
    }

    public HorizontalStepView setStepsViewIndicatorDefaultIcon(Drawable defaultIcon)
    {
        mStepsViewIndicator.setDefaultIcon(defaultIcon);
        return this;
    }

    public HorizontalStepView setStepsViewIndicatorCompleteIcon(Drawable completeIcon)
    {
        mStepsViewIndicator.setCompleteIcon(completeIcon);
        return this;
    }

    public HorizontalStepView setStepsViewIndicatorAttentionIcon(Drawable attentionIcon)
    {
        mStepsViewIndicator.setAttentionIcon(attentionIcon);
        return this;
    }

    @Override
    public void ondrawIndicator() {
        List<Float> completedXPosition = mStepsViewIndicator.getCircleCenterPositions();
        if (mTexts != null) {
            for (int i = 0; i< mTexts.size(); i++) {
                TextView textView = new TextView((getContext()));
                textView.setText(mTexts.get(i));
                Log.d("Dfish",mTexts.get(i));
                textView.setX(completedXPosition.get(i) - mStepsViewIndicator.getCircleRadius() - 10);
                textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                if (i <= mCompletingPosition) {
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
