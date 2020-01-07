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
import com.mahmoud.hadith.model.entity.api.favorite.FavoriteItem;
import com.mahmoud.hadith.model.interfaces.FavoriteClickListener;

import java.util.ArrayList;
import java.util.List;

public class FavoriteRecyclerAdapter extends RecyclerView.Adapter<FavoriteRecyclerAdapter.HadithViewHolder> {
    private static final String TAG = "HadithRecyclerAdapter";
    private List<FavoriteItem> mItemList = new ArrayList<>();
    private FavoriteClickListener mFavoriteClickListener;

    public FavoriteRecyclerAdapter(FavoriteClickListener favoriteClickListener) {
        mFavoriteClickListener =favoriteClickListener;
    }

    @NonNull
    @Override
    public HadithViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //if is not Data Binding
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.row_item_hadith,
                        parent,
                        false);
        Button button=rootView.findViewById(R.id.favorite_button);
        button.setText(rootView.getResources().getString(R.string.favorite_remove_name_button));
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

    public void setmItemList(List<FavoriteItem> favoriteItems) {
        mItemList.clear();
        mItemList.addAll(favoriteItems);
        notifyDataSetChanged();
    }


    class HadithViewHolder extends RecyclerView.ViewHolder {
        private TextView sanadHaditTextView,textHadithTextView;
        private Button shareButton,favoriteButton;
        private FavoriteItem favoriteItem;
        public HadithViewHolder(@NonNull View itemView) {
            super(itemView);
            sanadHaditTextView=itemView.findViewById(R.id.sanad_hadith_textview);
            textHadithTextView=itemView.findViewById(R.id.text_hadith_textview);
            shareButton=itemView.findViewById(R.id.share_button);
            favoriteButton=itemView.findViewById(R.id.favorite_button);

            shareButton.setOnClickListener(v -> {
                Log.i(TAG, "HadithViewHolder: ");
                mFavoriteClickListener.onShareClick(favoriteItem);

            });

            favoriteButton.setOnClickListener(v -> mFavoriteClickListener.onRemoveFavoriteClick(favoriteItem));




        }

        void onBind(FavoriteItem favoriteItem) {
            this.favoriteItem = favoriteItem;
            sanadHaditTextView.setText(favoriteItem.getSanad());
            textHadithTextView.setText(favoriteItem.getText());

        }

    }
}

