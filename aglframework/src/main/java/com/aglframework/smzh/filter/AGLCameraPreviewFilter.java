package com.aglframework.smzh.filter;

import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.aglframework.smzh.OpenGlUtils;

import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetUniformLocation;

public class AGLCameraPreviewFilter extends AGLFilter {

    private static final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "uniform mat4 u_Matrix;" +
                    "attribute vec2 atextureCoordinate;" +
                    "varying vec2 aCoordinate;" +

                    "void main() {" +
                    "  gl_Position = u_Matrix * vPosition;" +
                    "  aCoordinate = atextureCoordinate;" +
                    "}";

    private static final String fragmentShaderCode =
            "#extension GL_OES_EGL_image_external : require\n" +
                    "precision mediump float;" +
                    "uniform samplerExternalOES uTexture;" +
                    "varying vec2 aCoordinate;" +
                    "void main() {" +
                    "gl_FragColor=texture2D(uTexture,aCoordinate);" +
                    "}";


    private int u_matrix;
    private float[] matrix;

    @Override
    public void onInit() {
        programId = OpenGlUtils.loadProgram(vertexShaderCode, fragmentShaderCode);
        glAttrPosition = GLES20.glGetAttribLocation(programId, "vPosition");
        glAttrTextureCoordinate = GLES20.glGetAttribLocation(programId, "atextureCoordinate");
        u_matrix = glGetUniformLocation(programId, "u_Matrix");
        glUniformTexture = GLES20.glGetUniformLocation(programId, "uTexture");
    }

    @Override
    protected void onDrawArraysPre(Frame frame) {
        super.onDrawArraysPre(frame);
        GLES20.glUniformMatrix4fv(u_matrix, 1, false, matrix, 0);
    }

    @Override
    protected void bindTexture(int textureId) {
        glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);
    }

    public void setMatrix(float[] matrix){
        this.matrix = matrix;
    }

}
