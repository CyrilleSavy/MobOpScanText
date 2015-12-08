package com.example.cyrille.scanText;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;

/**
 * Created by cyrille on 26.10.15.
 */
public class CameraUtils
    {

    static final String TAG = "DBG_" + CameraUtils.class.getName();

    private static int openedCameraID = -1;

    //Check if the device has a camera
    public static boolean deviceHasCamera(Context context)
        {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
        }

    public static int openedCamera()
        {
        return openedCameraID;
        }

    public static void cameraReleased()
        {
        openedCameraID = -1;
        }

    //Get available camera
    public static Camera getCamera()
        {
        int numberOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        for (int i = 0; i < numberOfCameras; i++)
            {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK)
                {
                openedCameraID = i;
                return Camera.open(i);
                }
            }
        return null;
        }

    //Get available camera
    public static Point getCameraResolution(Context context, Camera camera)
        {
        Camera.Parameters parameters = camera.getParameters();
        Camera.Size size = parameters.getPictureSize();

        return new Point(size.width, size.height);
        }
    }
