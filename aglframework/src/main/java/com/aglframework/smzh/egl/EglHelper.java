package com.aglframework.smzh.egl;

import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.view.Surface;

public class EglHelper {

    private EGLDisplay eglDisplay = EGL14.EGL_NO_DISPLAY;
    private EGLContext eglContext = EGL14.EGL_NO_CONTEXT;
    private EGLSurface eglSurface = EGL14.EGL_NO_SURFACE;

    public void createGL(Surface surface) {

        eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);

        int[] version = new int[2];
        if (!EGL14.eglInitialize(eglDisplay, version, 0, version, 1)) {
            throw new RuntimeException("EGL error " + EGL14.eglGetError());
        }
    
        int[] configAttribs = {
                EGL14.EGL_BUFFER_SIZE, 32,
                EGL14.EGL_ALPHA_SIZE, 8,
                EGL14.EGL_BLUE_SIZE, 8,
                EGL14.EGL_GREEN_SIZE, 8,
                EGL14.EGL_RED_SIZE, 8,
                EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
                EGL14.EGL_SURFACE_TYPE, EGL14.EGL_WINDOW_BIT,
                EGL14.EGL_NONE
        };
        int[] numConfigs = new int[1];
        EGLConfig[] configs = new EGLConfig[1];
        if (!EGL14.eglChooseConfig(eglDisplay, configAttribs, 0, configs, 0, configs.length, numConfigs, 0)) {
            throw new RuntimeException("EGL error " + EGL14.eglGetError());
        }
        EGLConfig eglConfig = configs[0];

        int[] contextAttribs = {
                EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                EGL14.EGL_NONE
        };
        eglContext = EGL14.eglCreateContext(eglDisplay, eglConfig, EGL14.EGL_NO_CONTEXT, contextAttribs, 0);
        if (eglContext == EGL14.EGL_NO_CONTEXT) {
            throw new RuntimeException("EGL error " + EGL14.eglGetError());
        }


        final int[] surfaceAttribs = {EGL14.EGL_NONE};
        eglSurface = EGL14.eglCreateWindowSurface(eglDisplay, eglConfig, surface, surfaceAttribs, 0);

        if (!EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)){
            throw new RuntimeException("eglMakeCurrent fail");
        }
    }


    public void destroyGL() {
        EGL14.eglMakeCurrent(eglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
        EGL14.eglDestroyContext(eglDisplay, eglContext);
        EGL14.eglDestroySurface(eglDisplay, eglSurface);
        eglContext = EGL14.EGL_NO_CONTEXT;
        eglDisplay = EGL14.EGL_NO_DISPLAY;
    }

    public void swapBuffer() {
        EGL14.eglSwapBuffers(eglDisplay, eglSurface);
    }

    public EGLContext getEglContext() {
        return eglContext;
    }
}
