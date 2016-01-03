package com.example.cyrille.scanText;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Cyrille on 12/11/2015.
 */
public class EditTextActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener
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
                ClipboardManager clipMan = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = android.content.ClipData.newPlainText("WordKeeper", textView.getText());
                clipMan.setPrimaryClip(clip);
                Toast.makeText(this.getApplicationContext(), "Text copied into clipboard", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_save:
                //TODO : launch the save activity which propose to save text and/or base image
                this.showMenu();
                //TODO : propose to save image only if its a new picture from camera (if the image come from the gallery, its obviously already saved)
                //For doing this : use "Boolean MainActivity.isImageFromCamera" and "Bitmap MainActivity.getImage"
                return true;
            default:
                return super.onOptionsItemSelected(item);
            }
        }

    public void showMenu()
        {
        View menuItemView = findViewById(R.id.action_save);
        PopupMenu popup = new PopupMenu(this, menuItemView);

        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(this);

        if (MainActivity.isImageFromCamera())
            {
            popup.inflate(R.menu.popup_save_textimage);
            }
        else
            {
            popup.inflate(R.menu.popup_save_text);
            }
        popup.show();
        }

    @Override
    public boolean onMenuItemClick(MenuItem item)
        {
        switch (item.getItemId())
            {
            case R.id.savetext:
                Date date2 = new Date();
                SimpleDateFormat df2 = new SimpleDateFormat("yy-MM-dd'T'HH-mm-ss");
                String newTextFile = "ScanText_" + df2.format(date2) + ".txt";
                String newPath = Environment.getExternalStorageDirectory() + "/ScanText/";
                try
                    {
                    File f = new File(newPath);
                    f.mkdirs();
                    FileOutputStream fos = new FileOutputStream(newPath + newTextFile);
                    byte[] buffer = textView.getText().toString().getBytes();
                    fos.write(buffer, 0, buffer.length);
                    fos.close();
                    File file = new File(newPath + newTextFile);
                    if (file.exists())
                        Toast.makeText(this.getApplicationContext(), "Text saved in a file", Toast.LENGTH_SHORT).show();
                    } catch (Exception e)
                    {
                    }
                return true;
            case R.id.saveimage:
                Date date = new Date();
                SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd'T'HH-mm-ss");
                String newPicFile = "ScanText_" + df.format(date) + ".jpg";
                if (MediaStore.Images.Media.insertImage(getContentResolver(), MainActivity.getImage(), newPicFile, "Picture taken from the ScanText Application") != null)
                    {
                    Toast.makeText(this.getApplicationContext(), "Image Saved in Gallery", Toast.LENGTH_SHORT).show();
                    }
                return true;
            default:
                return false;
            }
        }
    }
