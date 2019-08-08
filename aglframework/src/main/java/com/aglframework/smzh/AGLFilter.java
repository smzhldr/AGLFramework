package com.aglframework.smzh;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static android.opengl.GLES20.GL_TRIANGLE_STRIP;

public abstract class AGLFilter implements IFilter {

    protected Context context;
    protected int programId;
    protected FloatBuffer cubeBuffer = ByteBuffer.allocateDirect(8 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    protected FloatBuffer textureBuffer = ByteBuffer.allocateDirect(8 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    protected int glAttrPosition;
    protected int glAttrTextureCoordinate;
    protected int glUniformTexture;
    protected Frame frame;
    private int outputWidth;
    private int outputHeight;
    private boolean hasInit;
    private boolean isNeedRendererScreen;

    public static final String NO_FILTER_VERTEX_SHADER = "" +
            "attribute vec4 position;\n" +
            "attribute vec4 inputTextureCoordinate;\n" +
            " \n" +
            "varying vec2 textureCoordinate;\n" +
            " \n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = position;\n" +
            "    textureCoordinate = inputTextureCoordinate.xy;\n" +
            "}";
    @SuppressWarnings("WeakerAccess")
    public static final String NO_FILTER_FRAGMENT_SHADER = "" +
            "varying highp vec2 textureCoordinate;\n" +
            " \n" +
            "uniform sampler2D inputImageTexture;\n" +
            " \n" +
            "void main()\n" +
            "{\n" +
            "     gl_FragColor = vec4(texture2D(inputImageTexture, textureCoordinate).rgb, 1.0);\n" +
            "}";

    public static final float[] cube = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f,
    };
    public static float[] textureCords = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f
    };

    public AGLFilter(Context context) {
        this.context = context;
    }

    private void init() {
        if (!hasInit) {
            onInit();
            hasInit = true;
        }
    }

    protected abstract void onInit();

    public Frame draw(Frame frame) {
        init();

        if (programId <= 0) {
            return frame;
        }

        int textureWidth = frame.getTextureWidth();
        int textureHeight = frame.getTextureHeight();
        updateOutputSize(textureWidth, textureHeight);
        bindFrameBuffer();

        GLES20.glUseProgram(programId);
        cubeBuffer.clear();
        cubeBuffer.put(cube).position(0);
        GLES20.glVertexAttribPointer(glAttrPosition, 2, GLES20.GL_FLOAT, false, 0, cubeBuffer);
        GLES20.glEnableVertexAttribArray(glAttrPosition);

        textureBuffer.clear();
        textureBuffer.put(textureCords).position(0);
        GLES20.glVertexAttribPointer(glAttrTextureCoordinate, 2, GLES20.GL_FLOAT, false, 0, textureBuffer);
        GLES20.glEnableVertexAttribArray(glAttrTextureCoordinate);

        onDrawArraysPre(frame);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        bindTexture(frame.getTextureId());
        GLES20.glUniform1i(glUniformTexture, 0);

        GLES20.glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

        GLES20.glDisableVertexAttribArray(glAttrPosition);
        GLES20.glDisableVertexAttribArray(glAttrTextureCoordinate);

        bindTexture(0);
        if (!isNeedRendererScreen) {
            return AGLFilter.this.frame;
        } else {
            return null;
        }
    }

    protected void onDrawArraysPre(Frame frameBuffer) {

    }

    protected void bindTexture(int textureId) {
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
    }

    private void updateOutputSize(int width, int height) {
        if (width != outputWidth || height != outputHeight) {
            outputWidth = width;
            outputHeight = height;
            FrameBufferProvider.destroyFrameBuffers(frame);
            frame = FrameBufferProvider.createFrameBuffers(outputWidth, outputHeight);
        }
    }

    private void bindFrameBuffer() {
        if (isNeedRendererScreen) {
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        } else {
            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frame.getFrameBuffer());
        }
        GLES20.glViewport(0, 0, outputWidth, outputHeight);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    }

    public void setNeedRendererOnScreen(boolean isNeedRendererScreen) {
        this.isNeedRendererScreen = isNeedRendererScreen;
    }

    public void destroy() {
        hasInit = false;
        FrameBufferProvider.destroyFrameBuffers(frame);
        frame = null;
        outputWidth = 0;
        outputHeight = 0;
    }
}
