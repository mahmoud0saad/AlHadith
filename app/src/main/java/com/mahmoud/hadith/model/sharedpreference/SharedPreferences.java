package com.mahmoud.hadith.model.sharedpreference;

import android.content.Context;

import androidx.preference.PreferenceManager;

import java.util.Map;
import java.util.Set;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */

public class SharedPreferences {
    private static SharedPreferences mInstance = null;
    private final android.content.SharedPreferences prefs;
    private final android.content.SharedPreferences.Editor editor;
    private final String filename = "preferences";

    private SharedPreferences(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor = prefs.edit();
    }


    public static SharedPreferences getInstance(Context context) {
        if (mInstance == null)
            mInstance = new SharedPreferences(context);
        return mInstance;
    }

    public void clearAll() {
        editor.clear();
        editor.apply();
    }

    public boolean contains(String key) {
        return prefs.contains(key);
    }

    public void removeValue(String key) {
        editor.remove(key);
        editor.apply();
    }

    private String getFilename() {
        return filename;
    }

    /*
        Retrieving methods
     */

    public boolean getBoolean(String key, boolean defaultValue) {
        return prefs.getBoolean(key, defaultValue);
    }

    public int getInteger(String key, int defaultValue) {
        return prefs.getInt(key, defaultValue);
    }

    public String getString(String key, String defaultValue) {
        return defaultValue != null ? prefs.getString(key, defaultValue).trim() : null;
    }

    public float getFloat(String key, float defaultValue) {
        return prefs.getFloat(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return prefs.getLong(key, defaultValue);
    }

    public double getDouble(String key, double defaultValue) {
        return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
    }

    public Set<String> getStringSet(String key, Set<String> defaultValue) {
        return prefs.getStringSet(key, defaultValue);
    }

    public Map<String, ?> getAll() {
        return prefs.getAll();
    }

    /*
        Saving methods
    */

    public void saveBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void saveInteger(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    public void saveString(String key, String value) {
        editor.putString(key, value != null ? value.trim() : null);
        editor.apply();
    }

    public void saveFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.apply();
    }

    public void saveLong(String key, long value) {
        editor.putLong(key, value);
        editor.apply();
    }

    public void saveDouble(String key, double value) {
        editor.putLong(key, Double.doubleToRawLongBits(value));
        editor.apply();
    }

    public void saveStringSet(String key, Set<String> set) {
        editor.putStringSet(key, set);
        editor.apply();
    }
}