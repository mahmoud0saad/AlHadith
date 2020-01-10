package com.mahmoud.hadith.repository.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mahmoud.hadith.model.entity.api.books.BooksItem;

import java.util.List;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


@Dao
public interface BooksDao {

    @Query("select * from bookItem  order by  Book_ID")
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
