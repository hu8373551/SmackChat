package com.sky.skyweight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.sky.skyweight.R;


/**
 * 自定义加载进度条
 *
 * @author andy hu
 */
public class LoadingProDialog extends Dialog {
    private Context mContext;
    private String message;
    private boolean flag =true;


    public LoadingProDialog(Context context, String msg) {
        super(context, R.style.loading_dialog);
        this.mContext = context;
        this.message = msg;
        initDialog();
    }

    public LoadingProDialog(Context context) {
        super(context, R.style.loading_dialog);
        this.mContext = context;
        initDialog();
    }

    public void isCancelable(boolean flag){
        this.flag =flag;
    }

    /**
     * 得到自定义的progressDialog
     *
     * @return
     */
//    Dialog loadingDialog;
    private void initDialog() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
        TextView tipTextView = (TextView) view.findViewById(R.id.loading_msg_tv);// 提示文字
        if (message != null && !message.isEmpty()) {
            tipTextView.setText(message);// 设置加载信息
        }
        this.getWindow().getAttributes().gravity = Gravity.CENTER;
        this.setContentView(view);
        this.setCancelable(flag);// 不可以用“返回键”取消
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
