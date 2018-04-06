package com.amit7itz.motivator.motivator;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amit7itz.motivator.motivator.db.Activity;
import com.amit7itz.motivator.motivator.db.ActivityType;
import com.amit7itz.motivator.motivator.db.AppDatabase;

public class MainActivity extends AppCompatActivity {
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "motivator").allowMainThreadQueries().build();
        this.updateTotalReward();
    }

    private void updateTotalReward() {
        TextView moneyText = findViewById(R.id.currentMoney);
        moneyText.setText(String.format("%s", this.db.activityDao().getTotalReward()));
    }

    public void addMoney(View view) {
        long type_id;
        if (this.db.activityTypeDao().count() == 0) {
            ActivityType some_type = new ActivityType();
            some_type.setName("Running");
            some_type.setDescription("bla bla bla");
            some_type.setReward(5);
            type_id = this.db.activityTypeDao().insert(some_type);
        }
        else {
            type_id = this.db.activityTypeDao().first().getId();
        }
        Activity act = new Activity();
        act.setActivityTypeId(type_id);
        act.setTimestamp(System.currentTimeMillis() / 1000);
        this.db.activityDao().insertAll(act);
        this.updateTotalReward();
    }
}
