package com.mahmoud.hadith.ui.activities.hadith;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.mahmoud.hadith.R;
import com.mahmoud.hadith.databinding.ActivityHadithBinding;
import com.mahmoud.hadith.model.entity.api.hadithwithout.HadithItem;
import com.mahmoud.hadith.model.interfaces.CheckFavoriteCallBack;
import com.mahmoud.hadith.model.interfaces.HadithClickListener;
import com.mahmoud.hadith.model.utils.Utils;
import com.mahmoud.hadith.model.viewmodel.hadith.HadithViewModel;
import com.mahmoud.hadith.ui.activities.base.BaseActivity;
import com.mahmoud.hadith.ui.activities.detail.DetailHadithActivity;
import com.mahmoud.hadith.ui.activities.main.MainActivity;
import com.mahmoud.hadith.ui.adapter.HadithRecyclerAdapter;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class HadithActivity extends BaseActivity implements HadithClickListener {

    private ActivityHadithBinding mHadithBinding;
    private HadithRecyclerAdapter mAdapter;
    private HadithViewModel mHadithViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHadithBinding= DataBindingUtil.setContentView(this,R.layout.activity_hadith);

        mHadithViewModel= ViewModelProviders.of(this).get(HadithViewModel.class);

        intiViews();


    }

    private void getAllHadith() {
        mHadithViewModel.prepareIntentAction(getIntent()).observe(this, s -> {
            if (s != null) {
                mAdapter.setmItemList(s);
                int bookId = mHadithViewModel.bookId;
                int chapterId = mHadithViewModel.chapterId;
                mAdapter.setIds(bookId, chapterId);
            }
            mHadithBinding.hadithShimmerViewContainer.setVisibility(View.GONE);
            mHadithBinding.hadithShimmerViewContainer.stopShimmer();

        });
    }


    private void intiViews() {

        initAppBar();

        mAdapter = new HadithRecyclerAdapter(this, false);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);

        mHadithBinding.hadithRecyclerView.setLayoutManager(linearLayoutManager);
        mHadithBinding.hadithRecyclerView.setAdapter(mAdapter);

        Snackbar snackbar = Snackbar.make(mHadithBinding.getRoot(), getResources().getString(R.string.internet_connection_message), Snackbar.LENGTH_SHORT);

        mHadithBinding.includeInternet.reloadInternetButton.setOnClickListener(v -> {
            if (Utils.isConnected(HadithActivity.this)) {
                getAllHadith();
                setViewsVisible(true);
            } else {
                snackbar.show();
            }
        });
        mHadithBinding.includeInternet.myDownloadButton.setOnClickListener(v -> {
            Intent intent = new Intent(HadithActivity.this, MainActivity.class);
            intent.putExtra(getResources().getString(R.string.go_download_fragment_key), "any");
            startActivity(intent);
            if (mHadithViewModel.isLanguageSystemArabic()) {
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            } else {
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
            finish();
        });

    }

    /**
     * save the hadith in favorite database or delter it depend on the state of hadith item
     * this called when use click on favorite button in recycler view
     *
     * @param hadithItem the hadith that user click on it
     */
    @Override
    public void onFavoriteClick(HadithItem hadithItem) {
        if (hadithItem.isFavoriteState()) {
            mHadithViewModel.addToFavorite(hadithItem).observe(this, aBoolean -> {
                if (!aBoolean) {
                    mHadithBinding.addedDoneButton.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(() -> mHadithBinding.addedDoneButton.setVisibility(View.GONE), 600);
                }
            });
        } else {
            mHadithViewModel.deleteFavorite(hadithItem).observe(this, aBoolean -> {
                if (!aBoolean) {
                    mHadithBinding.addedDoneButton.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(() -> mHadithBinding.addedDoneButton.setVisibility(View.GONE), 600);

                }
            });

        }
    }

    /**
     * share the data of hadith this called when use click on share button in recycler view
     * @param hadithItem the hadith that user click on it
     */
    @Override
    public void onShareClick(HadithItem hadithItem) {
        String body = "         " + hadithItem.getSanadText() + "     \n" +
                "\n' " + hadithItem.getHadithText() + " '\n";
        startActivity(Utils.getShareIntent(body));
    }

    /**
     * when use click on hadith text in recycler view
     *
     * @param hadithItem the hadith that user click on it
     */
    @Override
    public void onTextClick(HadithItem hadithItem) {
        Intent intent = new Intent(this, DetailHadithActivity.class);
        intent.putExtra(getString(R.string.single_hadith_item_key), hadithItem);
        startActivity(intent);
        if (mHadithViewModel.isLanguageSystemArabic()) {
            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        } else {
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
    }

    /**
     * check if the hadith is on favorite database or not and return result on call back
     *
     * @param hadithId the id of hadith to check
     * @param callBack the call back to return result of checking
     */
    @Override
    public void checkFavoriteOnDatabase(int hadithId, CheckFavoriteCallBack callBack) {
        mHadithViewModel.checkFavoriteInDatabase(hadithId, callBack);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mHadithViewModel.isSourseDownload(getIntent())) {
            getAllHadith();
        } else if (Utils.isConnected(this)) {
            setViewsVisible(true);

            getAllHadith();
        } else {
            setViewsVisible(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Snackbar.make(mHadithBinding.mainContainerRelativeLayout, "No Internet", Snackbar.LENGTH_SHORT);

    }

    /**
     * set title for action bar by text view and delete the default title toolbar
     */
    private void initAppBar() {
        TextView textView = mHadithBinding.includeToolbar.myToolbar.findViewById(R.id.title_toolbar_textview);
        textView.setText(getResources().getString(R.string.title_hadith));

        setSupportActionBar(mHadithBinding.includeToolbar.myToolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");

        }

    }

    private void setViewsVisible(boolean visible) {
        mHadithBinding.hadithRecyclerView.setVisibility(visible ? View.VISIBLE : View.GONE);
        mHadithBinding.includeInternet.noInternetRelativeLayout.setVisibility(visible ? View.GONE : View.VISIBLE);
        mHadithBinding.hadithShimmerViewContainer.setVisibility(visible ? View.VISIBLE:View.GONE);
    }


}
