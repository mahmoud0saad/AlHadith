package com.mahmoud.hadith.ui.activities.main;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.databinding.ActivityMainBinding;
import com.mahmoud.hadith.model.utils.Utils;
import com.mahmoud.hadith.model.viewmodel.main.MainViewModel;
import com.mahmoud.hadith.ui.activities.about.AboutActivity;
import com.mahmoud.hadith.ui.activities.azkar.AzkarActivity;
import com.mahmoud.hadith.ui.activities.base.BaseActivity;
import com.mahmoud.hadith.ui.activities.setting.SettingsActivity;
import com.mahmoud.hadith.ui.adapter.ViewPagerAdapter;
import com.mahmoud.hadith.ui.fragment.book.BookFragment;
import com.mahmoud.hadith.ui.fragment.favorite.FavoriteFragment;
import com.mahmoud.hadith.ui.fragment.mydownload.MyDownloadFragment;
import com.mahmoud.hadith.ui.fragment.search.SearchFragment;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class MainActivity extends BaseActivity {
    private static final float END_SCALE = 0.7f;

    private ActivityMainBinding mActivityBookBinding;
    private SearchFragment mSearchFragment;
    private MainViewModel mMainViewModel;
    private MenuItem previousMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityBookBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        initView();

    }

    private void initView() {

        setupBottomNavigationView();


        setupToolbar();


        setUpViewPager();


        prepareNavigationDrawer();

    }

    private void setupBottomNavigationView() {

        mActivityBookBinding.bottomNavView.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.navigation_books:
                    mActivityBookBinding.viewPager.setCurrentItem(0);
                    return true;

                case R.id.navigation_search:
                    mActivityBookBinding.viewPager.setCurrentItem(1);
                    return true;

                case R.id.navigation_fav:
                    mActivityBookBinding.viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_download:
                    mActivityBookBinding.viewPager.setCurrentItem(3);
                    return true;

            }
            return false;
        });

    }

    private void setUpViewPager() {

        setupAdapterViewPager();

        mActivityBookBinding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                //check if it first time of selected page or not
                if (previousMenuItem!=null){
                    //here not first time open

                    previousMenuItem.setChecked(false);
                } else {
                    //it first time
                    mActivityBookBinding.bottomNavView.getMenu().getItem(0).setChecked(false);
                }

                previousMenuItem = mActivityBookBinding.bottomNavView.getMenu().getItem(position);

                previousMenuItem.setChecked(true);

                mActivityBookBinding.titleToolbarTextview.setVisibility(View.VISIBLE);
                mActivityBookBinding.titleToolbarEdittext.setVisibility(View.GONE);

                switch (position){
                    case 0:
                        mActivityBookBinding.titleToolbarTextview.setText(getResources().getString(R.string.title_book));
                        break;

                    case 1:
                        mActivityBookBinding.titleToolbarTextview.setVisibility(View.GONE);
                        mActivityBookBinding.titleToolbarEdittext.setVisibility(View.VISIBLE);
                        break;

                    case 2:
                        mActivityBookBinding.titleToolbarTextview.setText(getResources().getString(R.string.title_favorite));
                        break;
                    case 3:
                        mActivityBookBinding.titleToolbarTextview.setText(getResources().getString(R.string.title_download));
                        break;


                }
            }
        });
    }

    private void setupAdapterViewPager() {
        mSearchFragment = new SearchFragment();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerAdapter.addFragment(new BookFragment());
        viewPagerAdapter.addFragment(mSearchFragment);
        viewPagerAdapter.addFragment(new FavoriteFragment());
        viewPagerAdapter.addFragment(new MyDownloadFragment());

        mActivityBookBinding.viewPager.setAdapter(viewPagerAdapter);

        //check if intent action to open my download fragment or not and open this fragment
        if (mMainViewModel.checkIntentForDownloadFragment(getIntent())) {
            goToMyDownload();
        }

    }

    private void setupToolbar() {
        deleteTitleActionBar();

        addActionBarDrawerToggle();

        //is watcher if user click on search button on keyboard
        //when click it button
        mActivityBookBinding.titleToolbarEdittext.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                mSearchFragment.searchTextAction(v.getText().toString());
                Utils.hideSoftKeyboard(mActivityBookBinding.titleToolbarEdittext);
                return true;
            }
            return false;
        });


    }

    private void addActionBarDrawerToggle() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, mActivityBookBinding.mainDrawerLayout, mActivityBookBinding.myToolbar, R.string.app_name, R.string.app_name);
        mActivityBookBinding.mainDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void deleteTitleActionBar() {
        setSupportActionBar(mActivityBookBinding.myToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
        }
    }

    private void prepareNavigationDrawer() {

        mActivityBookBinding.mainDrawerLayout.setDrawerElevation(0);
        mActivityBookBinding.mainDrawerLayout.setScrimColor(Color.TRANSPARENT);
        mActivityBookBinding.mainDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                mActivityBookBinding.mainRelativeLayout.setScaleX(offsetScale);
                mActivityBookBinding.mainRelativeLayout.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = mActivityBookBinding.mainRelativeLayout.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;

                mActivityBookBinding.mainRelativeLayout.setTranslationX(
                        mMainViewModel.checkLanguageArabic() ? xTranslation * -1 : xTranslation
                );
            }
        });

        mActivityBookBinding.navigationDrawerMain.setNavigationItemSelectedListener(menuItem -> {
            Intent intent;
            switch (menuItem.getItemId()) {
                case R.id.nav_azkar:
                    intent = new Intent(this, AzkarActivity.class);

                    startActivity(intent);
                    return true;


                case R.id.nav_setting:
                    intent = new Intent(MainActivity.this, SettingsActivity.class);
                    startActivity(intent);

                    return true;

                case R.id.nav_rate:
                    rateApp();
                    return true;

                case R.id.nav_about:
                    intent = new Intent(MainActivity.this, AboutActivity.class);
                    startActivity(intent);
                    return true;

                case R.id.nav_feedback:
                    intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/html");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"mahmoud1saad2@gmail.com"});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "hadith app");

                    startActivity(Intent.createChooser(intent, "Send Email"));
                    return true;


            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        closeDrawerLayout();

    }

    /**
     * go to my download fragment by view pager and change title of toolbar
     */
    public void goToMyDownload() {
        mActivityBookBinding.viewPager.setCurrentItem(3);
        mActivityBookBinding.titleToolbarTextview.setText(getResources().getString(R.string.title_download));
        mActivityBookBinding.bottomNavView.setSelectedItemId(R.id.navigation_download);
    }

    public void closeDrawerLayout() {
        if (mActivityBookBinding.mainDrawerLayout != null)
            mActivityBookBinding.mainDrawerLayout.closeDrawer(GravityCompat.START);
    }

    /*
     * Start with rating the app
     * Determine if the Play Store is installed on the device
     *
     * */
    public void rateApp() {
        try {
            Intent rateIntent = mMainViewModel.rateIntentForUrl("market://details");
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = mMainViewModel.rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

}
