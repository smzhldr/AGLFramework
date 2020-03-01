package com.aglframework.smzh.agl_framework.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.aglframework.smzh.agl_framework.R;
import com.aglframework.smzh.agl_framework.activity.CameraActivity;

public class BeautyFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private BeautyLevelListener levelListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CameraActivity) {
            levelListener = (BeautyLevelListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_beauty, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((SeekBar) view.findViewById(R.id.white)).setOnSeekBarChangeListener(this);
        ((SeekBar) view.findViewById(R.id.smooth)).setOnSeekBarChangeListener(this);
        view.findViewById(R.id.sticker).setOnClickListener(this);
        view.findViewById(R.id.hideBtn).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.sticker) {
            v.setSelected(!v.isSelected());
            levelListener.onStickerShow(v.isSelected());
        } else if (v.getId() == R.id.hideBtn) {
            levelListener.hideBeautyFragment();
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.getId() == R.id.white) {
            levelListener.onWhiteLevelChange(progress);
        }

        if (seekBar.getId() == R.id.smooth) {
            levelListener.onSmoothLevelChange(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public interface BeautyLevelListener {

        void onWhiteLevelChange(int level);

        void onSmoothLevelChange(int levl);

        void onStickerShow(boolean isShow);

        void hideBeautyFragment();
    }
}
