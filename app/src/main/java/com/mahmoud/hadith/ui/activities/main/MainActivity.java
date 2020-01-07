package com.mahmoud.hadith.ui.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mahmoud.hadith.R;
import com.mahmoud.hadith.databinding.ActivityMainBinding;
import com.mahmoud.hadith.ui.activities.base.BaseActivity;
import com.mahmoud.hadith.ui.activities.setting.SettingsActivity;
import com.mahmoud.hadith.ui.adapter.ViewPagerAdapter;
import com.mahmoud.hadith.ui.fragment.book.BookFragment;
import com.mahmoud.hadith.ui.fragment.favorite.FavoriteFragment;
import com.mahmoud.hadith.ui.fragment.mydownload.MyDownloadFragment;
import com.mahmoud.hadith.ui.fragment.search.SearchFragment;

public class MainActivity extends BaseActivity {

    ActivityMainBinding activityBookBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBookBinding= DataBindingUtil.setContentView(this,R.layout.activity_main);

        initView();
    }

    private void initView() {
        setSupportActionBar(activityBookBinding.myToolbar);

        activityBookBinding.navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_books:
                        activityBookBinding.viewPager.setCurrentItem(0);
                        return true;

                    case R.id.navigation_search:
                        activityBookBinding.viewPager.setCurrentItem(1);
                        return true;

                    case R.id.navigation_fav:
                        activityBookBinding.viewPager.setCurrentItem(2);
                        return true;
                    case R.id.navigation_download:
                        activityBookBinding.viewPager.setCurrentItem(3);
                        return true;

                }
                return false;
            }
        });

        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new BookFragment());
        viewPagerAdapter.addFragment(new SearchFragment());
        viewPagerAdapter.addFragment(new FavoriteFragment());
        viewPagerAdapter.addFragment(new MyDownloadFragment());
        activityBookBinding.viewPager.setAdapter(viewPagerAdapter);

        activityBookBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private MenuItem previousMenuItem;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (previousMenuItem!=null){
                    previousMenuItem.setChecked(false);
                }
                else {
                    activityBookBinding.navView.getMenu().getItem(0).setChecked(false);
                }
                previousMenuItem=activityBookBinding.navView.getMenu().getItem(position);
                previousMenuItem.setChecked(true);
                switch (position){
                    case 0:
                        activityBookBinding.titleToolbarTextview.setText(getResources().getString(R.string.title_book));
                        break;

                    case 1:
                        activityBookBinding.titleToolbarTextview.setText(getResources().getString(R.string.title_search));
                        break;

                    case 2:
                        activityBookBinding.titleToolbarTextview.setText(getResources().getString(R.string.title_favorite));
                        break;
                    case 3:
                        activityBookBinding.titleToolbarTextview.setText(getResources().getString(R.string.title_download));
                        break;


                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.setting_menu,menu);
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting:
                Intent intent=new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
