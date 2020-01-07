package com.mahmoud.hadith.ui.fragment.setting;


import android.os.Bundle;
import android.util.Log;

import androidx.preference.PreferenceFragmentCompat;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.model.utils.sharedpreference.UserData;

public class SettingFragment extends PreferenceFragmentCompat {
    private static final String TAG = "SettingFragment";
    UserData userData;
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        userData = UserData.getInstance(getContext());

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: " + userData.getLanguagePublic("ar"));
        Log.i(TAG, "onStart: " + userData.getLanguageSpecial("ar-noTashkeel"));

    }
}
