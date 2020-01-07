package com.mahmoud.hadith.model.utils;

import com.mahmoud.hadith.model.entity.api.favorite.FavoriteItem;
import com.mahmoud.hadith.model.entity.api.hadithwithout.HadithItem;

public  class ConverterUtils {
    public static FavoriteItem convertToFavorite(HadithItem hadithItem){
        FavoriteItem favoriteItem=new FavoriteItem();

        favoriteItem.setHadithID(hadithItem.getHadithID());
        if (hadithItem.getEnText()!=null) {
            favoriteItem.setSanad(hadithItem.getEnSanad());
            favoriteItem.setText(hadithItem.getEnText());
        }else if (hadithItem.getArText()!=null) {
            favoriteItem.setSanad(hadithItem.getArSanad1());
            favoriteItem.setText(hadithItem.getArText());
        }else if (hadithItem.getArTextWithoutTashkeel()!=null) {
            favoriteItem.setSanad(hadithItem.getArSanadWithoutTashkeel());
            favoriteItem.setText(hadithItem.getArTextWithoutTashkeel());
        }

        if (hadithItem.getBookId()!=0){
            favoriteItem.setBookId(hadithItem.getBookId());
            favoriteItem.setChapterID(hadithItem.getChapterID());
        }else if (hadithItem.getBookIdNew()!=0){
            favoriteItem.setBookId(hadithItem.getBookIdNew());
            favoriteItem.setChapterID(hadithItem.getChapterIDNew());
        }

        return favoriteItem;
    }
    public static HadithItem convertHadithToDatabase(HadithItem hadithItem){
        hadithItem.setArTextWithoutTashkeel(hadithItem.getHadithText());
        hadithItem.setArSanadWithoutTashkeel(hadithItem.getSanadText());
        return hadithItem;
    }
}
