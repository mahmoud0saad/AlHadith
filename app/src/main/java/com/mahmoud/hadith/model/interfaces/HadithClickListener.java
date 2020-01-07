package com.mahmoud.hadith.model.interfaces;


import com.mahmoud.hadith.model.entity.api.hadithwithout.HadithItem;

public interface HadithClickListener {


    void onFavoriteClick(HadithItem hadithItem);
    void onShareClick(HadithItem hadithItem);

}
