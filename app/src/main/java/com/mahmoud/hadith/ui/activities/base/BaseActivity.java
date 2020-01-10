package com.mahmoud.hadith.ui.activities.base;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.model.viewmodel.base.BaseViewModel;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public abstract class BaseActivity extends AppCompatActivity {
    private BaseViewModel mBaseViewMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBaseViewMode = ViewModelProviders.of(this).get(BaseViewModel.class);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mBaseViewMode.isLanguageSystemArabic()) {
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

        } else {
            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

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
    protected void onResume() {
        super.onResume();


        if (mBaseViewMode.userData.getChangeLanguageSystem()) {
            mBaseViewMode.userData.setChangeLanguageSystem(false);

            if (mBaseViewMode.isLanguageSystemArabic()) {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

            } else {
                getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

            }

        }
    }
}
