package com.example.cyrille.scanText;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
        textView.setTextSize(35);
        textView.setText(message);
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
        {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_edittext, menu);
        return true;
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
        {
        switch (item.getItemId())
            {
            case R.id.action_copy:
                //TODO : copy the text in the clipboard
                return true;
            case R.id.action_save:
                //TODO : launch the save activity which propose to save text and/or base image
                return true;
            default:
                return super.onOptionsItemSelected(item);
            }
        }
    }
