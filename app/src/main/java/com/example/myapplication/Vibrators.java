package com.example.myapplication;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.webkit.JavascriptInterface;

public class Vibrators {
    private static final String TAG = "BtMain";
    private Vibrator vibrator;
    private Context context;
    public Vibrators(Context context) {
        Log.d(TAG,"InitV");
        this.context = context;
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @JavascriptInterface
    public void vibraOnce() {
        if (vibrator.hasVibrator()){
            if (Build.VERSION.SDK_INT >= 26){
                vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                Log.d(TAG,"vibrator Once");

            } else {
                vibrator.vibrate(50);
            }
        }
    }
}
