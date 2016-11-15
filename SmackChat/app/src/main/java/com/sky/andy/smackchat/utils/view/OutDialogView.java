package com.sky.andy.smackchat.utils.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.sky.andy.smackchat.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hcc on 16-11-14.
 * Company MingThink
 */

public class OutDialogView extends Dialog {

    private Context mContext;
    @BindView(R.id.tv_logout)
    TextView tv_logout;
    @BindView(R.id.tv_out)
    TextView tv_out;

    public static OnOutDialogListener mOnOutDialogListener;

    public interface OnOutDialogListener {
        void clickLogOut();

        void clickOut();
    }

    public void setOnOutDialogListener(OnOutDialogListener mOnOutDialogListener) {
        this.mOnOutDialogListener = mOnOutDialogListener;
    }

    public OutDialogView(Context mContext) {
        super(mContext);
        this.mContext = mContext;
        initView();
    }

    private void initView() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.dialog_out_show, null);// 得到加载view
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        this.setContentView(view);
        this.setCancelable(true);// 不可以用“返回键”取消
        ButterKnife.bind(this, view);
    }

    @OnClick(R.id.tv_logout)
    public void logoutOnClick() {
        mOnOutDialogListener.clickLogOut();
    }

    @OnClick(R.id.tv_out)
    public void outOnClick() {
        mOnOutDialogListener.clickOut();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            this.dismiss();
        }
    }

}
