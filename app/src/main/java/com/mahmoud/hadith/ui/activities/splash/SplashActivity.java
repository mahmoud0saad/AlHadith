package com.mahmoud.hadith.ui.activities.splash;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.mahmoud.hadith.model.sharedpreference.UserData;
import com.mahmoud.hadith.model.utils.Utils;
import com.mahmoud.hadith.ui.activities.main.MainActivity;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userData = UserData.getInstance(this);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            Intent intent = new Intent();
//            String packageName = getPackageName();
//            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
//            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
//                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
//                intent.setData(Uri.parse("package:" + packageName));
//                startActivityForResult(intent, 1000);
//                Log.i(TAG, "onCreate: done 1");
//            }
//        }


//        BroadcastReceiver br = new BootCompleteBroadCast();
//        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
//        filter.addAction(Intent.ACTION_LOCKED_BOOT_COMPLETED);
//        filter.addAction(Intent.ACTION_REBOOT);
//        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
//
//        registerReceiver(br, filter, Manifest.permission.RECEIVE_BOOT_COMPLETED, null);
//
//
//        ComponentName receiver = new ComponentName(this, BootCompleteBroadCast.class);
//        PackageManager pm = this.getPackageManager();
//
//        pm.setComponentEnabledSetting(receiver,
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                PackageManager.DONT_KILL_APP);


        Utils.switchLocal(this, userData.getLanguageSystem(), this, false);
        Log.i(TAG, "onCreate: done 2");

        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        Log.i(TAG, "onCreate: done 3");
        finish();


    }

}
