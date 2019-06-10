package com.github.farzadfarazmand.remindme.core;

import android.app.Application;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.github.farzadfarazmand.remindme.dao.TaskDao;
import com.github.farzadfarazmand.remindme.model.Task;

@Database(entities = {Task.class}, version = 1)
public abstract class DatabaseHelper extends RoomDatabase {

    private static final String DATABASE_NAME = "remindMe_database";
    private static DatabaseHelper instance;

    public abstract TaskDao taskDao();

    public synchronized static DatabaseHelper getInstance(Application application) {
        if (instance == null) {
            instance = Room.databaseBuilder(application,
                    DatabaseHelper.class, DATABASE_NAME)
                    .build();
        }

        return instance;
    }
}
