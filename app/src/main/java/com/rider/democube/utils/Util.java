package com.rider.democube.utils;

import android.content.Context;

/**
 * Created by wubin on 2017/10/31 0031.
 */

public class Util {

    public static int dip2px(Context context, float dpValue) {
        if(context==null){
            return (int) dpValue;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float dpValue){
        if(context==null){
            return (int) dpValue;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        if(context==null){
            return (int) spValue;
        }
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
