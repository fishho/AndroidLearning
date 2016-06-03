package com.cfish.googlerainbow;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by GKX100217 on 2016/6/3.
 */
public class Utils {
    public static int dpToPx(float dp, Resources res) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                res.getDisplayMetrics());
    }
}
