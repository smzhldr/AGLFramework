package com.aglframework.smzh.filter;

import android.opengl.GLES20;

import com.aglframework.smzh.AGLFilter;
import com.aglframework.smzh.OpenGlUtils;

public class RenderScreenFilter extends AGLFilter {

    public RenderScreenFilter(){
        setNeedRendererOnScreen(true);
    }

    @Override
    public void onInit() {
        programId = OpenGlUtils.loadProgram(NO_FILTER_VERTEX_SHADER, NO_FILTER_FRAGMENT_SHADER);
        glAttrPosition = GLES20.glGetAttribLocation(programId, "position");
        glAttrTextureCoordinate = GLES20.glGetAttribLocation(programId, "inputTextureCoordinate");
        glUniformTexture = GLES20.glGetUniformLocation(programId, "inputImageTexture");
    }

}
