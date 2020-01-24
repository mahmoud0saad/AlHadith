package com.mahmoud.hadith.ui.fragment.setting;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SeekBarPreference;
import androidx.preference.SwitchPreference;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.model.sharedpreference.UserData;
import com.mahmoud.hadith.model.utils.Utils;
import com.mahmoud.hadith.ui.service.BootCompleteBroadCast;
import com.mahmoud.hadith.ui.service.IntialServiceBroadCast;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class SettingFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
    private static final String TAG = "SettingFragment";

    private final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 23;
    private ListPreference languagePublicHadithPreference;
    private ListPreference languageSpecialHadithPreference;
    private ListPreference languageSystemPreference;
    private SwitchPreference azkarSwitchPreference;
    private SeekBarPreference seekBarPreference;
    private UserData mUserData;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        languagePublicHadithPreference = findPreference(getString(R.string.shared_public_language_key));
        languageSpecialHadithPreference = findPreference(getString(R.string.shared_special_language_key));
        languageSystemPreference = findPreference(getString(R.string.shared_system_language_key));
        azkarSwitchPreference = findPreference(getString(R.string.shared_azkar_enable_key));
        seekBarPreference = findPreference(getString(R.string.shared_seek_bar_minute_key));

        mUserData = UserData.getInstance(getContext());


        setChangeListener(languageSystemPreference);
        setChangeListener(languageSpecialHadithPreference);

        initLanguagePublic();


        initAllSummary();

        initAzkarSwitch();

        initSeekBar();

    }

    private void initAllSummary() {
        seekBarPreference.setSummary(String.valueOf(mUserData.getAzkarReplayTime()));


    }

    private void initSeekBar() {

        seekBarPreference.setEnabled(!mUserData.isAzkzrEnable());


        seekBarPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            Log.i(TAG, "onPreferenceChange: " + mUserData.getAzkarReplayTime());
            preference.setSummary(newValue.toString());
            return true;
        });

    }


    private void initLanguagePublic() {
        if (languagePublicHadithPreference == null) return;

        if (languagePublicHadithPreference.getSummary().toString().equals(getString(R.string.language_ar_entrie))) {
            languageSpecialHadithPreference.setVisible(true);
        } else {
            languageSpecialHadithPreference.setVisible(false);

        }

        languagePublicHadithPreference.setOnPreferenceChangeListener((x, y) -> {
            makeChangePreference();
            if (y.toString().equals(getString(R.string.language_ar_value))) {
                languageSpecialHadithPreference.setVisible(true);
            } else {
                languageSpecialHadithPreference.setVisible(false);
            }
            return true;
        });
    }

    private void setChangeListener(Preference preference) {
        if (preference != null)
            preference.setOnPreferenceChangeListener(this);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        makeChangePreference();
        if (preference.getKey().equals(getResources().getString(R.string.shared_system_language_key))) {
            Utils.switchLocal(getContext(), newValue.toString(), getActivity(), true);
            mUserData.setChangeLanguageSystem(true);
        }
        return true;
    }

    private void makeChangePreference() {
        mUserData.setEventChange(true);

    }

    public void getPermissionOverLayer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(getContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getContext().getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!hasPermissionOverLayer()) {
                    azkarSwitchPreference.setChecked(false);
                } else {
                    startAlarm(getContext());
                }
            }
        }

    }

    private void initAzkarSwitch() {

        azkarSwitchPreference.setChecked(mUserData.isAzkzrEnable());

        azkarSwitchPreference.setOnPreferenceChangeListener((preference, newValue) -> {

            if ((boolean) newValue) {
                Log.i(TAG, "onPreferenceChange: alarm is created");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (hasPermissionOverLayer()) {
                        startAlarm(getContext());
                    } else {
                        getPermissionOverLayer();
                    }
                } else {
                    startAlarm(getContext());
                }

            } else {
                Log.i(TAG, "onPreferenceChange: alarm is stop");
                stopAlarm(getContext());

            }
            seekBarPreference.setEnabled(!(Boolean) newValue);

            return true;

        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasPermissionOverLayer() {
        return Settings.canDrawOverlays(getContext());
    }

    private void startAlarm(Context context) {
        Intent in = new Intent(context, IntialServiceBroadCast.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, in, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        int x = mUserData.getAzkarReplayTime();

        Log.i(TAG, "startAlarm: " + x);

        if (alarmManager != null)
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 60 * x, pendingIntent);


    }

    private void stopAlarm(Context context) {
        Intent in = new Intent(context, IntialServiceBroadCast.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, in, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null)
            alarmManager.cancel(pendingIntent);


    }

    private void registerBootedComplete(Context context) {
        BroadcastReceiver br = new BootCompleteBroadCast();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
        context.registerReceiver(br, filter);

    }

    private void unRegisterBootedComplete(Context context) {
        BroadcastReceiver br = new BootCompleteBroadCast();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
        context.unregisterReceiver(br);

    }
}
