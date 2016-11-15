package com.sky.skyweight.tablayout;

/**
 * Created by hcc on 16-10-31.
 * Company MingThink
 */
public class TabItem {
    public int imageResId;
    public int labelResId;
    public int count;

    public Class<? extends BaseFragment> tagFragmentClz;

    public TabItem(int imageResId, int labelResId, int count) {
        this.imageResId = imageResId;
        this.labelResId = labelResId;
        this.count = count;
    }

    public TabItem(int imageResId, int labelResId, Class<? extends BaseFragment> tagFragmentClz) {
        this.imageResId = imageResId;
        this.labelResId = labelResId;
        this.tagFragmentClz = tagFragmentClz;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
