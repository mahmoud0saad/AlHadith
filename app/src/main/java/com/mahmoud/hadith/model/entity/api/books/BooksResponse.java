package com.mahmoud.hadith.model.entity.api.books;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class BooksResponse{

	@SerializedName("code")
	private int code;

	@SerializedName("Books")
	private List<BooksItem> books;

	public void setCode(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public void setBooks(List<BooksItem> books){
		this.books = books;
	}

	public List<BooksItem> getBooks(){
		return books;
	}

	@Override
 	public String toString(){
		return 
			"BooksResponse{" + 
			"code = '" + code + '\'' + 
			",books = '" + books + '\'' + 
			"}";
		}
}