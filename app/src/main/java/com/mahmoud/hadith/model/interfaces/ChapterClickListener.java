package com.mahmoud.hadith.model.interfaces;


import com.mahmoud.hadith.model.entity.api.chapter.ChapterItem;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public interface ChapterClickListener {

    void onPrgressClick(ChapterItem chapterItem, DownloadCallBack callBack);

    void onTextClick(String  id);
}
