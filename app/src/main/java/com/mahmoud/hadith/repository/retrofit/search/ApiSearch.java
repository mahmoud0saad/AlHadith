package com.mahmoud.hadith.repository.retrofit.search;

import android.content.Context;

import com.mahmoud.hadith.model.entity.api.hadithwithout.HadithResponse;
import com.mahmoud.hadith.repository.retrofit.ApiConfig;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ApiSearch {
    private static ApiSearch apiServices;
    private static Retrofit retrofit;

    public static synchronized ApiSearch open(Context context){
        if (retrofit==null){
            retrofit= ApiConfig.getRetrofit(context);
        }
        if (apiServices==null){
            apiServices=new ApiSearch();
        }
        return apiServices;
    }

    public Observable<Response<HadithResponse>> searchForHadith(String searchKeyword, String language){
        ApiInterfaceSearch apiInterfaceSearch=retrofit.create(ApiInterfaceSearch.class);
        return apiInterfaceSearch.searchForHadith(searchKeyword,language)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
