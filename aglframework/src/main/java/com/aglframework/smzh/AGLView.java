package com.aglframework.smzh;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Size;


import com.aglframework.smzh.filter.AGLBaseFilter;

import java.util.concurrent.Semaphore;

public class AGLView extends GLSurfaceView {

    private AGLRenderer renderer;
    private AGLBaseFilter filter;
    public Size forceSize = null;

    public AGLView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
    }

    public AGLView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setEGLContextClientVersion(2);
        init();
    }

    public void init() {
        if (renderer == null) {
            renderer = new AGLRenderer();
            setEGLConfigChooser(8, 8, 8, 8, 16, 0);
            getHolder().setFormat(PixelFormat.RGBA_8888);
            setRenderer(renderer);
            setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }
    }

    public void setRendererSource(AGLRendererSource rendererSource) {
        renderer.setRendererSource(rendererSource);
        requestRender();
    }

    public void clear() {
        renderer.clear();
        requestRender();
    }

    public void setFilter(final AGLBaseFilter filter) {
        this.filter = filter;
        renderer.setFilter(this.filter);
        requestRender();
    }

    public void setDisabled(boolean disable) {
        renderer.setDisable(disable);
        requestRender();
    }

    public Bitmap capture(final int width, final int height) throws InterruptedException {

        final Semaphore waiter = new Semaphore(0);
        renderer.setSaveFilteredBitmapWaiter(waiter);
        requestRender();

        try {
            waiter.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return renderer.getFilteredBitmap();

    }

    public int getImageWidth() {
        if (renderer.getRendererSource() != null) {
            return renderer.getRendererSource().getWidth();
        } else {
            return 0;
        }
    }

    public int getImageHeight() {
        if (renderer.getRendererSource() != null) {
            return renderer.getRendererSource().getHeight();
        } else {
            return 0;
        }
    }
}
