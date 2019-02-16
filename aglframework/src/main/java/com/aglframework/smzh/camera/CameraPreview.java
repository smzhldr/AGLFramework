package com.aglframework.smzh.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.aglframework.smzh.AGLView;


public class CameraPreview extends AGLView {

    // For scaling
    private ScaleGestureDetector scaleDetector;

    // For focus
    private boolean isFocus;

    public CameraPreview(Context context) {
        super(context);
        init(context);
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleDetector.onTouchEvent(event);

        final int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                isFocus = true;
                break;
            }
            case MotionEvent.ACTION_UP: {

                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN: {
                isFocus = false;
                break;
            }
            case MotionEvent.ACTION_CANCEL: {
                isFocus = false;
                break;
            }
        }

        return true;
    }



    private void handleScale(float scaleFactor) {

    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            handleScale(detector.getScaleFactor());
            return true;
        }
    }
}
