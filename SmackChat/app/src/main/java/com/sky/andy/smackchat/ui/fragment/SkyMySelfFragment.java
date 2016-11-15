package com.sky.andy.smackchat.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sky.andy.smackchat.Constants;
import com.sky.andy.smackchat.R;
import com.sky.andy.smackchat.bean.UserInfo;
import com.sky.andy.smackchat.manager.SmackManager;
import com.sky.andy.smackchat.ui.activity.SkyAboutActivity;
import com.sky.andy.smackchat.ui.activity.SkyLoginActivity;
import com.sky.andy.smackchat.ui.activity.SkyUpdateActivity;
import com.sky.andy.smackchat.utils.SystemUtils;
import com.sky.andy.smackchat.utils.view.OutDialogView;
import com.sky.skyweight.SharePrefenceUtils;
import com.sky.skyweight.tablayout.BaseFragment;
import com.zzti.andy.imagepicker.PhotoSelectorActivity;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * Created by hcc on 16-10-31.
 * Company MingThink
 */

public class SkyMySelfFragment extends BaseFragment implements OutDialogView.OnOutDialogListener {

    private String userJid;
    private String userNickName;
    private String userHeaderImg;

    private final int REQUEST_CODE_1 = 1001;
    private final int REQUEST_CODE_2 = 1002;

    @BindView(R.id.rl_header)
    RelativeLayout rl_header;
    @BindView(R.id.rl_nick_name)
    RelativeLayout rl_nick_name;

    @BindView(R.id.my_header_img)
    ImageView my_header_img;
    @BindView(R.id.tv_nick_name)
    TextView tv_nick_name;
    @BindView(R.id.tv_jid)
    TextView tv_jid;

    @BindView(R.id.tv_out)
    TextView tv_out;

    @BindView(R.id.tv_about)
    TextView tv_about;

    private OutDialogView outDialogView;

    @Override
    public void fetchData() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_myself, container, false);
        ButterKnife.bind(this, view);
        outDialogView = new OutDialogView(getContext());
        userJid = SharePrefenceUtils.readString(getActivity(), Constants.SP_USER_INFO, Constants.SP_USER_JID);
        userNickName = SharePrefenceUtils.readString(getActivity(), Constants.SP_USER_INFO, Constants.SP_USER_NICKNAME);
        userHeaderImg = SharePrefenceUtils.readString(getActivity(), Constants.SP_USER_INFO, Constants.SP_USER_HEADER_IMAGE);

        tv_jid.setText(userJid);
        tv_nick_name.setText(userNickName);
        Glide.with(getActivity()).load(SharePrefenceUtils.StringToBytes(userHeaderImg))
                .asBitmap()
                .centerCrop()
                .into(my_header_img);

        outDialogView.setOnOutDialogListener(this);
        return view;
    }

    @OnClick(R.id.rl_header)
    public void headerOnClick() {
        Intent intent = new Intent(getActivity(), PhotoSelectorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("limit", 1);//number是选择图片的数量
        startActivityForResult(intent, REQUEST_CODE_1);
    }

    @OnClick(R.id.rl_nick_name)
    public void nickNameOnClick() {
        Intent intent = new Intent(getActivity(), SkyUpdateActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("nickName", tv_nick_name.getText().toString().trim());
        startActivityForResult(intent, REQUEST_CODE_2);
    }

    @OnClick(R.id.tv_out)
    public void outOnClick() {
        outDialogView.show();
    }

    @OnClick(R.id.tv_about)
    public void aboutOnClick() {
        SystemUtils.getInstance().showActivity(SkyAboutActivity.class, getActivity());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_1:
                    List<String> paths = (List<String>) data.getExtras().getSerializable("photos");//path是选择拍照或者图片的地址数组
                    //处理代码

                    File file = new File(paths.get(0));

                    byte[] imageByte = SharePrefenceUtils.getBytesFromFile(file);


                    Glide.with(getActivity()).load(imageByte)
                            .asBitmap()
                            .centerCrop()
                            .into(my_header_img);

//                    SharePrefenceUtils.write(getActivity(), Constants.SP_USER_INFO, Constants.SP_USER_JID, currentUserJid);
//                    SharePrefenceUtils.write(getActivity(), Constants.SP_USER_INFO, Constants.SP_USER_NICKNAME, currentUserNickName);
                    SharePrefenceUtils.write(getActivity(), Constants.SP_USER_INFO, Constants.SP_USER_HEADER_IMAGE,
                            SharePrefenceUtils.bytesToHexString(imageByte));

                    UserInfo userInfo = new UserInfo();
                    userInfo.setBareJid(userJid);
                    userInfo.setUserName(userNickName);
                    userInfo.setAvatar(imageByte);
                    SmackManager.getInstance().saveUserInfo(userInfo);
                    break;
                case REQUEST_CODE_2:
                    String nickName = data.getExtras().getString("nickName");
                    tv_nick_name.setText(nickName);
                    break;

            }
        }
    }

    @Override
    public void clickLogOut() {
        SystemUtils.getInstance().showToast(getActivity(), "logout", Toast.LENGTH_SHORT);
        SmackManager.getInstance().logout();
        SystemUtils.getInstance().skipActivity(SkyLoginActivity.class, getActivity());
    }

    @Override
    public void clickOut() {
        SystemUtils.getInstance().showToast(getActivity(), "clickOut", Toast.LENGTH_SHORT);
    }
}
