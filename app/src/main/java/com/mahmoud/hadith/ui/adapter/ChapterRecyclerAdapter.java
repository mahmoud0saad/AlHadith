package com.mahmoud.hadith.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.model.entity.api.chapter.ChapterItem;
import com.mahmoud.hadith.model.interfaces.ChapterClickListener;
import com.mahmoud.hadith.model.interfaces.DownloadCallBack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class ChapterRecyclerAdapter extends RecyclerView.Adapter<ChapterRecyclerAdapter.BookViewHolder> {

    private List<ChapterItem> mItemList = new ArrayList<>();
    private ChapterClickListener chapterClickListener;
    private int bookId;
    private boolean isDownload;

    public ChapterRecyclerAdapter(ChapterClickListener chapterClickListener) {
        this.chapterClickListener=chapterClickListener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //if is not Data Binding
        View rootView = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.list_item_chapter,
                        parent,
                        false);

        return new BookViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        holder.onBind(mItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public void setmItemList(List<ChapterItem> chapterItems) {
        mItemList.clear();
        mItemList.addAll(chapterItems);
        notifyDataSetChanged();
    }

    public void setIsDownload(boolean isDownload) {
        this.isDownload = isDownload;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        TextView nameBookTextView;
        ProgressBar progressBar;
        private ChapterItem mChapterItem;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            nameBookTextView=itemView.findViewById(R.id.name_chapter_text_view);
            progressBar=itemView.findViewById(R.id.load_chapter_progress_bar);

            progressBar.setOnClickListener(v ->{
                mChapterItem.setState(1);
                progressBar.setClickable(false);
                progressBar.setBackground(null);
                progressBar.setIndeterminate(true);
                chapterClickListener.onPrgressClick(mChapterItem, new DownloadCallBack() {
                    @Override
                    public void onResopnse(Boolean isMyDownload) {
                        if (isMyDownload) {
                            mChapterItem.setState(2);
                            progressBar.setIndeterminate(false);
                            progressBar.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.ic_done_black_24dp));
                        } else {
                            mChapterItem.setState(3);
                            progressBar.setIndeterminate(false);
                            convertToMyDownloadView(isDownload);

                            progressBar.setClickable(true);

                        }
                    }
                });
//

            });
            nameBookTextView.setOnClickListener(v->chapterClickListener.onTextClick((String) nameBookTextView.getTag()));
        }

        private void convertToMyDownloadView(boolean isDownload) {
            progressBar.setBackground(
                    itemView.getContext()
                            .getResources()
                            .getDrawable(isDownload ? R.drawable.ic_delete_forever_black_24dp : R.drawable.ic_arrow_downward_black_24dp)
            );
        }

        void onBind(ChapterItem chapterItem) {
            chapterItem.setBookId(bookId);
            if (isDownload) chapterItem.setState(0);


            mChapterItem = chapterItem;

            String tag=bookId+","+chapterItem.getChapterID();
            nameBookTextView.setTag(tag);
            nameBookTextView.setText(chapterItem.getChapterName());

            if (chapterItem.getState() == 0) {
                progressBar.setIndeterminate(false);
                convertToMyDownloadView(isDownload);
                progressBar.setClickable(true);
            } else if (chapterItem.getState() == 1) {
                progressBar.setClickable(false);
                progressBar.setBackground(null);
                progressBar.setIndeterminate(true);
            } else if (chapterItem.getState() == 2) {
                progressBar.setClickable(false);
                progressBar.setIndeterminate(false);
                progressBar.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.ic_done_black_24dp));
            } else {
                progressBar.setIndeterminate(false);
                convertToMyDownloadView(isDownload);
                progressBar.setClickable(true);
            }
        }


    }


}

