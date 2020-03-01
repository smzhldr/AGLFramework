package com.aglframework.smzh.agl_framework.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.aglframework.smzh.agl_framework.R;

public class ShutterButton extends View {

    private float radius = 100f;
    private Paint paint;

    public ShutterButton(Context context) {
        super(context);
    }

    public ShutterButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.shutter_button);
        radius = typedArray.getDimension(R.styleable.shutter_button_radius, 50);
        float stroke = typedArray.getDimension(R.styleable.shutter_button_stroke, 10);
        int color = typedArray.getInt(R.styleable.shutter_button_color, Color.WHITE);
        typedArray.recycle();
        radius = radius - stroke;

        paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(stroke);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, radius, paint);
    }

    public void scaleWithAnim(boolean shrink) {

        float scaleValue;
        float translateValue;
        if(shrink){
            scaleValue = 0.8f;
            translateValue = 0.5f;
        } else  {
            scaleValue = 1f;
             translateValue = 0f;
        }

        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                0,
                Animation.RELATIVE_TO_SELF,
                0,
                Animation.RELATIVE_TO_SELF,
                0,
                Animation.RELATIVE_TO_SELF,
                translateValue);
        translateAnimation.setDuration(200);
        translateAnimation.setFillAfter(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(1f,
                scaleValue,
                1f,
                scaleValue,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);
        scaleAnimation.setDuration(200);
        scaleAnimation.setFillAfter(true);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(translateAnimation);
        animationSet.addAnimation(scaleAnimation);
        animationSet.setDuration(200);
        animationSet.setFillAfter(true);

        if(shrink) {
            animationSet.setInterpolator(new DecelerateInterpolator());
        } else {
            animationSet.setInterpolator(new AccelerateInterpolator());
        }
        startAnimation(animationSet);
    }
}
