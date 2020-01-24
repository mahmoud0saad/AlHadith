package com.mahmoud.hadith.model.sharedpreference;

import android.content.Context;
import android.content.ContextWrapper;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.model.utils.Utils;

import java.util.Locale;
import java.util.Set;

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

    public String getLanguageSystem() {
        return sharedPreferences.getString(getResources().getString(R.string.shared_system_language_key), Locale.getDefault().getDisplayLanguage());
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
        return getLanguageSystem().equals(getResources().getString(R.string.language_ar_value));
    }

    public int getCurrentNumberAzkar() {
        return sharedPreferences.getInteger(getResources().getString(R.string.shared_current_number_azkar), 0);
    }

    public void setCurrentNumberAzkar(int currentNumber) {
        sharedPreferences.saveInteger(getResources().getString(R.string.shared_current_number_azkar), currentNumber);
    }

    public boolean isAzkzrEnable() {
        return sharedPreferences.getBoolean(getResources().getString(R.string.shared_azkar_enable_key), false);
    }

    public Set<String> getAzkarSet() {
        return sharedPreferences.getStringSet(getResources().getString(R.string.shared_azkar_sets_key), Utils.getDefaultAzkar());
    }

    public void setAzkarSet(Set<String> azkarSet) {
        sharedPreferences.saveStringSet(getResources().getString(R.string.shared_azkar_sets_key), azkarSet);

    }

    public int getAzkarReplayTime() {
        return sharedPreferences.getInteger(getResources().getString(R.string.shared_seek_bar_minute_key), 60);

    }
}
