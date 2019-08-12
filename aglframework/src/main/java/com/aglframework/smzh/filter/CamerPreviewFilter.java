package com.aglframework.smzh.filter;

import android.content.Context;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.aglframework.smzh.AGLFilter;
import com.aglframework.smzh.aglframework.R;

import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glGetUniformLocation;

public class CamerPreviewFilter extends AGLFilter {

    private int u_matrix;
    private float[] matrix;

    public CamerPreviewFilter(Context context) {
        super(context,R.raw.camera_prev_v, R.raw.camera_prev_f);
    }

    @Override
    public void onInit() {
        u_matrix = glGetUniformLocation(programId, "u_Matrix");
    }

    @Override
    protected void onDrawArraysPre(Frame frame) {
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
