package com.aglframework.smzh.agl_framework.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.IntRange;

import com.aglframework.smzh.CombineFilter;
import com.aglframework.smzh.IFilter;
import com.aglframework.smzh.agl_framework.MyApplication;
import com.aglframework.smzh.agl_framework.R;
import com.aglframework.smzh.agl_framework.data.Filter;
import com.aglframework.smzh.agl_framework.listener.CameraView;
import com.aglframework.smzh.agl_framework.utils.FilterType;
import com.aglframework.smzh.agl_framework.utils.Utils;
import com.aglframework.smzh.camera.AGLCamera;
import com.aglframework.smzh.camera.CameraPreview;
import com.aglframework.smzh.filter.LookupFilter;
import com.aglframework.smzh.filter.SmoothFilter;
import com.aglframework.smzh.filter.StickerFilter;
import com.aglframework.smzh.filter.WhiteFilter;

import java.util.ArrayList;
import java.util.List;

public class CameraPresenter {

    private CameraPreview cameraPreview;
    private CameraView cameraView;
    private AGLCamera aglCamera;

    private List<IFilter> filters = new ArrayList<>();
    private int whiteLevel;
    private int smoothLevel;
    private boolean showSticker;

    private WhiteFilter whiteFilter;
    private SmoothFilter smoothFilter;
    private StickerFilter stickerFilter;
    private LookupFilter lookupFilter;


    public CameraPresenter(CameraPreview cameraPreview, CameraView cameraView) {
        this.cameraPreview = cameraPreview;
        this.cameraView = cameraView;
        this.aglCamera = new AGLCamera(cameraPreview, 720, 1280);
        whiteLevel = 50;
        smoothLevel = 50;
        updateFilter();
    }


    public void onResume() {
        aglCamera.open();
    }


    public void onPause() {
        aglCamera.close();
    }

    public void switchCamera() {
        aglCamera.switchCamera();
    }

    public void changeFilter(@FilterType.Type int type, @IntRange(from = 0, to = 100) int level) {
        switch (type) {
            case FilterType.Type.WIHTE:
                whiteLevel = level;
                break;
            case FilterType.Type.SMOOTH:
                smoothLevel = level;
                break;
            default:
                break;
        }
        updateFilter();
    }

    public void applyLookupFilter(Filter filter) {
        if (lookupFilter == null) {
            lookupFilter = new LookupFilter(MyApplication.context);
        }
        Bitmap bitmap = Utils.loadBitmapSync("newlookupfilter/" + filter.getName() + ".png");
        if (bitmap != null) {
            lookupFilter.setBitmap(bitmap);
            updateFilter();
        }
    }

    public void setIntensity(@IntRange(from = 0, to = 100) int intensity) {
        if (lookupFilter != null) {
            lookupFilter.setIntensity(intensity / 100f);
        }
    }

    public void showSticker(boolean showSticker) {
        this.showSticker = showSticker;
        updateFilter();
    }


    private void updateFilter() {
        filters.clear();
        if (whiteLevel > 0) {
            if (whiteFilter == null) {
                whiteFilter = new WhiteFilter(MyApplication.context);
            }
            whiteFilter.setWhiteLevel(whiteLevel / 100f);
            filters.add(whiteFilter);
        }

        if (smoothLevel > 0) {
            if (smoothFilter == null) {
                smoothFilter = new SmoothFilter(MyApplication.context);
            }
            smoothFilter.setSmoothLevel(smoothLevel / 100f);
            filters.add(smoothFilter);
        }

        if (lookupFilter != null) {
            filters.add(lookupFilter);
        }

        if (showSticker) {
            if (stickerFilter == null) {
                stickerFilter = new StickerFilter(MyApplication.context);
                stickerFilter.setBitmap(BitmapFactory.decodeResource(MyApplication.context.getResources(), R.drawable.sticker));
            }
            filters.add(stickerFilter);
        }

        cameraPreview.setFilter(CombineFilter.getCombineFilter(filters));
    }
}
