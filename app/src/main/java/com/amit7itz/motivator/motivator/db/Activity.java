package com.amit7itz.motivator.motivator.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;
import java.util.Locale;

@Entity(tableName = "activity")
public class Activity {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "activity_type_id")
    private long activityTypeId;

    @ColumnInfo(name = "value")
    private long value;

    @ColumnInfo(name = "bonus")
    private long bonus = 0;

    @ColumnInfo(name = "total_value")
    private long totalValue;

    @ColumnInfo(name = "timestamp")
    private long timestamp;

    @ColumnInfo(name = "streak_value")
    private int streakValue = 1;

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

    public Calendar getCalendar() {
        Calendar c =  Calendar.getInstance(Locale.getDefault());
        c.setTimeInMillis(timestamp*1000);
        return c;
    }

    public Calendar getDateWithoutTime() {
        Calendar cal = getCalendar();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getBonus() {
        return bonus;
    }

    public void setBonus(long bonus) {
        this.bonus = bonus;
    }

    public long getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(long totalValue) {
        this.totalValue = totalValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStreakValue() {
        return streakValue;
    }

    public void setStreakValue(int streakValue) {
        this.streakValue = streakValue;
    }
}
