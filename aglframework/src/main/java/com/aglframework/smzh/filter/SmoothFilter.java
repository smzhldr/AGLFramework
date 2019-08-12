package com.aglframework.smzh.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.aglframework.smzh.AGLFilter;
import com.aglframework.smzh.aglframework.R;

public class SmoothFilter extends AGLFilter {

    private int glUniformsample;
    private int glUniformLevel;
    private float smoothLevel;

    public SmoothFilter(Context context) {
        super(context,R.raw.smooth_f);
    }

    @Override
    protected void onInit() {
        glUniformLevel = GLES20.glGetUniformLocation(programId, "intensity");
        glUniformsample = GLES20.glGetUniformLocation(programId, "singleStepOffset");
    }


    @Override
    protected void onDrawArraysPre(Frame frame) {
        GLES20.glUniform1f(glUniformLevel, smoothLevel);
        GLES20.glUniform2fv(glUniformsample, 1, new float[]{1.0f / frame.getTextureWidth(), 1.0f / frame.getTextureHeight()}, 0);
    }


    public void setSmoothLevel(float level) {
        this.smoothLevel = level;
    }
}
