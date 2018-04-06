package com.amit7itz.motivator.motivator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.amit7itz.motivator.MESSAGE";
    private FileManager file_manager;
    private FileManager money_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.file_manager = new FileManager("file.txt", this.getApplicationContext());
        this.money_manager = new FileManager("money.txt", this.getApplicationContext());
        String saved_message = this.file_manager.read();
        String saved_money = this.money_manager.read();
        if (saved_message != null) {
            EditText editText = findViewById(R.id.editText);
            editText.setText(saved_message);
        }
        if (saved_money != null) {
            TextView moneyText = findViewById(R.id.currentMoney);
            moneyText.setText(saved_money);
        }
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = findViewById(R.id.editText);
        String message = editText.getText().toString();
        this.file_manager.write(message);
        intent.putExtra(EXTRA_MESSAGE, message);

        startActivity(intent);
    }

    public void clearMessage(View view) {
        EditText editText = findViewById(R.id.editText);
        editText.setText("");
    }

    public void addMoney(View view) {
        TextView money_label = (TextView) findViewById(R.id.currentMoney);
        int money = Integer.parseInt(money_label.getText().toString());
        money += 5;
        money_label.setText(String.format("%s", money));
        this.money_manager.write(String.format("%s", money));
    }
}
