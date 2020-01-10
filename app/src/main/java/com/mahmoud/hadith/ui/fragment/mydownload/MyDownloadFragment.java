package com.mahmoud.hadith.ui.fragment.mydownload;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.databinding.FragmentDownloadBinding;
import com.mahmoud.hadith.model.entity.api.books.BooksItem;
import com.mahmoud.hadith.model.interfaces.BooksClickListener;
import com.mahmoud.hadith.model.interfaces.DownloadCallBack;
import com.mahmoud.hadith.model.viewmodel.mydownload.MyDownloadViewModel;
import com.mahmoud.hadith.ui.activities.chapter.ChapterActivity;
import com.mahmoud.hadith.ui.adapter.BooksRecyclerAdapter;
import com.mahmoud.hadith.ui.fragment.base.BaseFragment;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class MyDownloadFragment extends BaseFragment implements BooksClickListener {
    private static final String TAG = "BookFragment";
    public static final int DOWNLOAD_FRAGMENT_SOURSE_TAG=123;
    private BooksRecyclerAdapter mAdapter;
    private FragmentDownloadBinding fragmentDownloadBinding;
    private MyDownloadViewModel mDownloadViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentDownloadBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_download,container,false);

        mDownloadViewModel=  ViewModelProviders.of(this).get(MyDownloadViewModel.class);

        fragmentDownloadBinding.downloadShimmerViewContainer.startShimmer();
        initView();

        mDownloadViewModel.getBooksLiveData().observe(getViewLifecycleOwner(),s->{
            if (s != null)
                mAdapter.setmItemList(s);
            fragmentDownloadBinding.downloadShimmerViewContainer.stopShimmer();
            fragmentDownloadBinding.downloadShimmerViewContainer.setVisibility(View.GONE);
        });

        return fragmentDownloadBinding.getRoot();
    }

    private void initView() {
        mAdapter = new BooksRecyclerAdapter(this, true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        fragmentDownloadBinding.downloadRecyclerView.setLayoutManager(linearLayoutManager);
        fragmentDownloadBinding.downloadRecyclerView.setAdapter(mAdapter);

    }


    @Override
    public void onPrgressClick(DownloadCallBack callBack, BooksItem booksItem) {
        mDownloadViewModel.deleteBook(booksItem, callBack);
    }

    @Override
    public void onTextClick(BooksItem booksItem) {
        Intent intent=new Intent(getContext(), ChapterActivity.class);
        intent.putExtra(getString(R.string.book_id_key), booksItem);
        intent.putExtra(getString(R.string.sourse_intent_key),DOWNLOAD_FRAGMENT_SOURSE_TAG);
        startActivity(intent);
    }


}
