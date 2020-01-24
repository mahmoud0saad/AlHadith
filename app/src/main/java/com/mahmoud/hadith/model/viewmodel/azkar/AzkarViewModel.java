package com.mahmoud.hadith.model.viewmodel.azkar;

import android.app.Application;

import androidx.annotation.NonNull;

import com.mahmoud.hadith.model.sharedpreference.UserData;
import com.mahmoud.hadith.model.viewmodel.base.BaseViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AzkarViewModel extends BaseViewModel {
    private static final String TAG = "AzkarViewModel";

    private UserData mUserData;

    public AzkarViewModel(@NonNull Application application) {
        super(application);
        mUserData = UserData.getInstance(getApplication());
    }

    public List<String> getAzkarList() {
        return new ArrayList<>(mUserData.getAzkarSet());
    }

    public void saveAzkarList(Set<String> azkarSet) {
        mUserData.setAzkarSet(azkarSet);
    }


}
