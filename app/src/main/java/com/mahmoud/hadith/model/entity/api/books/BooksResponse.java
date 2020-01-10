package com.mahmoud.hadith.model.entity.api.books;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */

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