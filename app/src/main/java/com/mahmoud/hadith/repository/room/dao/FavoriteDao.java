package com.mahmoud.hadith.repository.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mahmoud.hadith.model.entity.api.favorite.FavoriteItem;

import java.util.List;

@Dao
public interface FavoriteDao {
    @Query("select * from favorite ")
    LiveData<List<FavoriteItem>> getAllHadith();


    @Query("select count(*) from favorite where chapter_id == :chapterId & book_id == :bookId & hadith_id ==:hadithId" )
    Integer checkFavoriteHadith(int bookId,int chapterId,int hadithId);

    @Query("select * from favorite where chapter_id = :chapterId & book_id = :bookId")
    LiveData<List<FavoriteItem>> getChapterBooks(int bookId,int chapterId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteItem favoriteItem);

    @Delete
    void delete(FavoriteItem favoriteItem);
}
