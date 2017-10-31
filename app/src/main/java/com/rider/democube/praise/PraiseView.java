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
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.rider.democube.R;

/**
 * Created by riderwu on 2017/10/26 0026.
 */

public class PraiseView extends View implements View.OnClickListener{


    private static final int DEFAULT_NUM = 100;
    private static final int DEFAULT_NORMAL_TEXTCOLOR = Color.parseColor("#FF9F9E9F");
    private static final float SCALE_MAX = 1.0f;
    private static final float SCALE_MIN = 0.5f;
    private static final float DISTANCE_MAX = 50;
    private static final float DISTANCE_MIN = 0;

    private int praiseNum = DEFAULT_NUM;
    private String[] nums = new String[]{"","",""};

    private Paint paint;
    private Paint bitmapPaint;

    private boolean praised = false;
    private int textSize = 30;
    private int startX;
    private int startY;

    private float mOldOffsetY;
    private float mNewOffsetY;
    private float aplhaSet;
    private Bitmap normalBitmap;
    private Bitmap praiseBitmap;
    private Bitmap circleBitmap;
    private boolean isThumbUp;


    public void setTextOffsetY(float offsetY) {
        aplhaSet = offsetY;
        if(praised){
            mOldOffsetY = - offsetY;
            mNewOffsetY = DISTANCE_MAX - offsetY;
        }else{
            mOldOffsetY = offsetY;
            mNewOffsetY = offsetY - DISTANCE_MAX;
        }

        postInvalidate();
    }


    public int getPraiseNum() {
        return praiseNum;
    }

    public void setPraiseNum(int praiseNum) {
        praised = praiseNum>this.praiseNum;
        calculateChangeNum(praiseNum);
//        showThumbUpAnim();
        this.praiseNum = praiseNum;
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
        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setOnClickListener(this);
        nums[0] = String.valueOf(praiseNum);

        normalBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_messages_like_unselected);
        praiseBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_messages_like_selected);
        circleBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_messages_like_selected_shining);
    }

    @Override
    public void onClick(View v) {
        praised = !praised;
        if(praised){
            praisetoUp();
        }else{
            praisetoDown();
        }
    }


    /**
     * 点赞
     */
    private void  praisetoUp(){
        calculateChangeNum(praiseNum+1);
        showUpAnim();
        praiseNum = praiseNum+1;
    }

    private void showUpAnim(){
        // 三个动画，默认 bitmap ， 点赞 bitmap，光圈 bitmap
        ObjectAnimator normalAnim = ObjectAnimator.ofFloat(this,"normalScale",SCALE_MAX,SCALE_MIN);
        normalAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isThumbUp = true;
            }
        });

        ObjectAnimator praiseAnim = ObjectAnimator.ofFloat(this,"praiseScale",SCALE_MIN,SCALE_MAX);
        praiseAnim.setInterpolator(new OvershootInterpolator());
        ObjectAnimator textAnim = ObjectAnimator.ofFloat(this,"textOffsetY",DISTANCE_MIN,DISTANCE_MAX);
        AnimatorSet anims = new AnimatorSet();
        anims.setDuration(250);

        anims.play(normalAnim).with(textAnim);
        anims.play(praiseAnim).after(normalAnim);
        anims.start();
    }

    private void setNormalScale(float scale){
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        normalBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_messages_like_unselected);
        normalBitmap = Bitmap.createBitmap(normalBitmap, 0, 0, normalBitmap.getWidth(), normalBitmap.getHeight(),
                matrix, true);
        postInvalidate();
    }

    /**
     * 取消点赞
     */
    private  void praisetoDown(){
        calculateChangeNum(praiseNum - 1);
        showDownAnim();
        praiseNum = praiseNum-1;
    }

    private void showDownAnim(){
        // 三个动画，默认 bitmap ， 点赞 bitmap，光圈 bitmap
        ObjectAnimator normalAnim = ObjectAnimator.ofFloat(this,"normalScale",SCALE_MIN,SCALE_MAX);
        normalAnim.setInterpolator(new OvershootInterpolator());
        ObjectAnimator praiseAnim = ObjectAnimator.ofFloat(this,"praiseScale",SCALE_MAX,SCALE_MIN);
        praiseAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isThumbUp = false;
            }
        });
        ObjectAnimator textAnim = ObjectAnimator.ofFloat(this,"textOffsetY",DISTANCE_MIN,DISTANCE_MAX);
        AnimatorSet anims = new AnimatorSet();
        anims.setDuration(250);
        anims.play(normalAnim).with(textAnim);
        anims.play(normalAnim).after(praiseAnim);
        anims.start();
    }

    private void setPraiseScale(float scale){
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        praiseBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_messages_like_selected);
        praiseBitmap = Bitmap.createBitmap(praiseBitmap, 0, 0, praiseBitmap.getWidth(), praiseBitmap.getHeight(),
                matrix, true);

        circleBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.ic_messages_like_selected_shining);
        circleBitmap = Bitmap.createBitmap(circleBitmap, 0, 0, circleBitmap.getWidth(), circleBitmap.getHeight(),
                matrix, true);
        postInvalidate();
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

        drawIcon(canvas);

        drawText(canvas);
    }


    private void drawIcon(Canvas canvas){
        if(isThumbUp){
            canvas.drawBitmap(praiseBitmap,startX - praiseBitmap.getWidth(),startY,bitmapPaint);
            canvas.drawBitmap(circleBitmap,startX - circleBitmap.getWidth(),startY-circleBitmap.getWidth(),bitmapPaint);
        }else{
            canvas.drawBitmap(normalBitmap,startX - normalBitmap.getWidth(),startY,bitmapPaint);
        }
    }

    private void drawText(Canvas canvas){
        paint.setTextSize(textSize);
//        paint.setColor(Color.YELLOW);
        String text = String.valueOf(1);
        float textWidth = paint.measureText(text) / text.length();
//        canvas.drawRect(0,0,getWidth(),getHeight(),paint);
        paint.setColor(Color.BLACK);
        canvas.drawText(nums[0],startX,startY,paint);
        paint.setAlpha(255 - (int)((255/DISTANCE_MAX)*aplhaSet));
        canvas.drawText(nums[1],startX + nums[0].length() * textWidth,startY + mOldOffsetY,paint);
        paint.setAlpha((int)((255/DISTANCE_MAX)*aplhaSet));
        canvas.drawText(nums[2],startX + nums[0].length() * textWidth,startY + mNewOffsetY,paint);
    }


    private void calculateChangeNum(int newNum){
        if(newNum == praiseNum){
            nums[0] = String.valueOf(praiseNum);
            nums[1] = "";
            nums[2] = "";
            return;
        }

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
