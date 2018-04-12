package com.amit7itz.motivator.motivator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    }

    @Override
    protected void onResume() {
        super.onResume();
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

        this.mAdapter = new ActivityTypesAdapter(this.getDb());
        this.mRecyclerView.setAdapter(this.mAdapter);
    }

    private AppDatabase getDb() {
        return AppDatabase.getAppDatabase(this.getApplicationContext());
    }

    public void addMoney(View view) {
        TextView t = (TextView) view;
        long type_id = (long) t.getTag();
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
