package com.example.cyrille.mobop_ex2;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraDevice;
import android.util.Log;

/**
 * Created by cyrille on 26.10.15.
 */
public class CameraUtils {

    static final String TAG = "DBG_" + CameraUtils.class.getName();

    //Check if the device has a camera
    public static boolean deviceHasCamera(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    //Get available camera
    public static Camera getCamera() {
        try {
            return Camera.open();
        } catch (Exception e) {
            Log.e(TAG, "Cannot getCamera()");
            return null;
        }
    }
}
