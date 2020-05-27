package com.aglframework.smzh.egl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.concurrent.LinkedBlockingQueue;

public class GLView extends SurfaceView implements SurfaceHolder.Callback {

    private GLThread glThread;

    public GLView(Context context) {
        super(context);
        init();
    }

    public GLView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        glThread = new GLThread();
        glThread.start();
        getHolder().addCallback(this);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        glThread.setHolder(holder);
        glThread.setSize(width, height);
        glThread.addMessage(GLThread.MSG_CREATE);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        glThread.addMessage(GLThread.MSG_DESTROY);
    }

    public void setRenderer(GLSurfaceView.Renderer renderer) {
        glThread.setRenderer(renderer);
    }

    public void setEGLContextClientVersion(int glVersion) {

    }

    public void setEGLConfigChooser(int r, int g, int b, int a, int depth, int steciel) {

    }

    public void setRenderMode(int mode) {

    }

    public void requestRender() {
        glThread.addMessage(GLThread.MSG_DRAW);
    }


    static class GLThread extends Thread {

        static final int MSG_CREATE = 1;
        static final int MSG_DRAW = 2;
        static final int MSG_DESTROY = 3;

        private EglHelper eglHelper;
        private SurfaceHolder holder;
        private int width, height;
        private GLSurfaceView.Renderer renderer;
        private boolean isRunning = true;
        private LinkedBlockingQueue<Integer> msgQueue = new LinkedBlockingQueue<>(10);

        void setSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        void setHolder(SurfaceHolder holder) {
            this.holder = holder;
        }

        void setRenderer(GLSurfaceView.Renderer renderer) {
            this.renderer = renderer;
        }

        void addMessage(int msg) {
            synchronized (this) {
                msgQueue.offer(msg);
                this.notifyAll();
            }
        }

        @Override
        public void run() {
            while (isRunning) {

                synchronized (this) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                if (msgQueue.peek() == null || renderer == null) {
                    continue;
                }

                int msg = msgQueue.poll();

                switch (msg) {
                    case MSG_CREATE:
                        if (eglHelper == null) {
                            eglHelper = new EglHelper();
                            eglHelper.createGL(holder.getSurface());
                            renderer.onSurfaceCreated(null, null);
                            renderer.onSurfaceChanged(null, width, height);
                            renderer.onDrawFrame(null);
                        }
                        break;
                    case MSG_DRAW:
                        if (eglHelper != null) {
                            renderer.onDrawFrame(null);
                        }
                        break;
                    case MSG_DESTROY:
                        if (eglHelper != null) {
                            eglHelper.destroyGL();
                            eglHelper = null;
                            width = 0;
                            height = 0;
                        }
                        break;
                    default:
                        break;
                }

                if (eglHelper != null) {
                    eglHelper.swapBuffer();
                }
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        glThread.isRunning = false;
    }
}
