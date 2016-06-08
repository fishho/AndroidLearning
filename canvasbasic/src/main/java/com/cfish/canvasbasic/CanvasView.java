package com.cfish.canvasbasic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by GKX100217 on 2016/6/8.
 */
public class CanvasView extends View {
    private Canvas canvas = new Canvas();
    private Paint paint = new Paint();
    RectF rect = new RectF(0,-100,100,0);
    private int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};
    public CanvasView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //translate
        for (int i= 0; i< 4; i++){
            paint.setColor(mColors[i%9]);
            Paint.Style style = (i/2 == 0)? Paint.Style.FILL:Paint.Style.STROKE;
            paint.setStrokeWidth(6);
            paint.setStyle(style);
            canvas.drawCircle(40,40,50,paint);
            canvas.translate(28,28);
        }

        //scale
        for (int i= 4; i < 8; i++) {
            paint.setColor(mColors[i%9]);
            Paint.Style style = (i/2 == 0)? Paint.Style.FILL:Paint.Style.STROKE;
            paint.setStrokeWidth(6);
            paint.setStyle(style);
            canvas.drawRect(rect,paint);
            if (i/2 == 0) {
                canvas.scale(1.1f, -1.1f);
            } else {
                canvas.scale(1.1f, 1.1f);
            }
        }

        //rotate
        canvas.translate(300,400);
        canvas.drawCircle(0,0,200,paint);
        canvas.drawCircle(0,0,220,paint);
        for (int i = 0;i <= 360; i+=10){
            paint.setColor(mColors[i%9]);
            Paint.Style style = (i/2 == 0)? Paint.Style.FILL:Paint.Style.STROKE;
            paint.setStrokeWidth(6);
            paint.setStyle(style);
            canvas.drawLine(0,200,0,220,paint);
            canvas.rotate(10);
        }

        //skew
/*        paint.setStrokeWidth(6);
        paint.setStyle(Paint.Style.FILL);
        canvas.skew(1,0);
        canvas.drawRect(rect, paint);*/

        RectF rectF = new RectF(0,-300,300,0);
        paint.setStyle(Paint.Style.FILL);
        //canvas.drawRect(rectF,paint);
        for (int i=0;i<=36;i++) {
            paint.setColor(mColors[i%9]);
            paint.setStrokeWidth(6);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawArc(rectF,10*i,10,true,paint);
        }

        canvas.translate(-150,150);
        for (int i=0;i<=36;i++) {
            paint.setColor(mColors[i%9]);
            paint.setStrokeWidth(6);
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawArc(rectF,0,10,true,paint);
            canvas.rotate(10,150,-150);
        }

    }
}
