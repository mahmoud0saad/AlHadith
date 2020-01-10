package com.mahmoud.hadith.model.interfaces;


import com.mahmoud.hadith.model.entity.api.books.BooksItem;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public interface BooksClickListener {

    void onPrgressClick(DownloadCallBack callBack, BooksItem bookId);

    void onTextClick(BooksItem bookId);
}
