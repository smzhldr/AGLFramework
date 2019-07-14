package com.aglframework.smzh;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class AGLView extends GLSurfaceView {

    private AGLRenderer renderer;

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

    public void setRendererSource(ISource rendererSource) {
        renderer.setSource(rendererSource);
        requestRender();
    }

    public void clear() {
        renderer.clear();
        requestRender();
    }

    public void setFilter(final IFilter filter) {
        renderer.setFilter(filter);
        requestRender();
    }

    public void setDisabled(boolean disable) {
        renderer.setDisable(disable);
        requestRender();
    }

    public int getImageWidth() {
        if (renderer.getSource() != null) {
            return renderer.getSource().getWidth();
        } else {
            return 0;
        }
    }

    public int getImageHeight() {
        if (renderer.getSource() != null) {
            return renderer.getSource().getHeight();
        } else {
            return 0;
        }
    }
}
