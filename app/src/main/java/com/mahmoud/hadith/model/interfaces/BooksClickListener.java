package com.mahmoud.hadith.model.interfaces;


import com.mahmoud.hadith.model.entity.api.books.BooksItem;

public interface BooksClickListener {

    void onPrgressClick(DownloadCallBack callBack, BooksItem bookId);

    void onTextClick(BooksItem bookId);
}
