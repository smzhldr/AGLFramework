package com.aglframework.smzh;

import com.aglframework.smzh.filter.AGLBaseFilter.Frame;

public interface AGLRendererSource {

    Frame createFrame();

    void onSizeChange();

    void destroy();

    int getWidth();

    int getHeight();
}
