package com.amit7itz.motivator.motivator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amit7itz.motivator.motivator.db.Activity;
import com.amit7itz.motivator.motivator.db.ActivityType;
import com.amit7itz.motivator.motivator.db.AppDatabase;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.updateTotalReward();
        this.fillActivitiesTypesViewer();
    }

    private void updateTotalReward() {
        TextView moneyText = findViewById(R.id.currentMoney);
        moneyText.setText(String.format("%s", this.getDb().activityDao().getTotalReward()));
    }

    private void fillActivitiesTypesViewer() {
        this.mRecyclerView = findViewById(R.id.activity_types_viewer);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        this.mRecyclerView.setHasFixedSize(false);

        // use a linear layout manager
        this.mLayoutManager = new LinearLayoutManager(this);
        this.mRecyclerView.setLayoutManager(this.mLayoutManager);

        // specify an adapter (see also next example)
        String[] sa = new String[3];
        sa[0] = "Run";
        sa[1] = "Walk";
        sa[2] = "Swim";
        this.mAdapter = new ActivityTypesAdapter(sa);
        this.mRecyclerView.setAdapter(this.mAdapter);
    }

    private AppDatabase getDb() {
        return AppDatabase.getAppDatabase(this.getApplicationContext());
    }

    public void addMoney(View view) {
        long type_id;
        if (this.getDb().activityTypeDao().count() == 0) {
            ActivityType some_type = new ActivityType();
            some_type.setName("Running");
            some_type.setDescription("bla bla bla");
            some_type.setReward(5);
            type_id = this.getDb().activityTypeDao().insert(some_type);
        }
        else {
            type_id = this.getDb().activityTypeDao().first().getId();
        }
        Activity act = new Activity();
        act.setActivityTypeId(type_id);
        act.setTimestamp(System.currentTimeMillis() / 1000);
        this.getDb().activityDao().insertAll(act);
        this.updateTotalReward();
    }

    public void addActivityTypeClick(View v) {
        Intent intent = new Intent(this, AddActivityTypeActivity.class);
        startActivity(intent);
    }
}
