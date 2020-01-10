package com.mahmoud.hadith.model.viewmodel.mydownload;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.mahmoud.hadith.model.entity.api.books.BooksItem;
import com.mahmoud.hadith.model.interfaces.DownloadCallBack;
import com.mahmoud.hadith.model.sharedpreference.UserData;
import com.mahmoud.hadith.model.utils.DeleteBookUtils;
import com.mahmoud.hadith.model.viewmodel.base.BaseViewModel;
import com.mahmoud.hadith.repository.room.RoomClient;

import java.util.List;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class MyDownloadViewModel extends BaseViewModel {
    private static final String TAG = "MyDownloadViewModel";
    private UserData mUserData;

    public MyDownloadViewModel(@NonNull Application application) {
        super(application);
        mUserData = UserData.getInstance(getApplication());
    }

    public LiveData<List<BooksItem>> getBooksLiveData(){
        return RoomClient.getInstance(getApplication()).getAppDatabase().booksDao().getAllBooks();
    }

    public void deleteBook(BooksItem booksItem, DownloadCallBack callBack) {
        mUserData.setEventChange(true);
        new DeleteBookUtils(getApplication(), callBack).deleteBook(booksItem);

    }


}
