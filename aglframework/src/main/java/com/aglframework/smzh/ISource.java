package com.aglframework.smzh;

public interface ISource {

    IFilter.Frame createFrame();

    void onSizeChange();

    void destroy();

    int getWidth();

    int getHeight();
}
