package com.sky.skyweight.topbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sky.skyweight.R;


/**
 * Created by Andy on 16/4/10.
 * TopBarViewWithLayout
 */
public class TopBarViewWithLayout extends RelativeLayout {

    private LayoutInflater mLayoutInflater;
    private ViewGroup mViewGroup;

    private RelativeLayout leftLayout, rightLayout;
    private ImageView leftImg, rightImg;

    private TextView leftTv;
    private TextView rightTv;
    private TextView tvTitle;


    private onTopBarClickListener listener;


    public interface onTopBarClickListener {
        void onClickLeftButton();

        void onClickRightButton();
    }

    public void setOnTopBarClickListener(onTopBarClickListener listener) {
        this.listener = listener;
    }


    public TopBarViewWithLayout(Context context) {
        this(context, null);
    }

    public TopBarViewWithLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopBarViewWithLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewGroup = (ViewGroup) mLayoutInflater.inflate(R.layout.topbar_layout, null);
        leftLayout = (RelativeLayout) mViewGroup.findViewById(R.id.leftLayout);
        leftImg = (ImageView) mViewGroup.findViewById(R.id.leftImage);
        leftTv = (TextView) mViewGroup.findViewById(R.id.leftText);

        rightLayout = (RelativeLayout) mViewGroup.findViewById(R.id.rightLayout);
        rightImg = (ImageView) mViewGroup.findViewById(R.id.rightImage);
        rightTv = (TextView) mViewGroup.findViewById(R.id.rightText);

        tvTitle = (TextView) mViewGroup.findViewById(R.id.title);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyBar, defStyleAttr, 0);
        leftImg.setImageResource(ta.getResourceId(R.styleable.MyBar_myLeftImg, 0));
        leftTv.setText(ta.getString(R.styleable.MyBar_myLeftTvTitle));
        leftTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ta.getDimensionPixelSize(R.styleable.MyBar_myLeftTvSize, 10));
        leftTv.setTextColor(ta.getColor(R.styleable.MyBar_myLeftTvColor, Color.BLACK));

        rightImg.setImageResource(ta.getResourceId(R.styleable.MyBar_myRightImg, 0));
        rightTv.setText(ta.getString(R.styleable.MyBar_myRightTvTitle));
        rightTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, ta.getDimensionPixelSize(R.styleable.MyBar_myRightTvSize, 10));
        rightTv.setTextColor(ta.getColor(R.styleable.MyBar_myRightTvColor, Color.BLACK));


        tvTitle.setText(ta.getString(R.styleable.MyBar_myTvTitle));
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, ta.getDimensionPixelSize(R.styleable.MyBar_myTvSize, 10));
        tvTitle.setTextColor(ta.getColor(R.styleable.MyBar_myTvColor, Color.BLACK));
        this.addView(mViewGroup);

        ta.recycle();


        //设置点击事件
        leftLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickLeftButton();
                }
            }
        });

        rightLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickRightButton();
                }
            }
        });


    }


    // 设置左边 btn 颜色
    public void serLeftLayoutColor(int color) {
        leftLayout.setBackgroundColor(color);
    }

    // 设置右边 btn 颜色
    public void setRightLayoutColor(int color) {
        rightLayout.setBackgroundColor(color);
    }

    // 设置左边  图片
    public void setLeftImageRecource(int resid) {
        leftImg.setBackgroundResource(resid);
    }

    // 设置右边  图片
    public void setRightImageRecource(int resid) {
        rightImg.setBackgroundResource(resid);
    }

    //设置左边text by res id
    public void setLeftText(int resid) {
        leftTv.setText(resid);
    }

    // 设置左边 text by string res
    public void setLeftText(String res) {
        leftTv.setText(res);
    }

    //设置右边text by res id
    public void setRightText(int resid) {
        rightTv.setText(resid);
    }

    // 设置右边 text by string res
    public void setRightText(String res) {
        rightTv.setText(res);
    }

    public void setTvTitle(int resId) {
        tvTitle.setText(resId);
    }

    public void setTvTitle(String res) {
        tvTitle.setText(res);
    }

    // 设置 leftLayout 显示 与否
    public void setLeftLayoutShow(boolean show) {
        if (show) {
            leftLayout.setVisibility(VISIBLE);
        } else {
            leftLayout.setVisibility(GONE);
        }
    }

    // 设置 rightLayout 显示 与否
    public void setRightLayoutShow(boolean show) {
        if (show) {
            rightLayout.setVisibility(VISIBLE);
        } else {
            rightLayout.setVisibility(GONE);
        }
    }
}
