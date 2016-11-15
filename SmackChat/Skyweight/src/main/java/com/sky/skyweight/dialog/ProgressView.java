package com.sky.skyweight.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.sky.skyweight.R;


/**
 * Created by hcc on 16-9-23.
 * Company MingThink
 * 菊花load view
 */

public class ProgressView extends ImageView {
    public ProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setAnimation(attrs);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ProgressView(Context context) {
        this(context,null);
    }

    private void setAnimation(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ProgressView);
        int frameCount = a.getInt(R.styleable.ProgressView_frameCount, 12);
        int duration = a.getInt(R.styleable.ProgressView_duration, 1000);
        a.recycle();
        setAnimation(frameCount, duration);
    }

    public void setAnimation(final int frameCount, final int duration) {
        Animation a = AnimationUtils.loadAnimation(getContext(), R.anim.loading_animation_style);
        a.setDuration(duration);
        a.setInterpolator(new Interpolator() {

            @Override
            public float getInterpolation(float input) {
                return (float) Math.floor(input * frameCount) / frameCount;
            }
        });
        Log.e("hcc","start animation-->>.");
        startAnimation(a);
    }
}
