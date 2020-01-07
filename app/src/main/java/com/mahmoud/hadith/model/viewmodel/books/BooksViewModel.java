package com.mahmoud.hadith.model.viewmodel.books;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.model.entity.api.books.BooksItem;
import com.mahmoud.hadith.model.entity.api.books.BooksResponse;
import com.mahmoud.hadith.model.interfaces.DownloadCallBack;
import com.mahmoud.hadith.model.utils.DownloadBookUtils;
import com.mahmoud.hadith.model.utils.sharedpreference.UserData;
import com.mahmoud.hadith.repository.retrofit.books.ApiBooks;
import com.mahmoud.hadith.repository.room.RoomClient;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class BooksViewModel extends AndroidViewModel {
    private static final String TAG = "BooksViewModel";
    MutableLiveData<List<BooksItem>> booksLiveData=new MutableLiveData<>();
    private UserData mUserData;
    public BooksViewModel(@NonNull Application application) {
        super(application);
        mUserData = UserData.getInstance(getApplication());
    }

    public MutableLiveData<List<BooksItem>> getBooksLiveData() {
            try {
                ApiBooks.open(getApplication())
                        .getBooks(mUserData.getLanguagePublic(getApplication().getResources().getString(R.string.language_ar)))
                        .flatMapIterable((Function<Response<BooksResponse>, Iterable<BooksItem>>)
                                booksResponseResponse -> booksResponseResponse.body()!=null?booksResponseResponse.body().getBooks():null)
                        .toList()
                        .subscribe(new SingleObserver<List<BooksItem>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(List<BooksItem> booksItems) {
                                if (booksItems!= null) {
                                    markForMyDatabaseSame(booksItems);
                                } else {
                                    booksLiveData.postValue(null);
                                }

                            }

                            @Override
                            public void onError(Throwable e) {
                                booksLiveData.postValue(null);
                            }
                        });


            }catch (Exception e){
                Log.i(TAG, "getBooksLiveData: "+e);
            }
            return booksLiveData;

    }

    private void markForMyDatabaseSame(List<BooksItem> booksItems) {
        Observable.fromCallable(() -> {
            List<Integer> integers = RoomClient.getInstance(getApplication()).getAppDatabase().booksDao().getAllBookId();
            return makeOperation(integers, booksItems);
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<BooksItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {


                    }

                    @Override
                    public void onNext(List<BooksItem> booksItems) {
                        booksLiveData.postValue(booksItems);
                    }

                    @Override
                    public void onError(Throwable e) {
                        booksLiveData.postValue(null);

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void downloadBook(DownloadCallBack callBack, BooksItem booksItem) {
        try {

            new DownloadBookUtils(getApplication(), callBack).downloadBook(booksItem, mUserData.getLanguagePublic(getApplication().getResources().getString(R.string.language_ar)));

        }catch (Exception e){
            callBack.onResopnse(false);

            Log.i(TAG, "getBooksLiveData: "+e);
        }
    }




    public List<BooksItem> makeOperation(List<Integer> integers, List<BooksItem> booksItems) {
        List<BooksItem> resultBooksItems=new ArrayList<>();
        for (BooksItem booksItem:booksItems){
            if (integers.contains(booksItem.getBookID())){
                booksItem.setState(2);
            }
            resultBooksItems.add(booksItem);
        }
        return resultBooksItems;
    }
}
