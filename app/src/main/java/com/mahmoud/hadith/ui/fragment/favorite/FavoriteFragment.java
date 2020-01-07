package com.mahmoud.hadith.ui.fragment.favorite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.databinding.FragmentFavoriteBinding;
import com.mahmoud.hadith.model.entity.api.favorite.FavoriteItem;
import com.mahmoud.hadith.model.interfaces.FavoriteClickListener;
import com.mahmoud.hadith.model.viewmodel.favorite.FavoriteViewModel;
import com.mahmoud.hadith.ui.adapter.FavoriteRecyclerAdapter;

public class FavoriteFragment extends Fragment implements FavoriteClickListener {
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
        mFavoriteViewModel.deleteFavorite(favoriteItem).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){
                    Toast.makeText(getContext(), "done delete", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), "fail delete", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onShareClick(FavoriteItem hadithItem) {

    }
}
