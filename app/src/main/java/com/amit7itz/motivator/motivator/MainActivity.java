package com.amit7itz.motivator.motivator;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.amit7itz.motivator.motivator.db.Activity;
import com.amit7itz.motivator.motivator.db.AppDatabase;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private final static long MaxActivityIntervalMinutes = 5;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.updateTotalReward();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);

        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        switch (menuItem.getItemId()){
                            case R.id.nav_about:
                                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                                startActivity(intent);
                        }

                        return true;
                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.fillActivitiesTypesViewer();
    }

    private static String psikify(String s) {
        long amount = Long.parseLong(s);
        DecimalFormat formatter = new DecimalFormat("#,###");

        return formatter.format(amount);
    }

    private void updateTotalReward() {
        final TextView moneyText = findViewById(R.id.currentMoney);
        long old_reward = Long.decode(moneyText.getText().toString().replaceAll(",", ""));
        long new_reward = this.getDb().activityDao().getTotalReward();
        ValueAnimator animator = ValueAnimator.ofInt((int) old_reward, (int) new_reward);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                moneyText.setText(psikify(animation.getAnimatedValue().toString()));
            }
        });
        animator.start();
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

    public static long getTimestampSeconds(){
        return System.currentTimeMillis() / 1000;
    }

    public void addActivity(View view) {
        long now = getTimestampSeconds();
        long last_activity_time = getDb().activityDao().getLastActivityTimestamp();
        if (now - last_activity_time < MaxActivityIntervalMinutes*60) {
            Messages.showMessage(this, String.format("Oops! Your last activity was less than %s minutes ago.\nYou can add new activity in %s seconds",
                    MaxActivityIntervalMinutes,
                    MaxActivityIntervalMinutes*60 - now + last_activity_time));
        }
        else {
            TextView t = (TextView) view;
            long type_id = (long) t.getTag();
            Activity act = new Activity();
            act.setActivityTypeId(type_id);
            act.setTimestamp(now);
            this.getDb().activityDao().insert(act);
            t.setTextColor(Color.parseColor("#4CAF50"));
            this.updateTotalReward();
        }
    }

    public void addActivityTypeClick(View v) {
        Intent intent = new Intent(this, AddActivityTypeActivity.class);
        startActivity(intent);
    }

}
