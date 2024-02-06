package com.mukuha.android.snacksmenu.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mukuha.android.snacksmenu.model.DataItem;

@Database(entities = {DataItem.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;
    public abstract DataItemDao dataItemDao();
    public static AppDatabase getInstance(Context context) {

        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "database.db")
                    .allowMainThreadQueries()
                    .build();
        }

        return instance;
    }
    public static void destroyInstance() {
        instance = null;
    }
}
