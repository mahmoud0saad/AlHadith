package com.mahmoud.hadith.model.utils.sharedpreference;

import android.content.Context;
import android.content.ContextWrapper;

import com.mahmoud.hadith.R;

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

}
