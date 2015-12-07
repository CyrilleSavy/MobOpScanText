package com.example.cyrille.snapText;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class EditTextActivity extends AppCompatActivity
    {

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);

        // Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Create the text view
        EditText textView = (EditText) findViewById(R.id.convertedText);
        textView.setTextSize(40);
        textView.setText(message);
        }
    }
