package com.mahmoud.hadith.ui.fragment.search;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.databinding.FragmentSearchBinding;
import com.mahmoud.hadith.model.entity.api.books.BooksItem;
import com.mahmoud.hadith.model.entity.api.hadithwithout.HadithItem;
import com.mahmoud.hadith.model.interfaces.HadithClickListener;
import com.mahmoud.hadith.model.utils.Utils;
import com.mahmoud.hadith.model.viewmodel.search.SearchViewModel;
import com.mahmoud.hadith.ui.adapter.HadithRecyclerAdapter;
import com.mahmoud.hadith.ui.adapter.SearchSpinnerAdapter;

public class SearchFragment extends Fragment implements HadithClickListener {
    private static final String TAG = "SearchFragment";
    private FragmentSearchBinding mFragmentSearchBinding;
    private SearchViewModel mSearchViewModel;
    private HadithRecyclerAdapter mRecyclerAdapter;
    private SearchSpinnerAdapter mSpinnerAdapter;
    private int bookIdSlected=0;
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

        mSearchViewModel.getBooksLiveData().observe(getViewLifecycleOwner(), resultList -> {
            if (resultList != null)
                mSpinnerAdapter.setBooksItems(resultList);

        });

        mFragmentSearchBinding.selectBookSpinner.setAdapter(mSpinnerAdapter);


        //adapter recycler view
        mRecyclerAdapter =new HadithRecyclerAdapter(this);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());

        mFragmentSearchBinding.resultSearchRecyclerView.setLayoutManager(linearLayoutManager);

        mFragmentSearchBinding.resultSearchRecyclerView.setAdapter(mRecyclerAdapter);


        allClicksListener();


    }

    private void allClicksListener(){

        //is watcher if user click on search button on keyboard
        //when click it button
        mFragmentSearchBinding.inputSearchEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {

                mFragmentSearchBinding.hadithShimmerViewContainer.startShimmer();
                mFragmentSearchBinding.hadithShimmerViewContainer.setVisibility(View.VISIBLE);


                mSearchViewModel.getHadithSearched(v.getText().toString(), bookIdSlected).observe(getViewLifecycleOwner(), hadithItems -> {
                    if (hadithItems != null)
                        mRecyclerAdapter.setmItemList(hadithItems);
                    mFragmentSearchBinding.hadithShimmerViewContainer.stopShimmer();
                    mFragmentSearchBinding.hadithShimmerViewContainer.setVisibility(View.GONE);
                });
                Utils.hideSoftKeyboard(mFragmentSearchBinding.inputSearchEditText);
                return true;
            }
            return false;
        });

       mFragmentSearchBinding.selectBookSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                BooksItem b=  mSpinnerAdapter.getBookItem(position);
                if (b!=null)
                    bookIdSlected=b.getBookID();

               Log.i(TAG, "onItemSelected: is "+bookIdSlected);
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });
    }

    @Override
    public void onFavoriteClick(HadithItem hadithItem) {
        mSearchViewModel.addToFavorite(hadithItem).observe(this, aBoolean -> {
            if (aBoolean){
                mFragmentSearchBinding.addedDoneButton.setVisibility(View.VISIBLE);
                new Handler().postDelayed(() -> mFragmentSearchBinding.addedDoneButton.setVisibility(View.GONE),600);
            }else {
                Toast.makeText(getContext(), "fail added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onShareClick(HadithItem hadithItem) {

    }


}
