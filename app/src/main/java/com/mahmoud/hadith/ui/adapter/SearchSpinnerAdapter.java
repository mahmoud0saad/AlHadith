package com.mahmoud.hadith.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.model.entity.api.books.BooksItem;

import java.util.ArrayList;
import java.util.List;

public class SearchSpinnerAdapter extends BaseAdapter {
    Context context;
    List<BooksItem> mBooksItems=new ArrayList<>();
    LayoutInflater inflter;

    public SearchSpinnerAdapter(Context applicationContext) {
        this.context = applicationContext;
        inflter = (LayoutInflater.from(applicationContext));
        addFieldAll();
    }

    public void setBooksItems(List<BooksItem> mBooksItems) {
        this.mBooksItems.clear();
        addFieldAll();
        this.mBooksItems.addAll(1,mBooksItems);
        notifyDataSetChanged();
    }

    public BooksItem getBookItem(int postion){

        return mBooksItems.get(postion);
    }

    @Override
    public int getCount() {
        return mBooksItems.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.row_item_spinner_search, null);
        TextView names = view.findViewById(R.id.spinner_book_text_view);
        names.setText(mBooksItems.get(i).getBookName());
        names.setTag(mBooksItems.get(i).getBookID());
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);

    }

    private void addFieldAll(){
        BooksItem booksItem=new BooksItem();
        booksItem.setBookID(0);
        booksItem.setBookName("All");

        this.mBooksItems.add(booksItem);
    }
}