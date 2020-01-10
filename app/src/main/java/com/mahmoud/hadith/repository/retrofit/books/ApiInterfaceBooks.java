package com.mahmoud.hadith.repository.retrofit.books;


import com.mahmoud.hadith.model.entity.api.books.BooksResponse;
import com.mahmoud.hadith.repository.retrofit.ApiConstans;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public interface ApiInterfaceBooks {

    @GET(ApiConstans.END_POINT_BOOKS +"{language}")
    Observable<Response<BooksResponse>> getBooks(@Path(value = "language") String Language);

}
