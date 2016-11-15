package com.sky.skyweight.tablayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by hcc on 16-10-31.
 * Company MingThink
 */

public class TabLayout extends LinearLayout implements View.OnClickListener {

    private ArrayList<TabItem> tabs;
    private OnTabClickListener listener;
    private int tabCount;
    private View selectView;

    public interface OnTabClickListener {
        void onTabClick(TabItem tabItem);
    }


    public TabLayout(Context context) {
        this(context, null);
    }

    public TabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setOrientation(HORIZONTAL);
    }

    public void setCurrentTab(int i) {
        if (i < tabCount && i >= 0) {
            View view = getChildAt(i);
            if (selectView != view) {
                view.setSelected(true);
                if (selectView != null) {
                    selectView.setSelected(false);
                }
                selectView = view;
            }
        }
    }

    public void onDataChanged(int i, int badgeCount) {
        if (i < tabCount && i >= 0) {
            TabView view = (TabView) getChildAt(i);
            view.onDataChanged(badgeCount);
        }
    }


    public void initData(ArrayList<TabItem> tabs, OnTabClickListener listener) {
        this.tabs = tabs;
        this.listener = listener;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        if (tabs != null && tabs.size() > 0) {
            tabCount = tabs.size();
            TabView mTabView;
            for (int i = 0; i < tabs.size(); i++) {
                mTabView = new TabView(getContext());
                mTabView.setTag(tabs.get(i));
                mTabView.initData(tabs.get(i));
                mTabView.setOnClickListener(this);
                addView(mTabView, params);
            }

        } else {
            throw new IllegalArgumentException("tabs can not be empty");
        }
    }



    @Override
    public void onClick(View view) {
        listener.onTabClick((TabItem) view.getTag());
    }
}
