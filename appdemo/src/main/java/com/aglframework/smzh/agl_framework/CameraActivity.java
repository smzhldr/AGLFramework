package com.aglframework.smzh.agl_framework;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.WindowManager;

import com.aglframework.smzh.AGLView;
import com.aglframework.smzh.camera.AGLCamera;

import java.util.List;

public class CameraActivity extends Activity /*implements EasyPermissions.PermissionCallbacks*/ {

    AGLView aglView;
    AGLCamera aglCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);

        aglView = findViewById(R.id.camera_preview);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (aglCamera == null) {
            aglCamera = new AGLCamera(aglView, 1080, 2160);
        }
        aglCamera.open();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (aglCamera != null) {
            aglCamera.close();
        }
    }

  /*  @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }*/

}
