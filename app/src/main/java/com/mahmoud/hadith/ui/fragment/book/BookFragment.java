package com.mahmoud.hadith.ui.fragment.book;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
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
import com.mahmoud.hadith.ui.adapter.BooksRecyclerAdapter;

import java.util.Objects;

public class BookFragment extends Fragment implements BooksClickListener {
    private static final String TAG = "BookFragment";
    public static final int BOOK_FRAGMENT_SOURSE_TAG=122;
    private BooksRecyclerAdapter mAdapter;
    private FragmentBookBinding fragmentBookBinding;
    private BooksViewModel booksViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentBookBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_book,container,false);

        booksViewModel=  ViewModelProviders.of(this).get(BooksViewModel.class);


        initView();


        return fragmentBookBinding.getRoot();
    }


    private void initView() {
        mAdapter = new BooksRecyclerAdapter(this, false);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        fragmentBookBinding.booksRecyclerView.setLayoutManager(linearLayoutManager);
        fragmentBookBinding.booksRecyclerView.setAdapter(mAdapter);

        fragmentBookBinding.includeInternet.reloadInternetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isConnected(Objects.requireNonNull(getContext()))) {
                    getAllBooks();
                    fragmentBookBinding.includeInternet.noInternetRelativeLayout.setVisibility(View.GONE);
                    fragmentBookBinding.shimmerViewContainer.setVisibility(View.VISIBLE);
                } else {
                    Snackbar.make(getView(), "No Internet", Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }


    @Override
    public void onPrgressClick(DownloadCallBack callBack, BooksItem booksItem) {
        booksViewModel.downloadBook(callBack, booksItem);
    }

    @Override
    public void onTextClick(BooksItem booksItem) {
        Intent intent=new Intent(getContext(), ChapterActivity.class);
        intent.putExtra(getString(R.string.book_id_key), booksItem);
        intent.putExtra(getString(R.string.sourse_intent_key),BOOK_FRAGMENT_SOURSE_TAG);
        startActivity(intent);

    }

    public void getAllBooks() {
        booksViewModel.getBooksLiveData().observe(getViewLifecycleOwner(), s -> {
            if (s != null)
                mAdapter.setmItemList(s);
            fragmentBookBinding.shimmerViewContainer.stopShimmer();
            fragmentBookBinding.shimmerViewContainer.setVisibility(View.GONE);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Utils.isConnected(Objects.requireNonNull(getContext()))) {
            fragmentBookBinding.includeInternet.noInternetRelativeLayout.setVisibility(View.GONE);
            fragmentBookBinding.shimmerViewContainer.setVisibility(View.VISIBLE);
            getAllBooks();

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
    public void onPause() {
        super.onPause();
        getActivity().overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.clear();
    }
}
