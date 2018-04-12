package com.amit7itz.motivator.motivator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.amit7itz.motivator.motivator.db.ActivityType;
import com.amit7itz.motivator.motivator.db.ActivityTypeDao;
import com.amit7itz.motivator.motivator.db.AppDatabase;

public class AddActivityTypeActivity extends AppCompatActivity {
    EditText name;
    EditText description;
    EditText reward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_type);

        name = findViewById(R.id.activtyName);
        description = findViewById(R.id.activityDescription);
        reward = findViewById(R.id.activityReward);
    }

    public void add_activity(View v){
        ActivityType new_type = new ActivityType();
        new_type.setName(name.getText().toString());
        new_type.setDescription(description.getText().toString());
        new_type.setReward(Integer.decode(reward.getText().toString()));
        ActivityTypeDao activity_types_dao = AppDatabase.getAppDatabase(this.getApplicationContext()).activityTypeDao();
        activity_types_dao.insert(new_type);
        finish();
    }
}