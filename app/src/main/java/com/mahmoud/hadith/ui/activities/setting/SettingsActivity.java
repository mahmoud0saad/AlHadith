package com.mahmoud.hadith.ui.activities.setting;

import android.os.Bundle;

import androidx.appcompat.app.ActionBar;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.ui.activities.base.BaseActivity;
import com.mahmoud.hadith.ui.fragment.setting.SettingFragment;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


}