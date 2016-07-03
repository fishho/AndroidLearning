package com.cfish.stepview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by GKX100217 on 2016/6/30.
 */
public class ForTryView extends View {
    public ForTryView(Context context) {
        super(context);
    }

    public ForTryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ForTryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBackgroundResource(android.R.color.holo_green_light);

        //画圆形，实心
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(40);
        canvas.drawCircle(350, 350, 100, paint);


        //画虚线
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10);
        DashPathEffect effect = new DashPathEffect(new float[]{1,2,4,8},1);
        Path path = new Path();
        path.moveTo(200, 200);
        path.lineTo(800, 200);
        paint.setPathEffect(effect);
        canvas.drawPath(path, paint);

    }
}
