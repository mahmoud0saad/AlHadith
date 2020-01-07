package com.mahmoud.hadith.repository.retrofit.hadith;


import com.mahmoud.hadith.model.entity.api.chapter.ChapterResponse;
import com.mahmoud.hadith.model.entity.api.hadithwithout.HadithResponse;
import com.mahmoud.hadith.repository.retrofit.ApiConstans;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterfaceHadith {

    @GET(ApiConstans.END_POINT_HADITH +"{bookId}/"+"{chapterId}/"+"{language}")
    Observable<Response<HadithResponse>> getAllHadith(
            @Path(value = "bookId") int bookId,
            @Path(value = "chapterId") int chapterId,
            @Path(value = "language") String Language
    );
}
