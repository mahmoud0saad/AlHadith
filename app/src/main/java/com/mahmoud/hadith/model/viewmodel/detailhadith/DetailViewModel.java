package com.mahmoud.hadith.model.viewmodel.detailhadith;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.model.entity.api.favorite.FavoriteItem;
import com.mahmoud.hadith.model.entity.api.hadithwithout.HadithItem;
import com.mahmoud.hadith.model.utils.ConverterUtils;
import com.mahmoud.hadith.model.utils.SingleLiveEvent;
import com.mahmoud.hadith.model.utils.Utils;
import com.mahmoud.hadith.model.viewmodel.base.BaseViewModel;
import com.mahmoud.hadith.repository.room.RoomClient;
import com.mahmoud.hadith.repository.room.dao.FavoriteDao;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class DetailViewModel extends BaseViewModel {
    private static final String TAG = "DetailViewModel";
    private SingleLiveEvent<Boolean> resultAdded = new SingleLiveEvent<>();
    private FavoriteDao mFavoriteDao;

    public DetailViewModel(@NonNull Application application) {
        super(application);
        mFavoriteDao = RoomClient.getInstance(getApplication()).getAppDatabase().favoriteDao();
    }


    public SingleLiveEvent<Boolean> addToFavorite(HadithItem hadithItem) {
        Log.i(TAG, "addToFavorite: hewllo befoe");
        try {
            Disposable disposable = Observable.fromCallable(() -> {

                int count;
                int bookId = hadithItem.getHadithID(), chapterId = hadithItem.getChapterID(), hadithId = hadithItem.getHadithID();
                if (bookId != 0 && chapterId != 0 && hadithId != 0) {
                    count = mFavoriteDao.checkFavoriteHadith(bookId, chapterId, hadithId);
                } else {
                    return false;
                }
                if (count == 0) {
                    mFavoriteDao.insert(ConverterUtils.convertToFavorite(hadithItem));
                }
                return hadithItem;
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((result) -> {
                        Log.i(TAG, "addToFavorite: done ");
                        resultAdded.postValue(true);
                    });
        } catch (Exception ex) {
            resultAdded.postValue(false);
        }
        return resultAdded;
    }

    public HadithItem prepareIntent(Intent intent) {
        HadithItem hadithItem = new HadithItem();
        if (intent.hasExtra(getApplication().getResources().getString(R.string.single_hadith_item_key))) {
            Object object = intent.getSerializableExtra(getApplication().getResources().getString(R.string.single_hadith_item_key));
            if (object instanceof FavoriteItem) {
                hadithItem = Utils.convertFromFavoriteItem((FavoriteItem) object);
            } else if (object instanceof HadithItem) {
                hadithItem = (HadithItem) object;
            }
        }

        return hadithItem;
    }
}
