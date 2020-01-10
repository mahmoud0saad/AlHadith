package com.mahmoud.hadith.model.viewmodel.favorite;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.mahmoud.hadith.model.entity.api.favorite.FavoriteItem;
import com.mahmoud.hadith.model.utils.SingleLiveEvent;
import com.mahmoud.hadith.model.viewmodel.base.BaseViewModel;
import com.mahmoud.hadith.repository.room.RoomClient;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class FavoriteViewModel extends BaseViewModel {
    private static final String TAG = "FavoriteViewModel";

    SingleLiveEvent<Boolean> resultDelete=new SingleLiveEvent<>();
    public FavoriteViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<FavoriteItem>> getAllFavorite(){
        return RoomClient.getInstance(getApplication()).getAppDatabase().favoriteDao().getAllHadith();
    }

    public SingleLiveEvent<Boolean> deleteFavorite(FavoriteItem favoriteItem){
        try {
            Disposable disposable = Observable.fromCallable(() -> {
                RoomClient.getInstance(getApplication()).getAppDatabase().favoriteDao().delete(favoriteItem.getBookId(), favoriteItem.getChapterID(), favoriteItem.getHadithID());
                return favoriteItem;
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((result) -> {
                        resultDelete.setValue(true);
                        Log.i(TAG, "addToFavorite: done ");
                    });
        }catch (Exception ex){
            resultDelete.setValue(false);
        }
        return resultDelete;
    }
}
