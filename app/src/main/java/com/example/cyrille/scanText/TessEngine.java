package com.example.cyrille.scanText;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

/**
 * Created by cyrille on 04.12.15.
 */
public class TessEngine
    {
    static final String TAG = "DBG_" + TessEngine.class.getName();

    private static final String numbers = "1234567890";
    private static final String lowercase = "abcdefghijklmnopqrstuvwxyz";
    private static final String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String space = " ";
    private static final String punctuation = ".,;:!?";
    private static final String maths = "+-*/=";
    private static final String specials = "@#$%^&_<>\"\\|~'`";
    private static final String brackets = "()[]{}";

    private Context context;

    private TessEngine(Context context)
        {
        this.context = context;
        }

    public static TessEngine Generate(Context context)
        {
        return new TessEngine(context);
        }

    public String detectText(Bitmap bitmap)
        {
        Log.d(TAG, "Initialization of TessBaseApi");
        TessDataManager.initTessTrainedData(context);
        TessBaseAPI tessBaseAPI = new TessBaseAPI();
        String path = TessDataManager.getTesseractFolder();
        Log.d(TAG, "Tess folder: " + path);
        tessBaseAPI.setDebug(true);
        tessBaseAPI.init(path, "eng");
//        tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "1234567890");
//        tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, "!@#$%^&*()_+=-qwertyuiop[]} { POIU " + "YTREWQasdASDfghFGHjklJKLl;L:'\"\\|~`xcvXCVbnmBNM,./<>?");
        tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, numbers + punctuation);
        tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, lowercase + uppercase + maths + space + brackets + specials);
        tessBaseAPI.setPageSegMode(TessBaseAPI.OEM_TESSERACT_CUBE_COMBINED);
        Log.d(TAG, "Ended initialization of TessEngine");
        Log.d(TAG, "Running inspection on bitmap");
        tessBaseAPI.setImage(bitmap);
        String inspection = tessBaseAPI.getUTF8Text();
        Log.d(TAG, "Got data: " + inspection);
        tessBaseAPI.end();
        System.gc();
        return inspection;
        }
    }
