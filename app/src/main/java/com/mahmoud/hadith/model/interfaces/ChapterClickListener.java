package com.mahmoud.hadith.model.interfaces;


import com.mahmoud.hadith.model.entity.api.chapter.ChapterItem;

public interface ChapterClickListener {

    void onPrgressClick(ChapterItem chapterItem, DownloadCallBack callBack);

    void onTextClick(String  id);
}
