package com.mahmoud.hadith.ui.fragment.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.databinding.FragmentFavoriteBinding;
import com.mahmoud.hadith.model.entity.api.favorite.FavoriteItem;
import com.mahmoud.hadith.model.interfaces.FavoriteClickListener;
import com.mahmoud.hadith.model.utils.Utils;
import com.mahmoud.hadith.model.viewmodel.favorite.FavoriteViewModel;
import com.mahmoud.hadith.ui.activities.detail.DetailHadithActivity;
import com.mahmoud.hadith.ui.adapter.FavoriteRecyclerAdapter;
import com.mahmoud.hadith.ui.fragment.base.BaseFragment;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class FavoriteFragment extends BaseFragment implements FavoriteClickListener {
    public static int SOURSE = 213;
    private FragmentFavoriteBinding mFragmentFavoriteBinding;
    private FavoriteRecyclerAdapter mAdapter;
    private FavoriteViewModel mFavoriteViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentFavoriteBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_favorite,container,false);

        mFavoriteViewModel= ViewModelProviders.of(this).get(FavoriteViewModel.class);

        initView();

        mFavoriteViewModel.getAllFavorite().observe(getViewLifecycleOwner(),list->{
            if (list != null)
                mAdapter.setmItemList(list);
            mFragmentFavoriteBinding.favoriteShimmerViewContainer.stopShimmer();
            mFragmentFavoriteBinding.favoriteShimmerViewContainer.setVisibility(View.GONE);

        });

        return mFragmentFavoriteBinding.getRoot();
    }

    private void initView() {
        mAdapter =new FavoriteRecyclerAdapter(this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        mFragmentFavoriteBinding.favoriteRecyclerView.setLayoutManager(linearLayoutManager);
        mFragmentFavoriteBinding.favoriteRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onRemoveFavoriteClick(FavoriteItem favoriteItem) {
        Toast toast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);


        mFavoriteViewModel.deleteFavorite(favoriteItem).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    toast.setText("done delete");
                    toast.show();
                }else {
                    toast.setText("fail delete");
                    toast.show();
                }
            }
        });
    }

    @Override
    public void onShareClick(FavoriteItem hadithItem) {
        String body = "         " + hadithItem.getSanad() + "     \n" +
                "\n' " + hadithItem.getText() + " '\n";
        startActivity(Utils.getShareIntent(body));
    }

    @Override
    public void onTextClick(FavoriteItem hadithItem) {
        Intent intent = new Intent(getContext(), DetailHadithActivity.class);
        intent.putExtra(getString(R.string.single_hadith_item_key), hadithItem);
        startActivity(intent);
    }

}
