package com.mahmoud.hadith.model.interfaces;


import com.mahmoud.hadith.model.entity.api.hadithwithout.HadithItem;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public interface HadithClickListener {


    void onFavoriteClick(HadithItem hadithItem);

    void onShareClick(HadithItem hadithItem);

    void onTextClick(HadithItem hadithItem);

    void checkFavoriteOnDatabase(int hadithId, CheckFavoriteCallBack callBack);
}
