package com.example.myapplication;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.bluetooth.BluetoothHidDeviceAppSdpSettings;
import android.bluetooth.BluetoothHidDeviceAppQosSettings;
import android.bluetooth.BluetoothHidDevice;  // HID库
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.midi.MidiDeviceService;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;
import android.content.Context;

import com.example.myapplication.HidConfig;// 描述符

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.createWebView();

        // 蓝牙【目前有问题暂时不解决】
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.BLUETOOTH_CONNECT}, 0);
        }
        Toast.makeText(this, "Written by Alphabet@Gitee.THU come from a game of Tsinghua in winter vacation.", Toast.LENGTH_SHORT).show();
        this.callBluetooth();
        this.enableBluetooth();// 启动蓝牙

        // 检测一下HID的支持情况
        if(isSupportBluetoothHid()){
            Toast.makeText(this,"系统支持蓝牙HID",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"系统不支持蓝牙HID",Toast.LENGTH_SHORT).show();
        }
        Button buttona = findViewById(R.id.button_send_a);
        buttona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendReport();
            }
        });

    }

    // 申请权限
    public ArrayList<String> requestList = new ArrayList<String>();
    private static final int REQ_PERMISSION_CODE = 1;

    public void getPermission() {
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
    private void createWebView() {
        final WebView webView = (WebView) findViewById(R.id.webview);
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

    /*
     * 2024/2/19 重新组织
     * */
    private static final String TAG = "BtMain";

    private static final int CONNECT_SUCCESS = 0x01;
    private static final int CONNECT_FAILURE = 0x02;
    private static final int DISCONNECT_SUCCESS = 0x03;
    private static final int SEND_SUCCESS = 0x04;
    private static final int SEND_FAILURE = 0x05;
    private static final int RECEIVE_SUCCESS = 0x06;
    private static final int RECEIVE_FAILURE = 0x07;
    private static final int START_DISCOVERY = 0x08;
    private static final int STOP_DISCOVERY = 0x09;
    private static final int DISCOVERY_DEVICE = 0x0A;
    private static final int DEVICE_BOND_NONE = 0x0B;
    private static final int DEVICE_BONDING = 0x0C;
    private static final int DEVICE_BONDED = 0x0D;

    private BluetoothHidDevice mHidDevice;
    private BluetoothDevice mHostDevice;
    private BluetoothAdapter mBtAdapter;

    private final ActivityResultLauncher<Intent> mRequestDiscoverableLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // 用户已启用设备发现
                } else {
                    // 用户未启用设备发现
                }
            }
    );

    // 实例化
    private void callBluetooth() {
        getPermission();
        Log.d(TAG, "callBluetooth");
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (mBtAdapter == null) {
//            Toast.makeText(this,"Device doesn't support bluetooth",Toast.LENGTH_SHORT).show();
//        } else {
//            if (!mBtAdapter)
//        }

        // 获取BluetoothHidDevice
        mBtAdapter.getProfileProxy(this, new BluetoothProfile.ServiceListener() {
            @Override
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                Log.d(TAG, "onServiceConnected: " + profile);
                Toast.makeText(MainActivity.this, "Okk_connected_service", Toast.LENGTH_SHORT).show();
                if (profile == BluetoothProfile.HID_DEVICE) {
                    if (!(proxy instanceof BluetoothHidDevice)) {
                        Log.e(TAG, "Proxy received but it isn't hid");
                        return;
                    }
                    mHidDevice = (BluetoothHidDevice) proxy;
                    if (mHidDevice!=null){
                        Toast.makeText(MainActivity.this,"OK for HID profile",Toast.LENGTH_SHORT).show();
                        registerApp();// 注册
                    } else {
                        Toast.makeText(MainActivity.this,"Disable for HID profile",Toast.LENGTH_SHORT).show();
                    }
                    // 启用设备发现
                    mRequestDiscoverableLauncher.launch(new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE));
                }
            }

            @Override
            public void onServiceDisconnected(int profile) {// 断开连接
                if (profile == BluetoothProfile.HID_DEVICE) {
                    Log.d(TAG, "onServiceDisconnected:" + profile);
                    mHidDevice = null;
                }
            }
        }, BluetoothProfile.HID_DEVICE);
    }

    // Android设备注册为蓝牙设备
    private void registerApp() {
        getPermission();
        // 创建一个BluetoothHidDeviceAppSdpSettings对象
        BluetoothHidDeviceAppSdpSettings Sdpsettings = new BluetoothHidDeviceAppSdpSettings(
                HidConfig.KEYBOARD_NAME,
                HidConfig.DESCRIPTION,
                HidConfig.PROVIDER,
                BluetoothHidDevice.SUBCLASS1_KEYBOARD,
                HidConfig.KEYBOARD_COMBO
        );

        // 创建一个BluetoothHidDeviceAppQosSettings对象，随机设置的(
        BluetoothHidDeviceAppQosSettings qosSettings = new BluetoothHidDeviceAppQosSettings(
                BluetoothHidDeviceAppQosSettings.SERVICE_BEST_EFFORT,
                800, 9, 0, 11250, BluetoothHidDeviceAppQosSettings.MAX);
//        BluetoothHidDeviceAppQosSettings qosOut = new BluetoothHidDeviceAppQosSettings(
//                BluetoothHidDeviceAppQosSettings.SERVICE_GUARANTEED,
//                0, 0, 0, 0, BluetoothHidDeviceAppQosSettings.MAX);

        // 注册你的应用
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mHidDevice.registerApp(Sdpsettings, null, qosSettings, Executors.newCachedThreadPool(), new BluetoothHidDevice.Callback() {
            private final int[] mMatchingStates = new int[]{
                    BluetoothProfile.STATE_DISCONNECTED,
                    BluetoothProfile.STATE_CONNECTING,
                    BluetoothProfile.STATE_CONNECTED,
                    BluetoothProfile.STATE_DISCONNECTED
            };

            @Override
            public void onAppStatusChanged(BluetoothDevice pluggedDevice, boolean registered) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Log.d(TAG, "onAppStatusChanged: " + (pluggedDevice != null ? pluggedDevice.getName() : "null") + "registered:" + registered);
                Toast.makeText(MainActivity.this, "onAppStatusChanged", Toast.LENGTH_SHORT).show();
                if (registered) {
                    // 应用已注册
                    List<BluetoothDevice> matchingDevices = mHidDevice.getDevicesMatchingConnectionStates(mMatchingStates);
                    Log.d(TAG, "paired devices: " + matchingDevices + "  " + mHidDevice.getConnectionState(pluggedDevice));
                    Toast.makeText(MainActivity.this, "paired devices: " + matchingDevices + "  " + mHidDevice.getConnectionState(pluggedDevice), Toast.LENGTH_SHORT).show();
                    if (pluggedDevice != null && mHidDevice.getConnectionState(pluggedDevice) != BluetoothProfile.STATE_CONNECTED) {
                        boolean result = mHidDevice.connect(pluggedDevice);// pluggedDevice即为连接到模拟HID的设备
                        Log.d(TAG, "hidDevice connect:" + result);
                        Toast.makeText(MainActivity.this, "hidDevice connect:" + result, Toast.LENGTH_SHORT).show();
                    } else if (matchingDevices != null && matchingDevices.size() > 0) {
                        // 选择连接的设备
                        mHostDevice = matchingDevices.get(0);// 获得第一个已经配对过的设备
                        Toast.makeText(MainActivity.this, "device_is_ok: " + mHostDevice.getName() + mHostDevice.getAddress(), Toast.LENGTH_SHORT).show();
                    } else {
                        // 注册成功未配对
                    }
                } else {
                    // 应用未注册
                }
            }

            @Override
            public void onConnectionStateChanged(BluetoothDevice device, int state) {
                Log.d(TAG, "omVonnectStateChanged:" + device + "  state:" + state);
                Toast.makeText(MainActivity.this,state,Toast.LENGTH_SHORT).show();
                if (state == BluetoothProfile.STATE_CONNECTED) {// 已经连接了
                    mHostDevice = device;
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Toast.makeText(MainActivity.this, "device_is_ok: " + mHostDevice.getName() + mHostDevice.getAddress(), Toast.LENGTH_SHORT).show();
                } else if (state == BluetoothProfile.STATE_DISCONNECTED) {
                    mHostDevice = null;
                    Toast.makeText(MainActivity.this, "device_is_null", Toast.LENGTH_SHORT).show();
                } else if (state == BluetoothProfile.STATE_CONNECTING) {

                }
            }

            // 你可以重写其他回调方法以处理连接、断开连接、报告等事件
        });
    }

    public void sendReport() {
        if (mHidDevice != null && mHostDevice != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Bluetooth connect permission is denied", Toast.LENGTH_SHORT).show();
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            byte[] report = new byte[]{0x04};// a
            mHidDevice.sendReport(mHostDevice, 1, report);
            Toast.makeText(this, "has_sent_a", Toast.LENGTH_SHORT).show();
        }
    }

    private final ActivityResultLauncher<Intent> mRequestEnableBtLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // 用户已开启蓝牙
                } else {
                    // 用户未开启蓝牙
                }
            }
    );

    private void enableBluetooth() {
        getPermission();
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // 设备不支持蓝牙
            Toast.makeText(this, "Device doesn't support bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                // 如果蓝牙未开启，请求用户开启蓝牙
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                mRequestEnableBtLauncher.launch(enableBtIntent);
            } else {
                // 蓝牙已经开启
                Toast.makeText(this,"Bluetooth is already enabled",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean isSupportBluetoothHid(){
        PackageManager pm = this.getPackageManager();
        Intent intent = new Intent("android.bluetooth.IBluetoothHidDevice");
        List<ResolveInfo> results = pm.queryIntentServices(intent,0);
        if (results == null) {
            return false;
        }
        ComponentName comp = null;
        for (int i=0; i<results.size(); i++) {
            ResolveInfo ri = results.get(i);
            if ((ri.serviceInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                continue;
            }
            ComponentName foundComp = new ComponentName(ri.serviceInfo.applicationInfo.packageName,
                    ri.serviceInfo.name);

            if (comp != null) {
                throw new IllegalStateException("Multiple system services handle " + this
                        + ": " + comp + ", " + foundComp);
            }
            comp = foundComp;
        }
        return comp != null;
    }
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