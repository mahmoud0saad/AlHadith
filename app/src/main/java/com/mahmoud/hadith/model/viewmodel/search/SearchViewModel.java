package com.mahmoud.hadith.model.viewmodel.search;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mahmoud.hadith.R;
import com.mahmoud.hadith.model.entity.api.books.BooksItem;
import com.mahmoud.hadith.model.entity.api.books.BooksResponse;
import com.mahmoud.hadith.model.entity.api.hadithwithout.HadithItem;
import com.mahmoud.hadith.model.entity.api.hadithwithout.HadithResponse;
import com.mahmoud.hadith.model.utils.ConverterUtils;
import com.mahmoud.hadith.model.utils.sharedpreference.UserData;
import com.mahmoud.hadith.repository.retrofit.books.ApiBooks;
import com.mahmoud.hadith.repository.retrofit.search.ApiSearch;
import com.mahmoud.hadith.repository.room.RoomClient;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class SearchViewModel extends AndroidViewModel {
    private static final String TAG = "SearchViewModel";

    private MutableLiveData<String > numberReultLiveData=new MutableLiveData<>("fsx");
    private MutableLiveData<List<HadithItem>> searchResultLiveData=new MutableLiveData<>();
    private MutableLiveData<Boolean> resultAdded=new MutableLiveData<>();
    private MutableLiveData<List<BooksItem>> booksLiveData=new MutableLiveData<>();

    private UserData mUserData;

    public MutableLiveData<String> getNumberReultLiveData() {
        return numberReultLiveData;
    }

    public SearchViewModel(@NonNull Application application) {
        super(application);
        mUserData = UserData.getInstance(getApplication());
    }

    public MutableLiveData<List<HadithItem>> getHadithSearched(String searchKeyword, int bookId) {
        try {
            ApiSearch
                    .open(getApplication())
                    .searchForHadith(searchKeyword, mUserData.getLanguageForHadith(getApplication().getResources().getString(R.string.language_ar_no_tashkeel)))
                    .flatMapIterable(new Function<Response<HadithResponse>, Iterable<HadithItem>>() {
                        @Override
                        public Iterable<HadithItem> apply(Response<HadithResponse> hadithResponseResponse) {
                            if (hadithResponseResponse.body() == null)
                                throw new NullPointerException();
                            return hadithResponseResponse.body().getHadithWithout();
                        }
                    }).filter(new Predicate<HadithItem>() {
                        @Override
                        public boolean test(HadithItem hadithItem) throws Exception {
                            if (bookId==0)
                                return true;
                            return hadithItem.getBookIdNew()==bookId;
                        }
                    }).toList().subscribe(new SingleObserver<List<HadithItem>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(List<HadithItem> hadithItems) {
                            searchResultLiveData.postValue(hadithItems);
                            numberReultLiveData.postValue(hadithItems.size()+"");
                        }

                        @Override
                        public void onError(Throwable e) {
                            searchResultLiveData.postValue(null);


                        }
                    });

        }catch (Exception e){
            Log.i(TAG, "getBooksLiveData: "+e);
        }
        return searchResultLiveData;

    }

    public MutableLiveData<Boolean> addToFavorite(HadithItem hadithItem) {
        Log.i(TAG, "addToFavorite: hewllo befoe");
        try {
            Disposable disposable = Observable.fromCallable(() -> {
                RoomClient
                        .getInstance(getApplication())
                        .getAppDatabase()
                        .favoriteDao()
                        .insert(ConverterUtils.convertToFavorite(hadithItem));

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

    public MutableLiveData<List<BooksItem>> getBooksLiveData() {
        try {
            ApiBooks.open(getApplication()).getBooks(mUserData.getLanguagePublic(getApplication().getResources().getString(R.string.language_ar))).subscribe(new Observer<Response<BooksResponse>>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Response<BooksResponse> booksResponseResponse) {
                    Log.i(TAG, "onSuccess: " + booksResponseResponse.body());
                    if (booksResponseResponse.body() != null) {
                        booksLiveData.postValue(booksResponseResponse.body().getBooks());
                    } else {
                        booksLiveData.postValue(null);

                    }
                }

                @Override
                public void onError(Throwable e) {
                    booksLiveData.postValue(null);

                }

                @Override
                public void onComplete() {

                }
            });
        }catch (Exception e){
            Log.i(TAG, "getBooksLiveData: "+e);
        }
        return booksLiveData;

    }
}

