package com.mahmoud.hadith.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.databinding.RowItemBookBinding;
import com.mahmoud.hadith.model.entity.api.books.BooksItem;
import com.mahmoud.hadith.model.interfaces.BooksClickListener;
import com.mahmoud.hadith.model.interfaces.DownloadCallBack;

import java.util.ArrayList;
import java.util.List;

public class BooksRecyclerAdapter extends RecyclerView.Adapter<BooksRecyclerAdapter.BookViewHolder> {

    private List<BooksItem> mItemList = new ArrayList<>();
    private BooksClickListener booksClickListener;
    private boolean isDownload = false;
    private RowItemBookBinding mRowItemBookBinding;

    public BooksRecyclerAdapter(BooksClickListener booksClickListener, boolean isDownload) {
        this.isDownload = isDownload;
        this.booksClickListener=booksClickListener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //if is not Data Binding
        mRowItemBookBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.row_item_book, parent, false);


        return new BookViewHolder(mRowItemBookBinding.getRoot());
    }


    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        holder.setIsRecyclable(false);

        holder.onBind(mItemList.get(position));

    }


    @Override
    public int getItemCount() {
        return mItemList.size();
    }

    public void setmItemList(List<BooksItem> BooksItemList) {
        mItemList.clear();
        mItemList.addAll(BooksItemList);
        notifyDataSetChanged();
    }

    public void clear() {
        mItemList.clear();
        isDownload = false;
        booksClickListener = null;
    }

    class BookViewHolder extends RecyclerView.ViewHolder {
        TextView nameBookTextView;
        ProgressBar progressBar;
        private BooksItem mBooksItem;
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            nameBookTextView=itemView.findViewById(R.id.name_book_text_view);
            progressBar=itemView.findViewById(R.id.load_book_progress_bar);
            progressBar.setOnClickListener(v ->{
                mBooksItem.setState(1);
                progressBar.setClickable(false);
                progressBar.setBackground(null);
                progressBar.setIndeterminate(true);

                //start download or delete of chapters of book and get the response
                booksClickListener.onPrgressClick(new DownloadCallBack() {
                    @Override
                    public void onResopnse(Boolean isMyDownload) {
                        if (isMyDownload) {
                            mBooksItem.setState(2);
                            progressBar.setIndeterminate(false);
                            if (isDownload) {
                                notifyDataSetChanged();
                            }
                            progressBar.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.ic_done_black_24dp));

                        }else {
                            mBooksItem.setState(3);
                            progressBar.setIndeterminate(false);
                            convertToMyDownloadView(isDownload);

                            progressBar.setClickable(true);

                        }
                    }
                }, mBooksItem);
//

            });
            nameBookTextView.setOnClickListener(v -> booksClickListener.onTextClick(mBooksItem));
        }

        void onBind(BooksItem booksItem) {
            mBooksItem=booksItem;
            nameBookTextView.setText(booksItem.getBookName());

            if (booksItem.getState()==0) {
                progressBar.setIndeterminate(false);
                convertToMyDownloadView(isDownload);
                progressBar.setClickable(true);
            }else if (booksItem.getState()==1){
                progressBar.setClickable(false);
                progressBar.setBackground(null);
                progressBar.setIndeterminate(true);
            }else if (booksItem.getState()==2){
                progressBar.setClickable(false);
                progressBar.setIndeterminate(false);
                progressBar.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.ic_done_black_24dp));
            }else {
                progressBar.setIndeterminate(false);
                convertToMyDownloadView(isDownload);
                progressBar.setClickable(true);
            }
        }


        private void convertToMyDownloadView(boolean isDownload) {
            progressBar.setBackground(
                    itemView.getContext()
                            .getResources()
                            .getDrawable(isDownload ? R.drawable.ic_delete_forever_black_24dp : R.drawable.ic_arrow_downward_black_24dp)
            );
        }
    }


}

