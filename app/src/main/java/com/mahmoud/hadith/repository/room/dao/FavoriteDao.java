package com.mahmoud.hadith.repository.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mahmoud.hadith.model.entity.api.favorite.FavoriteItem;

import java.util.List;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


@Dao
public interface FavoriteDao {
    @Query("select * from favorite order by hadith_id")
    LiveData<List<FavoriteItem>> getAllHadith();


    @Query("select count(*) from favorite where chapter_id == :chapterId and book_id == :bookId and hadith_id ==:hadithId")
    Integer checkFavoriteHadith(int bookId, int chapterId, int hadithId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteItem favoriteItem);

    @Query("delete from favorite where book_id=:bookId and chapter_id=:chapterId and hadith_id =:hadithId")
    void delete(int bookId, int chapterId, int hadithId);
}
