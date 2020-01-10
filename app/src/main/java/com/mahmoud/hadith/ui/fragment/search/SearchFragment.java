package com.mahmoud.hadith.ui.fragment.search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.mahmoud.hadith.R;
import com.mahmoud.hadith.databinding.FragmentSearchBinding;
import com.mahmoud.hadith.model.entity.api.books.BooksItem;
import com.mahmoud.hadith.model.entity.api.hadithwithout.HadithItem;
import com.mahmoud.hadith.model.interfaces.CheckFavoriteCallBack;
import com.mahmoud.hadith.model.interfaces.HadithClickListener;
import com.mahmoud.hadith.model.utils.Utils;
import com.mahmoud.hadith.model.viewmodel.search.SearchViewModel;
import com.mahmoud.hadith.ui.activities.detail.DetailHadithActivity;
import com.mahmoud.hadith.ui.activities.main.MainActivity;
import com.mahmoud.hadith.ui.adapter.HadithRecyclerAdapter;
import com.mahmoud.hadith.ui.adapter.SearchSpinnerAdapter;
import com.mahmoud.hadith.ui.fragment.base.BaseFragment;

import java.util.List;
import java.util.Objects;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class SearchFragment extends BaseFragment implements HadithClickListener {
    private static final String TAG = "SearchFragment";
    private FragmentSearchBinding mFragmentSearchBinding;
    private SearchViewModel mSearchViewModel;
    private HadithRecyclerAdapter mRecyclerAdapter;
    private SearchSpinnerAdapter mSpinnerAdapter;
    private int bookIdSlected=0;
    private List<HadithItem> mHadithItems;
    private String searchText;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentSearchBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_search,container,false);

        mSearchViewModel= ViewModelProviders.of(this ).get(SearchViewModel.class);

        mFragmentSearchBinding.setLifecycleOwner(getViewLifecycleOwner());

        initViews();

        return  mFragmentSearchBinding.getRoot();
    }

    private void initViews() {


        mFragmentSearchBinding.setNumberSearchResult(mSearchViewModel.getNumberReultLiveData());

        //adapter spinner
        mSpinnerAdapter= new SearchSpinnerAdapter(getContext());

        mFragmentSearchBinding.selectBookSpinner.setAdapter(mSpinnerAdapter);


        //adapter recycler view
        mRecyclerAdapter = new HadithRecyclerAdapter(this, true);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());

        mFragmentSearchBinding.resultSearchRecyclerView.setLayoutManager(linearLayoutManager);

        mFragmentSearchBinding.resultSearchRecyclerView.setAdapter(mRecyclerAdapter);


        observables();

    }

    private void observables() {

        mSearchViewModel.getBooksLiveData().observe(getViewLifecycleOwner(), resultList -> {
            if (resultList != null)
                mSpinnerAdapter.setBooksItems(resultList);

        });
        mFragmentSearchBinding.selectBookSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                BooksItem b=  mSpinnerAdapter.getBookItem(position);
                if (b!=null)
                    bookIdSlected=b.getBookID();

                Log.i(TAG, "onItemSelected: is " + bookIdSlected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mFragmentSearchBinding.includeInternet.reloadInternetButton.setOnClickListener(v -> {
            if (Utils.isConnected(Objects.requireNonNull(getContext()))) {
                mFragmentSearchBinding.includeInternet.noInternetRelativeLayout.setVisibility(View.GONE);
                searchTextAction(searchText);
            } else {
                if (getView() != null)
                    Snackbar.make(getView(), getResources().getString(R.string.internet_connection_message), Snackbar.LENGTH_LONG).show();
            }
        });
        mFragmentSearchBinding.includeInternet.myDownloadButton.setOnClickListener(v -> {
            if (getActivity() != null)
                ((MainActivity) getActivity()).goToMyDownload();
        });

    }


    public void searchTextAction(String text) {

        if (!Utils.isConnected(getContext())) {
            mFragmentSearchBinding.includeInternet.noInternetRelativeLayout.setVisibility(View.VISIBLE);
            searchText = text;
            return;
        } else {
            mFragmentSearchBinding.includeInternet.noInternetRelativeLayout.setVisibility(View.GONE);
        }


        if (mFragmentSearchBinding.hadithShimmerViewContainer != null) {
            mFragmentSearchBinding.hadithShimmerViewContainer.setVisibility(View.VISIBLE);
            mFragmentSearchBinding.hadithShimmerViewContainer.startShimmer();
            mRecyclerAdapter.clear();
        }


        mSearchViewModel.getHadithSearched(text, bookIdSlected).observe(getViewLifecycleOwner(), hadithItems -> {
            if (hadithItems != null) {
                mHadithItems = hadithItems;
                mRecyclerAdapter.setmItemList(hadithItems);
            }
            mFragmentSearchBinding.hadithShimmerViewContainer.stopShimmer();
            mFragmentSearchBinding.hadithShimmerViewContainer.setVisibility(View.GONE);
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mHadithItems != null) {
            mRecyclerAdapter.setmItemList(mHadithItems);
        }
    }


    @Override
    public void onFavoriteClick(HadithItem hadithItem) {

    }

    @Override
    public void onShareClick(HadithItem hadithItem) {

    }

    @Override
    public void onTextClick(HadithItem hadithItem) {
        Intent intent = new Intent(getContext(), DetailHadithActivity.class);
        intent.putExtra(getString(R.string.single_hadith_item_key), hadithItem);
        startActivity(intent);
    }

    @Override
    public void checkFavoriteOnDatabase(int hadithId, CheckFavoriteCallBack callBack) {

    }


}
