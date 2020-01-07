package com.mahmoud.hadith.model.viewmodel.mydownload;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mahmoud.hadith.model.entity.api.books.BooksItem;
import com.mahmoud.hadith.model.interfaces.DownloadCallBack;
import com.mahmoud.hadith.model.utils.DeleteBookUtils;
import com.mahmoud.hadith.repository.room.RoomClient;

import java.util.List;

public class MyDownloadViewModel extends AndroidViewModel {
    private static final String TAG = "MyDownloadViewModel";


    public MyDownloadViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<BooksItem>> getBooksLiveData(){
        return RoomClient.getInstance(getApplication()).getAppDatabase().booksDao().getAllBooks();
    }

    public void deleteBook(BooksItem booksItem, DownloadCallBack callBack) {
        new DeleteBookUtils(getApplication(), callBack).deleteBook(booksItem);
    }
}
