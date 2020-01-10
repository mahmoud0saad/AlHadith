package com.mahmoud.hadith.ui.activities.chapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.mahmoud.hadith.R;
import com.mahmoud.hadith.databinding.ActivityChapterBinding;
import com.mahmoud.hadith.model.entity.api.books.BooksItem;
import com.mahmoud.hadith.model.entity.api.chapter.ChapterItem;
import com.mahmoud.hadith.model.interfaces.ChapterClickListener;
import com.mahmoud.hadith.model.interfaces.DownloadCallBack;
import com.mahmoud.hadith.model.utils.Utils;
import com.mahmoud.hadith.model.viewmodel.chapter.ChapterViewModel;
import com.mahmoud.hadith.ui.activities.base.BaseActivity;
import com.mahmoud.hadith.ui.activities.hadith.HadithActivity;
import com.mahmoud.hadith.ui.activities.main.MainActivity;
import com.mahmoud.hadith.ui.adapter.ChapterRecyclerAdapter;
import com.mahmoud.hadith.ui.fragment.book.BookFragment;
import com.mahmoud.hadith.ui.fragment.mydownload.MyDownloadFragment;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class ChapterActivity extends BaseActivity implements ChapterClickListener {
    private static final String TAG = "ChapterActivity";
    private ActivityChapterBinding mActivityChapterBinding;
    private ChapterRecyclerAdapter adapter;
    private ChapterViewModel mChapterViewModel;
    private int sourse;
    private BooksItem mBooksItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityChapterBinding = DataBindingUtil.setContentView(this,R.layout.activity_chapter);

        mChapterViewModel= ViewModelProviders.of(this).get(ChapterViewModel.class);

        initViews();

    }


    private void initViews() {

        initAppBar();

        adapter=new ChapterRecyclerAdapter(this);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        mActivityChapterBinding.chapterRecyclerView.setLayoutManager(linearLayoutManager);
        mActivityChapterBinding.chapterRecyclerView.setAdapter(adapter);
        Snackbar snackbar = Snackbar.make(mActivityChapterBinding.getRoot(), getResources().getString(R.string.internet_connection_message), Snackbar.LENGTH_SHORT);
        mActivityChapterBinding.includeInternet.reloadInternetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isConnected(ChapterActivity.this)) {
                    getAllChapter();
                    setViewsVisible(true);
                } else {
                    snackbar.show();
                }
            }
        });

        mActivityChapterBinding.includeInternet.myDownloadButton.setOnClickListener(v -> {
            Intent intent = new Intent(ChapterActivity.this, MainActivity.class);
            intent.putExtra(getResources().getString(R.string.go_download_fragment_key), "any");
            startActivity(intent);
            if (mChapterViewModel.isLanguageSystemArabic()) {
                overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
            } else {
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
            finish();
        });
    }

    /**
     * download the chapter on data base
     *
     * @param chapterItem the chapter that downloading
     * @param callBack    the callback to receive the result when done downloading
     */
    @Override
    public void onPrgressClick(ChapterItem chapterItem, DownloadCallBack callBack) {
        mChapterViewModel.actionProgressClick(chapterItem, callBack);
    }

    /**
     * start of hadith activity called when user click text of hadith on recycler view
     * @param id the id of hadith
     */
    @Override
    public void onTextClick(String id) {
        int chapterId,bookId;

        Log.i(TAG, "onTextClick: "+id);

        String[] allId = id.split(",");

        bookId=Integer.valueOf(allId[0]);
        chapterId=Integer.valueOf(allId[1]);


        Intent intent=new Intent(this, HadithActivity.class);

        intent.putExtra(getString(R.string.book_id_from_chapter_key), bookId);
        intent.putExtra(getString(R.string.chapter_id_from_chapter_key), chapterId);
        intent.putExtra(getString(R.string.sourse_intent_key), mChapterViewModel.isSourseDownload ? MyDownloadFragment.DOWNLOAD_FRAGMENT_SOURSE_TAG : BookFragment.BOOK_FRAGMENT_SOURSE_TAG);
        startActivity(intent);

        if (mChapterViewModel.isLanguageSystemArabic()) {
            overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
        } else {
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mChapterViewModel.isSourseDownload(getIntent())) {
            getAllChapter();
        } else if (Utils.isConnected(this)) {
            setViewsVisible(true);

            getAllChapter();
        } else {
            setViewsVisible(false);

        }
    }

    /**
     * get all chapter from database or internet depend on the intent sourse
     */
    private void getAllChapter() {
        mChapterViewModel.prepareIntentAction(getIntent()).observe(this, chapterItems -> {

            mActivityChapterBinding.chapterShimmerViewContainer.setVisibility(View.GONE);
            mActivityChapterBinding.chapterShimmerViewContainer.stopShimmer();

            if (chapterItems == null) return;

            if (mChapterViewModel.isSourseDownload) {
                //if sourse of data from data base
                adapter.setIsDownload(mChapterViewModel.isSourseDownload);

                adapter.setmItemList(chapterItems);

                adapter.setBookId(mChapterViewModel.mBooksItem != null ? mChapterViewModel.mBooksItem.getBookID() : 0);

            } else {
                //if sourse of data from internet

                mChapterViewModel.setStateForChapterList(chapterItems).observe(this, chapters -> {
                    if (chapters == null) return;

                    adapter.setmItemList(chapters);

                    adapter.setBookId(mChapterViewModel.mBooksItem != null ? mChapterViewModel.mBooksItem.getBookID() : 0);

                });
            }
        });

    }

    /**
     * set title for action bar by text view and delete the default title toolbar
     */
    private void initAppBar() {
        TextView textView = mActivityChapterBinding.includeToolbar.myToolbar.findViewById(R.id.title_toolbar_textview);
        textView.setText(getResources().getString(R.string.title_chapter));

        setSupportActionBar(mActivityChapterBinding.includeToolbar.myToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");

        }
    }

    private void setViewsVisible(boolean visible) {
        mActivityChapterBinding.chapterRecyclerView.setVisibility(visible ? View.VISIBLE : View.GONE);
        mActivityChapterBinding.includeInternet.noInternetRelativeLayout.setVisibility(visible ? View.GONE : View.VISIBLE);
        mActivityChapterBinding.chapterShimmerViewContainer.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

}
