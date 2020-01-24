package com.mahmoud.hadith.ui.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.mahmoud.hadith.model.sharedpreference.UserData;

public class IntialServiceBroadCast extends BroadcastReceiver {
    private static final String TAG = "IntialServiceBroadCast";

    private UserData userData;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, "onReceive: is here done ");

        userData = UserData.getInstance(context);

        if (userData.isAzkzrEnable()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, OverLayerService.class));

            } else {
                context.startService(new Intent(context, OverLayerService.class));
            }
        }

    }
}
