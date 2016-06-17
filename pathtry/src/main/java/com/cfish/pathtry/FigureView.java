package com.cfish.pathtry;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.renderscript.Type;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by GKX100217 on 2016/6/17.
 */
public class FigureView extends ImageView {

    protected int mViewWidth, mViewHeight;
    protected Paint mPaint = new Paint();
    protected Rect rect = new Rect();
    protected float length = 0;

    private RectF rectF = new RectF();
    private Path mPath = new Path();
    private Path mPath1 = new Path();

    private int modeFlag = 0x00;
    private static final int CIRCLE = 0X00;
    private static final int ROUNDRECT = 0x01;
    private static final int SECTOR = 0x02;
    private static final int RING = 0x03;
    private int radius = 50;
    private float angle = -120;

    public FigureView(Context context) {
        this(context, null);
    }

    public FigureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FigureView(Context context, AttributeSet attrs,  int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FigureImageView,defStyleAttr,0);
        for (int i =0,  n = array.getIndexCount(); i < n; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.FigureImageView_mode:
                    modeFlag = array.getInt(attr, modeFlag);
                    break;
                case R.styleable.FigureImageView_radius:
                    radius = array.getDimensionPixelSize(attr,radius);
                    break;
                case R.styleable.FigureImageView_angle:
                    angle = array.getFloat(attr,angle);
                    break;
                case R.styleable.FigureImageView_length:
                    length = array.getDimensionPixelSize(attr,(int)length)*2;
                    break;
            }
        }
        array.recycle();
        //初始化画笔
        mPaint.setAntiAlias(true);
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w+ 1;
        mViewHeight = h+ 1;
        size();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int saveCount = canvas.saveLayerAlpha(0.0F,0.0F,canvas.getWidth(),canvas.getHeight(),
                255, canvas.FULL_COLOR_LAYER_SAVE_FLAG);
        super.onDraw(canvas);
        canvas.translate(mViewWidth/2, mViewHeight/2);
        mPath.addRect(-mViewWidth/2, -mViewHeight/2, mViewWidth/2, mViewHeight/2, Path.Direction.CW);
        canvas.drawPath(pathFigure(), mPaint);
        mPath.reset();
        canvas.restoreToCount(saveCount);
    }

    protected void size() {
        length = length == 0? Math.min(mViewWidth, mViewHeight)/2: length;
        rect =  new Rect(-(int)length,-(int)length,(int)length, (int)length);
        rectF = new RectF(-length, -length, length, length);
    }

    protected Path pathFigure() {
        switch (modeFlag) {
            case CIRCLE:
                mPath.addCircle(0,0,length, Path.Direction.CCW);
                break;
            case ROUNDRECT:
                rectF.left = -length;
                rectF.top = -length;
                rectF.right = length;
                rectF.bottom = length;
                mPath.addRoundRect(rectF,radius,radius, Path.Direction.CCW);
                break;
            case SECTOR:
                mPath1.reset();
                rectF.left = -length*2;
                rectF.top = -length;
                rectF.right = length*2;
                rectF.bottom = length*3;
                mPath.moveTo(0,length);
                mPath.arcTo(rectF,angle,-angle*2-180);
                mPath1.addRect(-mViewWidth/2,-mViewHeight/2,mViewWidth/2,mViewHeight/2,Path.Direction.CCW);
                mPath.op(mPath1, Path.Op.INTERSECT);
                mPath1.reset();
                break;
            case RING:
                mPath1.reset();
                rectF.left = -length*2;
                rectF.top = -length;
                rectF.right = length*2;
                rectF.bottom = length*3;
                mPath1.moveTo(0,length);
                mPath1.arcTo(rectF,angle,-angle*2-180);

                rectF.left = -length/2;
                rectF.top = length/2;
                rectF.right = length/2;
                rectF.bottom = length*3/2;
                mPath.moveTo(0, length);
                mPath.arcTo(rectF, angle, -angle*2-180);

                mPath.op(mPath1, Path.Op.XOR);
                mPath1.reset();
                break;
        }
        return mPath;
    }

    public void setModeFlag(int modeFlag) {
        this.modeFlag = modeFlag;
    }

    public void setAngle(float angle) {
        this.angle =  angle;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setLength(float length) {
        this.length = length;
    }
}
