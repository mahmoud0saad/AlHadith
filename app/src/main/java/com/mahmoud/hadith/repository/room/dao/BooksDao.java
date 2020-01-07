package com.mahmoud.hadith.repository.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mahmoud.hadith.model.entity.api.books.BooksItem;

import java.util.List;

@Dao
public interface BooksDao {

    @Query("select * from bookItem ")
    LiveData<List<BooksItem>> getAllBooks();

    @Query("select Book_ID from bookItem ")
    List<Integer> getAllBookId();

    @Query("select count(*) from bookItem where Book_ID = :id")
    Integer checkBook(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BooksItem booksItem);

    @Delete
    void delete(BooksItem booksItem);

}
