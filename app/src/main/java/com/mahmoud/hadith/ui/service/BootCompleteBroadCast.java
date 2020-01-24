package com.mahmoud.hadith.ui.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.mahmoud.hadith.model.sharedpreference.UserData;

public class BootCompleteBroadCast extends BroadcastReceiver {
    private static final String TAG = "IntialServiceBroadCast";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive: " + intent.getAction());

        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            UserData userData = UserData.getInstance(context);

            Intent in = new Intent(context, IntialServiceBroadCast.class);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, in, 0);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if (alarmManager != null)
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * userData.getAzkarReplayTime(), pendingIntent);

        }
    }
}
