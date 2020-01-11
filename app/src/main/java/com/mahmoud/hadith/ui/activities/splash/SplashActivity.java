package com.mahmoud.hadith.ui.activities.splash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.model.sharedpreference.UserData;
import com.mahmoud.hadith.model.utils.Utils;
import com.mahmoud.hadith.ui.activities.main.MainActivity;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class SplashActivity extends Activity {
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    private UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        userData = UserData.getInstance(this);


        new Handler().postDelayed(() -> {

            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            Utils.switchLocal(this, userData.getLanguageSystem(), this);

            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            finish();
        }, SPLASH_DISPLAY_LENGTH);

    }
}
