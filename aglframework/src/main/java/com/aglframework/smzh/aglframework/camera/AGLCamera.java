package com.aglframework.smzh.aglframework.camera;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;

import com.aglframework.smzh.aglframework.AGLView;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class AGLCamera {

    private Camera camera;
    private int cameraId;
    private AGLView cuteImageView;
    private int previewWidth;
    private int previewHeight;


    public AGLCamera(AGLView cuteImageView, int width, int height) {
        this.cuteImageView = cuteImageView;
        this.previewWidth = width;
        this.previewHeight = height;
        if (Camera.getNumberOfCameras() > 1) {
            cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }

    }

    public void open() {
        if (camera == null) {
            camera = Camera.open(cameraId);
            Camera.Parameters parameters = camera.getParameters();
            List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();

            int width = previewHeight;
            int height = previewWidth;
            if (sizeList.size() > 1) {
                Iterator<Camera.Size> iterator = sizeList.iterator();
                while (iterator.hasNext()) {
                    Camera.Size cur = iterator.next();
                    if (cur.width >= width && cur.height >= height) {
                        width = cur.width;
                        height = cur.height;
                        break;
                    }
                }
            }
            parameters.setPreviewSize(width, height);
            if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
            camera.setParameters(parameters);
            camera.autoFocus(new Camera.AutoFocusCallback() {
                @Override
                public void onAutoFocus(boolean success, Camera camera) {

                }
            });
        }
        cuteImageView.setRendererSource(new CuteRendererSourceCamera(this, new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
                cuteImageView.requestRender();
            }
        }));
    }

    public void close() {
        camera.stopPreview();
        camera.release();
        camera = null;
        cuteImageView.clear();

    }

    public void switchCamera() {
        cameraId = (cameraId + 1) % 2;
        close();
        open();
    }


    public void startPreview(SurfaceTexture surfaceTexture) {
        try {
            camera.setPreviewTexture(surfaceTexture);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Camera.Parameters getParameter() {
        return camera.getParameters();
    }


    public int getCameraId() {
        return cameraId;
    }
}
