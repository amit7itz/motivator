package com.amit7itz.motivator.motivator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.amit7itz.motivator.motivator.db.ActivityType;
import com.amit7itz.motivator.motivator.db.ActivityTypeDao;
import com.amit7itz.motivator.motivator.db.AppDatabase;

public class AddActivityTypeActivity extends AppCompatActivity {
    private final static int RewardLimit = 500;
    EditText name;
    EditText description;
    EditText reward;
    CheckBox major;
    Button add;
    long activity_type_id;

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
        major = findViewById(R.id.major);
        add = findViewById(R.id.Add);

        activity_type_id = getIntent().getLongExtra("activity_type_id", 0);
        if (activity_type_id != 0) {
            ActivityTypeDao activity_types_dao = AppDatabase.getAppDatabase(this.getApplicationContext()).activityTypeDao();
            ActivityType act_type = activity_types_dao.getById(activity_type_id);
            name.setText(act_type.getName());
            description.setText(act_type.getDescription());
            reward.setText(String.valueOf(act_type.getReward()));
            major.setChecked(act_type.getMajor());
            add.setText(R.string.modify);
        }
    }

    public void add_activity(View v){
        int reward_num = Integer.decode(reward.getText().toString());
        if (reward_num > RewardLimit) {
            Messages.showMessage(this, String.format("Reward must be up to %s", RewardLimit));
            return;
        }
        ActivityTypeDao activity_types_dao = AppDatabase.getAppDatabase(this.getApplicationContext()).activityTypeDao();
        ActivityType new_type;
        if (activity_type_id == 0) {
            new_type = new ActivityType();
        }
        else {
            new_type = activity_types_dao.getById(activity_type_id);
        }
        new_type.setName(name.getText().toString());
        new_type.setDescription(description.getText().toString());
        new_type.setReward(reward_num);
        new_type.setMajor(major.isChecked());
        if (activity_type_id == 0) {
            activity_types_dao.insert(new_type);
        }
        else {
            activity_types_dao.update(new_type);
        }
        finish();
    }
}
