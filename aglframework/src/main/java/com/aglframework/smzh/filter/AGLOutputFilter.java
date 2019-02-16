package com.aglframework.smzh.filter;

import android.opengl.GLES20;

import com.aglframework.smzh.OpenGlUtils;

public class AGLOutputFilter extends AGLFilter {

    public AGLOutputFilter(){
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
