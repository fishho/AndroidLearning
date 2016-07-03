package com.cfish.stepview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckedTextView;

/**
 * Created by GKX100217 on 2016/6/30.
 */
public class CheckView extends View {

    //绘制圆弧的进度
    private int progress = 0;
    //线1的x轴
    private int line_x = 0;
    private int line_y = 0;
    //private int line2_x = 0;
    //private int line2_y = 0;

    public CheckView(Context context) {
        super(context);
    }

    public CheckView(Context context, AttributeSet attrs) {
        super(context);
    }

    public CheckView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        progress +=2;

        //绘制圆弧
        Paint paint = new Paint();
        Path path = new Path();
        paint.setColor(ContextCompat.getColor(getContext(),android.R.color.holo_red_light));
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);

        int center = getWidth() / 2;
        int center1 = center - getWidth() / 5;
        int radius = getWidth() / 2 - 5;

        RectF rectF = new RectF(center - radius - 1, center - radius -1, center + radius +1, center + radius + 1);

        canvas.drawArc(rectF, 235, -360*progress/100, false, paint);

        if (progress >= 100) {
            if (line_x <= radius / 3) {
                line_x +=2;
                line_y ++;
                canvas.drawLine(center1, center, center1 + line_x, center +line_y,paint);
            } else {
                canvas.drawLine(center1, center, center1 + radius / 3, center +radius / 3,paint);
            }

            if (line_x >= radius / 3 && line_x <= radius) {
                line_x+= 2;
                line_y--;


            }
            canvas.drawLine(center1+radius / 3-1, center+radius / 3, center1 + line_x+1, center+line_y-1,paint);
        }

        postInvalidateDelayed(10);
    }
}
