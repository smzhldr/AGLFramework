package com.aglframework.smzh.filter;

public abstract class AGLBaseFilter {

    abstract public Frame draw(Frame frame);

    abstract public void destroy();

    public static class Frame {
        private int textureId;
        private int textureWidth;
        private int textureHeight;

        public Frame(int textureId,int textureWidth,int textureHeight){
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
    }

}
