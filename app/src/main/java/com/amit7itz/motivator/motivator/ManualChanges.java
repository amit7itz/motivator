package com.amit7itz.motivator.motivator;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amit7itz.motivator.motivator.db.Activity;
import com.amit7itz.motivator.motivator.db.AppDatabase;

import static com.amit7itz.motivator.motivator.Messages.showYesNoMessage;
import static com.amit7itz.motivator.motivator.TimeUtils.getTimestampSeconds;

public class ManualChanges extends AppCompatActivity {
    EditText description;
    EditText balance;
    EditText streak;
    TextView lastActivityContent;
    Button deleteLastActivityButton;


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
        this.lastActivityContent = findViewById(R.id.last_activity);
        this.deleteLastActivityButton = findViewById(R.id.delete_last_activity_button);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateLastActivity();
    }

    private AppDatabase getDb() {
        return AppDatabase.getAppDatabase(this.getApplicationContext());
    }

    public void updateLastActivity() {
        Activity act = getDb().activityDao().getLast();
        if (act == null) {
            this.lastActivityContent.setText(getString(R.string.last_activity_content));
            this.deleteLastActivityButton.setEnabled(false);
        }
        else {
            String last_act_details_template = "Description: %s\n" +
                    "Value: %s\n" +
                    "Bonus: %s\n" +
                    "Total value: %s\n" +
                    "Streak value: %s\n";
            String last_act_details = String.format(last_act_details_template,
                    act.getDescription(),
                    act.getValue(),
                    act.getBonus(),
                    act.getTotalValue(),
                    act.getStreakValue()
                    );
            this.lastActivityContent.setText(last_act_details);
            this.deleteLastActivityButton.setEnabled(true);
        }
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
        super.onBackPressed();
    }

    public void deleteLastActivity(View v){
        final Activity act = getDb().activityDao().getLast();
        if (act != null) {
            showYesNoMessage(this,
                    "Delete Last Activity",
                    String.format("You are going to delete \"%s\",\n" +
                            "This action cannot be undone\n" +
                            "Are you sure?", act.getDescription()),
                    new Runnable() {
                        @Override
                        public void run() {
                            getDb().activityDao().delete(act);
                            updateLastActivity();
                        }
                    },
            null);
        }
    }
}
