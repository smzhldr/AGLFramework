package com.aglframework.smzh;

import android.opengl.GLES20;

public interface IFilter {

    Frame draw(Frame frameBuffer);

    void destroy();


    class FrameBufferProvider {

        static Frame createFrameBuffers(int textureWidth, int textureHeight) {
            int[] frameBuffer = new int[1];
            int[] frameBufferTexture = new int[1];

            GLES20.glGenFramebuffers(1, frameBuffer, 0);
            GLES20.glGenTextures(1, frameBufferTexture, 0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, frameBufferTexture[0]);
            GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, textureWidth, textureHeight, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);

            GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, frameBuffer[0]);
            GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, frameBufferTexture[0], 0);
            return new Frame(frameBuffer[0], frameBufferTexture[0], textureWidth, textureHeight);
        }


        static void destroyFrameBuffers(Frame frameBuffer) {
            if (frameBuffer == null) {
                return;
            }
            int[] frameBufferTextures = {frameBuffer.textureId};
            GLES20.glDeleteTextures(frameBufferTextures.length, frameBufferTextures, 0);

            int[] frameBuffers = {frameBuffer.frameBuffer};
            GLES20.glDeleteFramebuffers(frameBuffers.length, frameBuffers, 0);
        }
    }

    class Frame {

        private int frameBuffer;
        private int textureId;
        private int textureWidth;
        private int textureHeight;

        public Frame(int frameBuffer, int textureId, int textureWidth, int textureHeight) {
            this.frameBuffer = frameBuffer;
            this.textureId = textureId;
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
        }

        public int getTextureId() {
            return textureId;
        }

        public int getTextureWidth() {
            return textureWidth;
        }

        public int getTextureHeight() {
            return textureHeight;
        }

        public int getFrameBuffer() {
            return frameBuffer;
        }
    }
}
