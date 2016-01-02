package com.example.cyrille.scanText;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by Cyrille on 12/11/2015.
 */
public class EditTextActivity extends AppCompatActivity
    {

    private EditText textView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_text);

        // Get the message from the intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Create the text view
        textView = (EditText) findViewById(R.id.convertedText);
        textView.setTextSize(25);
        textView.setText(message);

        ImageView image = (ImageView) findViewById(R.id.imageViewTest);
        image.setImageBitmap(MainActivity.getImageCropped());
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
                ClipboardManager clipMan = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = android.content.ClipData.newPlainText("WordKeeper", textView.getText());
                clipMan.setPrimaryClip(clip);
                //TODO : make a toast indicating the text has been copied in clipboard
                Toast.makeText(this.getApplicationContext(), "Text copied into clipboard", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_save:
                //TODO : launch the save activity which propose to save text and/or base image
                //TODO : propose to save image only if its a new picture from camera (if the image come from the gallery, its obviously already saved)
                //For doing this : use "Boolean MainActivity.isImageFromCamera" and "Bitmap MainActivity.getImage"
                return true;
            default:
                return super.onOptionsItemSelected(item);
            }
        }
    }
