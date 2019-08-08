package com.aglframework.smzh.filter;

import android.content.Context;
import android.opengl.GLES20;

import com.aglframework.smzh.AGLFilter;
import com.aglframework.smzh.OpenGlUtils;
import com.aglframework.smzh.aglframework.R;

public class RenderScreenFilter extends AGLFilter {

    public RenderScreenFilter(Context context){
        super(context);
        setNeedRendererOnScreen(true);
    }

    @Override
    public void onInit() {
        programId = OpenGlUtils.loadProgram(context, R.raw.single_input_v,R.raw.full_screen_f);
        glAttrPosition = GLES20.glGetAttribLocation(programId, "position");
        glAttrTextureCoordinate = GLES20.glGetAttribLocation(programId, "inputTextureCoordinate");
        glUniformTexture = GLES20.glGetUniformLocation(programId, "inputImageTexture");
    }

}
