package com.aglframework.smzh.aglframework.filter;

import com.aglframework.smzh.aglframework.Frame;

public interface AGLBaseFilter {

    Frame draw(Frame frame);

    void destroy();
}
