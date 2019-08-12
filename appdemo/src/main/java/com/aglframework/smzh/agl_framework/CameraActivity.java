package com.aglframework.smzh.agl_framework;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.aglframework.smzh.AGLView;
import com.aglframework.smzh.CombineFilter;
import com.aglframework.smzh.IFilter;
import com.aglframework.smzh.camera.AGLCamera;
import com.aglframework.smzh.filter.SmoothFilter;
import com.aglframework.smzh.filter.StickerFilter;
import com.aglframework.smzh.filter.WhiteFilter;

import java.util.ArrayList;
import java.util.List;


public class CameraActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    AGLView aglView;
    AGLCamera aglCamera;

    ImageView switchButton;
    ImageView homeButton;
    ImageView stickerButton;

    SeekBar whiteSeekBar;
    SeekBar smoothSeekBar;

    WhiteFilter whiteFilter;
    SmoothFilter smoothFilter;
    StickerFilter stickerFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            View decorView = window.getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);

            window.setNavigationBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        aglView = findViewById(R.id.camera_preview);

        switchButton = findViewById(R.id.iv_camera_switch);
        switchButton.setOnClickListener(this);

        homeButton = findViewById(R.id.iv_camera_home);
        homeButton.setOnClickListener(this);

        whiteSeekBar = findViewById(R.id.sb_white);

        smoothSeekBar = findViewById(R.id.sb_smooth);

        whiteSeekBar.setOnSeekBarChangeListener(this);

        smoothSeekBar.setOnSeekBarChangeListener(this);

        stickerButton = findViewById(R.id.iv_sticker_button);
        stickerButton.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_camera_switch) {
            if (aglCamera != null) {
                aglCamera.switchCamera();
            }
        } else if (v.getId() == R.id.iv_camera_home) {
            finish();
        } else if (v.getId() == R.id.iv_sticker_button) {
            stickerButton.setSelected(!stickerButton.isSelected());
            if (stickerFilter == null) {
                stickerFilter = new StickerFilter(this);
                stickerFilter.setBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sticker));
            }
            updateFilter();
        }
    }


    private List<IFilter> filters = new ArrayList<>();
    private int whiteLevel;
    private int smoothLevel;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        filters.clear();
        if (seekBar.getId() == R.id.sb_white) {
            if (whiteFilter == null) {
                whiteFilter = new WhiteFilter(this);
            }
            whiteLevel = progress;
            whiteFilter.setWhiteLevel(progress / 100f);
        }

        if (seekBar.getId() == R.id.sb_smooth) {
            if (smoothFilter == null) {
                smoothFilter = new SmoothFilter(this);
            }
            smoothLevel = progress;
            smoothFilter.setSmoothLevel(progress / 100f);
        }
        updateFilter();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    private void updateFilter() {
        filters.clear();
        if (whiteLevel > 0) {
            filters.add(whiteFilter);
        }

        if (smoothLevel > 0) {
            filters.add(smoothFilter);
        }

        if (stickerButton.isSelected()) {
            filters.add(stickerFilter);
        }
        aglView.setFilter(CombineFilter.getCombineFilter(filters));
    }
}
