package com.rider.democube.simpe;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by wubin on 2017/11/6 0006.
 */

public class SimpleSquareImageView extends ImageView {


    public SimpleSquareImageView(Context context) {
        super(context);
    }

    public SimpleSquareImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleSquareImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = getMeasuredWidth();
        int measureHeight = getMeasuredHeight();

        if(measureHeight > measureWidth){
            measureHeight = measureWidth;
        }else{
            measureWidth = measureHeight;
        }

        setMeasuredDimension(measureWidth,measureHeight);
    }
}
