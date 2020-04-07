package com.hp.ocr_googlevisionapi;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    SurfaceView mCameraView;
    TextView mTextView;
    CameraSource mCameraSource;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedPref;




    private static final String TAG = "MainActivity";
    private static final int requestPermissionID = 101;


    Boolean iscurrency = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCameraView = findViewById(R.id.surfaceView);
        mTextView = findViewById(R.id.text_view);
        sharedPref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        startCameraSource();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != requestPermissionID) {
            Log.d(TAG, "Got unexpected permission result: " + requestCode);
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mCameraSource.start(mCameraView.getHolder());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startCameraSource() {

        //Create the TextRecognizer
        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Log.w(TAG, "Detector dependencies not loaded yet");
        } else {

            //Initialize camerasource to use high resolution and set Autofocus on.
            mCameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setAutoFocusEnabled(true)
                    .setRequestedFps(2.0f)
                    .build();

            /**
             * Add call back to SurfaceView and check if camera permission is granted.
             * If permission is granted we can start our cameraSource and pass it to surfaceView
             */
            mCameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    try {

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    requestPermissionID);
                            return;
                        }
                        mCameraSource.start(mCameraView.getHolder());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    mCameraSource.stop();
                }
            });

            //Set the TextRecognizer's Processor.
            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {
                }


                /**
                 * Detect all the text from camera using TextBlock and the values into a stringBuilder
                 * which will then be set to the textView.
                 * */




                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    final SparseArray<TextBlock> items = detections.getDetectedItems();
                    if (items.size() != 0) {

                        mTextView.post(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < items.size(); i++) {
                                    TextBlock item = items.valueAt(i);
                                    stringBuilder.append(item.getValue());
                                    stringBuilder.append("\n");

                                    if (item.getValue().equals("RESERVE BANK OF INDIA")) {
                                        iscurrency = true;
                                        Toast.makeText(MainActivity.this, "Currency found", Toast.LENGTH_SHORT).show();


                                    }

                                    if (item.getValue().equals("10")) {
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("scan", "10");
                                        editor.commit();
                                        Toast.makeText(MainActivity.this, "10 Rs note found", Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), ScanResult.class));
                                    } else if (item.getValue().equals("20")) {
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("scan", "20");
                                        editor.commit();
                                        Toast.makeText(MainActivity.this, "20 Rs note found", Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), ScanResult.class));

                                    } else if (item.getValue().equals("50")) {
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("scan", "50");
                                        editor.commit();
                                        Toast.makeText(MainActivity.this, "50 Rs note found", Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), ScanResult.class));

                                    } else if (item.getValue().equals("100")) {
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("scan", "100");
                                        editor.commit();
                                        Toast.makeText(MainActivity.this, "100 Rs note found", Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), ScanResult.class));

                                    } else if (item.getValue().equals("2000")) {
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("scan", "2000");
                                        editor.commit();
                                        Toast.makeText(MainActivity.this, "2000 Rs note found", Toast.LENGTH_SHORT).show();

                                        finish();
                                        startActivity(new Intent(getApplicationContext(), ScanResult.class));

                                    } else if (item.getValue().equals("500")) {
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putString("scan", "500");
                                        editor.commit();
                                        Toast.makeText(MainActivity.this, "2000 Rs note found", Toast.LENGTH_SHORT).show();
                                        finish();
                                        startActivity(new Intent(getApplicationContext(), ScanResult.class));

                                    }


                                }
                                //  mTextView.setText(stringBuilder.toString());
                            }
                        });
                    }
                }
            });
        }
    }
}