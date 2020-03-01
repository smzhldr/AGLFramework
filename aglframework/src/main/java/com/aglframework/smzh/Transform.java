package com.aglframework.smzh;

public class Transform {

    public static final float[] TEXTURE_NO_ROTATION = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
    };
    public static final float[] TEXTURE_ROTATED_90 = {
            0.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
    };
    public static final float[] TEXTURE_ROTATED_180 = {
            1.0f, 1.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
    };
    public static final float[] TEXTURE_ROTATED_270 = {
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            0.0f, 1.0f,
    };
    public static final float[] TEXTURE_FLIP_HORIZONTAL_AND_NO_ROTATION = {
            1.0f, 0.0f,
            0.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f,
    };
    public static final float[] TEXTURE_FLIP_HORIZONTAL_AND_ROTATED_90 = {
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            0.0f, 0.0f,
    };
    public static final float[] TEXTURE_FLIP_HORIZONTAL_AND_ROTATED_180 = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 0.0f,
    };
    public static final float[] TEXTURE_FLIP_HORIZONTAL_AND_ROTATED_270 = {
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
    };


    public static final float[] RECTANGLE_VERTICES = {
            -1.0f, -1.0f,
            1.0f, -1.0f,
            -1.0f, 1.0f,
            1.0f, 1.0f
    };

    public static final float[] RECTANGLE_TEXTURE = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f
    };


    public static float[] adjustVetices(int inWidth, int inHeight, int outWidth, int outHeight) {
        float[] cube = new float[8];
        System.arraycopy(RECTANGLE_VERTICES, 0, cube, 0, 8);
        float ratio = (float) outWidth / outHeight;

        if (inWidth > inHeight * ratio) {
            float scale = (float) inWidth / (inHeight * ratio);
            cube[0] *= scale;
            cube[2] *= scale;
            cube[4] *= scale;
            cube[6] *= scale;
        } else if (inWidth < inHeight * ratio) {
            float scale = (float) inHeight / (inWidth / ratio);
            cube[1] *= scale;
            cube[2] *= scale;
            cube[3] *= scale;
            cube[4] *= scale;
        }

        return cube;
    }


}
