package com.mahmoud.hadith.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahmoud.hadith.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AzkarRecyclerAdapter extends RecyclerView.Adapter<AzkarRecyclerAdapter.AzkarViewHolder> {

    private List<String> mItemList = new ArrayList<>();

    public AzkarRecyclerAdapter() {

    }

    @NonNull
    @Override
    public AzkarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.list_item_azkar,
                        parent,
                        false);

        return new AzkarViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull AzkarViewHolder holder, int position) {
        holder.onBind(mItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public void setmItemList(List<String> azkarList) {
        mItemList.clear();

        mItemList.addAll(azkarList);

        notifyDataSetChanged();
    }

    public void addItem(String azkarText) {
        mItemList.add(azkarText);
        notifyDataSetChanged();
    }

    public Set<String> getSetItem() {
        return new HashSet<>(mItemList);
    }

    class AzkarViewHolder extends RecyclerView.ViewHolder {

        private TextView mAzkarTextView;


        public AzkarViewHolder(@NonNull View itemView) {
            super(itemView);
            mAzkarTextView = itemView.findViewById(R.id.azkar_text_view);
            ImageView imageView = itemView.findViewById(R.id.remove_azkar_image_view);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemList.size() != 1)
                        mItemList.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }

        void onBind(String textAzkar) {

            mAzkarTextView.setText(textAzkar);
        }


    }

}

