package com.aglframework.smzh;

import com.aglframework.smzh.IFilter.Frame;

public interface ISource {

    Frame createFrame();

    void onSizeChange();

    void destroy();

    int getWidth();

    int getHeight();
}
