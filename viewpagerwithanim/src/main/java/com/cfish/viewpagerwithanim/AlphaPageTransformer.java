package com.cfish.viewpagerwithanim;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by GKX100217 on 2016/5/16.
 */
public class AlphaPageTransformer implements ViewPager.PageTransformer {
    private static final float DEFAULT_MIN_ALPHA = 0.5f;
    private float mMinAlpha = DEFAULT_MIN_ALPHA;
    @Override
    public void transformPage(View page, float position) {
        if (position < -1) {
            page.setAlpha(mMinAlpha);
            page.setScaleX(0.9f);
            page.setScaleY(0.9f);
            page.setRotation(30f);
        } else if (position <= 1) {
            if (position < 0 ) {
                float factor = mMinAlpha + (1-mMinAlpha)*(1+position);
                page.setAlpha(factor);
            } else {
                float factor = mMinAlpha +(1-mMinAlpha)*(1-position);
                page.setAlpha(factor);
            }
        } else {
            page.setAlpha(mMinAlpha);
            page.setScaleX(0.3f);
            page.setScaleY(0.3f);//scale plus rotate
        }
    }
}
