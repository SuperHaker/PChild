package com.example.android.pchild;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.commonsware.cwac.cam2.CameraActivity;
import com.commonsware.cwac.cam2.Facing;
import com.commonsware.cwac.cam2.FlashMode;
import com.commonsware.cwac.cam2.ZoomStyle;
import com.commonsware.cwac.security.RuntimePermissionUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String[] PERMS_ALL={
            CAMERA,
            RECORD_AUDIO,
            WRITE_EXTERNAL_STORAGE
    };
    private static final FlashMode[] FLASH_MODES={
            FlashMode.ALWAYS,
            FlashMode.AUTO
    };


    private static final int REQUEST_PORTRAIT_RFC=1337;
    private static final int REQUEST_PORTRAIT_FFC=REQUEST_PORTRAIT_RFC+1;
    private static final int REQUEST_LANDSCAPE_RFC=REQUEST_PORTRAIT_RFC+2;
    private static final int REQUEST_LANDSCAPE_FFC=REQUEST_PORTRAIT_RFC+3;
    private static final int RESULT_PERMS_ALL=REQUEST_PORTRAIT_RFC+4;
    private static final String STATE_TEST_ROOT="Saved Images IDK";

    private File previewFrame;
    private RuntimePermissionUtils utils;
    private File testRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Environment.MEDIA_MOUNTED
                .equals(Environment.getExternalStorageState())) {
            Toast
                    .makeText(this, "Cannot access external storage!",
                            Toast.LENGTH_LONG)
                    .show();
            finish();
        }

        previewFrame=
                new File(getExternalCacheDir(), "cam2-preview.jpg");


        setContentView(R.layout.activity_main);

        utils=new RuntimePermissionUtils(this);

        if (savedInstanceState==null) {
            String filename = "cam2_" + Build.MANUFACTURER + "_" + Build.PRODUCT
                    + "_" + new SimpleDateFormat("yyyyMMdd'-'HHmmss").format(new Date());

            filename = filename.replaceAll(" ", "_");

            testRoot = new File(getExternalFilesDir(null), filename);
        }

        else {
            testRoot=new File(savedInstanceState.getString(STATE_TEST_ROOT));
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);





        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                capturePortraitFFC();

            }

        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void capturePortraitFFC() {

        Intent i;


            i=new CameraActivity.IntentBuilder(this)
                    .skipConfirm()
                    .facing(Facing.BACK)
                    .facingExactMatch()
                    .to(new File(testRoot, "landscape-rear.jpg"))
                    .updateMediaStore()
                    .flashModes(FLASH_MODES)
                    .zoomStyle(ZoomStyle.SEEKBAR)
                    .debugSavePreviewFrame()
                    .debug()
                    .build();


        startActivityForResult(i, REQUEST_PORTRAIT_RFC);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        savePreviewFrame(new File(testRoot,
                "preview-portrait-rear.jpg"));
    }

    private void savePreviewFrame(File previewDest) {
        if (previewFrame.exists()) {
            if (previewDest.exists()) {
                previewDest.delete();
            }

            previewFrame.renameTo(previewDest);
        }
    }
}
