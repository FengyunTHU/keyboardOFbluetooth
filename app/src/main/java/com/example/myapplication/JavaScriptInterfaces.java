package com.example.myapplication;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.activity.result.ActivityResultLauncher;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
    // 修改用于更换键盘背景2024/4/4
    public void getImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        requestLauncher.launch(intent);
    }

    @JavascriptInterface
    public void getPosition() {
        Log.d(TAG,"position_enter");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        String[] mimeTypes = {"text/csv","text/plain"};// .csv & .txt
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        requestLauncher.launch(intent);
    }

    public String getFileType(Uri uri) {
        ContentResolver contentResolver = activity.getContentResolver();
        return contentResolver.getType(uri);
    }

    public void SendResult_pic(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Log.d(TAG,"Url_img"+uri);
            // 获取Uri后开启图片裁剪

            String Type = getFileType(uri);
            Log.d(TAG,"GetType_img: "+Type);
            String base64Image = convertImageToBase64(uri);
            Log.d(TAG,"GetString_img: "+base64Image);
            webView.loadUrl("javascript:setPictureOfKeyboard('"+base64Image+"','"+Type+"')");
            Log.d(TAG,"AfterLoadJS_img");
        }
    }

    public void SendResult_position(int resultCode,Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            Log.d(TAG,"Url_position"+uri);
            String Type = getFileType(uri);
            Log.d(TAG,"GetType_img: "+Type);
            String position = readPositionFile(uri);
            Log.d(TAG,"GetString_position: "+position);
            webView.loadUrl("javascript:setPositionOfKeyboard('"+position+"')");
            Log.d(TAG,"AfterLoadJS_position");
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

    @JavascriptInterface
    public void setSVG() {
        try (
                InputStream inputStream = activity.getAssets().open("img/svg.svg");
                InputStream inputStream1 = activity.getAssets().open("img/svg_dark.svg");
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                BufferedReader reader1 = new BufferedReader(new InputStreamReader(inputStream1));
        ) {
            StringBuilder svgData = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                // 转义单引号
                line = line.replace("'", "\\'");
                svgData.append(line).append("\\n"); // 使用双反斜杠来表示换行，以便在JavaScript中正确解析
            }

            StringBuilder svgData1 = new StringBuilder();
            String line1;

            while ((line1 = reader1.readLine()) != null) {
                // 转义单引号
                line1 = line1.replace("'", "\\'");
                svgData1.append(line1).append("\\n"); // 使用双反斜杠来表示换行，以便在JavaScript中正确解析
            }

            // 将SVG内容传递给JavaScript函数
//            String jsScript = "javascript:preSVG('" + svgData.toString() + "','" +svgData1.toString()+"')";
//            Log.d(TAG,jsScript);
            webView.loadUrl("javascript:preSVG('" + svgData.toString() + "','" +svgData1.toString()+"')");
            Log.d(TAG,"oksvg");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String readPositionFile(Uri uri) {
        try {
            Log.d(TAG,"start read position");
            InputStream inputStream = activity.getContentResolver().openInputStream(uri);
            assert inputStream != null;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder positionData = new StringBuilder();
            String lines;

            while ((lines = bufferedReader.readLine()) != null) {
                // 转义单引号
                lines = lines.replace("'", "\\'");
                positionData.append(lines).append("\\n"); // 使用双反斜杠来表示换行，以便在JavaScript中正确解析
            }
            return positionData.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
