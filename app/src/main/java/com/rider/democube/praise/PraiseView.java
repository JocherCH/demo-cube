package com.rider.democube.praise;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.rider.democube.R;

/**
 * Created by wubin on 2017/10/26 0026.
 */

public class PraiseView extends View implements View.OnClickListener{


    private static final int DEFAULT_NUM = 100;
    private static final int DEFAULT_NORMAL_TEXTCOLOR = Color.parseColor("#FF9F9E9F");

    private int praiseNum = DEFAULT_NUM;
    private String[] nums = new String[]{"","",""};

    private Paint paint;

    private boolean praised = false;
    private int textSize = 30;
    private int startX;
    private int startY;
    private float mOldOffsetY;
    private float mNewOffsetY;


    public void setTextOffsetY(float offsetY) {

        if(praised){
            mOldOffsetY = - offsetY;
            mNewOffsetY = 100 - offsetY;
        }else{
            mOldOffsetY = offsetY;
            mNewOffsetY = offsetY - 100;
        }

        invalidate();
    }



    public void setPraiseNum(int praiseNum) {
        calculateChangeNum(praiseNum);
        showThumbUpAnim();
    }

    private void showThumbUpAnim(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(this,"textOffsetY",0,100);
        animator.setDuration(5000);
        animator.start();
    }

    public PraiseView(Context context) {
        super(context);
        init();
    }

    public PraiseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PraiseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PraiseView);
        try {
            int length = typedArray.getIndexCount();
            for(int i = 0; i < length; i++){
                int attr = typedArray.getIndex(i);
                switch (attr){
                    case R.styleable.PraiseView_praiseNum:
                        praiseNum = typedArray.getInteger(attr,DEFAULT_NUM);
                        break;
                }
            }
        }finally {
            typedArray.recycle();
        }
        init();
    }


    private void init(){
        //开启抗锯齿效果
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setOnClickListener(this);
        nums[0] = String.valueOf(praiseNum);


    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        startX = getWidth() / 2;
        startY = getHeight() / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawText(canvas);
    }

    private void drawText(Canvas canvas){
        paint.setTextSize(textSize);
        paint.setColor(Color.YELLOW);
        String text = String.valueOf(1);
        float textWidth = paint.measureText(text) / text.length();
        canvas.drawRect(0,0,getWidth(),getHeight(),paint);
        paint.setColor(Color.BLACK);
        canvas.drawText(nums[0],startX,startY,paint);

        canvas.drawText(nums[1],startX + nums[0].length() * textWidth,startY + mOldOffsetY,paint);

        canvas.drawText(nums[2],startX + nums[0].length() * textWidth,startY + mNewOffsetY,paint);
    }


    private void calculateChangeNum(int newNum){
        if(newNum == praiseNum){
            nums[0] = String.valueOf(praiseNum);
            nums[1] = "";
            nums[2] = "";
            return;
        }
        //判断是否点赞
        praised = newNum > praiseNum;

        String oldNum = String.valueOf(praiseNum);
        String new_Num = String.valueOf(newNum);
        int oldLength = oldNum.length();
        int newLength =new_Num .length();

        if(oldLength != newLength){
            nums[0] = "";
            nums[1] = oldNum;
            nums[2] = new_Num;
        }else{
            for(int i = 0; i < oldLength; i++){
                char oldChar = oldNum.charAt(i);
                char newChar = new_Num.charAt(i);

                if(oldChar!= newChar){
                    nums[0] = oldNum.substring(0,i);
                    nums[1] = oldNum.substring(i);
                    nums[2] = new_Num.substring(i);
                    break;
                }
            }

        }







    }
}
