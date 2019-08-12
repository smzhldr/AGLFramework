package com.aglframework.smzh.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.aglframework.smzh.AGLFilter;
import com.aglframework.smzh.aglframework.R;

public class WhiteFilter extends AGLFilter {


    private int glUniformLevel;

    private float level;


    public WhiteFilter(Context context) {
        super(context,R.raw.light_f);
    }

    @Override
    protected void onInit() {
        glUniformLevel = GLES20.glGetUniformLocation(programId, "level");
    }

    @Override
    protected void onDrawArraysPre(Frame frame) {
        GLES20.glUniform1f(glUniformLevel, level);
    }

    public void setWhiteLevel(float level) {
        this.level = level;
    }
}
