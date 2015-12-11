package com.example.cyrille.scanText;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity
    {
    private static final String TAG = "DBG_" + MainActivity.class.getName();

    //attributes for the activity_main layout
    private ImageView imagePreview;
    private FocusBoxView focusBox;

    //static otherwise deleted on screen rotation
    private static Uri imageUri;
    private static Bitmap imageBmp;
    private static Bitmap imageCropped;
    private static Boolean imageFromCamera = false;

    private String textResult;
    public final static String EXTRA_MESSAGE = "com.example.cyrille.scanText.MESSAGE";
    public final static String EXTRA_IMAGE = "com.example.cyrille.scanText.IMAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "setContentView OK");
        }

    static public boolean isImageFromCamera()
        {
        return imageFromCamera;
        }

    static public Bitmap getImage()
        {
        return imageBmp;
        }

    public String getLanguage()
        {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String s = sharedPrefs.getString("language_preference", null);
        if (s == null)
            {
            PreferenceManager.setDefaultValues(getApplicationContext(),
                    R.xml.advanced_preferences, false);
            }
        return sharedPrefs.getString("language_preference", null);
        }

    public Set<String> getCharsets()
        {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> s = sharedPrefs.getStringSet("charsets_preference", null);
        if (s == null)
            {
            PreferenceManager.setDefaultValues(getApplicationContext(),
                    R.xml.advanced_preferences, false);
            }
        return sharedPrefs.getStringSet("charsets_preference", null);
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
        {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
        }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
        {
        super.onConfigurationChanged(newConfig);
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
        {
        switch (item.getItemId())
            {
            case R.id.action_settings:
                startSettings();
                return true;
            case R.id.action_takephoto:
                takePicture();
                return true;
            case R.id.action_scantext:
                scanText();
                return true;
            case R.id.action_importimage:
                importImage();
                return true;
            default:
                return super.onOptionsItemSelected(item);
            }
        }

    @Override
    protected void onResume()
        {
        super.onResume();

        //attributes for the activity_main layout
        imagePreview = (ImageView) findViewById(R.id.imagePreview);
        focusBox = (FocusBoxView) findViewById(R.id.focusBox);

        if (imageBmp != null)
            {
            imagePreview.setImageBitmap(imageBmp);
            }
        }

    @Override
    protected void onPause()
        {
        super.onPause();
        }

    @Override
    protected void onDestroy()
        {
        super.onDestroy();

        }

    static final int CAMERA_IMAGE = 1;  // The request code

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
        {
        // Check which request we're responding to
        if ((requestCode == CAMERA_IMAGE) && (resultCode == RESULT_OK))
            {
            // The user took a photo
            try
                {
                imageBmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageFromCamera = true;
                imagePreview.setImageBitmap(imageBmp);
                File f = new File(imageUri.getPath());
                if (f.exists())
                    f.delete();
                } catch (IOException e)
                {
                e.printStackTrace();
                }
            }
        if ((requestCode == SELECT_PICTURE) && (resultCode == RESULT_OK))
            {
            // The user selected a photo
            try
                {
                imageUri = data.getData();
                imageBmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageFromCamera = false;
                imagePreview.setImageBitmap(imageBmp);
                } catch (IOException e)
                {
                e.printStackTrace();
                }
            }
        }

    private void takePicture()
        {
        //intent to launch the Camera Activity
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Construct temporary image path and name to save the taken photo
        imageUri = Uri.fromFile(new File(Environment
                .getExternalStorageDirectory(), "tmp_"
                + String.valueOf(System.currentTimeMillis()) + ".bmp"));
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                imageUri);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CAMERA_IMAGE);
        }

    private void scanText()
        {
        //check if there is a picture ...
        if (imageBmp == null)
            {
            Toast.makeText(this.getApplicationContext(), "No image... Import one or take a picture", Toast.LENGTH_SHORT).show();
            }
        else
            {
            //intent to launch the ScanTextActivity
            imageCropped = Tools.getFocusedBitmap(imageBmp, (View) imagePreview, focusBox.getBox());
            try
                {
                textResult = new TessAsyncEngine().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, this, imageCropped, getLanguage(), getCharsets()).get();
                //launch the activity that shows the converted text and allows to modify it
                //intent to launch the EditTextActivity
                Intent intent = new Intent(this, EditTextActivity.class);
                // Send the converted text as an input to the EditTextActivity
                intent.putExtra(EXTRA_MESSAGE, textResult);
                intent.putExtra(EXTRA_IMAGE, imageCropped);
                startActivity(intent);
                } catch (InterruptedException e)
                {
                e.printStackTrace();
                } catch (ExecutionException e)
                {
                e.printStackTrace();
                }
            }
        }

    static final int SELECT_PICTURE = 2;  // The request code

    private void importImage()
        {
        //import an image an put it in "imageBmp" and show in "imagePreview"
        // select a file
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
        }

    private void startSettings()
        {
        //start the the settings activity
        Intent intent = new Intent(this, MainPreferencesActivity.class);
        startActivity(intent);
        }
    }
