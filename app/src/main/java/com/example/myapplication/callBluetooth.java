package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothHidDeviceAppQosSettings;
import android.bluetooth.BluetoothHidDeviceAppSdpSettings;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

// 注册信息
public class callBluetooth {
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
    private Context context;
    private ActivityResultLauncher<Intent> requestLauncher;
    private ActivityResultLauncher<Intent> requestLauncher_for_bluetooth;
    private final List<BluetoothDevice> discoveredDevices = new ArrayList<>();

    public callBluetooth(Context context, ActivityResultLauncher<Intent> requestLauncher, ActivityResultLauncher<Intent> requestLauncher_for_bluetooth) {
        this.context = context;
        this.requestLauncher = requestLauncher;
        this.requestLauncher_for_bluetooth = requestLauncher_for_bluetooth;
    }

    public void RunBluetooth() {// 写在一起
        Log.d(TAG, "RUN_Start");
        enableBluetooth();
        // discoverAndPairDevice();
        Log.d(TAG, "outENABLE enterCALL");
        Log.d(TAG, (mBtAdapter.isEnabled() ? "open" : "no"));
        CallBluetooth();
        Log.d(TAG, "RUN_End");
    }

    // 实例化
    public void CallBluetooth() {
        //getPermission();
        Log.d(TAG, "callBluetooth");
        // mBtAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (mBtAdapter == null) {
//            Toast.makeText(this,"Device doesn't support bluetooth",Toast.LENGTH_SHORT).show();
//        } else {
//            if (!mBtAdapter)
//        }

        // 获取BluetoothHidDevice
        mBtAdapter.getProfileProxy(context, new BluetoothProfile.ServiceListener() {
            @Override
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                Log.d(TAG, "onServiceConnected: " + profile);
                Toast.makeText(context, "Okk_connected_service", Toast.LENGTH_SHORT).show();
                if (profile == BluetoothProfile.HID_DEVICE) {
                    Log.d(TAG, "Proxy received but it isn't hid_OUT");
                    if (!(proxy instanceof BluetoothHidDevice)) {
                        Log.e(TAG, "Proxy received but it isn't hid");
                        return;
                    }
                    mHidDevice = (BluetoothHidDevice) proxy;
                    Log.d(TAG, "proxyOK");
                    if (mHidDevice != null) {
                        Toast.makeText(context, "OK for HID profile", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "HID_OK");
                        Log.d(TAG, "Get in register");
                        //getPermission();
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
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            return;
                        }
                        mHidDevice.registerApp(Sdpsettings, null, null, Executors.newCachedThreadPool(), mCallback);
                        // registerApp();// 注册
                    } else {
                        Toast.makeText(context, "Disable for HID profile", Toast.LENGTH_SHORT).show();
                    }
                    // 启用设备发现
                    requestLauncher.launch(new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE));
                    Log.d(TAG, "Discover");
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



    public BluetoothHidDevice.Callback mCallback = new BluetoothHidDevice.Callback() {
        private final int[] mMatchingStates = new int[]{
                BluetoothProfile.STATE_DISCONNECTED,
                BluetoothProfile.STATE_CONNECTING,
                BluetoothProfile.STATE_CONNECTED,
                BluetoothProfile.STATE_DISCONNECTED
        };
        @Override
        public void onAppStatusChanged(BluetoothDevice pluggedDevice, boolean registered) {
            Log.d(TAG, "ccccc_str");
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
            }
            Log.d(TAG, "onAppStatusChanged: " + (pluggedDevice != null ? pluggedDevice.getName() : "null") + "registered:" + registered);
            Toast.makeText(context, "onAppStatusChanged", Toast.LENGTH_SHORT).show();
            if (registered) {
                // 应用已注册
                List<BluetoothDevice> matchingDevices = mHidDevice.getDevicesMatchingConnectionStates(mMatchingStates);
                Log.d(TAG, "paired devices: " + matchingDevices + "  " + mHidDevice.getConnectionState(pluggedDevice));
                Toast.makeText(context, "paired devices: " + matchingDevices + "  " + mHidDevice.getConnectionState(pluggedDevice), Toast.LENGTH_SHORT).show();
                if (pluggedDevice != null && mHidDevice.getConnectionState(pluggedDevice) != BluetoothProfile.STATE_CONNECTED) {
                    boolean result = mHidDevice.connect(pluggedDevice);// pluggedDevice即为连接到模拟HID的设备
                    Log.d(TAG, "hidDevice connect:" + result);
                    Toast.makeText(context, "hidDevice connect:" + result, Toast.LENGTH_SHORT).show();
                } else if (matchingDevices != null && matchingDevices.size() > 0) {
                    // 选择连接的设备
                    mHostDevice = matchingDevices.get(0);// 获得第一个已经配对过的设备
                    Toast.makeText(context, "device_is_ok: " + mHostDevice.getName() + mHostDevice.getAddress(), Toast.LENGTH_SHORT).show();
                } else {
                    // 注册成功未配对
                }
            } else {
                // 应用未注册
            }
        }

        @Override
        public void onConnectionStateChanged(BluetoothDevice device, int state) {
            Log.d(TAG, "onConnectStateChanged:" + device + "  state:" + state);
            Toast.makeText(context, state, Toast.LENGTH_SHORT).show();
            if (state == BluetoothProfile.STATE_CONNECTED) {// 已经连接了
                mHostDevice = device;
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
                Toast.makeText(context, "device_is_ok: " + mHostDevice.getName() + mHostDevice.getAddress(), Toast.LENGTH_SHORT).show();
            } else if (state == BluetoothProfile.STATE_DISCONNECTED) {
                mHostDevice = null;
                Toast.makeText(context, "device_is_null", Toast.LENGTH_SHORT).show();
            } else if (state == BluetoothProfile.STATE_CONNECTING) {

            }
        }
    };


    // Android设备注册为蓝牙设备


    public void sendReport() {
        if (mHidDevice != null && mHostDevice != null) {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(context, "Bluetooth connect permission is denied", Toast.LENGTH_SHORT).show();
                // TODO: Consider calling
                return;
            }
            byte[] report = new byte[]{0x04};// a
            mHidDevice.sendReport(mHostDevice, 1, report);
            Toast.makeText(context, "has_sent_a", Toast.LENGTH_SHORT).show();
        }
    }


    public void enableBluetooth() {
        Log.d(TAG,"enableBluetooth");
        //getPermission();
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            // 设备不支持蓝牙
            Toast.makeText(context, "Device doesn't support bluetooth", Toast.LENGTH_SHORT).show();
        } else {
            if (!mBtAdapter.isEnabled()) {
                // 如果蓝牙未开启，请求用户开启蓝牙
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                requestLauncher.launch(enableBtIntent);
                Log.d(TAG, "OpenBLE");
            } else {
                // 蓝牙已经开启
                Toast.makeText(context, "Bluetooth is already enabled", Toast.LENGTH_SHORT).show();
                CallBluetooth();
            }
        }
    }

    public boolean isSupportBluetoothHid() {
        PackageManager pm = context.getPackageManager();
        Intent intent = new Intent("android.bluetooth.IBluetoothHidDevice");
        List<ResolveInfo> results = pm.queryIntentServices(intent, 0);
        if (results == null) {
            return false;
        }
        ComponentName comp = null;
        for (int i = 0; i < results.size(); i++) {
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


    public void discoverAndPairDevice() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.d(TAG,"PairStart");

        // 注册设备发现的广播接收器
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        context.registerReceiver(receiver, filter);

        // 开始设备发现
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        bluetoothAdapter.startDiscovery();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // 发现设备
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d(TAG,"findDevice");

                // 添加设备到列表
                discoveredDevices.add(device);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                // 设备发现结束，显示设备列表
                showDeviceList();
            }
        }
    };

    private void showDeviceList() {
        // 创建设备名称列表
        List<String> deviceNames = new ArrayList<>();
        for (BluetoothDevice device : discoveredDevices) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            deviceNames.add(device.getName());
        }
        Log.d(TAG, "Show");
        // 创建并显示 AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Select a device")
                .setItems(deviceNames.toArray(new String[0]), (dialog, which) -> {
                    // 用户选择了一个设备，进行配对操作
                    BluetoothDevice selectedDevice = discoveredDevices.get(which);
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        return;
                    }
                    if (selectedDevice.getBondState() != BluetoothDevice.BOND_BONDED) {
                        selectedDevice.createBond();
                    }
                })
                .show();
    }




    // 以下为废稿
    private void registerApp() {
        Log.d(TAG, "Get in register");
        //getPermission();
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

        // 注册应用
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        Log.d(TAG, "Before callback");
        mHidDevice.registerApp(Sdpsettings, null, qosSettings, Executors.newCachedThreadPool(), new BluetoothHidDevice.Callback() {
            private final int[] mMatchingStates = new int[]{
                    BluetoothProfile.STATE_DISCONNECTED,
                    BluetoothProfile.STATE_CONNECTING,
                    BluetoothProfile.STATE_CONNECTED,
                    BluetoothProfile.STATE_DISCONNECTED
            };

            @Override
            public void onAppStatusChanged(BluetoothDevice pluggedDevice, boolean registered) {
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                }
                Log.d(TAG, "ccccc_str");
                Log.d(TAG, "onAppStatusChanged: " + (pluggedDevice != null ? pluggedDevice.getName() : "null") + "registered:" + registered);
                Toast.makeText(context, "onAppStatusChanged", Toast.LENGTH_SHORT).show();
                if (registered) {
                    // 应用已注册
                    List<BluetoothDevice> matchingDevices = mHidDevice.getDevicesMatchingConnectionStates(mMatchingStates);
                    Log.d(TAG, "paired devices: " + matchingDevices + "  " + mHidDevice.getConnectionState(pluggedDevice));
                    Toast.makeText(context, "paired devices: " + matchingDevices + "  " + mHidDevice.getConnectionState(pluggedDevice), Toast.LENGTH_SHORT).show();
                    if (pluggedDevice != null && mHidDevice.getConnectionState(pluggedDevice) != BluetoothProfile.STATE_CONNECTED) {
                        boolean result = mHidDevice.connect(pluggedDevice);// pluggedDevice即为连接到模拟HID的设备
                        Log.d(TAG, "hidDevice connect:" + result);
                        Toast.makeText(context, "hidDevice connect:" + result, Toast.LENGTH_SHORT).show();
                    } else if (matchingDevices != null && matchingDevices.size() > 0) {
                        // 选择连接的设备
                        mHostDevice = matchingDevices.get(0);// 获得第一个已经配对过的设备
                        Toast.makeText(context, "device_is_ok: " + mHostDevice.getName() + mHostDevice.getAddress(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(context, state, Toast.LENGTH_SHORT).show();
                if (state == BluetoothProfile.STATE_CONNECTED) {// 已经连接了
                    mHostDevice = device;
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        return;
                    }
                    Toast.makeText(context, "device_is_ok: " + mHostDevice.getName() + mHostDevice.getAddress(), Toast.LENGTH_SHORT).show();
                } else if (state == BluetoothProfile.STATE_DISCONNECTED) {
                    mHostDevice = null;
                    Toast.makeText(context, "device_is_null", Toast.LENGTH_SHORT).show();
                } else if (state == BluetoothProfile.STATE_CONNECTING) {

                }
            }
            // 你可以重写其他回调方法以处理连接、断开连接、报告等事件
        });
    }





}