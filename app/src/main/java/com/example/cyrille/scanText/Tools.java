package com.example.cyrille.scanText;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.view.View;

/**
 * Created by Cyrille on 12/11/2015.
 */
public class Tools
    {
    public static Bitmap rotateBitmap(Bitmap source, float angle)
        {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        }

    public static Bitmap preRotateBitmap(Bitmap source, float angle)
        {
        Matrix matrix = new Matrix();
        matrix.preRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, false);
        }

    public static enum ScalingLogic
        {
            CROP, FIT
        }

    public static int calculateSampleSize(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
                                          ScalingLogic scalingLogic)
        {
        if (scalingLogic == ScalingLogic.FIT)
            {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect)
                {
                return srcWidth / dstWidth;
                }
            else
                {
                return srcHeight / dstHeight;
                }
            }
        else
            {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect)
                {
                return srcHeight / dstHeight;
                }
            else
                {
                return srcWidth / dstWidth;
                }
            }
        }

    public static Bitmap decodeByteArray(byte[] bytes, int dstWidth, int dstHeight,
                                         ScalingLogic scalingLogic)
        {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth,
                dstHeight, scalingLogic);
        Bitmap unscaledBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        return unscaledBitmap;
        }

    public static Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
                                        ScalingLogic scalingLogic)
        {
        if (scalingLogic == ScalingLogic.CROP)
            {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect)
                {
                final int srcRectWidth = (int) (srcHeight * dstAspect);
                final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
                return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight);
                }
            else
                {
                final int srcRectHeight = (int) (srcWidth / dstAspect);
                final int scrRectTop = (int) (srcHeight - srcRectHeight) / 2;
                return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);
                }
            }
        else
            {
            return new Rect(0, 0, srcWidth, srcHeight);
            }
        }

    public static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight,
                                        ScalingLogic scalingLogic)
        {
        if (scalingLogic == ScalingLogic.FIT)
            {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;

            if (srcAspect > dstAspect)
                {
                return new Rect(0, 0, dstWidth, (int) (dstWidth / srcAspect));
                }
            else
                {
                return new Rect(0, 0, (int) (dstHeight * srcAspect), dstHeight);
                }
            }
        else
            {
            return new Rect(0, 0, dstWidth, dstHeight);
            }
        }

    public static Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight,
                                            ScalingLogic scalingLogic)
        {
        //doesn't give the good result...
        Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(),
                dstWidth, dstHeight, scalingLogic);
        Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(),
                dstWidth, dstHeight, scalingLogic);
        Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(Paint.FILTER_BITMAP_FLAG));

        return scaledBitmap;
        }

    public static Bitmap getFocusedBitmap(Context context, View view, Camera camera, byte[] data, Rect box)
        {
        Point CamRes = CameraUtils.getCameraResolution(context, camera);
        Bitmap bmpIn = Tools.decodeByteArray(data, CamRes.x, CamRes.y, Tools.ScalingLogic.CROP);

        int VW = view.getWidth();
        int VH = view.getHeight();

        int IW = bmpIn.getWidth();
        int IH = bmpIn.getHeight();

        int SW = (int) (((float) VH / (float) IH) * (float) IW);
        int SH = (int) (((float) VW / (float) IW) * (float) IH);

        int X = (VW > SW) ? SW : VW;
        int Y = (VH > SH) ? SH : VH;

        Bitmap bmp = Bitmap.createScaledBitmap(bmpIn, X, Y, false);

        int BW = bmp.getWidth();
        int BH = bmp.getHeight();

        int Woffset = (VW - BW);
        int Hoffset = (VH - BH);

        int RBL = (box.left > Woffset) ? (box.left - Woffset / 2) : 0;
        int RBT = (box.top > Hoffset) ? (box.top - Hoffset / 2) : 0;

        int RW = box.width();
        int RH = box.height();

        int RBW = (RW > BW) ? BW : RW;
        int RBH = (RH > BH) ? BH : RH;

        Bitmap res = Bitmap.createBitmap(bmp, RBL, RBT, RBW, RBH);
        bmp.recycle();

        return res;
        }

    public static Bitmap getFocusedBitmap(Bitmap bmpIn, View view, Rect box)
        {
        //TODO : take care of the screen orientation
        int VW = view.getWidth();
        int VH = view.getHeight();

        int IW = bmpIn.getWidth();
        int IH = bmpIn.getHeight();

        int SW = (int) (((float) VH / (float) IH) * (float) IW);
        int SH = (int) (((float) VW / (float) IW) * (float) IH);

        int X = (VW > SW) ? SW : VW;
        int Y = (VH > SH) ? SH : VH;

        Bitmap bmp = Bitmap.createScaledBitmap(bmpIn, X, Y, false);

        int BW = bmp.getWidth();
        int BH = bmp.getHeight();

        int Woffset = (VW - BW);
        int Hoffset = (VH - BH);

        int RBL = (box.left > Woffset) ? (box.left - Woffset / 2) : 0;
        int RBT = (box.top > Hoffset) ? (box.top - Hoffset / 2) : 0;

        int RW = box.width();
        int RH = box.height();

        int RBW = (RW > BW) ? BW : RW;
        int RBH = (RH > BH) ? BH : RH;

        Bitmap res = Bitmap.createBitmap(bmp, RBL, RBT, RBW, RBH);
        bmp.recycle();

        return res;
        }
    }
