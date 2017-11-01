package com.rider.democube.praise;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.rider.democube.R;
import com.rider.democube.utils.Util;

/**
 * Created by riderwu on 2017/10/26 0026.
 */

public class PraiseView extends View implements View.OnClickListener {

    //两个数字之间的距离
    private static final int OFFSET_NUM = 100;
    //图标与背景的距离
    private static final int OFFSET_ICON = 60;
    //图标与数字之间的距离
    private static final int OFFSET_ICON_NUM = 60;
    //默认点赞数
    private static final int COUNT_PRAISE = 99;
    // 图标大小
    private static final int SIZE_ICON = 16;
    // 背景大小
    private static final int SIZE_ICONBG = 16;
    // 动画时间
    private static final int DURATION = 250;
    //文字默认颜色
    private static final int COLOR_TEXT = Color.parseColor("#FF9F9E9F");

    private static final int SIZE_TEXT = 24;

    private static final float SCALE_UP = 1.0f;
    private static final float SCALE_DOWN = 0.5f;

    private float scaleUp;
    private float scaleDown;
    private float getScaleDown;
    private float offsetNum;
    private float offsetIcon;
    private float offsetIconNum;
    private int countPraise;
    private int sizeIcon;
    private int sizeText;
    private int sizeIconBg;
    private int animDuration;
    private int colorText;
    private Bitmap normalBitmap;
    private Bitmap praiseBitmap;
    private Bitmap bgBitmap;

    private String[] nums = new String[]{"", "", ""};
    private boolean praiseState;
    private boolean isThumbUp;
    private float nowOffsetNum;
    private float mOldOffsettNum;
    private float mNewOffsettNum;

    private Paint paint;
    private Paint bitmapPaint;

    private int centerX;
    private int centerY;
    private int startX_icon;
    private int startY_icon;
    private int startX_text;
    private int startY_text;

    public PraiseView(Context context) {
        super(context);
        initData();
    }

    public PraiseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    public PraiseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PraiseView);
        try {
            int length = typedArray.getIndexCount();
            for (int i = 0; i < length; i++) {
                int attr = typedArray.getIndex(i);
                switch (attr) {
                    case R.styleable.PraiseView_praiseCount:
                        countPraise = typedArray.getInteger(attr, COUNT_PRAISE);
                        break;
                    case R.styleable.PraiseView_iconPraise:
                        praiseBitmap = BitmapFactory.decodeResource(getResources(),
                                typedArray.getResourceId(attr, R.drawable.ic_messages_like_selected));
                        break;
                    case R.styleable.PraiseView_iconNormal:
                        normalBitmap = BitmapFactory.decodeResource(getResources(),
                                typedArray.getResourceId(attr, R.drawable.ic_messages_like_unselected));
                        break;
                    case R.styleable.PraiseView_iconBG:
                        bgBitmap = BitmapFactory.decodeResource(getResources(),
                                typedArray.getResourceId(attr, R.drawable.ic_messages_like_selected_shining));
                        break;
                    case R.styleable.PraiseView_iconSize:
                        sizeIcon = typedArray.getInteger(attr, Util.dip2px(getContext(),SIZE_ICON));
                        break;
                    case R.styleable.PraiseView_iconBGSize:
                        sizeIconBg = typedArray.getInteger(attr,Util.dip2px(getContext(),SIZE_ICONBG));
                        break;
                    case R.styleable.PraiseView_textColor:
                        colorText = typedArray.getColor(attr,COLOR_TEXT);
                        break;
                    case R.styleable.PraiseView_textSize:
                        sizeText = typedArray.getInteger(attr,Util.sp2px(getContext(),SIZE_TEXT));
                        break;
                    case R.styleable.PraiseView_duration:
                        animDuration = typedArray.getInteger(attr,DURATION);
                        break;
                    case R.styleable.PraiseView_numOffset:
                        offsetNum = typedArray.getFloat(attr,OFFSET_NUM);
                        break;
                    case R.styleable.PraiseView_numOffsetIcon:
                        offsetIconNum = typedArray.getFloat(attr,OFFSET_ICON_NUM);
                        break;
                    case R.styleable.PraiseView_iconOffset:
                        offsetIcon = typedArray.getFloat(attr,OFFSET_ICON);
                        break;
                }
            }
        } finally {
            typedArray.recycle();
        }
        initData();
    }

    private void initData(){
        if(countPraise == 0){
            countPraise = COUNT_PRAISE;
        }

        if(offsetNum == 0){
            offsetNum =OFFSET_NUM;
        }

        if(offsetIcon == 0){
            offsetIcon = OFFSET_ICON;
        }

        if(offsetIconNum == 0){
            offsetIconNum = OFFSET_ICON_NUM;
        }

        if(sizeIcon == 0){
            sizeIcon =  SIZE_ICON;
        }
        if(sizeText == 0){
            sizeText = Util.sp2px(getContext(),SIZE_TEXT);
        }
        if(sizeIconBg == 0){
            sizeIconBg = SIZE_ICONBG;
        }

        if(animDuration == 0){
            animDuration = DURATION;
        }

        if(colorText == 0){
            colorText = COLOR_TEXT;
        }

        if(normalBitmap == null){
            normalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_unselected);
        }
        if(praiseBitmap == null){
            praiseBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_selected);
        }
        if(bgBitmap == null){
            bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_selected_shining);
        }

        //开启抗锯齿效果
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setOnClickListener(this);
        nums[0] = String.valueOf(countPraise);




    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        startX_icon = 0;
        startY_icon = 0;

        startX_text = (int)(centerX );
        startY_text = (int)(centerY+sizeText);


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawIcon(canvas);
        drawText(canvas);
    }


    @Override
    public void onClick(View v) {
        praiseState = !praiseState;
        if (praiseState) {
            //点赞效果
            calculateChangeNum(countPraise+1);
            countPraise++;
            ThumbstoLight();

        } else {
            //取消点赞效果
            calculateChangeNum(countPraise -1);
            countPraise--;
            ThumbstoDrak();

        }
    }

    private void ThumbstoLight(){

        ObjectAnimator normalAnim = ObjectAnimator.ofFloat(this, "scaleDown", SCALE_UP, SCALE_DOWN);
        normalAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isThumbUp = true;
            }
        });

        ObjectAnimator praiseAnim = ObjectAnimator.ofFloat(this, "scaleUp", SCALE_DOWN, SCALE_UP);
        praiseAnim.setInterpolator(new OvershootInterpolator());
        ObjectAnimator textAnim = ObjectAnimator.ofFloat(this, "offsetNum", 0, offsetNum);
        AnimatorSet anims = new AnimatorSet();
        anims.setDuration(animDuration);
        anims.play(normalAnim).with(textAnim);
        anims.play(praiseAnim).after(normalAnim);
        anims.start();
    }


    private void ThumbstoDrak(){

        ObjectAnimator praiseAnim = ObjectAnimator.ofFloat(this, "scaleUp" , SCALE_UP,SCALE_DOWN);
        praiseAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isThumbUp = false;
            }
        });

        ObjectAnimator normalAnim = ObjectAnimator.ofFloat(this, "scaleDown" ,SCALE_DOWN,SCALE_UP);
        normalAnim.setInterpolator(new OvershootInterpolator());
        ObjectAnimator textAnim = ObjectAnimator.ofFloat(this, "offsetNum", 0, -offsetNum);
        AnimatorSet anims = new AnimatorSet();
        anims.setDuration(animDuration);
        anims.play(praiseAnim).with(textAnim);
        anims.play(normalAnim).after(praiseAnim);
        anims.start();
    }









    private void drawIcon(Canvas canvas) {
        if (isThumbUp) {
            canvas.drawBitmap(bgBitmap,startX_icon,startY_icon+offsetIcon,bitmapPaint);
            canvas.drawBitmap(praiseBitmap, startX_icon , startY_icon+offsetIcon*2, bitmapPaint);
        } else {
            canvas.drawBitmap(normalBitmap, startX_icon , startY_icon+offsetIcon*2, bitmapPaint);
        }
    }

    private void drawText(Canvas canvas) {
        paint.setTextSize(sizeText);
        float length_0 = paint.measureText(nums[0]);
        paint.setColor(COLOR_TEXT);
        canvas.drawText(nums[0], startX_text, startY_text, paint);
        paint.setAlpha(255 - (int) ((255 / offsetNum) * Math.abs(mOldOffsettNum)+0.5f));
        canvas.drawText(nums[1], startX_text + length_0, startY_text - mOldOffsettNum, paint);
        paint.setAlpha((int) ((255 / offsetNum) * Math.abs(mOldOffsettNum)+0.5f));
        canvas.drawText(nums[2], startX_text + length_0, startY_text - mNewOffsettNum, paint);
    }


    private void calculateChangeNum(int newNum) {
        if (newNum == countPraise) {
            nums[0] = String.valueOf(countPraise);
            nums[1] = "";
            nums[2] = "";
            return;
        }

        String oldNum = String.valueOf(countPraise);
        String new_Num = String.valueOf(newNum);
        int oldLength = oldNum.length();
        int newLength = new_Num.length();

        if (oldLength != newLength) {
            nums[0] = "";
            nums[1] = oldNum;
            nums[2] = new_Num;
        } else {
            for (int i = 0; i < oldLength; i++) {
                char oldC1 = oldNum.charAt(i);
                char newC1 = new_Num.charAt(i);
                if (oldC1 != newC1) {
                    if (i == 0) {
                        nums[0] = "";
                    } else {
                        nums[0] = new_Num.substring(0, i);
                    }
                    nums[1] = oldNum.substring(i);
                    nums[2] = new_Num.substring(i);
                    break;
                }
            }

        }
    }

    public void setCountPraise(int countPraise) {
        this.countPraise = countPraise;
        postInvalidate();
    }

    public void setPraiseState(boolean praiseState) {
        this.praiseState = praiseState;
    }

    public void setOffsetNum(float offsetNum) {

        this.mOldOffsettNum = offsetNum;//变大是从[0,1]，变小是[0,-1]
        if (praiseState) {//从下到上[-1,0]
            this.mNewOffsettNum = offsetNum - this.offsetNum;
        } else {//从上到下[1,0]
            this.mNewOffsettNum = this.offsetNum + offsetNum;
        }

        postInvalidate();
    }

    public void setScaleUp(float scaleUp) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleUp, scaleUp);
        praiseBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_selected);
        praiseBitmap = Bitmap.createBitmap(praiseBitmap, 0, 0, praiseBitmap.getWidth(), praiseBitmap.getHeight(),
                matrix, true);

        bgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_messages_like_selected_shining);
        bgBitmap = Bitmap.createBitmap(bgBitmap, 0, 0, bgBitmap.getWidth(), bgBitmap.getHeight(),
                matrix, true);
        postInvalidate();
    }

    public void setScaleDown(float scaleDown) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleDown, scaleDown);
        normalBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_messages_like_unselected);
        normalBitmap = Bitmap.createBitmap(normalBitmap,0,0,normalBitmap.getWidth(),normalBitmap.getHeight(),matrix,true);
        postInvalidate();
    }


}
