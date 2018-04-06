package com.amit7itz.motivator.motivator.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {ActivityType.class, Activity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ActivityTypeDao activityTypeDao();
    public abstract ActivityDao activityDao();
}
