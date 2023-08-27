package com.accenture.accpenture.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface AppDAO {
    @Insert
    void insert(AppData appData);

    @Query("SELECT COUNT(*) FROM AppData")
    int count();

    @Query("SELECT * FROM AppData LIMIT 1")
    AppData getFirstRow();

    @Query("DELETE FROM AppData")
    void deleteAll();
}
