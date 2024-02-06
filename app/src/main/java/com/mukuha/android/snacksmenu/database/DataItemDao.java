package com.mukuha.android.snacksmenu.database;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.mukuha.android.snacksmenu.model.DataItem;

import java.util.List;

@Dao
public interface DataItemDao {

    @Insert
    void insertAll(List<DataItem> items);

    @Insert
    void insertAll(DataItem... items);

    @Query("SELECT COUNT(*) FROM DataItem")
    int countItems();

    @Query("SELECT * FROM DataItem ORDER BY itemName")
    List<DataItem> retrieveAll();

    @Query("SELECT * FROM DataItem WHERE category = :category ORDER BY itemName")
    List<DataItem> retrieveAll(String category);

    @Query("SELECT * FROM DataItem WHERE itemId = :itemId")
    DataItem retrieveByID(String itemId);

    @Query("SELECT category FROM DataItem ORDER BY category")
    String[] retrieveCategories();
}
