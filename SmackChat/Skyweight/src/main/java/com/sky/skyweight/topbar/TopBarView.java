package com.sky.skyweight.topbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sky.skyweight.R;


/**
 * Created by Andy on 16/4/10.
 */
public class TopBarView extends RelativeLayout {

    private TextView leftBtn;
    private TextView rightBtn;
    private TextView tvTitle;

    private LayoutParams leftParams, rightParams, tvParams;
    private onTopBarClickListener listener;

    public interface onTopBarClickListener {
        void onClickLeftButton();

        void onClickRightButton();
    }

    public void setOnTopBarClickListener(onTopBarClickListener listener) {
        this.listener = listener;
    }

    public TopBarView(Context context) {
        this(context, null);
    }

    public TopBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopBar, defStyleAttr, 0);
        leftBtn = new TextView(context);
        rightBtn = new TextView(context);
        tvTitle = new TextView(context);

        leftBtn.setTextSize(ta.getDimension(R.styleable.TopBar_leftTextSize, 12));
        leftBtn.setTextColor(ta.getColor(R.styleable.TopBar_leftTextColor, 0));
        leftBtn.setText(ta.getString(R.styleable.TopBar_leftTitle));
        leftBtn.setBackgroundResource(ta.getResourceId(R.styleable.TopBar_leftBackground, 0));
        leftBtn.setGravity(Gravity.CENTER);

        rightBtn.setTextSize(ta.getDimension(R.styleable.TopBar_rightTextSize, 12));
        rightBtn.setTextColor(ta.getColor(R.styleable.TopBar_rightTextColor, 0));
        rightBtn.setText(ta.getString(R.styleable.TopBar_rightTitle));
        rightBtn.setBackgroundResource(ta.getResourceId(R.styleable.TopBar_rightBackground, 0));
        tvTitle.setGravity(Gravity.CENTER);

        tvTitle.setTextSize(ta.getDimension(R.styleable.TopBar_tvTitleTextSize, 12));
        tvTitle.setTextColor(ta.getColor(R.styleable.TopBar_tvTitleTextColor, 0));
        tvTitle.setText(ta.getString(R.styleable.TopBar_tvTitle));
        tvTitle.setGravity(Gravity.CENTER);

//        setBackgroundColor(0xFFF59955);

        ta.recycle();

        leftParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        leftParams.addRule(ALIGN_PARENT_LEFT, TRUE);
        addView(leftBtn, leftParams);

        rightParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rightParams.addRule(ALIGN_PARENT_RIGHT, TRUE);
        rightBtn.setLayoutParams(rightParams);
        addView(rightBtn, rightParams);

        tvParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        tvParams.addRule(CENTER_IN_PARENT, TRUE);
        tvTitle.setLayoutParams(tvParams);
        addView(tvTitle, tvParams);


        leftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickLeftButton();
                }
            }
        });

        rightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClickRightButton();
                }
            }
        });
    }


    // 设置左边 btn 颜色
    public void setLeftBtnColor(int color) {
        leftBtn.setBackgroundColor(color);
    }

    // 设置右边 btn 颜色
    public void setRightBtnColor(int color) {
        rightBtn.setBackgroundColor(color);
    }

    // 设置左边 btn 图片
    public void setLeftBtnRecource(int resid) {
        leftBtn.setBackgroundResource(resid);
    }

    // 设置右边 btn 图片
    public void setRightBtnRecource(int resid) {
        rightBtn.setBackgroundResource(resid);
    }
}
