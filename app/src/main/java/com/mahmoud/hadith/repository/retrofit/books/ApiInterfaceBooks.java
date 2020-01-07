package com.mahmoud.hadith.repository.retrofit.books;


import com.mahmoud.hadith.model.entity.api.books.BooksResponse;
import com.mahmoud.hadith.repository.retrofit.ApiConstans;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterfaceBooks {

    @GET(ApiConstans.END_POINT_BOOKS +"{language}")
    Observable<Response<BooksResponse>> getBooks(@Path(value = "language") String Language);

}
