package com.aglframework.smzh;

public class Transform {

    public static float[] TEXTURE_NOMAL = {
            0f, 0f,
            1f, 0f,
            0f, 1f,
            1f, 1f
    };

    public static float[] TEXTURE_ROTATION_270 = {
            0f, 1f,
            0f, 0f,
            1f, 1f,
            1f, 0f
    };


    public static float[] TEXTURE_ROTATION_90_FLIP = {
            1f, 1f,
            1f, 0f,
            0f, 1f,
            0f, 0f
    };


    public static float[] VERTICES_NOMAL = {
            -1f, -1f,
            1f, -1f,
            -1f, 1f,
            1f, 1f
    };


    public static float[] adjustVetices(int inWidth, int inHeight, int outWidth, int outHeight) {
        float[] cube = new float[8];
        System.arraycopy(VERTICES_NOMAL, 0, cube, 0, 8);
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
