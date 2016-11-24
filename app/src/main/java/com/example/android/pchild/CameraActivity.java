package com.example.android.pchild;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceView;

import net.bozho.easycamera.DefaultEasyCamera;
import net.bozho.easycamera.EasyCamera;

import java.io.IOException;

public class CameraActivity extends AppCompatActivity {


    SurfaceView surfaceView;
    EasyCamera.CameraActions actions;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);

    }

    @Override
    protected void onResume() {
        super.onResume();
        EasyCamera camera = DefaultEasyCamera.open();
        try {
            actions =
                    camera.startPreview(surfaceView.getHolder());
        }
        catch (IOException e)
        {

        }
        EasyCamera.PictureCallback callback = new EasyCamera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, EasyCamera.CameraActions cameraActions) {

            }
        };
        actions.takePicture(EasyCamera.Callbacks.create().withJpegCallback(callback));


    }
}

