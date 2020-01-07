package com.mahmoud.hadith.repository.retrofit.books;

import android.content.Context;

import com.mahmoud.hadith.model.entity.api.books.BooksResponse;
import com.mahmoud.hadith.repository.retrofit.ApiConfig;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ApiBooks {
    private static ApiBooks apiServices;
    private static Retrofit retrofit;

    public static synchronized ApiBooks open(Context context){
        if (retrofit==null){
            retrofit= ApiConfig.getRetrofit(context);
        }
        if (apiServices==null){
            apiServices=new ApiBooks();
        }
        return apiServices;
    }

    public Observable<Response<BooksResponse>> getBooks(String language){
        ApiInterfaceBooks apiInterfaceBooks=retrofit.create(ApiInterfaceBooks.class);
        return apiInterfaceBooks.getBooks(language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
