package com.aglframework.smzh.agl_framework.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.aglframework.smzh.agl_framework.MyApplication;

import java.io.IOException;
import java.io.InputStream;

public class Utils {


    public static Bitmap loadBitmapSync(String assetFileName) {
        try {
            InputStream inputStream = MyApplication.context.getAssets().open(assetFileName);
            return BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            return null;
        }
    }
}