package com.example.myapplication;

import static androidx.core.app.ActivityCompat.requestPermissions;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.audiofx.DynamicsProcessing;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.IOUtils;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

// 定义一系列JavaScript接口
public class JavaScriptInterfaces {
    private static final String TAG = "BtMain";
    private Activity activity;
    private WebView webView;
    private LocationManager locationManager;
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
        Log.d(TAG,"enter in pic");
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
            Log.d(TAG,"选择图片完毕");
            Uri uri = data.getData();
            Log.d(TAG,"Url_img"+uri);
            // 获取Uri后开启图片裁剪
            EditPhoto(uri);
            Log.d(TAG,"开始裁剪");

//            String Type = getFileType(uri);
//            Log.d(TAG,"GetType_img: "+Type);
//            String base64Image = convertImageToBase64(uri);
//            Log.d(TAG,"GetString_img: "+base64Image);
//            webView.loadUrl("javascript:setPictureOfKeyboard('"+base64Image+"','"+Type+"')");
//            Log.d(TAG,"AfterLoadJS_img");
        }
    }

    public void EditPhoto(Uri uri) {
        UCrop.Options options = new UCrop.Options();
        // 隐藏网格线
        options.setShowCropGrid(true);
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setFreeStyleCropEnabled(false);
        options.setAllowedGestures(UCropActivity.ALL, UCropActivity.ALL, UCropActivity.ALL);
        options.setCompressionQuality(100);
        options.setHideBottomControls(false);
        File saveDir = activity.getFilesDir();
        Log.d(TAG,"即将开始裁剪工作"+saveDir.toString());
        UCrop.of(uri,Uri.fromFile(new File(saveDir,"_myBackground.jpg")))
                .withAspectRatio(431,174)
                .withOptions(options)
                .start(activity);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            // 获取裁剪后的图片的Uri
            Uri croppedImageUri = UCrop.getOutput(data);

                // 在这里，你可以使用裁剪后的图片
                // 例如，你可以将其显示在一个ImageView中
                // ImageView imageView = findViewById(R.id.imageView);
                // imageView.setImageURI(croppedImageUri);

                // 或者，你可以将其转换为Base64编码的字符串，然后传递给JavaScript
            String base64Image = convertImageToBase64(croppedImageUri);
            Log.d(TAG,"已经裁剪完成传回");
            Log.d(TAG,base64Image);
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:setPictureOfKeyboard('" + base64Image + "','image/jpeg')");
                }
            });
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


    // 写一个toast接口
    @JavascriptInterface
    public void myToast(String myMessage) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity,myMessage,Toast.LENGTH_SHORT).show();
            }
        });
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


    // 获取定位
    @JavascriptInterface
    public void getMyCity() {
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        String locationProvider = LocationManager.GPS_PROVIDER;

        try {
            // 获取最后已知位置
            Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
            if (lastKnownLocation != null) {
                getcity(lastKnownLocation);
            }

            locationManager.requestSingleUpdate(locationProvider, new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    getcity(location);
                }
            },null);
        } catch (SecurityException e) {
            String[] list = new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.INTERNET
            };
            requestPermissions(activity, list, 1);
            return;
        }
    }

    private void getcity(Location location) {
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        String cityName = "";

        Geocoder geocoder = new Geocoder(activity, Locale.CHINA);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (!addresses.isEmpty()) {
                cityName = addresses.get(0).getLocality();
                Log.d(TAG,cityName);
            }
            String finalCityName = cityName;
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:getweather('"+ finalCityName +"')");
                }
            });
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
