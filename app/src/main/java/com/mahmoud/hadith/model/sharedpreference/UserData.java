package com.mahmoud.hadith.model.sharedpreference;

import android.content.Context;
import android.content.ContextWrapper;

import com.mahmoud.hadith.R;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class UserData extends ContextWrapper {

    private static UserData instance;
    private SharedPreferences sharedPreferences;

    public UserData(Context base) {
        super(base);
        sharedPreferences = SharedPreferences.getInstance(base);
    }

    public static synchronized UserData getInstance(Context context) {
        if (instance == null) {
            instance = new UserData(context);
        }
        return instance;
    }

    public String getLanguageSystem(String stringDefault) {
        return sharedPreferences.getString(getResources().getString(R.string.shared_system_language_key), stringDefault);
    }

    public String getLanguagePublic(String stringDefault) {
        return sharedPreferences.getString(getResources().getString(R.string.shared_public_language_key), stringDefault);
    }

    public String getLanguageSpecial(String stringDefault) {
        return sharedPreferences.getString(getResources().getString(R.string.shared_special_language_key), stringDefault);
    }

    public String getLanguageForHadith(String stringDefault) {
        String language = getLanguagePublic(stringDefault);
        if (language.equals("ar")) {
            language = getLanguageSpecial(stringDefault);
        }
        return language;
    }

    public boolean getEventChange() {
        return sharedPreferences.getBoolean(getString(R.string.shared_change_preference_key), false);
    }

    public void setEventChange(boolean event) {
        sharedPreferences.saveBoolean(getString(R.string.shared_change_preference_key), event);
    }

    public boolean getChangeLanguageSystem() {
        return sharedPreferences.getBoolean(getString(R.string.change_language_system_preference_key), false);
    }

    public void setChangeLanguageSystem(boolean event) {
        sharedPreferences.saveBoolean(getString(R.string.change_language_system_preference_key), event);
    }

    public boolean isSystemLanguageArabic() {
        return getLanguageSystem("en").equals(getResources().getString(R.string.language_ar_value));
    }

}
