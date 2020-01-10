package com.mahmoud.hadith.ui.activities.about;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.databinding.ActivityAboutBinding;
import com.mahmoud.hadith.ui.activities.base.BaseActivity;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class AboutActivity extends BaseActivity {
    ActivityAboutBinding mAboutBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAboutBinding = DataBindingUtil.setContentView(this, R.layout.activity_about);


        initAppBar();
    }


    /**
     * set title for action bar by text view and delete the default title toolbar
     */
    private void initAppBar() {
        TextView textView = mAboutBinding.includeToolbar.myToolbar.findViewById(R.id.title_toolbar_textview);
        textView.setText(getResources().getString(R.string.title_hadith));

        setSupportActionBar(mAboutBinding.includeToolbar.myToolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");

        }

    }
}
