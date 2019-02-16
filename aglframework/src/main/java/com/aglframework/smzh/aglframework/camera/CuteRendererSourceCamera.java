package com.aglframework.smzh.aglframework.camera;

import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.hardware.Camera;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;

import com.aglframework.smzh.aglframework.AGLRendererSource;
import com.aglframework.smzh.aglframework.Frame;
import com.aglframework.smzh.aglframework.OpenGlUtils;
import com.aglframework.smzh.aglframework.filter.AGLCameraPreviewFilter;


import javax.microedition.khronos.opengles.GL10;

public class CuteRendererSourceCamera implements AGLRendererSource {

    private OnFrameAvailableListener onFrameAvailableListener;
    private AGLCamera cuteCamera;
    private SurfaceTexture surfaceTexture;
    private AGLCameraPreviewFilter aglCameraPreviewFilter;
    private Frame frame;
    private int width;
    private int height;

    public CuteRendererSourceCamera(AGLCamera cuteCamera, OnFrameAvailableListener availableListener) {
        this.onFrameAvailableListener = availableListener;
        this.cuteCamera = cuteCamera;
        this.aglCameraPreviewFilter = new AGLCameraPreviewFilter();
    }

    @Override
    public Frame createFrame() {
        if (surfaceTexture == null) {
            int textureId = createOESTexture();
            surfaceTexture = new SurfaceTexture(textureId);
            if (onFrameAvailableListener != null) {
                surfaceTexture.setOnFrameAvailableListener(onFrameAvailableListener);
                cuteCamera.startPreview(surfaceTexture);
            }
            Camera.Size size = cuteCamera.getParameter().getPreviewSize();
            frame = new Frame(textureId, size.height, size.width);
        }
        try {
            surfaceTexture.updateTexImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return aglCameraPreviewFilter.draw(frame);
    }


    @Override
    public void onSizeChange(int width, int height) {
        this.width = width;
        this.height = height;
        float[] matrix = new float[16];
        Camera.Size size = cuteCamera.getParameter().getPreviewSize();

        OpenGlUtils.getShowMatrix(matrix, size.height, size.width, width, height);
        if (cuteCamera.getCameraId() == Camera.CameraInfo.CAMERA_FACING_FRONT) {
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
