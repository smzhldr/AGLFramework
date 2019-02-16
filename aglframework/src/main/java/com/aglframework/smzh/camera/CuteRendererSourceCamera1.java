package com.aglframework.smzh.camera;

import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.hardware.Camera;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.aglframework.smzh.AGLRendererSource;
import com.aglframework.smzh.OpenGlUtils;
import com.aglframework.smzh.filter.AGLBaseFilter;
import com.aglframework.smzh.filter.AGLBaseFilter.*;
import com.aglframework.smzh.filter.AGLCameraPreviewFilter;


import javax.microedition.khronos.opengles.GL10;

@SuppressWarnings("deprecation")
public class CuteRendererSourceCamera1 implements AGLRendererSource {

    private OnFrameAvailableListener onFrameAvailableListener;
    private AGLCamera1 aglCamera1;
    private SurfaceTexture surfaceTexture;
    private AGLCameraPreviewFilter aglCameraPreviewFilter;
    private Frame frame;
    private int width;
    private int height;

    public CuteRendererSourceCamera1(AGLCamera1 cuteCamera, OnFrameAvailableListener availableListener) {
        this.onFrameAvailableListener = availableListener;
        this.aglCamera1 = cuteCamera;
        this.aglCameraPreviewFilter = new AGLCameraPreviewFilter();
        Camera.Size size = cuteCamera.getParameter().getPreviewSize();
        this.width = size.height;
        this.height = size.width;
    }

    @Override
    public AGLBaseFilter.Frame createFrame() {
        if (surfaceTexture == null) {
            int textureId = createOESTexture();
            surfaceTexture = new SurfaceTexture(textureId);
            if (onFrameAvailableListener != null) {
                surfaceTexture.setOnFrameAvailableListener(onFrameAvailableListener);
                aglCamera1.startPreview(surfaceTexture);
            }
            frame = new AGLBaseFilter.Frame(textureId, width, height);
        }
        try {
            surfaceTexture.updateTexImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aglCameraPreviewFilter.draw(frame);
    }


    @Override
    public void onSizeChange() {
        float[] matrix = new float[16];
        OpenGlUtils.getShowMatrix(matrix, width, height, width, height);
        if (aglCamera1.getCameraId() == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            OpenGlUtils.rotate(matrix, 90);
        } else {
            OpenGlUtils.flip(matrix, true, false);
            OpenGlUtils.rotate(matrix, 270);
        }
        aglCameraPreviewFilter.setMatrix(matrix);
    }

    private int createOESTexture() {
        int[] tex = new int[1];
        GLES20.glGenTextures(1, tex, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, tex[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, 0);
        return tex[0];
    }

    @Override
    public void destroy() {
        aglCameraPreviewFilter.destroy();
    }


    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
