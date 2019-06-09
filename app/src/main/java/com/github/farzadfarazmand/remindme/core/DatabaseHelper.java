package com.github.farzadfarazmand.remindme.core;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.github.farzadfarazmand.remindme.model.Task;

@Database(entities = {Task.class}, version = 1)
public abstract class DatabaseHelper extends RoomDatabase {

    private static final String DATABASE_NAME = "remindMe_database";
    private static DatabaseHelper instance;

    private DatabaseHelper() {
    }

    public synchronized static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    DatabaseHelper.class, DATABASE_NAME)
                    .build();
        }

        return instance;
    }
}
