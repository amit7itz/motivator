package com.amit7itz.motivator.motivator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.amit7itz.motivator.MESSAGE";
    private FileManager money_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.money_manager = new FileManager("money.txt", this.getApplicationContext());
        String saved_money = this.money_manager.read();
        if (saved_money != null) {
            TextView moneyText = findViewById(R.id.currentMoney);
            moneyText.setText(saved_money);
        }
    }

    public void addMoney(View view) {
        TextView money_label = (TextView) findViewById(R.id.currentMoney);
        int money = Integer.parseInt(money_label.getText().toString());
        money += 5;
        money_label.setText(String.format("%s", money));
        this.money_manager.write(String.format("%s", money));
    }
}
