package com.sky.andy.smackchat.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sky.skyweight.tablayout.BaseFragment;
import com.sky.skyweight.tablayout.TabItem;

import java.util.ArrayList;

/**
 * Created by hcc on 16-10-31.
 * Company MingThink
 */

public class FragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<TabItem> tabs;
    private BaseFragment fragment;

    public FragmentAdapter(FragmentManager fm, ArrayList<TabItem> tabs) {
        super(fm);
        this.tabs = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        try {
            return tabs.get(position).tagFragmentClz.newInstance();

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tabs.size();
    }

}
