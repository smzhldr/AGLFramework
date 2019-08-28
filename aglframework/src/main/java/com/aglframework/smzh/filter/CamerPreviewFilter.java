package com.aglframework.smzh.filter;

import android.content.Context;
import android.opengl.GLES11Ext;

import com.aglframework.smzh.AGLFilter;
import com.aglframework.smzh.aglframework.R;

import static android.opengl.GLES20.glBindTexture;

public class CamerPreviewFilter extends AGLFilter {

    public CamerPreviewFilter(Context context) {
        super(context,R.raw.single_input_v, R.raw.camera_prev_f);
    }


    @Override
    protected void bindTexture(int textureId) {
        glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);
    }
}
