package com.hp.ocr_googlevisionapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import static com.hp.ocr_googlevisionapi.MainActivity.MyPREFERENCES;

public class ScanResult extends AppCompatActivity {

    private TextView restext;
    TextToSpeech t1;
    private SharedPreferences sharedPref;
    public static final String MyPREFERENCES = "MyPrefs";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        initView();

        sharedPref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String scan=sharedPref.getString("scan", null);
        Log.e("SCAN",scan);
        restext.setText(scan + " Rupees note Found");
        t1 = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = t1.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "This Language is not supported");
                    }
                    speak(restext.getText().toString());

                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });

        t1.speak(restext.getText().toString(), TextToSpeech.QUEUE_FLUSH, null,null);


        restext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t1.speak(restext.getText().toString(), TextToSpeech.QUEUE_FLUSH, null,null);

            }
        });
    }



    private void initView() {
        restext = findViewById(R.id.restext);
    }

    @Override
    protected void onResume() {
        super.onResume();
       // t1.speak("HAI", TextToSpeech.QUEUE_FLUSH, null);

    }

    @Override
    protected void onPause() {
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }

    public void cl(View view) {
        t1.speak(restext.getText().toString(), TextToSpeech.QUEUE_FLUSH, null,null);

    }
    private void speak(String text){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            t1.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);

        }else{
            t1.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    @Override
    public void onDestroy() {
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        super.onDestroy();
    }
}
