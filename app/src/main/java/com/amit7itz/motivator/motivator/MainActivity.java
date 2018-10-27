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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amit7itz.motivator.motivator.db.Activity;
import com.amit7itz.motivator.motivator.db.ActivityType;
import com.amit7itz.motivator.motivator.db.AppDatabase;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

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

    private Calendar today() {
        Calendar c = Calendar.getInstance(Locale.getDefault());
        c.setTimeInMillis(getTimestampSeconds()*1000);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return c;
    }

    private int getStreak() {
        int streak = 0;
        Activity last_act = null;
        for (Activity act: this.getDb().activityDao().getAllStreakReversed()) {
            Calendar last_act_date;
            if (last_act == null) {
                last_act_date = today();
            }
            else {
                last_act_date = last_act.getDateWithoutTime();
            }
            if (TimeUnit.MILLISECONDS.toDays(last_act_date.getTimeInMillis() - act.getDateWithoutTime().getTimeInMillis()) <= StreakInvervalDays) {
                streak += act.getStreakValue();
            }
            else {
                return streak;
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
                Activity last_major_activity = getDb().activityDao().getLastMajorActivity();
                long close_activities_bonus_percent = 0;
                long close_activities_max_interval = 0;
                long days_since_last_activity = TimeUnit.MILLISECONDS.toDays(today().getTimeInMillis() - last_major_activity.getDateWithoutTime().getTimeInMillis());
                if (activity_type.getMajor() && days_since_last_activity <= 1) {
                    close_activities_bonus_percent = 10;
                    close_activities_max_interval = 1;
                }
                else if (activity_type.getMajor() && days_since_last_activity <= 2) {
                    close_activities_bonus_percent = 5;
                    close_activities_max_interval = 2;
                }
                long streak_bonus = Math.min(25, streak);
                long total_bonus_percent = close_activities_bonus_percent + streak_bonus;

                long bonus = Math.round(total_bonus_percent / 100.0 * act.getValue());

                act.setBonus(bonus);

                String bonus_message = String.format("Well done! You get %s points bonus", bonus);
                bonus_message += String.format("\n%s%% for maintaining %s activities streak", Math.min(25, streak), streak);
                if (close_activities_bonus_percent > 0) {
                    bonus_message += String.format("\n%s%% for performing 2 major activities in less than %s days",
                            close_activities_bonus_percent,
                            close_activities_max_interval);
                }
                bonus_message += "\nKeep up the great work!";
                if (bonus > 0) {
                    Messages.showMessage(this, "Streak Bonus!!", bonus_message);
                }
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

    public void showLastMajorActivityTime(View v) {
        Activity last_major_act = getDb().activityDao().getLastMajorActivity();
        String message = "Last major activity:\n";
        message += last_major_act.getTimestampStr() + "\n\n";
        long minute = 60;
        long hour = minute * 60;
        long day = hour * 24;
        long next_activity_time = last_major_act.getDateWithoutTime().getTimeInMillis() / 1000 + (StreakInvervalDays + 1) * day;
        long time_left = next_activity_time - getTimestampSeconds();
        if (time_left > 0) {
            message += "Time left for next activity:\n";
            long days = time_left / day;
            time_left %= day;
            long hours = time_left / hour;
            time_left %= hour;
            long minutes = time_left / minute;
            time_left %= minute;
            long seconds = time_left;
            message += String.format("%sd %sh %sm %ss", days, hours, minutes, seconds);
        }
        Messages.showMessage(this, "Streak Status", message);
    }

}
