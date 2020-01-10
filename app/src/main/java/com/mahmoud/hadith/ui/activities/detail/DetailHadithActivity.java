package com.mahmoud.hadith.ui.activities.detail;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.databinding.ActivityDetailBinding;
import com.mahmoud.hadith.model.entity.api.hadithwithout.HadithItem;
import com.mahmoud.hadith.model.utils.Utils;
import com.mahmoud.hadith.model.viewmodel.detailhadith.DetailViewModel;
import com.mahmoud.hadith.ui.activities.base.BaseActivity;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class DetailHadithActivity extends BaseActivity {

    private ActivityDetailBinding mDetailBinding;
    private DetailViewModel mDetailViewModel;
    private HadithItem mHadithItem;
    private ShareActionProvider mShareActionProvider;
    private String mShareBodyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        mDetailViewModel = ViewModelProviders.of(this).get(DetailViewModel.class);

        mHadithItem = mDetailViewModel.prepareIntent(getIntent());

        initFab();
        initView();
    }

    private void initView() {

        mDetailBinding.collapsingToolbar.setTitle(mHadithItem.getSanadText());

        mDetailBinding.textHadithTextview.setText(mHadithItem.getHadithText());

        mDetailBinding.sanadHadithTextview.setText(mHadithItem.getSanadText());

        initAppBar();

        mShareBodyText = "         " + mHadithItem.getSanadText() + "     \n" +
                "\n' " + mHadithItem.getHadithText() + " '\n";

    }

    private void initAppBar() {

        setSupportActionBar(mDetailBinding.singleToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

    }


    private void initFab() {
        mDetailBinding.favoriteFabButton.setOnClickListener(v -> {
            if (mHadithItem != null) {
                mDetailViewModel.addToFavorite(mHadithItem).observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        mDetailBinding.addedDoneButton.setVisibility(View.VISIBLE);
                        new Handler().postDelayed(() -> {
                            mDetailBinding.addedDoneButton.setVisibility(View.GONE);
                        }, 600);

                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_hadith_details, menu);
        MenuItem item = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        Intent intent = Utils.getShareIntent(mShareBodyText);
        setShareIntent(intent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            Intent intent = Utils.getShareIntent(mShareBodyText);
            setShareIntent(intent);
            startActivity(Intent.createChooser(intent, "Choose sharing trivia"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
}
