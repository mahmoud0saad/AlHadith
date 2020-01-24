package com.mahmoud.hadith.ui.activities.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.model.viewmodel.base.BaseViewModel;
import com.mahmoud.hadith.ui.fragment.setting.SettingFragment;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class SettingsActivity extends AppCompatActivity {
    private BaseViewModel mBaseViewMode;
    private Fragment mSettingFragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        mSettingFragment = new SettingFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, mSettingFragment)
                .commit();

        mBaseViewMode = ViewModelProviders.of(this).get(BaseViewModel.class);

        initAppBar();
    }

    private void initAppBar() {

        setSupportActionBar(findViewById(R.id.my_toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!mBaseViewMode.isLanguageSystemArabic()) {
            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

        } else {
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);


        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSettingFragment.onActivityResult(requestCode, resultCode, data);
    }
}