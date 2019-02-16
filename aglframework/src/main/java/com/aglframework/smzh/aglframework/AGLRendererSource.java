package com.aglframework.smzh.aglframework;

public interface AGLRendererSource {

    Frame createFrame();

    void onSizeChange(int width, int height);

    void destroy();

    int getWidth();

    int getHeight();
}
