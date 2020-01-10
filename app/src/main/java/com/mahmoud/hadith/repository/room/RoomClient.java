package com.mahmoud.hadith.repository.room;

import android.content.Context;

import androidx.room.Room;

/**
 * Created by MAHMOUD SAAD MOHAMED , mahmoud1saad2@gmail.com on 10/1/2020.
 * Copyright (c) 2020 , MAHMOUD All rights reserved
 */


public class RoomClient {
    private static AppDatabase appDatabase;
    private static RoomClient instance;

    private RoomClient(Context context){
        appDatabase =Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }

    public static synchronized RoomClient getInstance(Context context) {

        if (instance==null){
            instance=new RoomClient(context);
        }

        return instance;
    }

    public AppDatabase getAppDatabase(){return appDatabase;}


}
