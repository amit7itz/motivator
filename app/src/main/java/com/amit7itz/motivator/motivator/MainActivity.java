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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amit7itz.motivator.motivator.db.Activity;
import com.amit7itz.motivator.motivator.db.ActivityType;
import com.amit7itz.motivator.motivator.db.AppDatabase;

import java.text.DecimalFormat;

import static com.amit7itz.motivator.motivator.TimeUtils.getTimestampSeconds;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private final static long MaxActivityIntervalMinutes = 1;
    private DrawerLayout mDrawerLayout;
    private final static int StreakInvervalDays = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        Class target = null;
                        switch (menuItem.getItemId()){
                            case R.id.nav_manual:
                                target = ManualChanges.class;
                                break;
                            case R.id.nav_about:
                                target = AboutActivity.class;
                                break;
                        }
                        if (target != null) {
                            Intent intent = new Intent(getApplicationContext(), target);
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
        this.updateTotalReward();
        this.updateStreakView();
    }

    private static String psikify(String s) {
        long amount = Long.parseLong(s);
        DecimalFormat formatter = new DecimalFormat("#,###");

        return formatter.format(amount);
    }

    private void updateTotalReward(boolean animation) {
        long new_reward = this.getDb().activityDao().getTotalReward();
        final TextView moneyText = findViewById(R.id.currentMoney);
        if (animation) {
            long old_reward = Long.decode(moneyText.getText().toString().replaceAll(",", ""));
            ValueAnimator animator = ValueAnimator.ofInt((int) old_reward, (int) new_reward);
            animator.setDuration(2000);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    moneyText.setText(psikify(animation.getAnimatedValue().toString()));
                }
            });
            animator.start();
        }
        else {
            moneyText.setText(psikify(String.format("%s", new_reward)));
        }
    }

    private void updateTotalReward() {
        updateTotalReward(false);
    }

    private void updateStreakView() {
        final TextView streakViewer = findViewById(R.id.streakViewer);
        streakViewer.setText(String.format("%s", this.getStreak()));
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

    private int getStreak() {
        long now = getTimestampSeconds();
        int streak = 0;
        Activity last_act = null;
        for (Activity act: this.getDb().activityDao().getAllStreakReversed()) {
            if (last_act == null) {
                if ((now - act.getTimestamp()) <= StreakInvervalDays*60*60*24) {
                    streak += act.getStreakValue();
                }
                else {
                    return 0;
                }
            }
            else {
                if ((act.getTimestamp() - last_act.getTimestamp()) <= StreakInvervalDays*60*60*24) {
                    streak += act.getStreakValue();
                }
                else {
                    return streak;
                }
            }
            last_act = act;
        }
        return streak;
    }

    public void addActivity(View view) {
        long now = getTimestampSeconds();
        long last_activity_time = getDb().activityDao().getLastActivityTimestamp();
        if (now - last_activity_time < MaxActivityIntervalMinutes*60) {
            Messages.showMessage(this, "Oops!",
                    String.format("Your last activity was less than %s minutes ago.\nYou can add new activity in %s seconds",
                    MaxActivityIntervalMinutes,
                    MaxActivityIntervalMinutes*60 - now + last_activity_time));
        }
        else {
            LinearLayout t = (LinearLayout) view;
            long type_id = (long) t.getTag();
            ActivityType activity_type = this.getDb().activityTypeDao().getById(type_id);
            Activity act = new Activity();
            act.setActivityTypeId(type_id);
            act.setTimestamp(now);
            act.setDescription(activity_type.getName());
            act.setValue(activity_type.getReward());
            int streak = this.getStreak();
            if (streak > 0) {
                streak += 1 ; // for this activity that we haven't added yet
                long last_major_activity_time = getDb().activityDao().getLastMajorActivityTimestamp();
                long close_activities_bonus_percent = 0;
                long close_activities_max_interval = 0;
                long time_since_last_activity = (last_major_activity_time - now);
                if (activity_type.getMajor() && time_since_last_activity <= 60*60*24) {
                    close_activities_bonus_percent = 10;
                    close_activities_max_interval = 24;
                }
                else if (activity_type.getMajor() && time_since_last_activity <= 2*60*60*24) {
                    close_activities_bonus_percent = 5;
                    close_activities_max_interval = 48;
                }
                long streak_bonus = Math.min(25, streak);
                long total_bonus_percent = close_activities_bonus_percent + streak_bonus;
                String bonus_message = String.format("Well done! You get %s%% bonus", total_bonus_percent);
                bonus_message += String.format("\n%s%% for maintaining %s activities streak", Math.min(25, streak), streak);
                if (close_activities_bonus_percent > 0) {
                    bonus_message += String.format("\n%s%% for performing 2 major activities in less than %s hours",
                            close_activities_bonus_percent,
                            close_activities_max_interval);
                }
                bonus_message += "\nKeep up the great work!";
                long bonus = (long) Math.floor(total_bonus_percent / 100.0 * act.getValue());
                if (bonus > 0) {
                    Messages.showMessage(this, "Streak Bonus!!", bonus_message);
                }
                act.setBonus(bonus);
            }
            act.setTotalValue(act.getValue() + act.getBonus());
            this.getDb().activityDao().insert(act);
            TextView act_name_textview = t.findViewById(R.id.activity_type_name);
            TextView act_reward_textview = t.findViewById(R.id.activity_type_reward);
            act_name_textview.setTextColor(Color.parseColor("#4CAF50"));
            act_reward_textview.setTextColor(Color.parseColor("#4CAF50"));
            this.updateTotalReward(true);
            this.updateStreakView();
        }
    }

    public void addActivityTypeClick(View v) {
        Intent intent = new Intent(this, AddActivityTypeActivity.class);
        startActivity(intent);
    }

}
