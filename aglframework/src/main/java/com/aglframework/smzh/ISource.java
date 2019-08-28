package com.aglframework.smzh;

public interface ISource {

    IFilter.Frame createFrame();

    void destroy();

    int getWidth();

    int getHeight();
}
