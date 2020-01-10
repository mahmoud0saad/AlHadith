package com.mahmoud.hadith.ui.fragment.base;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.model.viewmodel.base.BaseViewModel;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public abstract class BaseFragment extends Fragment {
    BaseViewModel baseViewModel;


    @Override
    public void onStart() {
        super.onStart();
        baseViewModel = ViewModelProviders.of(this).get(BaseViewModel.class);

    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() != null) {
            if (baseViewModel.isLanguageSystemArabic()) {
                getActivity().overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            } else {
                getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        }

    }
}
