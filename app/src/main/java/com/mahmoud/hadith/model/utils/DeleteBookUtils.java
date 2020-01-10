package com.mahmoud.hadith.model.utils;

import android.content.Context;
import android.util.Log;

import com.mahmoud.hadith.model.entity.api.books.BooksItem;
import com.mahmoud.hadith.model.entity.api.chapter.ChapterItem;
import com.mahmoud.hadith.model.entity.api.hadithwithout.HadithItem;
import com.mahmoud.hadith.model.interfaces.DownloadCallBack;
import com.mahmoud.hadith.repository.room.AppDatabase;
import com.mahmoud.hadith.repository.room.RoomClient;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class DeleteBookUtils {
    private static final String TAG = "DeleteBookUtils";
    private Context mContext;
    private AppDatabase mAppDatabase;
    private int countDeleteTest;
    private DownloadCallBack mCallBack;

    public DeleteBookUtils(Context context, DownloadCallBack callBack) {
        mCallBack = callBack;
        mContext = context;
        mAppDatabase = RoomClient.getInstance(mContext).getAppDatabase();
    }

    public void deleteBook(BooksItem booksItem) {
        Observable.fromCallable(() -> {
            mAppDatabase.booksDao().delete(booksItem);
            Log.i(TAG, "deleteBook: the book is is ==>  " + booksItem.getBookID());
            return mAppDatabase.chapterDao().getAllChapterForDelete(booksItem.getBookID());

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ChapterItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<ChapterItem> listChapter) {
                        deleteAllChapter(listChapter);
                    }

                    @Override
                    public void onError(Throwable e) {

                        mCallBack.onResopnse(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void deleteAllChapter(List<ChapterItem> listChapter) {
        for (ChapterItem chapterItem : listChapter) {
            deleteChapter(chapterItem);
        }
    }

    public void deleteChapter(ChapterItem chapterItem) {
        Observable.fromCallable(() -> {
            mAppDatabase.chapterDao().delete(chapterItem);
            Log.i(TAG, "deleteBook: the book is is ==>  " + chapterItem.getChapterID());
            return mAppDatabase.hadithDao().getAllHadithForDelete(chapterItem.getBookId(), chapterItem.getChapterID());

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<HadithItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<HadithItem> hadithItemList) {
                        deleteAllHadith(hadithItemList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mCallBack.onResopnse(false);

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void deleteAllHadith(List<HadithItem> hadithItemList) {
        for (HadithItem hadithItem : hadithItemList) {
            deleteHadith(hadithItem);
        }
    }

    private void deleteHadith(HadithItem hadithItem) {
        Observable.fromCallable((Callable<Object>) () -> {
            mAppDatabase.hadithDao().delete(hadithItem);
            return hadithItem;

        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Object listChapter) {
                        Log.i(TAG, "onNext: deleted test " + countDeleteTest++);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mCallBack.onResopnse(false);

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


}
