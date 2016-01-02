package com.example.cyrille.scanText;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.util.Set;

/**
 * Created by cyrille on 04.12.15.
 */
public class TessEngine
    {
    static final String TAG = "DBG_" + TessEngine.class.getName();

    private static final String numbers = "1234567890";
    private static final String lowercase = "abcdefghijklmnopqrstuvwxyz";
    private static final String uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String accentuated = "àäéèöüêâôû";
    private static final String space = " ";
    private static final String punctuation = ".,;:!?";
    private static final String maths = "+-*/=";
    private static final String specials = "@#$%^&_<>\"\\|~'`“”‘’";
    private static final String brackets = "()[]{}";

    private static final String[] allCharsets =

            {
                    numbers, lowercase, uppercase, accentuated, space, punctuation, maths, specials, brackets
            };

    private Context context;
    private String language;

    private String whitelist;
    private String blacklist;

    private TessEngine(Context context, String language, Set<String> charsets)
        {
        this.context = context;
        this.language = language;

        StringBuilder stringBuilderWhite = new StringBuilder();
        StringBuilder stringBuilderBlack = new StringBuilder();
        for (Integer i = 1; i <= allCharsets.length; i++)
            {
            if (charsets.contains(i.toString()))
                {
                stringBuilderWhite.append(allCharsets[i - 1]);
                }
            else
                {
                stringBuilderBlack.append(allCharsets[i - 1]);
                }
            }
        whitelist = stringBuilderWhite.toString();
        blacklist = stringBuilderBlack.toString();
        }

    public static TessEngine Generate(Context context, String language, Set<String> charsets)
        {
        return new TessEngine(context, language, charsets);
        }

    public String detectText(Bitmap bitmap)
        {
        Log.d(TAG, "Initialization of TessBaseApi");
        TessDataManager.initTessTrainedData(context, language);
        TessBaseAPI tessBaseAPI = new TessBaseAPI();
        String path = TessDataManager.getTesseractFolder();
        Log.d(TAG, "Tess folder: " + path);
        tessBaseAPI.setDebug(true);
        tessBaseAPI.init(path, language);
        tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, whitelist);
        tessBaseAPI.setVariable(TessBaseAPI.VAR_CHAR_BLACKLIST, blacklist);
        tessBaseAPI.setPageSegMode(TessBaseAPI.OEM_TESSERACT_CUBE_COMBINED);
        Log.d(TAG, "Ended initialization of TessEngine");
        Log.d(TAG, "Running inspection on bitmap");
        //TODO : add image preprocessing to improve conversion
        tessBaseAPI.setImage(bitmap);
        String inspection = tessBaseAPI.getUTF8Text();
        Log.d(TAG, "Got data: " + inspection);
        tessBaseAPI.end();
        System.gc();
        return inspection;
        }
    }
