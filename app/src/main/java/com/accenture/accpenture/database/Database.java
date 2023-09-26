package com.accenture.accpenture.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {AppData.class, ExpenseData.class}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {
    private static final String DB_NAME = "Database";
    private static Database instance;

    public static synchronized Database getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, Database.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }

    public abstract AppDAO appDao();
    public abstract ExpenseDAO expenseDao();
}
