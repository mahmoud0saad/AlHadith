package com.mahmoud.hadith.repository.retrofit.chapter;

import android.content.Context;

import com.mahmoud.hadith.model.entity.api.chapter.ChapterResponse;
import com.mahmoud.hadith.repository.retrofit.ApiConfig;
import com.mahmoud.hadith.repository.retrofit.books.ApiInterfaceBooks;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ApiChapter {
    private static ApiChapter apiServices;
    private static Retrofit retrofit;

    public static synchronized ApiChapter open(Context context){
        if (retrofit==null){
            retrofit= ApiConfig.getRetrofit(context);
        }
        if (apiServices==null){
            apiServices=new ApiChapter();
        }
        return apiServices;
    }

    public  Single<Response<ChapterResponse>> getChapter(int bookId,String language){
        ApiInterfaceChapter apiInterfaceChapter=retrofit.create(ApiInterfaceChapter.class);
        return apiInterfaceChapter.getChapters(bookId,language)
                .singleOrError()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
