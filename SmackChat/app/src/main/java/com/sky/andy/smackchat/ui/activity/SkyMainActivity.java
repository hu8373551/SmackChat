package com.sky.andy.smackchat.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.sky.andy.smackchat.R;
import com.sky.andy.smackchat.base.BaseActivity;
import com.sky.andy.smackchat.bean.AddFriendModel;
import com.sky.andy.smackchat.bean.GetChatMessage;
import com.sky.andy.smackchat.manager.SmackManager;
import com.sky.andy.smackchat.ui.adapter.FragmentAdapter;
import com.sky.andy.smackchat.ui.fragment.SkyChatFragment;
import com.sky.andy.smackchat.ui.fragment.SkyConnectFragment;
import com.sky.andy.smackchat.ui.fragment.SkyMySelfFragment;
import com.sky.andy.smackchat.utils.SystemUtils;
import com.sky.skyweight.tablayout.TabItem;
import com.sky.skyweight.tablayout.TabLayout;
import com.sky.skyweight.topbar.TopBarViewWithLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

import static com.sky.andy.smackchat.db.SmackDataBaseHelper.getInstants;

/**
 * Created by hcc on 16-10-28.
 * Company MingThink
 */

public class SkyMainActivity extends BaseActivity implements TabLayout.OnTabClickListener {

    private ArrayList<TabItem> tabs = new ArrayList<>();
    private ActionBar actionBar;

    private TabItem msgTab;
    private TabItem connectTab;
    private TabItem myselfTab;

    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.my_bar)
    TopBarViewWithLayout mMyBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);

//        actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.hide();
//        }

        mMyBar.setOnTopBarClickListener(new TopBarViewWithLayout.onTopBarClickListener() {
            @Override
            public void onClickLeftButton() {
//                if (SmackManager.getInstance().logout()) {
//                    Log.e("hcc", "is connnected-->>" + SmackManager.getInstance().isConnected());
//                    if (SmackManager.getInstance().disconnect()) {
//                        SystemUtils.getInstance().showToast(SkyMainActivity.this, "断开链接", Toast.LENGTH_SHORT);
//                    } else {
//                        SystemUtils.getInstance().showToast(SkyMainActivity.this, "断开链接失败", Toast.LENGTH_SHORT);
//                    }
//                } else {
//                    SystemUtils.getInstance().showToast(SkyMainActivity.this, "无法注销", Toast.LENGTH_SHORT);
//                }
            }

            @Override
            public void onClickRightButton() {
                //添加好友
                SystemUtils.getInstance().showActivity(SkyAddFriendsActivity.class, SkyMainActivity.this);
            }
        });
        initData();
    }


    private void initData() {
        msgTab = new TabItem(R.drawable.selector_tab_msg, R.string.text_sky_chat, SkyChatFragment.class);
        connectTab = new TabItem(R.drawable.selector_tab_contact, R.string.text_sky_connect, SkyConnectFragment.class);
        myselfTab = new TabItem(R.drawable.selector_tab_profile, R.string.text_sky_myself, SkyMySelfFragment.class);
        tabs.add(msgTab);
        tabs.add(connectTab);
//        tabs.add(new TabItem(R.drawable.selector_tab_moments, R.string.text_sky_find, SkyFindFragment.class));
        tabs.add(myselfTab);

        mTabLayout.initData(tabs, this);
        mTabLayout.setCurrentTab(0);

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), tabs);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout.setCurrentTab(position);
//                actionBar.setTitle(tabs.get(position).lableResId);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void onTabClick(TabItem tabItem) {
        mViewPager.setCurrentItem(tabs.indexOf(tabItem));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.e("hcc", "onPostResume-->>");

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int totalUnReadCount = getInstants().getTotalUnReadCount();
                mTabLayout.onDataChanged(0,totalUnReadCount);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        SmackManager.getInstance().logout();
//        SmackManager.getInstance().disconnect();

        Log.e("hcc", "onDestroy-->>");
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void responseAddFriend(AddFriendModel addFriendModel) {
        final String fromName = addFriendModel.getFromName();
        String name = null;
        if (fromName != null) {
            //裁剪JID得到对方用户名
            name = fromName.substring(0, fromName.indexOf("@"));
        }
        //弹出一个对话框，包含同意和拒绝按钮
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("添加好友请求");
        builder.setMessage("用户" + name + "请求添加你为好友");
        final String finalName = name;
        builder.setPositiveButton("同意", new DialogInterface.OnClickListener() {
            //同意按钮监听事件，发送同意Presence包及添加对方为好友的申请
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                SmackManager.getInstance().acceptFriend(fromName, finalName);
            }
        });
        builder.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
            //拒绝按钮监听事件，发送拒绝Presence包
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                SmackManager.getInstance().cancelFriend(fromName);
            }
        });
        builder.show();
    }


    @Subscribe(threadMode = ThreadMode.MainThread)
    public void showUnReadCount(GetChatMessage message) {
        Log.e("hcc", "showUnReadCount-->>" + message.getUnRead());
        mTabLayout.onDataChanged(0, message.getUnRead());
    }


}
