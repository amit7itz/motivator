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

    @Query("SELECT sum(reward) FROM activity JOIN activity_type on activity_type_id=activity_type.id")
    int getTotalReward();

    @Insert
    void insertAll(Activity... activity);

    @Delete
    void delete(Activity activity);
}
