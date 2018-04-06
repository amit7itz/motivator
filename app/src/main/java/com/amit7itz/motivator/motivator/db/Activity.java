package com.amit7itz.motivator.motivator.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "activity")
public class Activity {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "activity_type_id")
    private long activityTypeId;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getActivityTypeId() {
        return activityTypeId;
    }

    public void setActivityTypeId(long activityTypeId) {
        this.activityTypeId = activityTypeId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
