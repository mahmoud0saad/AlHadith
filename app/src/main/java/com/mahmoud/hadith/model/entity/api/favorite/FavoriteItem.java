package com.mahmoud.hadith.model.entity.api.favorite;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


@Entity(tableName = "favorite")
public class FavoriteItem implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "favorite_id")
    private int favoriteId;

    @ColumnInfo(name = "hadith_id")
    private int hadithID;

    @ColumnInfo(name = "hadith_sanad")
    private String sanad;

    @ColumnInfo(name = "hadith_text")
    private String text;

    @ColumnInfo(name = "book_id")
    private int bookId;

    @ColumnInfo(name = "chapter_id")
    private int chapterID;


    public int getFavoriteId() {
        return favoriteId;
    }

    public void setFavoriteId(int favoriteId) {
        this.favoriteId = favoriteId;
    }

    public void setHadithID(int hadithID){
        this.hadithID = hadithID;
    }

    public int getHadithID(){
        return hadithID;
    }

    public String getSanad() {
        return sanad;
    }

    public void setSanad(String sanad) {
        this.sanad = sanad;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getChapterID() {
        return chapterID;
    }

    public void setChapterID(int chapterID) {
        this.chapterID = chapterID;
    }


    @Override
    public String toString(){
        return
                "HadithItem{" +
                        "hadith_ID = '" + hadithID + '\'' +
                        ",ar_Sanad_Without_Tashkeel = '" + sanad + '\'' +
                        ",ar_Text_Without_Tashkeel = '" + text + '\'' +
                        "}";
    }
}