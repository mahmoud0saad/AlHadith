package com.mahmoud.hadith.model.entity.api.chapter;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;

import com.google.gson.annotations.SerializedName;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


@Entity(tableName = "chapter", primaryKeys = {"chapter_id", "book_id"})
public class ChapterItem{


    @SerializedName("Chapter_ID")
	@ColumnInfo(name = "chapter_id")
	private int chapterID;

	@SerializedName("Chapter_Name")
	@ColumnInfo(name = "chapter_Name")
	private String chapterName;


	@ColumnInfo(name = "book_id")
	private int bookId;

    @Ignore
    private int state = 0;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

	public void setChapterID(int chapterID){
		this.chapterID = chapterID;
	}

	public int getChapterID(){
		return chapterID;
	}

	public void setChapterName(String chapterName){
		this.chapterName = chapterName;
	}

	public String getChapterName(){
		return chapterName;
	}



	public int getBookId() {
		return bookId;
	}

	public void setBookId(int bookId) {
		this.bookId = bookId;
	}

	@Override
 	public String toString(){
		return 
			"HadithItem{" +
			"chapter_ID = '" + chapterID + '\'' + 
			",chapter_Name = '" + chapterName + '\'' + 
			"}";
		}
}