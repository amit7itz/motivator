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

    @Query("SELECT * FROM activity_type WHERE id = :id")
    ActivityType getById(long id);

    @Query("SELECT * FROM activity_type LIMIT 1")
    ActivityType first();

    @Query("SELECT count(*) FROM activity_type")
    long count();

    @Insert
    void insertAll(ActivityType... activity_types);

    @Insert
    long insert(ActivityType activity_type);

    @Delete
    void delete(ActivityType activity);
}
