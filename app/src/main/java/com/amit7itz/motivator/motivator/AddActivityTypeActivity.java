package com.amit7itz.motivator.motivator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.amit7itz.motivator.motivator.db.ActivityType;
import com.amit7itz.motivator.motivator.db.ActivityTypeDao;
import com.amit7itz.motivator.motivator.db.AppDatabase;

public class AddActivityTypeActivity extends AppCompatActivity {
    private final static int RewardLimit = 500;
    EditText name;
    EditText description;
    EditText reward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_type);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        name = findViewById(R.id.activtyName);
        description = findViewById(R.id.activityDescription);
        reward = findViewById(R.id.activityReward);
    }

    public void add_activity(View v){
        int reward_num = Integer.decode(reward.getText().toString());
        if (reward_num > RewardLimit) {
            Messages.showMessage(this, String.format("Reward must be up to %s", RewardLimit));
            return;
        }
        ActivityType new_type = new ActivityType();
        new_type.setName(name.getText().toString());
        new_type.setDescription(description.getText().toString());
        new_type.setReward(reward_num);
        ActivityTypeDao activity_types_dao = AppDatabase.getAppDatabase(this.getApplicationContext()).activityTypeDao();
        activity_types_dao.insert(new_type);
        finish();
    }
}
