package com.mahmoud.hadith.repository.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mahmoud.hadith.model.entity.api.hadithwithout.HadithItem;

import java.util.List;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


@Dao
public interface HadithDao {


    @Query("select * from hadith where chapter_id = :chapterId and book_id = :bookId order by hadith_id")
    LiveData<List<HadithItem>> getAllHadith(int bookId, int chapterId);

    @Query("select * from hadith where chapter_id = :chapterId and book_id = :bookId")
    List<HadithItem> getAllHadithForDelete(int bookId, int chapterId);

    @Query("select count(*) from hadith where chapter_id = :chapterId & book_id = :bookId & hadith_id =:hadithId" )
    Integer checkHadith(int bookId, int chapterId, int hadithId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HadithItem chapter);

    @Delete
    void delete(HadithItem chapter);
}
