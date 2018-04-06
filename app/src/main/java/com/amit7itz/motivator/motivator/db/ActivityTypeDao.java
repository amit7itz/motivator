package com.amit7itz.motivator.motivator.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ActivityTypeDao {
    @Query("SELECT * FROM activity_type")
    List<ActivityType> getAll();

    @Insert
    void insertAll(ActivityType... activite_types);

    @Delete
    void delete(ActivityType activity);
}
