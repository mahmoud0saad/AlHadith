package com.mahmoud.hadith.ui.activities.base;

import androidx.appcompat.app.AppCompatActivity;

import com.mahmoud.hadith.R;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);

    }
}
