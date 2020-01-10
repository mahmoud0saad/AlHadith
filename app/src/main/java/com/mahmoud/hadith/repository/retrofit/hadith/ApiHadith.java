package com.mahmoud.hadith.repository.retrofit.hadith;

import android.content.Context;

import com.mahmoud.hadith.model.entity.api.hadithwithout.HadithResponse;
import com.mahmoud.hadith.repository.retrofit.ApiConfig;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class ApiHadith {
    private static ApiHadith apiServices;
    private static Retrofit retrofit;

    public static synchronized ApiHadith open(Context context){
        if (retrofit==null){
            retrofit= ApiConfig.getRetrofit(context);
        }
        if (apiServices==null){
            apiServices=new ApiHadith();
        }
        return apiServices;
    }

    public  Single<Response<HadithResponse>> getAllHadith(int bookId, int chapterId, String language){
        ApiInterfaceHadith apiInterfaceHadith=retrofit.create(ApiInterfaceHadith.class);
        return apiInterfaceHadith.getAllHadith(bookId,chapterId,language)
                .singleOrError()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
