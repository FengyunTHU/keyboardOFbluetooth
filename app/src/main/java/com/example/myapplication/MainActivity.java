package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
        webView.setWebViewClient(new WebViewClient(){});
        String url = "file:///android_asset/index.html";
        webView.loadUrl(url);

        try (
                InputStream inputStream = getAssets().open("data/position.csv");
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))
        ) {
            StringBuilder csvData = new StringBuilder();
            String line;
            String jsScript;

            while ((line = reader.readLine()) != null) {
                // 转义单引号
                line = line.replace("'", "\\'");
                csvData.append(line).append("\\n"); // 使用双反斜杠来表示换行，以便在JavaScript中正确解析
            }
            // 将换行符转换为字符串字面量
            jsScript = "javascript:processCSVData('" + csvData.toString() + "')";
            // 确保WebView已经准备好接收JavaScript代码
            if (webView != null) {
                webView.evaluateJavascript(jsScript, null);
            }
        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
        }

    }
}