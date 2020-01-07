package com.mahmoud.hadith.model.viewmodel.hadith;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.model.entity.api.hadithwithout.HadithItem;
import com.mahmoud.hadith.model.entity.api.hadithwithout.HadithResponse;
import com.mahmoud.hadith.model.utils.ConverterUtils;
import com.mahmoud.hadith.model.utils.SingleLiveEvent;
import com.mahmoud.hadith.model.utils.sharedpreference.UserData;
import com.mahmoud.hadith.repository.retrofit.hadith.ApiHadith;
import com.mahmoud.hadith.repository.room.RoomClient;
import com.mahmoud.hadith.repository.room.dao.FavoriteDao;
import com.mahmoud.hadith.ui.fragment.mydownload.MyDownloadFragment;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class HadithViewModel extends AndroidViewModel {
    private static final String TAG = "HadithViewModel";
    SingleLiveEvent<Boolean> resultAdded=new SingleLiveEvent<>();
    private FavoriteDao favoriteDao;
    private MutableLiveData<List<HadithItem>> chapterLiveData=new MutableLiveData<>();
    public int bookId, chapterId;
    private UserData mUserData;

    public HadithViewModel(@NonNull Application application) {
        super(application);
        favoriteDao=RoomClient.getInstance(getApplication()).getAppDatabase().favoriteDao();
        mUserData = UserData.getInstance(getApplication());
    }

    public MutableLiveData<List<HadithItem>> getHadithInternetLiveData(int bookId, int chapterId) {
        try {
            ApiHadith.open(getApplication()).getAllHadith(bookId, chapterId, mUserData.getLanguageForHadith(getApplication().getResources().getString(R.string.language_ar))).subscribe(new SingleObserver<Response<HadithResponse>>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onSuccess(Response<HadithResponse> hadithResponseResponse) {
                    Log.i(TAG, "onSuccess: " + hadithResponseResponse.body());
                    if (hadithResponseResponse.body() != null){
                        chapterLiveData.postValue(hadithResponseResponse.body().getHadithWithout());
                    } else {
                        chapterLiveData.postValue(null);
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Log.i(TAG, "onError: " + e);
                    chapterLiveData.postValue(null);

                }
            });
        }catch (Exception e){
            Log.i(TAG, "getBooksLiveData: "+e);
        }
        return chapterLiveData;

    }


    public SingleLiveEvent<Boolean> addToFavorite(HadithItem hadithItem) {
        Log.i(TAG, "addToFavorite: hewllo befoe");
        try {
            Disposable disposable = Observable.fromCallable(() -> {
                favoriteDao.insert(ConverterUtils.convertToFavorite(hadithItem));

                return hadithItem;
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((result) -> {
                        Log.i(TAG, "addToFavorite: done ");
                        resultAdded.postValue(true);
                    });
        }catch (Exception ex){
            resultAdded.postValue(false);
        }
        return resultAdded;
    }

    public LiveData<List<HadithItem>> getHadithDatabaseLiveData(int bookId, int chapterId){
        return RoomClient.getInstance(getApplication()).getAppDatabase().hadithDao().getAllHadith(bookId,chapterId);
    }

    public  LiveData<List<HadithItem>> prepareIntentAction(Intent intent) {
        if (intent==null)return new MutableLiveData<>();

        if (!isSourseDownload(intent)) {
            if (intent.hasExtra(getApplication().getResources().getString(R.string.book_id_from_chapter_key))
                    &&intent.hasExtra(getApplication().getResources().getString(R.string.chapter_id_from_chapter_key))){

                bookId = intent.getIntExtra(getApplication().getResources().getString(R.string.book_id_from_chapter_key), 0);
                chapterId = intent.getIntExtra(getApplication().getResources().getString(R.string.chapter_id_from_chapter_key), 0);
                return getHadithInternetLiveData(bookId, chapterId);

            }
        } else if (isSourseDownload(intent)) {
            if (intent.hasExtra(getApplication().getResources().getString(R.string.book_id_from_chapter_key))
                    &&intent.hasExtra(getApplication().getResources().getString(R.string.chapter_id_from_chapter_key))){

                bookId = intent.getIntExtra(getApplication().getResources().getString(R.string.book_id_from_chapter_key), 0);
                chapterId = intent.getIntExtra(getApplication().getResources().getString(R.string.chapter_id_from_chapter_key), 0);
                return getHadithDatabaseLiveData(bookId,chapterId);
            }
        }
        return new MutableLiveData<>();
    }

    public boolean isSourseDownload(Intent intent) {
        int sourse = 0;
        if (intent.hasExtra(getApplication().getResources().getString(R.string.sourse_intent_key))) {
            sourse = intent.getIntExtra(getApplication().getResources().getString(R.string.sourse_intent_key), 0);
        }
        return sourse == MyDownloadFragment.DOWNLOAD_FRAGMENT_SOURSE_TAG;
    }

}
