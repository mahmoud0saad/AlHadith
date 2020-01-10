package com.mahmoud.hadith.model.interfaces;


import com.mahmoud.hadith.model.entity.api.favorite.FavoriteItem;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public interface FavoriteClickListener {


    void onRemoveFavoriteClick(FavoriteItem hadithItem);

    void onShareClick(FavoriteItem hadithItem);

    void onTextClick(FavoriteItem hadithItem);

}
