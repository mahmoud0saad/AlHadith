package com.mahmoud.hadith.model.viewmodel.favorite;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mahmoud.hadith.model.entity.api.favorite.FavoriteItem;
import com.mahmoud.hadith.model.utils.SingleLiveEvent;
import com.mahmoud.hadith.repository.room.RoomClient;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FavoriteViewModel extends AndroidViewModel {
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
                RoomClient.getInstance(getApplication()).getAppDatabase().favoriteDao().delete(favoriteItem);
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
