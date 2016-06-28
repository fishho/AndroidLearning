package com.cfish.radarview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

/**
 * Created by GKX100217 on 2016/6/28.
 */
public class RadarView extends View {

    private String mImageUrl;
    private boolean threadIsRunning = false;
    private int start = 0;
    private RadarThread radarThread;
    private Paint mPaintBitmap;//换中间图片的画笔
    private Paint mPaintLine; //画圆圈的画笔
    private Paint mPaintCircle; //画雷达的画笔
    private Matrix matrix;//通过矩阵变换，做出扫描效果

    private Bitmap mBitmap;
    private float mBitmapWidth = 150;
    private float mCircleWidth = 2;
    private float mCircleMargin = 30;
    private int mCircleColor = Color.RED;
    private int mCircleColorx = Color.RED;
    private int mCircleColorxx = Color.RED;
    private int mCircleColorxxx = Color.RED;
    private int mScanColor = Color.RED;

    private int mdefaultImage;
    private Bitmap mDefaultBitmap;

    float mWidth;
    float mHeight;

    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RadarView, defStyleAttr, 0);
        mBitmapWidth = array.getDimension(R.styleable.RadarView_image_width, mBitmapWidth);
        mCircleMargin = array.getDimension(R.styleable.RadarView_circle_margin, mCircleMargin);
        mCircleWidth = array.getDimension(R.styleable.RadarView_circle_width, mCircleWidth);
        mCircleColor = array.getColor(R.styleable.RadarView_circle_color, mCircleColor);
        mCircleColorx = array.getColor(R.styleable.RadarView_circle_colorx, mCircleColor);
        mCircleColorxx = array.getColor(R.styleable.RadarView_circle_colorxx, mCircleColor);
        mCircleColorxxx = array.getColor(R.styleable.RadarView_circle_colorxxx, mCircleColor);
        mScanColor = array.getColor(R.styleable.RadarView_saner_color, mScanColor);
        mdefaultImage = array.getResourceId(R.styleable.RadarView_default_image,0);
        array.recycle();

        initView();
    }

    private void initView() {
        mPaintBitmap = new Paint();
        mPaintLine = new Paint();
        mPaintLine.setColor(mCircleColor);
        mPaintLine.setStrokeWidth(mCircleWidth);
        mPaintLine.setAntiAlias(true);
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintCircle = new Paint();
        mPaintCircle.setColor(mScanColor);
        mPaintCircle.setAntiAlias(true);
        matrix = new Matrix();
        if (mdefaultImage != 0) {
            getBitmapFromGlide(mdefaultImage);
        }
    }

    public void setImageUrl(String imageUrl) {
        this.mImageUrl = imageUrl;
        getBitmapFromGlide(this.mImageUrl);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth / 2 - mBitmapWidth / 2, mHeight / 2 - mBitmapWidth / 2);
        if (mBitmap != null) {
            mPaintBitmap.reset();

            canvas.drawBitmap(mBitmap, 0, 0, mPaintBitmap);
        } else if (mDefaultBitmap != null) {
            mPaintBitmap.reset();
            BitmapShader mBitmapShader = new BitmapShader(mDefaultBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mPaintBitmap.setShader(mBitmapShader);
            canvas.drawCircle(mBitmapWidth / 2, mBitmapWidth / 2,mBitmapWidth /2, mPaintBitmap);
        }
        canvas.translate(mBitmapWidth / 2, mBitmapWidth / 2);

        mPaintLine.setColor(mCircleColor);
        canvas.drawCircle(0, 0, mBitmapWidth / 2+ mCircleMargin, mPaintLine);
        mPaintLine.setColor(mCircleColorx);
        canvas.drawCircle(0, 0, mBitmapWidth / 2 + mCircleMargin*2, mPaintLine);
        mPaintLine.setColor(mCircleColorxx);
        canvas.drawCircle(0, 0, mBitmapWidth / 2 + mCircleMargin * 3, mPaintLine);
        mPaintLine.setColor(mCircleColorxxx);
        canvas.drawCircle(0, 0, mBitmapWidth / 2 + mCircleMargin * 4, mPaintLine);

        Shader shader = new SweepGradient(0, 0, Color.TRANSPARENT, mScanColor);
        mPaintCircle.setShader(shader);

        //利用矩阵，旋转动画

        canvas.concat(matrix);
        canvas.drawCircle(0 , 0, mBitmapWidth /2 + mCircleMargin*4+100,mPaintCircle);
    }

    public void start() {
        if (threadIsRunning) {
            return;
        }
        threadIsRunning = true;
        radarThread = new RadarThread();
        radarThread.start();
    }

    public void stop() {
        threadIsRunning = false;
    }

    private void getBitmapFromGlide(int resource) {
        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(new ViewGroup.MarginLayoutParams((int)mBitmapWidth,(int)mBitmapWidth));
        Glide.with(getContext()).load(resource).asBitmap().centerCrop().transform(new RoundImageTransform(
                getContext())).into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                mDefaultBitmap = resource;
            }
        });
    }

    private void getBitmapFromGlide(String url) {
        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(new ViewGroup.MarginLayoutParams((int)mBitmapWidth,(int)mBitmapWidth));
        Glide.with(getContext()).load(url).asBitmap().centerCrop().transform(new RoundImageTransform(getContext())).into(new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                mBitmap = resource;
            }
        });
    }

    class RadarThread extends Thread {
        @Override
        public void run() {
            while(threadIsRunning) {
                RadarView.this.post(new Runnable() {
                    @Override
                    public void run() {
                        start = start + 1;
                        matrix.setRotate(start, 0, 0);
                        RadarView.this.invalidate();
                    }
                });
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
