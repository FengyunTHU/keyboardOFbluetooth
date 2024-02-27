package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "BtMain";
    private callBluetooth callBluetooth;
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.webview);
        this.createWebView(webView);

        // 蓝牙【目前有问题暂时不解决】
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, 0);
        }
        Toast.makeText(this, "Written by Alphabet@Gitee.THU come from a game of Tsinghua in winter vacation.", Toast.LENGTH_SHORT).show();
        callBluetooth = new callBluetooth(this,mRequestLauncher);
//        callBluetooth.CallBluetooth();
//        callBluetooth.enableBluetooth();// 启动蓝牙
        callBluetooth.RunBluetooth();

        // 检测一下HID的支持情况
        if(callBluetooth.isSupportBluetoothHid()){
            Toast.makeText(this,"系统支持蓝牙HID",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"系统不支持蓝牙HID",Toast.LENGTH_SHORT).show();
        }
        Log.d(TAG,"END_Main");
//        Button buttona = findViewById(R.id.button_send_a);
//        buttona.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                callBluetooth.sendReport();
//            }
//        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }

    // 申请权限
    public ArrayList<String> requestList = new ArrayList<String>();
    private static final int REQ_PERMISSION_CODE = 1;

    public void getPermission() {// 暂时先不用
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestList.add(android.Manifest.permission.INTERNET);
            requestList.add(android.Manifest.permission.BLUETOOTH);
            requestList.add(android.Manifest.permission.BLUETOOTH_ADMIN);
            requestList.add(android.Manifest.permission.BLUETOOTH_ADVERTISE);
            requestList.add(android.Manifest.permission.BLUETOOTH_SCAN);
            requestList.add(android.Manifest.permission.BLUETOOTH_CONNECT);
            requestList.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            requestList.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            requestList.add(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION);
        }
        if (requestList.size() != 0) {
            ActivityCompat.requestPermissions(this, requestList.toArray(new String[0]), REQ_PERMISSION_CODE);
        }
    }

    // 创建WebView
    @SuppressLint("SetJavaScriptEnabled")
    private void createWebView(WebView webView) {

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        if (Build.VERSION.SDK_INT > 11) {
            webView.getSettings().setDisplayZoomControls(false);
        }
        // webView.setWebViewClient(new WebViewClient(){});
        String url = "file:///android_asset/index.html";

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
            webView.loadUrl(url);
            // 确保WebView已经准备好接收JavaScript代码
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    webView.evaluateJavascript(jsScript, null);
                }
            });
//            if (webView != null) {
//                webView.evaluateJavascript(jsScript, null);
//            }
        } catch (IOException e) {
            // 处理异常
            e.printStackTrace();
        }

    }

    private final ActivityResultLauncher<Intent> mRequestLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // 用户已启用设备发现
                } else {
                    // 用户未启用设备发现
                }
            }
    );


//    private final ActivityResultLauncher<Intent> mRequestEnableBtLauncher = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            result -> {
//                if (result.getResultCode() == RESULT_OK) {
//                    // 用户已开启蓝牙
//                } else {
//                    // 用户未开启蓝牙
//                }
//            }
//    );

}


//    private static final String TAG = "BtMain";
//
//    private static final int CONNECT_SUCCESS = 0x01;
//    private static final int CONNECT_FAILURE = 0x02;
//    private static final int DISCONNECT_SUCCESS = 0x03;
//    private static final int SEND_SUCCESS = 0x04;
//    private static final int SEND_FAILURE= 0x05;
//    private static final int RECEIVE_SUCCESS= 0x06;
//    private static final int RECEIVE_FAILURE =0x07;
//    private static final int START_DISCOVERY = 0x08;
//    private static final int STOP_DISCOVERY = 0x09;
//    private static final int DISCOVERY_DEVICE = 0x0A;
//    private static final int DEVICE_BOND_NONE= 0x0B;
//    private static final int DEVICE_BONDING = 0x0C;
//    private static final int DEVICE_BONDED = 0x0D;
//
//    // 蓝牙
//    private BluetoothAdapter bluetoothAdapter;
//    private BtBroadcastReceiver btBroadcastReceiver;
//    // UUID
//    public static final String MY_BLUETOOTH_UUID = "00001101-0000-1000-8000-00805F9B34FB";  //蓝牙通讯[非手机]
//    public static final String MY_BLUETOOTH_UUID_PHONE = "00001105-0000-1000-8000-00805f9b34fb";// 手机端
//    // 连接设备
//    private BluetoothDevice curbluetoothDevice;
//    // 连接线程
//    private ConnectThread connectThread;
//    // 管理线程
//    private ConnectedThread connectedThread;
//    // 连接状态
//    private boolean curConnState = false;
//    // 配对状态
//    private boolean curBondState = false;
//
//    // 定义一个message集合
//    Handler mHandler = new Handler(Looper.getMainLooper()) {
//        @SuppressLint("HandlerLeak")
//        @Override
//        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
//
//        }
//    };
//
//    private void initBluetooth() {
//        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (bluetoothAdapter == null) {
//            Toast.makeText(this, "当前设备不支持蓝牙", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            if (bluetoothAdapter.isEnabled()) {
//                Toast.makeText(this, "手机蓝牙已开启", Toast.LENGTH_SHORT).show();
//            }
//            else {
//                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                startActivity(enableBtIntent);
//            }
//        }
//    }
//
//    // 搜索设备的函数
//    private void searchBtDevice() {
//        if (bluetoothAdapter.isDiscovering()){
//            return;
//        }
//        bluetoothAdapter.startDiscovery();
//    }
//
//
//    // 搜索监听【不知干what的】
//    public interface OnDeviceSearchListener {
//        void onDiscoveryStart();
//        void onDiscoveryStop();
//        void onDeviceFound(BluetoothDevice bluetoothDevice,int rssi);// 搜索到设备
//    }
//    private static OnDeviceSearchListener onDeviceSearchListener;
//    public void setOnDeviceSearchListener(OnDeviceSearchListener onDeviceSearchListener) {
//        this.onDeviceSearchListener = onDeviceSearchListener;
//    }
//
//    // 蓝牙广播接受器
//    private static class BtBroadcastReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context context,Intent intent) {
//            String action = intent.getAction();
//            if (TextUtils.equals(action, BluetoothAdapter.ACTION_DISCOVERY_STARTED)) {// 开启搜索
//                if (onDeviceSearchListener != null) {
//                    onDeviceSearchListener.onDiscoveryStart();
//                }
//            } else if (TextUtils.equals(action,BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {// 完成搜索
//                if (onDeviceSearchListener != null) {
//                    onDeviceSearchListener.onDiscoveryStop();
//                }
//            } else if (TextUtils.equals(action,BluetoothDevice.ACTION_FOUND)) {// 搜索到设备
//                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                // 添加信号强度
//                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
//
//                Log.d(TAG,"扫描到设备："+ bluetoothDevice.getName() + "-->" + bluetoothDevice.getAddress());
//                if (onDeviceSearchListener != null) {
//                    onDeviceSearchListener.onDeviceFound(bluetoothDevice,rssi);// 回调重新
//                }
//            }
//        }
//    }
//
//    // 注册广播接受器
//    private void initBtBroadcast() {
//        btBroadcastReceiver = new BtBroadcastReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
//        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
//        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
//        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
//        registerReceiver(btBroadcastReceiver,intentFilter);
//    }
//    @Override
//    protected void onDestroy(){
//        super.onDestroy();
//        // 注销
//        unregisterReceiver(btBroadcastReceiver);
//    }
//
//    /*
//    * 开始连接
//    *  @param bluetoothDevice 蓝牙设备
//    *  @param uuid 发起连接的UUID
//    *  @param conOutTime 连接超时时间
//    * */
//    public void startConnectDevice(final BluetoothDevice bluetoothDevice,String uuid,long conOutTime){
//        if(bluetoothDevice == null) {
//            Log.e(TAG,"startConnectDevice-->bluetoothDevice == null");
//            return;// 直接返回
//        }
//        if(bluetoothAdapter == null){
//            Log.e(TAG,"startConnectDevice-->bluetoothAdapter == null");
//            return;// 直接返回
//        }
//        // 连接开始
//        connectThread = new ConnectThread(bluetoothAdapter,curbluetoothDevice,uuid);
//        connectThread.setOnBluetoothConnectListener(new ConnectThread.OnBluetoothConnectListener(){
//            @Override
//            public void onStartConn(){
//                Log.d(TAG,"startConnectDevice-->开始连接…"+bluetoothDevice.getName()+"-->"+bluetoothDevice.getAddress());
//            }
//            @Override
//            public void onConnSuccess(BluetoothSocket bluetoothSocket) {
//                // 注意连接超时
//                Log.d(TAG,"startConnect-->移除连接超时");
//                Log.w(TAG,"startConnect-->连接成功");
//                // 暂时省去Handler的UI更新
//                curConnState = true;
//
//            }
//        });
//    }
//
//    /*
//    * 管理连接和数据收发
//    * @param bluetoothSocket 已经建立的连接
//    * */