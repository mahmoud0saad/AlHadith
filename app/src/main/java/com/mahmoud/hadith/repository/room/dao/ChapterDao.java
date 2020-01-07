package com.mahmoud.hadith.repository.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mahmoud.hadith.model.entity.api.chapter.ChapterItem;

import java.util.List;

@Dao
public interface ChapterDao {

    @Query("select chapter_id from chapter where book_id = :bookId")
    List<Integer> getAllChapterId(int bookId);

    @Query("select * from chapter where book_id = :bookId")
    LiveData<List<ChapterItem>> getAllChapter(int bookId);

    @Query("select * from chapter where book_id = :bookId")
    List<ChapterItem> getAllChapterForDelete(int bookId);

    @Query("select count(*) from chapter where chapter_id = :chapterId and book_id = :bookId")
    LiveData<Integer> checkChapter(int bookId,int chapterId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ChapterItem chapter);

    @Delete
    void delete(ChapterItem chapter);
}
