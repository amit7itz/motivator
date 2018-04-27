package com.amit7itz.motivator.motivator.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ActivityDao {
    @Query("SELECT * FROM activity")
    List<Activity> getAll();

    @Query("SELECT * FROM activity LEFT JOIN activity_type on activity_type_id = activity_type.id WHERE not activity_type_id or major ORDER BY timestamp DESC")
    List<Activity> getAllStreakReversed();

    @Query("SELECT sum(total_value) FROM activity")
    int getTotalReward();

    @Query("SELECT max(timestamp) FROM activity")
    long getLastActivityTimestamp();

    @Query("SELECT max(timestamp) FROM activity JOIN activity_type on activity_type_id = activity_type.id WHERE major")
    long getLastMajorActivityTimestamp();

    @Insert
    void insertAll(Activity... activity);

    @Insert
    long insert(Activity activity);

    @Delete
    void delete(Activity activity);
}
