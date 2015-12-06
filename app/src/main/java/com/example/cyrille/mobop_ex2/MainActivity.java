package com.example.cyrille.mobop_ex2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends Activity implements View.OnClickListener
    {
    static final String TAG = "DBG_" + MainActivity.class.getName();

    //attributes for the activity_main layout
    Button takePictureButton;
    Button importFileButton;
    Button scanTextButton;
    ImageView imagePreview;
    FocusBoxView focusBox;

    Uri imageUri;
    Bitmap imageBmp;
    Bitmap imageCropped;
    Boolean imageFromCamera = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
        {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "setContentView OK");
        }

    @Override
    protected void onResume()
        {
        super.onResume();

        //attributes for the activity_main layout
        takePictureButton = (Button) findViewById(R.id.takePictureButton);
        importFileButton = (Button) findViewById(R.id.importFileButton);
        scanTextButton = (Button) findViewById(R.id.scanTextButton);
        imagePreview = (ImageView) findViewById(R.id.imagePreview);
        focusBox = (FocusBoxView) findViewById(R.id.focusBox);

        //listeners
        takePictureButton.setOnClickListener(this);
        importFileButton.setOnClickListener(this);
        scanTextButton.setOnClickListener(this);

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
    public void onClick(View v)
        {
        if (v == takePictureButton)
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

        if (v == importFileButton)
            {
            //TODO : intent to launch the ImportFileActivity
            }

        if (v == scanTextButton)
            {
            //TODO : check if there is a picture ...
            //TODO : intent to launch the ScanTextActivity
            imageCropped = Tools.getFocusedBitmap(imageBmp, (View) imagePreview, focusBox.getBox());
            imagePreview.setImageBitmap(imageCropped);//TODO : to remove
            }
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
        {
        // Check which request we're responding to
        if (requestCode == CAMERA_IMAGE)
            {
            // Make sure the request was successful
            if (resultCode == RESULT_OK)
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
            }
        }
    }
