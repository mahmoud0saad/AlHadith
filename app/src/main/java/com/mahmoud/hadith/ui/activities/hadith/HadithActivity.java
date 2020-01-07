package com.mahmoud.hadith.ui.activities.hadith;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.databinding.ActivityHadithBinding;
import com.mahmoud.hadith.model.entity.api.hadithwithout.HadithItem;
import com.mahmoud.hadith.model.interfaces.HadithClickListener;
import com.mahmoud.hadith.model.utils.Utils;
import com.mahmoud.hadith.model.viewmodel.hadith.HadithViewModel;
import com.mahmoud.hadith.ui.activities.base.BaseActivity;
import com.mahmoud.hadith.ui.adapter.HadithRecyclerAdapter;

public class HadithActivity extends BaseActivity implements HadithClickListener {

    private ActivityHadithBinding mHadithBinding;
    private HadithRecyclerAdapter mAdapter;
    private HadithViewModel mHadithViewModel;
    private int sourse;
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

        mAdapter=new HadithRecyclerAdapter(this);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);

        mHadithBinding.hadithRecyclerView.setLayoutManager(linearLayoutManager);
        mHadithBinding.hadithRecyclerView.setAdapter(mAdapter);

        mHadithBinding.includeInternet.reloadInternetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.isConnected(HadithActivity.this)) {
                    getAllHadith();
                    mHadithBinding.includeInternet.noInternetRelativeLayout.setVisibility(View.GONE);
                    mHadithBinding.hadithShimmerViewContainer.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onFavoriteClick(HadithItem hadithItem) {
        mHadithViewModel.addToFavorite(hadithItem).observe(this, aBoolean -> {
            if (aBoolean){
                mHadithBinding.addedDoneButton.setVisibility(View.VISIBLE);
                new Handler().postDelayed(() -> mHadithBinding.addedDoneButton.setVisibility(View.GONE),600);
            }else {
                Toast.makeText(HadithActivity.this, "fail added", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onShareClick(HadithItem hadithItem) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mHadithViewModel.isSourseDownload(getIntent())) {
            getAllHadith();
        } else if (Utils.isConnected(this)) {
            mHadithBinding.includeInternet.noInternetRelativeLayout.setVisibility(View.GONE);
            mHadithBinding.hadithShimmerViewContainer.setVisibility(View.VISIBLE);

            getAllHadith();
        } else {
            mHadithBinding.includeInternet.noInternetRelativeLayout.setVisibility(View.VISIBLE);
            mHadithBinding.hadithShimmerViewContainer.setVisibility(View.GONE);
        }
    }


}
