package com.mahmoud.hadith.ui.activities.chapter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.mahmoud.hadith.ui.adapter.ChapterRecyclerAdapter;
import com.mahmoud.hadith.ui.fragment.book.BookFragment;
import com.mahmoud.hadith.ui.fragment.mydownload.MyDownloadFragment;

public class ChapterActivity extends BaseActivity implements ChapterClickListener {
    private static final String TAG = "ChapterActivity";
    private ActivityChapterBinding mActivityChapterBinding;
    private   ChapterRecyclerAdapter adapter;
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
        adapter=new ChapterRecyclerAdapter(this);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        mActivityChapterBinding.chapterRecyclerView.setLayoutManager(linearLayoutManager);
        mActivityChapterBinding.chapterRecyclerView.setAdapter(adapter);

        mActivityChapterBinding.includeInternet.reloadInternetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isConnected(ChapterActivity.this)) {
                    getAllChapter();
                    mActivityChapterBinding.includeInternet.noInternetRelativeLayout.setVisibility(View.GONE);
                    mActivityChapterBinding.chapterShimmerViewContainer.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    public void onPrgressClick(ChapterItem chapterItem, DownloadCallBack callBack) {
        mChapterViewModel.actionProgressClick(chapterItem, callBack);
    }

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

        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mChapterViewModel.isSourseDownload(getIntent())) {
            getAllChapter();
        } else if (Utils.isConnected(this)) {
            mActivityChapterBinding.includeInternet.noInternetRelativeLayout.setVisibility(View.GONE);
            mActivityChapterBinding.chapterShimmerViewContainer.setVisibility(View.VISIBLE);

            getAllChapter();
        } else {
            mActivityChapterBinding.includeInternet.noInternetRelativeLayout.setVisibility(View.VISIBLE);
            mActivityChapterBinding.chapterShimmerViewContainer.setVisibility(View.GONE);
        }
    }

    private void getAllChapter() {
        mChapterViewModel.prepareIntentAction(getIntent()).observe(this, chapterItems -> {

            mActivityChapterBinding.chapterShimmerViewContainer.setVisibility(View.GONE);
            mActivityChapterBinding.chapterShimmerViewContainer.stopShimmer();

            if (chapterItems == null) return;

            if (mChapterViewModel.isSourseDownload) {
                adapter.setIsDownload(mChapterViewModel.isSourseDownload);

                adapter.setmItemList(chapterItems);

                adapter.setBookId(mChapterViewModel.mBooksItem != null ? mChapterViewModel.mBooksItem.getBookID() : 0);

            } else {
                mChapterViewModel.setStateForChapterList(chapterItems).observe(this, chapters -> {
                    if (chapters == null) return;

                    adapter.setmItemList(chapters);

                    adapter.setBookId(mChapterViewModel.mBooksItem != null ? mChapterViewModel.mBooksItem.getBookID() : 0);

                });
            }


        });

    }

}
