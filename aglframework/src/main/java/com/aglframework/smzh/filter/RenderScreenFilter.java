package com.aglframework.smzh.filter;

import android.content.Context;

import com.aglframework.smzh.AGLFilter;

public class RenderScreenFilter extends AGLFilter {

    public RenderScreenFilter(Context context){
        super(context);
        setNeedRendererOnScreen(true);
    }
}
