package com.cfish.viewpagerwithanim;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by GKX100217 on 2016/5/16.
 */
public class RotatePageTransformer implements ViewPager.PageTransformer {
    private static final float DEFAULT_MAX_ROATE = 15.0f;
    private float mMaxRoate = DEFAULT_MAX_ROATE;

    @Override
    public void transformPage(View page, float position) {
        if(position < -1)
        {
            page.setRotation(mMaxRoate*-1);
            page.setPivotX(page.getWidth());
            page.setPivotY(page.getHeight());
        } else if (position <= 1) {
            if (position < 0) {
                page.setPivotX(page.getWidth()*(0.5f + 0.5f*(-position)));
                page.setPivotX(page.getHeight());
                page.setRotation(mMaxRoate*position);
            } else {
                page.setPivotX(page.getWidth()*0.5f*(1-position));
                page.setPivotY(page.getHeight());
                page.setRotation(mMaxRoate*position);
            }
        } else {
            page.setRotation(mMaxRoate);
            page.setPivotX(page.getWidth()*0);
            page.setPivotY(page.getHeight());
        }
    }
}
