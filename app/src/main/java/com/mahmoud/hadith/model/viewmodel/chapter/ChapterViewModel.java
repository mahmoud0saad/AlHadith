package com.mahmoud.hadith.model.viewmodel.chapter;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.model.entity.api.books.BooksItem;
import com.mahmoud.hadith.model.entity.api.chapter.ChapterItem;
import com.mahmoud.hadith.model.entity.api.chapter.ChapterResponse;
import com.mahmoud.hadith.model.interfaces.DownloadCallBack;
import com.mahmoud.hadith.model.utils.DeleteBookUtils;
import com.mahmoud.hadith.model.utils.DownloadBookUtils;
import com.mahmoud.hadith.model.utils.sharedpreference.UserData;
import com.mahmoud.hadith.repository.retrofit.chapter.ApiChapter;
import com.mahmoud.hadith.repository.room.RoomClient;
import com.mahmoud.hadith.ui.fragment.mydownload.MyDownloadFragment;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ChapterViewModel extends AndroidViewModel {
    private static final String TAG = "ChapterViewModel";
    MutableLiveData<List<ChapterItem>> chapterLiveData=new MutableLiveData<>();
    public boolean isSourseDownload;
    public BooksItem mBooksItem;
    private UserData mUserData;

    public ChapterViewModel(@NonNull Application application) {
        super(application);
        mUserData = UserData.getInstance(getApplication());
    }


    public MutableLiveData<List<ChapterItem>> getChapterInternetLiveData(int id) {
        try {
            ApiChapter.open(getApplication()).getChapter(id, mUserData.getLanguagePublic(getApplication().getResources().getString(R.string.language_ar))).subscribe(new SingleObserver<Response<ChapterResponse>>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onSuccess(Response<ChapterResponse> chapterResponseResponse) {
                    Log.i(TAG, "onSuccess: " + chapterResponseResponse.body());
                    if (chapterResponseResponse.body() != null) {
                        chapterLiveData.postValue(chapterResponseResponse.body().getChapter());
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

    private LiveData<List<ChapterItem>> getChapterDatabaseLiveData(int id) {
        return RoomClient.getInstance(getApplication()).getAppDatabase().chapterDao().getAllChapter(id);

    }

    public LiveData<List<ChapterItem>> prepareIntentAction(Intent intent) {
        if (intent == null) return new MutableLiveData<>();

        if (isSourseDownload(intent)) {
            isSourseDownload = true;
            if (intent.hasExtra(getApplication().getResources().getString(R.string.book_id_key))) {
                mBooksItem = (BooksItem) intent.getSerializableExtra(getApplication().getResources().getString(R.string.book_id_key));
                return getChapterDatabaseLiveData(mBooksItem != null ? mBooksItem.getBookID() : 0);
            }
        } else {
            isSourseDownload = false;
            if (intent.hasExtra(getApplication().getResources().getString(R.string.book_id_key))) {
                mBooksItem = (BooksItem) intent.getSerializableExtra(getApplication().getResources().getString(R.string.book_id_key));
                return getChapterInternetLiveData(mBooksItem != null ? mBooksItem.getBookID() : 0);
            }
        }
        return new MutableLiveData<>();
    }


    private void downloadChapterAndBook(DownloadCallBack callBack, ChapterItem chapterItem) {
        try {

            DownloadBookUtils downloadBookUtils = new DownloadBookUtils(getApplication(), callBack);
            downloadBookUtils.storeBookOnDatabase(mBooksItem);
            downloadBookUtils.downloadChapter(chapterItem, mUserData.getLanguagePublic(getApplication().getResources().getString(R.string.language_ar)));

        } catch (Exception e) {
            callBack.onResopnse(false);

            Log.i(TAG, "getBooksLiveData: " + e);
        }
    }

    private void deleteChapterFromDatabase(ChapterItem chapterItem, DownloadCallBack callBack) {
        try {
            new DeleteBookUtils(getApplication(), callBack).deleteChapter(chapterItem);

        } catch (Exception ex) {
            callBack.onResopnse(false);

            Log.i(TAG, "getBooksLiveData: " + ex);
        }
    }


    public void actionProgressClick(ChapterItem chapterItem, DownloadCallBack callBack) {
        if (isSourseDownload) {
            deleteChapterFromDatabase(chapterItem, callBack);
        } else {
            downloadChapterAndBook(callBack, chapterItem);
        }
    }


    public List<ChapterItem> makeOperation(List<Integer> integers, List<ChapterItem> chapterItems) {
        List<ChapterItem> resultChapterItems = new ArrayList<>();
        for (ChapterItem chapterItem : chapterItems) {
            if (integers.contains(chapterItem.getChapterID())) {
                chapterItem.setState(2);
            }
            resultChapterItems.add(chapterItem);
        }
        return resultChapterItems;
    }

    public MutableLiveData<List<ChapterItem>> setStateForChapterList(List<ChapterItem> chapterItems) {
        MutableLiveData<List<ChapterItem>> mutableLiveData = new MutableLiveData<>();

        Observable.fromCallable(() -> RoomClient.getInstance(getApplication()).getAppDatabase().chapterDao().getAllChapterId(mBooksItem.getBookID())).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<Integer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<Integer> integers) {

                        mutableLiveData.postValue(makeOperation(integers, chapterItems));
                    }

                    @Override
                    public void onError(Throwable e) {
                        mutableLiveData.postValue(null);

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        return mutableLiveData;
    }

    public boolean isSourseDownload(Intent intent) {
        int sourse = 0;
        if (intent.hasExtra(getApplication().getResources().getString(R.string.sourse_intent_key))) {
            sourse = intent.getIntExtra(getApplication().getResources().getString(R.string.sourse_intent_key), 0);
        }
        return sourse == MyDownloadFragment.DOWNLOAD_FRAGMENT_SOURSE_TAG;
    }
}
