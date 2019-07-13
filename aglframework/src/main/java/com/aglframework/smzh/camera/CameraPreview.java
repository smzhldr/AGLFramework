package com.aglframework.smzh.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.aglframework.smzh.AGLView;


public class CameraPreview extends AGLView {

    //定制view特性，如触摸等

    public CameraPreview(Context context) {
        super(context);
    }

    public CameraPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

}
