package com.cfish.googlerainbow;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by GKX100217 on 2016/6/3.
 */
public class Rainbow extends View {
    //progressbar color
    int barColor = Color.parseColor("#1E88E5");
    //each bar segment width
    int hspace = Utils.dpToPx(80, getResources());
    //each bar segment height
    int vspace = Utils.dpToPx(4, getResources());
    //spaces between bar segments
    int space =Utils.dpToPx(10, getResources());
    int index = 0;

    float startX = 0;
    float delta = 10f;
    Paint mPaint;

    public Rainbow(Context context) {
        super(context);
    }

    public Rainbow(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Rainbow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //read custom attrs
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.rainbow, 0 , 0);
        hspace = t.getDimensionPixelSize(R.styleable.rainbow_rainbow_hspace, hspace);
        vspace = t.getDimensionPixelSize(R.styleable.rainbow_rainbow_vspace, vspace);
        barColor = t.getColor(R.styleable.rainbow_rainbow_color, barColor);
        t.recycle(); //always recycle after use
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(barColor);
        mPaint.setStrokeWidth(vspace);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float sw = this.getMeasuredWidth();
        if (startX >= sw + (hspace + space) -(sw%(hspace + space))) {
            startX = 0;
        } else {
            startX += delta;
        }
        float start = startX;
        while (start < sw) {
            if (start > sw/2) {
                mPaint.setColor(Color.parseColor("#eeaa00"));
            } else if(start > sw/4) {
                mPaint.setColor(Color.parseColor("#EE004B"));
            }
            canvas.drawLine(start, 5, start + hspace, 5, mPaint);
            start += (hspace + vspace);
        }

        start  = startX - space - hspace;
        while (start > -hspace) {
            if (start > -hspace/1.5) {
                mPaint.setColor(Color.parseColor("#AB00EE"));
            } else {
                mPaint.setColor(Color.parseColor("#77004B"));
            }
            canvas.drawLine(start, 5, start + hspace, 5, mPaint);
            start -= (hspace + vspace);
        }

        if (index >= 700000) {
                index = 0 ;
        }
        invalidate();
    }
}
