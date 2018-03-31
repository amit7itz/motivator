package com.amit7itz.motivator.motivator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.amit7itz.motivator.MESSAGE";
    private FileManager file_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText editText = (EditText) findViewById(R.id.editText);
        this.file_manager = new FileManager("file.txt", this.getApplicationContext());
        String saved_message = this.file_manager.read();
        if (saved_message != null) {
            editText.setText(saved_message);
        }
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        this.file_manager.write(message);
        intent.putExtra(EXTRA_MESSAGE, message);

        startActivity(intent);
    }

    public void clearMessage(View view) {
        EditText editText = (EditText) findViewById(R.id.editText);
        editText.setText("");
    }
}
