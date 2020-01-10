package com.mahmoud.hadith.ui.fragment.book;

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

import com.google.android.material.snackbar.Snackbar;
import com.mahmoud.hadith.R;
import com.mahmoud.hadith.databinding.FragmentBookBinding;
import com.mahmoud.hadith.model.entity.api.books.BooksItem;
import com.mahmoud.hadith.model.interfaces.BooksClickListener;
import com.mahmoud.hadith.model.interfaces.DownloadCallBack;
import com.mahmoud.hadith.model.utils.Utils;
import com.mahmoud.hadith.model.viewmodel.books.BooksViewModel;
import com.mahmoud.hadith.ui.activities.chapter.ChapterActivity;
import com.mahmoud.hadith.ui.activities.main.MainActivity;
import com.mahmoud.hadith.ui.adapter.BooksRecyclerAdapter;
import com.mahmoud.hadith.ui.fragment.base.BaseFragment;

import java.util.List;
import java.util.Objects;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class BookFragment extends BaseFragment implements BooksClickListener {
    private static final String TAG = "BookFragment";
    public static final int BOOK_FRAGMENT_SOURSE_TAG=122;
    private BooksRecyclerAdapter mAdapter;
    private FragmentBookBinding fragmentBookBinding;
    private BooksViewModel mBooksViewModel;
    private List<BooksItem> mBooksItems;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentBookBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_book,container,false);

        mBooksViewModel = ViewModelProviders.of(this).get(BooksViewModel.class);
        initView();


        return fragmentBookBinding.getRoot();
    }


    private void initView() {
        mAdapter = new BooksRecyclerAdapter(this, false);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        fragmentBookBinding.booksRecyclerView.setLayoutManager(linearLayoutManager);
        fragmentBookBinding.booksRecyclerView.setAdapter(mAdapter);

        fragmentBookBinding.includeInternet.reloadInternetButton.setOnClickListener(v -> {
            if (Utils.isConnected(Objects.requireNonNull(getContext()))) {
                fragmentBookBinding.includeInternet.noInternetRelativeLayout.setVisibility(View.GONE);
                fragmentBookBinding.shimmerViewContainer.setVisibility(View.VISIBLE);
                getAllBooks();

            } else {
                if (getView() != null)
                    Snackbar.make(getView(), getResources().getString(R.string.internet_connection_message), Snackbar.LENGTH_LONG).show();
            }
        });
        fragmentBookBinding.includeInternet.myDownloadButton.setOnClickListener(v -> {
            if (getActivity() != null)
                ((MainActivity) getActivity()).goToMyDownload();
        });

    }


    @Override
    public void onPrgressClick(DownloadCallBack callBack, BooksItem booksItem) {
        mBooksViewModel.downloadBook(callBack, booksItem);
    }

    @Override
    public void onTextClick(BooksItem booksItem) {
        Intent intent=new Intent(getContext(), ChapterActivity.class);
        intent.putExtra(getString(R.string.book_id_key), booksItem);
        intent.putExtra(getString(R.string.sourse_intent_key),BOOK_FRAGMENT_SOURSE_TAG);
        startActivity(intent);

    }

    private void getAllBooks() {
        mBooksViewModel.getBooksLiveData().observe(getViewLifecycleOwner(), s -> {
            if (s != null) {
                mAdapter.setmItemList(s);
                mBooksItems = s;
            }
            fragmentBookBinding.shimmerViewContainer.stopShimmer();
            fragmentBookBinding.shimmerViewContainer.setVisibility(View.GONE);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Utils.isConnected(Objects.requireNonNull(getContext())) || mBooksItems != null) {
            fragmentBookBinding.includeInternet.noInternetRelativeLayout.setVisibility(View.GONE);
            if (mBooksItems == null) {
                fragmentBookBinding.shimmerViewContainer.setVisibility(View.VISIBLE);
                fragmentBookBinding.shimmerViewContainer.startShimmer();

                getAllBooks();
            } else {
                mAdapter.setmItemList(mBooksItems);
            }

        } else {
            fragmentBookBinding.shimmerViewContainer.setVisibility(View.GONE);
            fragmentBookBinding.includeInternet.noInternetRelativeLayout.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onStop() {
        super.onStop();

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mBooksViewModel.changeOnPreference()) {
            fragmentBookBinding.shimmerViewContainer.setVisibility(View.VISIBLE);
            fragmentBookBinding.shimmerViewContainer.startShimmer();

            getAllBooks();
        }
    }
}
