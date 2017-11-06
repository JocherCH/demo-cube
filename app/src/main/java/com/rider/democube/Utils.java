package com.rider.democube;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by wubin on 2017/11/6 0006.
 */

public class Utils {

    public static float dpToPixel(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * metrics.density;
    }
}
