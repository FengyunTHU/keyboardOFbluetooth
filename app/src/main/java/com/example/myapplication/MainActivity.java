package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.createWebView();
    }

    // 创建WebView
    @SuppressLint("SetJavaScriptEnabled")
    private void createWebView() {
        final WebView webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setBuiltInZoomControls(false);
        if (Build.VERSION.SDK_INT > 11) {
            webView.getSettings().setDisplayZoomControls(false);
        }
        webView.setWebViewClient(new WebViewClient());
        String url = "file:///android_asset/index.html";
        webView.loadUrl(url);
    }
}