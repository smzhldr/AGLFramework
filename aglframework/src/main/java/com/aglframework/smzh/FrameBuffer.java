package com.aglframework.smzh;

public class FrameBuffer {

    public int frameBuffer;
    public int texture;
    public int textureWidth;
    public int textureHeight;
    public int useCount;

    public FrameBuffer(int frameBuffer, int texture, int textureWidth, int textureHeight) {
        this.frameBuffer = frameBuffer;
        this.texture = texture;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

}
