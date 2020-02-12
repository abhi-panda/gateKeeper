package com.android.silverpanda.gatekeeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

public class MiddleActivity extends AppCompatActivity {

    private static int MY_CAMERA_REQUEST_CODE=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_middle);
        if (ContextCompat.checkSelfPermission(MiddleActivity.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i("CameraPermissionNotGranted", "true");
            ActivityCompat.requestPermissions(MiddleActivity.this,
                    new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE
            );
        }
        else {
            Log.i("CameraPermissionNotGranted","false");
            startActivity(new Intent(MiddleActivity.this,HomeActivity.class));
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivity(new Intent(MiddleActivity.this,HomeActivity.class));
                    finish();
                } else {
                    closeNow();
                }
                break;
            }
        }
    }
    private void closeNow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            finishAffinity();
        }

        else
        {
            finish();
        }
    }
}
