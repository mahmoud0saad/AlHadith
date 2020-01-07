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

public class HadithRecyclerAdapter extends RecyclerView.Adapter<HadithRecyclerAdapter.HadithViewHolder> {
    private static final String TAG = "HadithRecyclerAdapter";
    private List<HadithItem> mItemList = new ArrayList<>();
    private int bookId,chapterId;
    private HadithClickListener mHadithClickListener;

    public HadithRecyclerAdapter(HadithClickListener hadithClickListener) {
        mHadithClickListener=hadithClickListener;
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

    public void setIds(int bookId,int chapterId){
        this.bookId=bookId;
        this.chapterId=chapterId;
    }

    class HadithViewHolder extends RecyclerView.ViewHolder {
        private TextView sanadHaditTextView,textHadithTextView;
        private Button shareButton,favoriteButton;
        private HadithItem hadithItem;
        public HadithViewHolder(@NonNull View itemView) {
            super(itemView);
            sanadHaditTextView=itemView.findViewById(R.id.sanad_hadith_textview);
            textHadithTextView=itemView.findViewById(R.id.text_hadith_textview);
            shareButton=itemView.findViewById(R.id.share_button);
            favoriteButton=itemView.findViewById(R.id.favorite_button);

            shareButton.setOnClickListener(v -> {
                Log.i(TAG, "HadithViewHolder: ");
                mHadithClickListener.onShareClick(hadithItem);

            });

            favoriteButton.setOnClickListener(v -> mHadithClickListener.onFavoriteClick(hadithItem));




        }

        void onBind(HadithItem hadithItem) {
            if (hadithItem.getBookId()==0&&hadithItem.getChapterID()==0) {
                hadithItem.setBookId(bookId);
                hadithItem.setChapterID(chapterId);
            }

            this.hadithItem=hadithItem;

            sanadHaditTextView.setText(hadithItem.getSanadText());

            textHadithTextView.setText(hadithItem.getHadithText());
        }

    }
}

