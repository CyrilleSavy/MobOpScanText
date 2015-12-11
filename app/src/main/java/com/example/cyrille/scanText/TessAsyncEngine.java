package com.example.cyrille.scanText;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.util.Set;

/**
 * Created by cyrille on 04.12.15.
 */
public class TessAsyncEngine extends AsyncTask<Object, String, String>
    {
    static final String TAG = "DBG_" + TessAsyncEngine.class.getName();

    private Bitmap bmp;
    private Activity context;
    private String language;
    private Set<String> charsets;

    @Override
    protected String doInBackground(Object... params)
        {
        try
            {
            if (params.length < 4)
                {
                Log.e(TAG, "Error passing parameter to execute - missing params");
                return null;
                }

            if (!(params[0] instanceof Activity) || !(params[1] instanceof Bitmap))
                {
                Log.e(TAG, "Error passing parameter to execute(context, bitmap)");
                return null;
                }

            context = (Activity) params[0];
            bmp = (Bitmap) params[1];
            if (context == null || bmp == null)
                {
                Log.e(TAG, "Error passed null parameter to execute(context, bitmap)");
                return null;
                }

            if (!(params[2] instanceof String) || !(params[3] instanceof Set))
                {
                Log.e(TAG, "Error passing parameter to execute(language, charsets)");
                return null;
                }

            language = (String) params[2];
            charsets = (Set<String>) params[3];
            if (language == null || charsets == null)
                {
                Log.e(TAG, "Error passed null parameter to execute(language, charsets)");
                return null;
                }

            int rotate = 0;

            if (params.length == 5 && params[4] != null && params[4] instanceof Integer)
                {
                rotate = (Integer) params[4];
                }

            if (rotate >= -180 && rotate <= 180 && rotate != 0)
                {
                bmp = Tools.preRotateBitmap(bmp, rotate);
                Log.d(TAG, "Rotated OCR bitmap " + rotate + " degrees");
                }

            TessEngine tessEngine = TessEngine.Generate(context, language, charsets);

            bmp = bmp.copy(Bitmap.Config.ARGB_8888, true);

            String result = tessEngine.detectText(bmp);

            Log.d(TAG, result);

            return result;
            } catch (Exception ex)
            {
            Log.d(TAG, "Error: " + ex + "\n" + ex.getMessage());
            }

        return null;
        }

    @Override
    protected void onPostExecute(String s)
        {
        if (s == null || bmp == null || context == null)
            return;

        super.onPostExecute(s);
        }
    }
