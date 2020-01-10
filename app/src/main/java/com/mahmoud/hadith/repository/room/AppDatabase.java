package com.mahmoud.hadith.repository.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.mahmoud.hadith.model.entity.api.books.BooksItem;
import com.mahmoud.hadith.model.entity.api.chapter.ChapterItem;
import com.mahmoud.hadith.model.entity.api.favorite.FavoriteItem;
import com.mahmoud.hadith.model.entity.api.hadithwithout.HadithItem;
import com.mahmoud.hadith.repository.room.dao.BooksDao;
import com.mahmoud.hadith.repository.room.dao.ChapterDao;
import com.mahmoud.hadith.repository.room.dao.FavoriteDao;
import com.mahmoud.hadith.repository.room.dao.HadithDao;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


@Database(entities = {BooksItem.class, ChapterItem.class, HadithItem.class, FavoriteItem.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public static String DB_NAME="Hadith";

    public abstract BooksDao booksDao();

    public abstract ChapterDao chapterDao();

    public abstract HadithDao hadithDao();

    public abstract FavoriteDao favoriteDao();


}
