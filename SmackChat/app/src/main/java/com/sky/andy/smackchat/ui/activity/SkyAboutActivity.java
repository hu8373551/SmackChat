package com.sky.andy.smackchat.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.sky.andy.smackchat.R;
import com.sky.andy.smackchat.base.BaseActivity;
import com.sky.skyweight.topbar.TopBarViewWithLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hcc on 16-11-15.
 * Company MingThink
 */

public class SkyAboutActivity extends BaseActivity implements TopBarViewWithLayout.onTopBarClickListener {

    @BindView(R.id.my_bar)
    TopBarViewWithLayout my_bar;

    @BindView(R.id.web_view)
    WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        my_bar.setTvTitle("关于云信");
        my_bar.setLeftLayoutShow(true);
        my_bar.setLeftImageRecource(R.drawable.back_btn);
        my_bar.setRightLayoutShow(false);
        my_bar.setOnTopBarClickListener(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://github.com/hu8373551/SmackChat/blob/master/README.md");
        webView.setWebViewClient(new WebSiteClient());
        showProgressDialog();
    }

    private class WebSiteClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dismissProgressDialog();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            dismissProgressDialog();
        }
    }

    @Override
    public void onClickLeftButton() {
        this.finish();
    }

    @Override
    public void onClickRightButton() {

    }
}
