package com.aglframework.smzh.camera;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.aglframework.smzh.AGLView;


public class CameraPreview extends AGLView implements View.OnTouchListener {

    //定制view特性，如触摸等

    private GestureListener gestureListener;
    private PointF downPosition;

    public CameraPreview(Context context) {
        super(context);
        initialize();
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {
        downPosition = new PointF();
    }

    public void setGestureListener(GestureListener gestureListener) {
        this.gestureListener = gestureListener;
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downPosition.x = event.getX();
                downPosition.y = event.getY();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if(gestureListener != null && Math.abs(event.getX() - downPosition.x) < 20 && Math.abs(event.getY() - downPosition.y) < 20){
                   gestureListener.onClick();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (gestureListener != null) {
                    if (event.getX() - downPosition.x > 100) {
                        gestureListener.rightSlide();
                    } else if (event.getX() - downPosition.x < -100) {
                        gestureListener.leftSlide();
                    } else if (event.getY() - downPosition.y > 100) {
                        gestureListener.bottomSlide();
                    } else if (event.getY() - downPosition.y < -100) {
                        gestureListener.topSlide();
                    }
                }
                break;
        }
        return false;
    }

    public interface GestureListener {
        void leftSlide();

        void rightSlide();

        void topSlide();

        void bottomSlide();

        void zoom(float distance);

        void onClick();
    }
}
