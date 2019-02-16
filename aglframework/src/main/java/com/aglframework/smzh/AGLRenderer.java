package com.aglframework.smzh;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import com.aglframework.smzh.filter.AGLBaseFilter;
import com.aglframework.smzh.filter.AGLOutputFilter;

import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class AGLRenderer implements GLSurfaceView.Renderer {

    private AGLRendererSource rendererSource;
    private AGLOutputFilter outputFilter;
    private final Queue<Runnable> runOnDraw;
    private final Queue<Runnable> runOnDrawEnd;
    private AGLBaseFilter filter;
    private boolean disable;
    private Semaphore saveFilteredBitmapWaiter;
    private Bitmap filteredBitmap;


    AGLRenderer() {
        outputFilter = new AGLOutputFilter();
        runOnDraw = new LinkedList<>();
        runOnDrawEnd = new LinkedList<>();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        if (rendererSource != null) {
            rendererSource.onSizeChange();
        }
    }


    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        runAll(runOnDraw);

        if (rendererSource != null) {
            AGLBaseFilter.Frame frame = rendererSource.createFrame();

            if (filter != null && !disable) {
                frame = filter.draw(frame);
            }

            if (saveFilteredBitmapWaiter != null) {
                saveFilteredBitmap(frame);
                saveFilteredBitmapWaiter.release();
                saveFilteredBitmapWaiter = null;
            }

            if (outputFilter != null) {
                outputFilter.draw(frame);
            }
        }

        runAll(runOnDrawEnd);
    }

    void setRendererSource(AGLRendererSource rendererSource) {
        this.rendererSource = rendererSource;
        this.rendererSource.onSizeChange();
    }

    void clear() {
        if (rendererSource != null) {
            rendererSource.destroy();
            rendererSource = null;
        }

        if (outputFilter != null) {
            outputFilter.destroy();
        }

    }

    void setFilter(final AGLBaseFilter filter) {
        runOnDraw(new Runnable() {
            @Override
            public void run() {
                final AGLBaseFilter oldFilter = AGLRenderer.this.filter;
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

    void setSaveFilteredBitmapWaiter(Semaphore saveFilteredBitmapWaiter) {
        this.saveFilteredBitmapWaiter = saveFilteredBitmapWaiter;
    }

    private void saveFilteredBitmap(AGLBaseFilter.Frame frame) {
        int[] frameBuffers = new int[1];

        GLES20.glGenFramebuffers(1, frameBuffers, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffers[0]);
        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
                GLES20.GL_TEXTURE_2D, frame.getTextureId(), 0);

        final int bitmapBuffer[] = new int[frame.getTextureWidth() * frame.getTextureHeight()];
        IntBuffer filteredBitmapBuffer = IntBuffer.wrap(bitmapBuffer);
        filteredBitmapBuffer.position(0);
        GLES20.glReadPixels(0, 0, frame.getTextureWidth(), frame.getTextureHeight(), GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, filteredBitmapBuffer);

        filteredBitmap = Bitmap.createBitmap(frame.getTextureWidth(), frame.getTextureHeight(), Bitmap.Config.ARGB_8888);
        filteredBitmap.copyPixelsFromBuffer(filteredBitmapBuffer);

        GLES20.glDeleteFramebuffers(frameBuffers.length, frameBuffers, 0);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    Bitmap getFilteredBitmap() {
        return filteredBitmap;
    }


    public AGLRendererSource getRendererSource() {
        return rendererSource;
    }
}

