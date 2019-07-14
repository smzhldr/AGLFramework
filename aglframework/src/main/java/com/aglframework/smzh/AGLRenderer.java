package com.aglframework.smzh;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.aglframework.smzh.filter.RenderScreenFilter;

import java.util.LinkedList;
import java.util.Queue;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class AGLRenderer implements GLSurfaceView.Renderer {

    private ISource iSource;
    private RenderScreenFilter screenFilter;
    private final Queue<Runnable> runOnDraw;
    private final Queue<Runnable> runOnDrawEnd;
    private IFilter filter;
    private boolean disable;

    AGLRenderer() {
        screenFilter = new RenderScreenFilter();
        runOnDraw = new LinkedList<>();
        runOnDrawEnd = new LinkedList<>();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (iSource != null) {
            iSource.onSizeChange();
        }
    }


    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        runAll(runOnDraw);

        if (iSource != null) {
            IFilter.Frame frame = iSource.createFrame();

            if (filter != null && !disable) {
                frame = filter.draw(frame);
            }

            if (screenFilter != null) {
                screenFilter.draw(frame);
            }
        }

        runAll(runOnDrawEnd);
    }

    void setSource(ISource iSource) {
        this.iSource = iSource;
        this.iSource.onSizeChange();
    }

    void clear() {
        if (iSource != null) {
            iSource.destroy();
            iSource = null;
        }

        if (screenFilter != null) {
            screenFilter.destroy();
        }

    }

    void setFilter(final IFilter filter) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                final IFilter oldFilter = AGLRenderer.this.filter;
                AGLRenderer.this.filter = filter;
                if (oldFilter != null) {
                    oldFilter.destroy();
                }
            }
        });
    }

    private void runAll(Queue<Runnable> queue) {
        synchronized (queue) {
            while (!queue.isEmpty()) {
                queue.poll().run();
            }
        }
    }

    private void runOnDraw(final Runnable runnable) {
        synchronized (runOnDraw) {
            runOnDraw.add(runnable);
        }
    }

    protected void runOnDrawEnd(final Runnable runnable) {
        synchronized (runOnDrawEnd) {
            runOnDrawEnd.add(runnable);
        }
    }

    void setDisable(boolean disable){
        this.disable = disable;
    }

    public ISource getSource() {
        return iSource;
    }
}

