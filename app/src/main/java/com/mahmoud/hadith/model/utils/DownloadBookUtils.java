package com.mahmoud.hadith.model.utils;

import android.content.Context;
import android.util.Log;

import com.mahmoud.hadith.model.entity.api.books.BooksItem;
import com.mahmoud.hadith.model.entity.api.chapter.ChapterItem;
import com.mahmoud.hadith.model.entity.api.chapter.ChapterResponse;
import com.mahmoud.hadith.model.entity.api.hadithwithout.HadithItem;
import com.mahmoud.hadith.model.entity.api.hadithwithout.HadithResponse;
import com.mahmoud.hadith.model.interfaces.DownloadCallBack;
import com.mahmoud.hadith.repository.retrofit.chapter.ApiChapter;
import com.mahmoud.hadith.repository.retrofit.hadith.ApiHadith;
import com.mahmoud.hadith.repository.room.RoomClient;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class DownloadBookUtils {
    private  final String TAG = "DownloadBookUtils";

    private Context mContext;
    private DownloadCallBack mCallBack;
    private int hadithNumberCount=0;
    private int targetHadith=0;

    public DownloadBookUtils(Context context,DownloadCallBack callBack){
        mCallBack=callBack;
        mContext=context;
    }

    public  void downloadBook(BooksItem booksItem, String language){

        storeBookOnDatabase(booksItem);

        downloadAllChaptersOfBook(booksItem.getBookID(),language);
    }

    public void storeBookOnDatabase(BooksItem booksItem) {
        Observable.fromCallable(new Callable<BooksItem>() {
            @Override
            public BooksItem call() {
                RoomClient.getInstance(mContext).getAppDatabase().booksDao().insert(booksItem);
                return booksItem;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BooksItem>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BooksItem booksItem) {
                        Log.i(TAG, "onNext: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG, "onError: " + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "addToFavorite:book done ");

                    }
                });
    }

    private  void downloadAllChaptersOfBook(int bookId, String language){

        ApiChapter.open(mContext).getChapter(bookId,language)
                .subscribe(new SingleObserver<Response<ChapterResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<ChapterResponse> chapterResponseResponse) {
                        if (chapterResponseResponse.body()!=null){
                            downloadAllChapter(bookId,language,chapterResponseResponse.body().getChapter());
                        }else {
                            mCallBack.onResopnse(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mCallBack.onResopnse(false);
                    }
                });

    }

    private  void downloadAllChapter( int bookId, String language, List<ChapterItem> chapter) {

        for (ChapterItem chapterItem:chapter ) {
            chapterItem.setBookId(bookId);
           downloadChapter(chapterItem,language);
        }

    }

    public void downloadChapter(ChapterItem chapterItem, String language) {
        storeChapterOnDatabase(chapterItem);
        downloadAllHadithOnChapter(chapterItem.getChapterID(),chapterItem.getBookId(),language);
    }
    private  void storeChapterOnDatabase(ChapterItem chapterItem) {
        Disposable disposable = Observable.fromCallable(() -> {
            chapterItem.setState(0);
            RoomClient.getInstance(mContext).getAppDatabase().chapterDao().insert(chapterItem);
            return chapterItem;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((result) -> {
                    Log.i(TAG, "addToFavorite: done ");
                });
    }

    private void downloadAllHadithOnChapter(int chapterID, int bookId, String language){
        ApiHadith.open(mContext).getAllHadith(bookId,chapterID,language)
                .subscribe(new SingleObserver<Response<HadithResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Response<HadithResponse> hadithResponseResponse) {
                        if (hadithResponseResponse.body()!=null){
                            targetHadith+=hadithResponseResponse.body().getHadithWithout().size();
                            downloadAllHadith(hadithResponseResponse.body().getHadithWithout(),bookId,chapterID);
                        }else {
                            mCallBack.onResopnse(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mCallBack.onResopnse(false);
                    }
                });
    }

    private void downloadAllHadith(List<HadithItem> hadithItems,int bookId,int chapterId){
        for (HadithItem hadithItem : hadithItems) {
            storeHadithOnDatabase(hadithItem,bookId,chapterId);
        }
    }
    private synchronized void storeHadithOnDatabase(HadithItem hadithItem,int bookId,int chapterId) {
        Disposable disposable = Observable.fromCallable(() -> {
            HadithItem hadithItem1=ConverterUtils.convertHadithToDatabase(hadithItem);
            Log.i(TAG, "storeHadithOnDatabase: "+hadithItem.toString());
            hadithItem1.setBookId(bookId);
            hadithItem1.setChapterID(chapterId);
            RoomClient.getInstance(mContext).getAppDatabase().hadithDao().insert(hadithItem1);
            return hadithItem;
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((result) -> {
                    Log.i(TAG, "addToFavorite: done "+hadithNumberCount+"  /  "+targetHadith);
                    hadithNumberCount++;
                    if (hadithNumberCount==targetHadith){
                        mCallBack.onResopnse(true);
                    }
                });
    }
}
