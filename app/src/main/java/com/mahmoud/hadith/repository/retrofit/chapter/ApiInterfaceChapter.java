package com.mahmoud.hadith.repository.retrofit.chapter;


import com.mahmoud.hadith.model.entity.api.chapter.ChapterResponse;
import com.mahmoud.hadith.repository.retrofit.ApiConstans;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public interface ApiInterfaceChapter {

    @GET(ApiConstans.END_POINT_CHAPTER +"{bookId}/"+"{language}")
    Observable<Response<ChapterResponse>> getChapters(
            @Path(value = "bookId") int bookId,
            @Path(value = "language") String Language
    );
}
