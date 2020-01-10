package com.mahmoud.hadith.model.viewmodel.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.mahmoud.hadith.model.sharedpreference.UserData;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class BaseViewModel extends AndroidViewModel {
    public UserData userData;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        userData = UserData.getInstance(getApplication());
    }

    public boolean isLanguageSystemArabic() {
        return userData.isSystemLanguageArabic();
    }
}
