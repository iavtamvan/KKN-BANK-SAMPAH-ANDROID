package com.iavariav.kkntambakajibanksampah.ui.user;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.iavariav.kkntambakajibanksampah.R;
import com.iavariav.kkntambakajibanksampah.helper.Config;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private String url;
    private ProgressBar pbView;


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        getSupportActionBar().setHomeButtonEnabled(true);
        initView();

        url = getIntent().getStringExtra(Config.BUNDLE_URL_NEWS);
        // Enable Javascript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

    private void initView() {
        webView = findViewById(R.id.web_view);
        pbView = findViewById(R.id.pb_view);
    }
}
