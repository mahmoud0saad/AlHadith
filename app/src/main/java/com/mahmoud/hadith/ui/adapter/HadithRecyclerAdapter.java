package com.mahmoud.hadith.ui.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.model.entity.api.hadithwithout.HadithItem;
import com.mahmoud.hadith.model.interfaces.HadithClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class HadithRecyclerAdapter extends RecyclerView.Adapter<HadithRecyclerAdapter.HadithViewHolder> {
    private static final String TAG = "HadithRecyclerAdapter";
    private List<HadithItem> mItemList = new ArrayList<>();
    private int bookId,chapterId;
    private HadithClickListener mHadithClickListener;
    private boolean isSearch;

    public HadithRecyclerAdapter(HadithClickListener hadithClickListener, boolean isSearchFragment) {
        mHadithClickListener=hadithClickListener;
        isSearch = isSearchFragment;
    }

    @NonNull
    @Override
    public HadithViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //if is not Data Binding
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.list_item_hadith,
                        parent,
                        false);
        if (isSearch) {
            rootView.findViewById(R.id.favorite_share_linear_layout).setVisibility(View.GONE);
        }
        return new HadithViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull HadithViewHolder holder, int position) {
        holder.onBind(mItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public void setmItemList(List<HadithItem> HadithItemList) {
        mItemList.clear();
        mItemList.addAll(HadithItemList);
        notifyDataSetChanged();
    }

    public void clear() {
        mItemList.clear();
        notifyDataSetChanged();
    }

    public void setIds(int bookId, int chapterId){
        this.bookId=bookId;
        this.chapterId=chapterId;
    }

    class HadithViewHolder extends RecyclerView.ViewHolder {
        private TextView sanadHaditTextView,textHadithTextView;
        private Button shareButton,favoriteButton;
        private HadithItem mHadithItem;

        public HadithViewHolder(@NonNull View itemView) {
            super(itemView);
            sanadHaditTextView=itemView.findViewById(R.id.sanad_hadith_textview);
            textHadithTextView=itemView.findViewById(R.id.text_hadith_textview);
            shareButton=itemView.findViewById(R.id.share_button);
            favoriteButton=itemView.findViewById(R.id.favorite_button);

            setIsRecyclable(false);

            shareButton.setOnClickListener(v -> {
                Log.i(TAG, "HadithViewHolder: ");
                mHadithClickListener.onShareClick(mHadithItem);

            });


            favoriteButton.setOnClickListener(v -> {
                mHadithItem.setFavoriteState(!mHadithItem.isFavoriteState());
                changeFavorite(mHadithItem.isFavoriteState());

                mHadithClickListener.onFavoriteClick(mHadithItem);
            });
            sanadHaditTextView.setOnClickListener(v -> mHadithClickListener.onTextClick(mHadithItem));

            textHadithTextView.setOnClickListener(v -> mHadithClickListener.onTextClick(mHadithItem));


        }

        void onBind(HadithItem hadithItem) {
            if (hadithItem.getBookId()==0&&hadithItem.getChapterID()==0) {
                hadithItem.setBookId(bookId);
                hadithItem.setChapterID(chapterId);
            }


            this.mHadithItem = hadithItem;


            mHadithClickListener.checkFavoriteOnDatabase(hadithItem.getHadithID(), this::changeFavorite);

            sanadHaditTextView.setText(hadithItem.getSanadText());

            textHadithTextView.setText(hadithItem.getHadithText());
        }

        private void changeFavorite(boolean isFavorite) {
            favoriteButton.setCompoundDrawablesWithIntrinsicBounds(
                    itemView.getResources()
                            .getDrawable(
                                    isFavorite ? R.drawable.ic_favorite_red_24dp : R.drawable.ic_favorite_green_24dp
                            ),
                    null,
                    null,
                    null
            );
        }

    }
}

