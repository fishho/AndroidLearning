package com.cfish.androidlearning;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout mContentRl;
    private ImageView mLogoIv;
    private TextView mDescTv;
    private Button mOpenBtn;

    private int centerX;
    private int centerY;
    private int depthZ = 400;
    private int duration = 600;
    private Rotate3dAnimation openAnimation;
    private Rotate3dAnimation closeAnimation;

    private boolean isOpen = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContentRl = (RelativeLayout) findViewById(R.id.rl_content);
        mLogoIv = (ImageView) findViewById(R.id.iv_logo);
        mDescTv = (TextView) findViewById(R.id.tv_desc);
        mOpenBtn = (Button) findViewById(R.id.btn_open);
    }

    /**
     * 卡牌文本介绍打开效果
     * 注意旋转角度
     */

    private void initOpenAnim() {
        //从0到90度，顺时针旋转，此时reverse 为true;
        //90度后视图不可见
        openAnimation = new Rotate3dAnimation(0, 90, centerX, centerY, depthZ, true);
        openAnimation.setDuration(duration);
        openAnimation.setFillAfter(true);
        openAnimation.setInterpolator(new AccelerateInterpolator());
        openAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLogoIv.setVisibility(View.GONE);
                mDescTv.setVisibility(View.VISIBLE);

                //从270到360度，顺时针旋转视图，此时reverse为false;
                //360时，视图可见
                Rotate3dAnimation rotateAnimation = new Rotate3dAnimation(270, 360, centerX, centerY, depthZ,false);
                rotateAnimation.setDuration(duration);
                rotateAnimation.setFillAfter(true);
                rotateAnimation.setInterpolator(new DecelerateInterpolator());
                mContentRl.startAnimation(rotateAnimation);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 卡牌文本介绍关闭效果
     * 旋转角度与打开时相反即可
     */
    private void initCloseAnim() {
        closeAnimation = new Rotate3dAnimation(360, 270, centerX, centerY, depthZ, true);
        closeAnimation.setDuration(duration);
        closeAnimation.setFillAfter(true);
        closeAnimation.setInterpolator(new AccelerateInterpolator());
        closeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLogoIv.setVisibility(View.VISIBLE);
                mDescTv.setVisibility(View.GONE);

                Rotate3dAnimation rotateAnimation =  new Rotate3dAnimation(90, 0, centerX, centerY, depthZ, false);
                rotateAnimation.setDuration(duration);
                rotateAnimation.setFillAfter(true);
                rotateAnimation.setInterpolator(new DecelerateInterpolator());
                mContentRl.setAnimation(rotateAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void onClickView(View v) {
        //以旋转对象的中心点为旋转中心，此处不应在onCreate中获取，因为彼时，获取的宽高为0;
        centerX = mContentRl.getWidth()/2;
        centerY = mContentRl.getHeight()/2;
        if (openAnimation == null ) {
            initOpenAnim();
            initCloseAnim();
        }

        if (openAnimation.hasStarted() && !openAnimation.hasEnded()) {
            return;
        }
        if (closeAnimation.hasStarted() && !closeAnimation.hasEnded()) {
            return;
        }

        if (isOpen) {
            mContentRl.startAnimation(closeAnimation);
        } else {
            mContentRl.startAnimation(openAnimation);

        }
        isOpen = !isOpen;
        mOpenBtn.setText(isOpen? "关闭": "打开");
    }
}
