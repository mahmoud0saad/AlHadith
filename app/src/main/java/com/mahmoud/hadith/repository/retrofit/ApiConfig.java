package com.mahmoud.hadith.repository.retrofit;

import android.content.Context;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class ApiConfig {
    private static final String TAG = "ApiConfig";
    private final static int CACHE_SIZE_BYTES = 1024 * 1024 * 2;
    private static Retrofit retrofit;
    private static OkHttpClient client;

    public static synchronized Retrofit getRetrofit(Context context){
        if (retrofit==null){
            retrofit=new Retrofit.Builder()
                    .baseUrl(ApiConstans.BASE_URL)
                    .client(getClient(context))
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static OkHttpClient getClient(Context context) {
        if (client==null){
            HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
            httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            Interceptor interceptor=new Interceptor() {

                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request=chain.request();
                    Log.i(TAG, "intercept:reques "+chain+request.body());
                    Response response=chain.proceed(request);
                    Log.i(TAG, "intercept:response "+response+response.body());
                    return response;
                }
            };

            client=new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .addInterceptor(httpLoggingInterceptor)
                    .cache(getCashe(context))
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();
        }
        return client;
    }

    private static Cache getCashe(Context context) {
        if (context==null)return null;
        return new Cache(context.getCacheDir(),CACHE_SIZE_BYTES);
    }

}
