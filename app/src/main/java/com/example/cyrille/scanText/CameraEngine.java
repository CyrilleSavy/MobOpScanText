package com.example.cyrille.scanText;

import android.hardware.Camera;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Created by cyrille on 26.10.15.
 */
public class CameraEngine
    {

    static final String TAG = "DBG_" + CameraEngine.class.getName();

    private boolean on;
    private Camera camera;
    private SurfaceHolder surfaceHolder;

    public boolean isOn()
        {
        return on;
        }

    private CameraEngine(SurfaceHolder surfaceHolder)
        {
        this.surfaceHolder = surfaceHolder;
        this.on = false;
        }

    static public CameraEngine New(SurfaceHolder surfaceHolder)
        {
        Log.d(TAG, "Creating camera engine");
        return new CameraEngine(surfaceHolder);
        }

    public void requestFocus()
        {
        if (camera == null)
            return;

        if (isOn())
            {
            Camera.Parameters params = camera.getParameters();
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            camera.setParameters(params);
            }
        }

    public void start(int rotation)
        {
        Log.d(TAG, "Entered CameraEngine - start()");
        this.camera = CameraUtils.getCamera();

        if (this.camera == null)
            return;

        Log.d(TAG, "Got camera hardware");

        try
            {
            int degrees = 0;
            switch (rotation)
                {
                default:
                case Surface.ROTATION_0:
                    degrees = 0;
                    break;
                case Surface.ROTATION_90:
                    degrees = 90;
                    break;
                case Surface.ROTATION_180:
                    degrees = 180;
                    break;
                case Surface.ROTATION_270:
                    degrees = 270;
                    break;
                }
            android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
            android.hardware.Camera.getCameraInfo(CameraUtils.openedCamera(), info);
            int screenRotationDegrees;
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT)
                {
                screenRotationDegrees = (info.orientation + degrees) % 360;
                screenRotationDegrees = (360 - screenRotationDegrees) % 360;  // compensate the mirror
                }
            else
                {  // back-facing
                screenRotationDegrees = (info.orientation - degrees + 360) % 360;
                }

            this.camera.setPreviewDisplay(this.surfaceHolder);
            this.camera.setDisplayOrientation(screenRotationDegrees);//Portrait Camera
            this.camera.startPreview();

            on = true;

            Log.d(TAG, "CameraEngine preview started");

            } catch (IOException e)
            {
            Log.e(TAG, "Error in setPreviewDisplay");
            }
        }

    public void stop()
        {
        if (camera != null)
            {
            //this.autoFocusEngine.stop();
            camera.stopPreview();
            camera.release();
            CameraUtils.cameraReleased();
            camera = null;
            }

        on = false;

        Log.d(TAG, "CameraEngine Stopped");
        }

    public void takeShot(Camera.ShutterCallback shutterCallback,
                         Camera.PictureCallback rawPictureCallback,
                         Camera.PictureCallback jpegPictureCallback)
        {
        if (isOn())
            {
            camera.takePicture(shutterCallback, rawPictureCallback, jpegPictureCallback);
            }
        }

    }
