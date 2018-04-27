package com.amit7itz.motivator.motivator;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.amit7itz.motivator.motivator.db.Activity;
import com.amit7itz.motivator.motivator.db.AppDatabase;
import static com.amit7itz.motivator.motivator.TimeUtils.getTimestampSeconds;

public class ManualChanges extends AppCompatActivity {
    EditText description;
    EditText balance;
    EditText streak;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_changes);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.description = findViewById(R.id.activityDescription);
        this.balance = findViewById(R.id.balance);
        this.streak = findViewById(R.id.streak);
    }

    private AppDatabase getDb() {
        return AppDatabase.getAppDatabase(this.getApplicationContext());
    }

    public void updateBalance(View v){
        Activity act = new Activity();
        act.setDescription(this.description.getText().toString());
        long total_value = Long.decode(this.balance.getText().toString());
        act.setValue(total_value);
        act.setTotalValue(total_value);
        act.setStreakValue(Integer.decode(this.streak.getText().toString()));
        act.setTimestamp(getTimestampSeconds());
        this.getDb().activityDao().insert(act);
        finish();
    }
}
