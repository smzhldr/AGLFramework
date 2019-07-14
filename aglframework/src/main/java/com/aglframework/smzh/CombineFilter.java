package com.aglframework.smzh;

import java.util.ArrayList;
import java.util.List;

public class CombineFilter implements IFilter {

    private List<IFilter> filters = new ArrayList<>();

    public static IFilter getCombineFilter(List<IFilter> filters) {
        return new CombineFilter(filters);
    }


    private CombineFilter(List<IFilter> filters) {
        this.filters.addAll(filters);
    }

    @Override
    public Frame draw(Frame frame) {

        for (IFilter filter : filters) {
            frame = filter.draw(frame);
        }
        return frame;
    }

    @Override
    public void destroy() {
        for (IFilter filter : filters) {
            filter.destroy();
        }
    }
}
