package com.mahmoud.hadith.model.viewmodel.main;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.model.sharedpreference.UserData;
import com.mahmoud.hadith.model.viewmodel.base.BaseViewModel;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class MainViewModel extends BaseViewModel {

    private UserData mUserData;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mUserData = UserData.getInstance(getApplication());
    }

    public boolean checkLanguageArabic() {
        String languageSystem = mUserData.getLanguageSystem(getApplication()
                .getResources()
                .getString(R.string.language_en_value)
        );

        return languageSystem.equals(getApplication()
                .getResources()
                .getString(R.string.language_ar_value)
        );
    }

    public void prepareIntent(Intent intent) {

    }

    public boolean checkIntentForDownloadFragment(Intent intent) {
        if (intent == null) return false;
        String key = getApplication().getResources().getString(R.string.go_download_fragment_key);

        return intent.hasExtra(key);
    }
}
