package com.mahmoud.hadith.model.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.mahmoud.hadith.model.entity.api.favorite.FavoriteItem;
import com.mahmoud.hadith.model.entity.api.hadithwithout.HadithItem;

import java.util.Locale;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class Utils {

    public static void hideSoftKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager)editText.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE
        );
        if (imm!=null)
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static boolean isConnected(Context context) {
        if (context == null) return true;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            return (mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting());
        } else return false;
    }

    public static HadithItem convertFromFavoriteItem(FavoriteItem favoriteItem) {
        HadithItem hadithItem = new HadithItem();
        hadithItem.setChapterID(favoriteItem.getChapterID());
        hadithItem.setBookId(favoriteItem.getBookId());
        hadithItem.setArSanad1(favoriteItem.getSanad());
        hadithItem.setArText(favoriteItem.getText());
        return hadithItem;
    }

    public static Intent getShareIntent(String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        return intent;
    }

    public static void switchLocal(Context context, String lcode, Activity activity) {
        if (lcode.equalsIgnoreCase(""))
            return;
        Resources resources = context.getResources();
        Locale locale = new Locale(lcode);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new
                android.content.res.Configuration();
        config.locale = locale;
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        //restart base activity
        activity.finish();
        activity.startActivity(activity.getIntent());
    }

}
