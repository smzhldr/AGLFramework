package com.aglframework.smzh.agl_framework.activity;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.aglframework.smzh.AGLView;
import com.aglframework.smzh.agl_framework.R;
import com.aglframework.smzh.agl_framework.adapter.FilterListAdapter;
import com.aglframework.smzh.agl_framework.data.Filter;
import com.aglframework.smzh.agl_framework.fragment.BeautyFragment;
import com.aglframework.smzh.agl_framework.fragment.FilterFragment;
import com.aglframework.smzh.agl_framework.listener.CameraView;
import com.aglframework.smzh.agl_framework.presenter.CameraPresenter;
import com.aglframework.smzh.agl_framework.utils.FilterType;
import com.aglframework.smzh.agl_framework.widget.ShutterButton;
import com.aglframework.smzh.camera.CameraPreview;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class CameraActivity extends AppCompatActivity implements View.OnClickListener, BeautyFragment.BeautyLevelListener
        , CameraView, CameraPreview.GestureListener, FilterListAdapter.FilterApplyListener {

    private CameraPreview cameraPreview;
    private CameraPresenter presenter;
    private boolean isBottomFragmentShow;
    private FrameLayout bottomContainer;
    private ImageView filterButton;
    private ImageView beautyButton;
    private ShutterButton shutterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);
        initView();
        presenter = new CameraPresenter(cameraPreview, this);
    }

    private void initView() {
        cameraPreview = findViewById(R.id.camera_preview);
        ImageView switchButton = findViewById(R.id.iv_camera_switch);
        ImageView homeButton = findViewById(R.id.iv_camera_home);
        bottomContainer = findViewById(R.id.camera_bottom_container);
        filterButton = findViewById(R.id.iv_camera_filter);
        beautyButton = findViewById(R.id.iv_camera_beauty);
        shutterButton = findViewById(R.id.iv_camera_shutter);

        bottomContainer.setVisibility(View.GONE);

        switchButton.setOnClickListener(this);
        homeButton.setOnClickListener(this);
        cameraPreview.setGestureListener(this);
        beautyButton.setOnClickListener(this);
        filterButton.setOnClickListener(this);
        shutterButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_camera_switch) {
            presenter.switchCamera();
        } else if (v.getId() == R.id.iv_camera_home) {
            finish();
        } else if (v.getId() == R.id.iv_camera_filter) {
            showFilterFragmentIfNeed();
        } else if (v.getId() == R.id.iv_camera_beauty) {
            showBeautyFragmentIfNeed();
        } else if (v.getId() == R.id.iv_camera_shutter) {
            capture();
        }
    }

    private void capture() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                cameraPreview.capture(new AGLView.CaptureListener() {
                    @Override
                    public void captured(Bitmap bitmap) {
                        if (bitmap == null) {
                            return;
                        }
                        try {
                            String title = System.currentTimeMillis() + ".png";
                            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/" + title;
                            File f = new File(path);
                            if (f.exists()) {
                                boolean exist = f.delete();
                            }
                            FileOutputStream out = new FileOutputStream(f);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                            out.flush();
                            out.close();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CameraActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                            ContentValues currentVideoValues = new ContentValues();
                            currentVideoValues.put(MediaStore.Images.Media.TITLE, title);
                            currentVideoValues.put(MediaStore.Images.Media.DISPLAY_NAME, title);
                            currentVideoValues.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
                            currentVideoValues.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
                            currentVideoValues.put(MediaStore.Images.Media.DATA, path);
                            currentVideoValues.put(MediaStore.Images.Media.DESCRIPTION, "com.smzh.aglframework");
                            currentVideoValues.put(MediaStore.Images.Media.SIZE, f.length());
                            getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, currentVideoValues);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        hideBottomFragmentIfNeed();
    }

    @Override
    public void leftSlide() {
        Toast.makeText(this, "left", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void rightSlide() {
        Toast.makeText(this, "right", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void topSlide() {
        Toast.makeText(this, "top", Toast.LENGTH_SHORT).show();
        showFilterFragmentIfNeed();
    }

    @Override
    public void bottomSlide() {
        Toast.makeText(this, "down", Toast.LENGTH_SHORT).show();
        hideBottomFragmentIfNeed();
    }

    @Override
    public void onClick() {
        hideBottomFragmentIfNeed();
    }

    @Override
    public void zoom(float distance) {

    }


    private void showBeautyFragmentIfNeed() {
        if (isBottomFragmentShow) {
            return;
        }
        shutterButton.scaleWithAnim(true);
        isBottomFragmentShow = true;
        bottomContainer.setVisibility(View.VISIBLE);

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        animation.setFillAfter(true);
        animation.setDuration(200);
        animation.setInterpolator(new AccelerateInterpolator());
        bottomContainer.startAnimation(animation);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(BeautyFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = new BeautyFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.camera_bottom_container, fragment, BeautyFragment.class.getSimpleName())
                    .commitAllowingStateLoss();
        } else {
            fragmentManager.beginTransaction()
                    .show(fragment)
                    .commitAllowingStateLoss();
        }

        onBottomFragmentShow();
    }


    private void showFilterFragmentIfNeed() {
        if (isBottomFragmentShow) {
            return;
        }

        shutterButton.scaleWithAnim(true);
        isBottomFragmentShow = true;
        bottomContainer.setVisibility(View.VISIBLE);

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0);
        animation.setFillAfter(true);
        animation.setDuration(200);
        animation.setInterpolator(new AccelerateInterpolator());
        bottomContainer.startAnimation(animation);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(FilterFragment.class.getSimpleName());
        if (fragment == null) {
            fragment = new FilterFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.camera_bottom_container, fragment, FilterFragment.class.getSimpleName())
                    .commitAllowingStateLoss();
        } else {
            fragmentManager.beginTransaction()
                    .show(fragment)
                    .commitAllowingStateLoss();
        }

        onBottomFragmentShow();
    }

    private void hideBottomFragmentIfNeed() {
        if (!isBottomFragmentShow) {
            return;
        }
        shutterButton.scaleWithAnim(false);
        isBottomFragmentShow = false;

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1);
        animation.setFillAfter(true);
        animation.setDuration(200);
        animation.setInterpolator(new AccelerateInterpolator());
        bottomContainer.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment filterFragment = fragmentManager.findFragmentByTag(FilterFragment.class.getSimpleName());
                if (filterFragment != null) {
                    fragmentManager.beginTransaction()
                            .hide(filterFragment)
                            .commitAllowingStateLoss();
                }

                Fragment beautyFragment = fragmentManager.findFragmentByTag(BeautyFragment.class.getSimpleName());
                if (beautyFragment != null) {
                    fragmentManager.beginTransaction()
                            .hide(beautyFragment)
                            .commitAllowingStateLoss();
                }

                onBottomFragmentHide();
                bottomContainer.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void onBottomFragmentShow() {
        filterButton.setVisibility(View.INVISIBLE);
        beautyButton.setVisibility(View.INVISIBLE);
    }


    private void onBottomFragmentHide() {
        filterButton.setVisibility(View.VISIBLE);
        beautyButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void onFilterApply(@NotNull Filter filter) {
        presenter.applyLookupFilter(filter);
    }

    @Override
    public void onIntensityChange(int intensity) {
        presenter.setIntensity(intensity);
    }

    @Override
    public int hideFilterFragment() {
        hideBottomFragmentIfNeed();
        return 0;
    }

    @Override
    public void onWhiteLevelChange(int level) {
        presenter.changeFilter(FilterType.Type.WIHTE, level);
    }

    @Override
    public void onSmoothLevelChange(int levl) {
        presenter.changeFilter(FilterType.Type.SMOOTH, levl);
    }

    @Override
    public void onStickerShow(boolean isShow) {
        presenter.showSticker(isShow);
    }

    @Override
    public void hideBeautyFragment() {
        hideBottomFragmentIfNeed();
    }
}
