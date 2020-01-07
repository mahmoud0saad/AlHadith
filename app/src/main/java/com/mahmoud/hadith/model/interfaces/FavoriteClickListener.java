package com.mahmoud.hadith.model.interfaces;


import com.mahmoud.hadith.model.entity.api.favorite.FavoriteItem;

public interface FavoriteClickListener {


    void onRemoveFavoriteClick(FavoriteItem hadithItem);
    void onShareClick(FavoriteItem hadithItem);

}
