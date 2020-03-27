package com.hp.ocr_googlevisionapi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int ttsLang = t1.setLanguage(Locale.US);

                    if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                            || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("TTS", "The Language is not supported!");
                    } else {
                        Log.e("TTS", "Language Supported.");
                    }
                    Log.e("TTS", "Initialization success.");
                } else {
                    Toast.makeText(getApplicationContext(), "TTS Initialization failed!", Toast.LENGTH_SHORT).show();
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
}
