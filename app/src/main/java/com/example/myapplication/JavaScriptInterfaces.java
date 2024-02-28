package com.example.myapplication;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.activity.result.ActivityResultLauncher;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

// 定义一系列JavaScript接口
public class JavaScriptInterfaces {
    private static final String TAG = "BtMain";
    private Activity activity;
    private WebView webView;
    private ActivityResultLauncher<Intent> requestLauncher;

    public JavaScriptInterfaces(Activity activity,WebView webView,ActivityResultLauncher<Intent> requestLauncher){
        this.activity = activity;
        this.webView = webView;
        this.requestLauncher = requestLauncher;
    }

    @JavascriptInterface
    public void getImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        requestLauncher.launch(intent);
    }

    public String getFileType(Uri uri) {
        ContentResolver contentResolver = activity.getContentResolver();
        return contentResolver.getType(uri);
    }

    public void SendResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Log.d(TAG,"Url"+uri);
            String Type = getFileType(uri);
            Log.d(TAG,"GetType: "+Type);
            String base64Image = convertImageToBase64(uri);
            Log.d(TAG,"GetString: "+base64Image);
            webView.loadUrl("javascript:setPictureOfKeyboard('"+base64Image+"','"+Type+"')");
            Log.d(TAG,"AfterLoadJS");
        }
    }

    private String convertImageToBase64(Uri uri) {// 得到base64编码
        try {
            Log.d(TAG,"start convert base64");
            InputStream inputStream = activity.getContentResolver().openInputStream(uri);
            assert inputStream != null;
            byte[] bytes = IOUtils.toByteArray(inputStream);
            return Base64.encodeToString(bytes,Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
