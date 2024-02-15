package com.example.myapplication;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothHidDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public class ScanBluetooth extends AppCompatActivity {

    private static final String TAG = "BluetoothHidActivity";
    private BluetoothAdapter mBtAdapter;
    private BluetoothHidDevice mHidDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置视图内容等...

        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            // 设备不支持蓝牙
            Log.e(TAG, "Bluetooth is not supported on this device");
            finish();
            return;
        }

        // 注册蓝牙HID设备代理
        mBtAdapter.getProfileProxy(this, new BluetoothProfile.ServiceListener() {
            @Override
            public void onServiceConnected(int i, BluetoothProfile bluetoothProfile) {
                Log.d(TAG, "onServiceConnected: " + i);
                if (i == BluetoothProfile.HID_DEVICE) {
                    if (!(bluetoothProfile instanceof BluetoothHidDevice)) {
                        Log.e(TAG, "Proxy received but it's not BluetoothHidDevice");
                        return;
                    }
                    mHidDevice = (BluetoothHidDevice) bluetoothProfile;
                    registerBluetoothHid();
                    // 启动设备发现
                    startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE), 1);
                }
            }

            @Override
            public void onServiceDisconnected(int i) {
                Log.d(TAG, "onServiceDisconnected: " + i);
                // 服务断开连接的处理
            }
        }, BluetoothProfile.HID_DEVICE);
    }

    private void registerBluetoothHid() {
        // 注册蓝牙HID设备的逻辑
        // 这里可以设置HID设备描述符，订阅客户端配置等
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_CANCELED) {
                // 用户取消设备可发现
                Log.d(TAG, "User cancelled the discoverability");
            } else {
                // 设备现在是可发现的
                int discoverableTimeout = resultCode;
                Log.d(TAG, "Device is now discoverable for " + discoverableTimeout + " seconds");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 解绑蓝牙代理
        if (mBtAdapter != null) {
            mBtAdapter.closeProfileProxy(BluetoothProfile.HID_DEVICE, mHidDevice);
        }
    }
}
