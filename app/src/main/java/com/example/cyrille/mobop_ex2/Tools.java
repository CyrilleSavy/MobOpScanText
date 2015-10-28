package com.example.cyrille.mobop_ex2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;

/**
 * Created by cyrille on 28.10.15.
 */
public class Tools {
    public static Bitmap getFocusedBitmap(Context context, Camera camera, byte[] data, Rect box) {
        Point CamRes = FocusBoxUtils.getCameraResolution(context, camera);
        Point ScrRes = FocusBoxUtils.getScreenResolution(context);

        int SW = ScrRes.x; //SCREEN WIDTH - HEIGHT
        int SH = ScrRes.y;

        int RW = box.width(); // FOCUS BOX RECT WIDTH - HEIGHT - TOP - LEFT
        int RH = box.height();
        int RL = box.left;
        int RT = box.top;

        float RSW = (float) (RW * Math.pow(SW, -1)); //DIMENSION RATIO OF FOCUSBOX OVER SCREEN
        float RSH = (float) (RH * Math.pow(SH, -1));

        float RSL = (float) (RL * Math.pow(SW, -1));
        float RST = (float) (RT * Math.pow(SH, -1));

        float k = 0.5f;

        int CW = CamRes.x;
        int CH = CamRes.y;

        int X = (int) (k * CW); //SCALED BITMAP FROM CAMERA
        int Y = (int) (k * CH);

        //SCALING WITH SONY TOOLS
        // http://developer.sonymobile.com/2011/06/27/how-to-scale-images-for-your-android-application/

        Bitmap unscaledBitmap = Tools.decodeByteArray(data, X, Y, Tools.ScalingLogic.CROP);
        Bitmap bmp = Tools.createScaledBitmap(unscaledBitmap, X, Y, Tools.ScalingLogic.CROP);
        unscaledBitmap.recycle();

        if (CW > CH)
            bmp = Tools.rotateBitmap(bmp, 90);

        int BW = bmp.getWidth();   //NEW FULL CAPTURED BITMAP DIMENSIONS
        int BH = bmp.getHeight();

        int RBL = (int) (RSL * BW); // NEW CROPPED BITMAP IN THE FOCUS BOX
        int RBT = (int) (RST * BH);

        int RBW = (int) (RSW * BW);
        int RBH = (int) (RSH * BH);

        Bitmap res = Bitmap.createBitmap(bmp, RBL, RBT, RBW, RBH);
        bmp.recycle();

        return res;
    }
}
