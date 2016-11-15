package com.sky.skyweight.tablayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.skyweight.R;

/**
 * Created by hcc on 16-10-31.
 * Company MingThink
 */

public class TabView extends LinearLayout implements View.OnClickListener {

    private ImageView mTabImage;
    private TextView mTabLabel;
    private TextView mCountTv;


    public TabView(Context context) {
        this(context, null);
    }

    public TabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        LayoutInflater.from(context).inflate(R.layout.tab_view, this, true);
        mTabImage = (ImageView) findViewById(R.id.tab_image);
        mTabLabel = (TextView) findViewById(R.id.tab_label);
        mCountTv = (TextView) findViewById(R.id.tv_new_friend);
    }

    public void initData(TabItem tabItem) {
        mTabImage.setImageResource(tabItem.imageResId);
        mTabLabel.setText(tabItem.labelResId);
        mCountTv.setText(tabItem.getCount() + "");
    }


    @Override
    public void onClick(View view) {

    }

    public void onDataChanged(int badgeCount) {
        //  TODO notify new message, change the badgeView
        if (badgeCount > 0) {
            mCountTv.setVisibility(VISIBLE);
        } else {
            mCountTv.setVisibility(GONE);
        }
        mCountTv.setText(badgeCount + "");
    }
}
