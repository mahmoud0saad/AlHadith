package com.mahmoud.hadith.model.entity.api.books;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity(tableName = "bookItem")
public class BooksItem implements Serializable {


	@SerializedName("Book_Name")
	@ColumnInfo(name = "Book_Name")
	private String bookName;

	@SerializedName("Book_ID")
	@PrimaryKey
	@ColumnInfo(name = "Book_ID")
	private int bookID;

	@Ignore
	private int state;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setBookName(String bookName){
		this.bookName = bookName;
	}

	public String getBookName(){
		return bookName;
	}

	public void setBookID(int bookID){
		this.bookID = bookID;
	}

	public int getBookID(){
		return bookID;
	}

	@Override
 	public String toString(){
		return 
			"Book{" +
			"book_Name = '" + bookName + '\'' + 
			",book_ID = '" + bookID + '\'' + 
			"}";
		}
}