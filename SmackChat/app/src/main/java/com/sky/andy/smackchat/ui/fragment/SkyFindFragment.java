package com.sky.andy.smackchat.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sky.andy.smackchat.R;
import com.sky.skyweight.tablayout.BaseFragment;

/**
 * Created by hcc on 16-10-31.
 * Company MingThink
 */

public class SkyFindFragment extends BaseFragment {
    @Override
    public void fetchData() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_find, container, false);
        return view;
    }
}
