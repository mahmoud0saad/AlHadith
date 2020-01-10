package com.mahmoud.hadith.ui.fragment.setting;


import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.model.sharedpreference.UserData;
import com.mahmoud.hadith.model.utils.Utils;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class SettingFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {
    private Preference languagePublicHadithPreference;
    private Preference languageSpecialHadithPreference;
    private Preference languageSystemPreference;
    private UserData mUserData;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        languagePublicHadithPreference = findPreference(getString(R.string.shared_public_language_key));
        languageSpecialHadithPreference = findPreference(getString(R.string.shared_special_language_key));
        languageSystemPreference = findPreference(getString(R.string.shared_system_language_key));

        mUserData = UserData.getInstance(getContext());


        setChangeListener(languageSystemPreference);
        setChangeListener(languageSpecialHadithPreference);

        initLanguagePublic();
    }

    private void initLanguagePublic() {
        if (languagePublicHadithPreference == null) return;

        if (languagePublicHadithPreference.getSummary().toString().equals(getString(R.string.language_ar_entrie))) {
            languageSpecialHadithPreference.setVisible(true);
        } else {
            languageSpecialHadithPreference.setVisible(false);
        }

        languagePublicHadithPreference.setOnPreferenceChangeListener((x, y) -> {
            makeChangePreference();
            if (y.toString().equals(getString(R.string.language_ar_value))) {
                languageSpecialHadithPreference.setVisible(true);
            } else {
                languageSpecialHadithPreference.setVisible(false);
            }
            return true;
        });
    }

    private void setChangeListener(Preference preference) {
        if (preference != null)
            preference.setOnPreferenceChangeListener(this);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        makeChangePreference();
        if (preference.getKey().equals(getResources().getString(R.string.shared_system_language_key))) {
            Utils.switchLocal(getContext(), newValue.toString(), getActivity());
            mUserData.setChangeLanguageSystem(true);
        }
        return true;
    }

    private void makeChangePreference() {
        mUserData.setEventChange(true);

    }


}
