package com.hp.ocr_googlevisionapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {
    private int Delay=2000;
    boolean loged=false;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        loged = sharedpreferences.getBoolean("isloggedin",false);
        Log.e("isloggedin", String.valueOf(loged));
        final int Userid;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Splash.this);
        Userid= prefs.getInt("userid", 0);

        Timer RunSplash = new Timer();
        TimerTask ShowSplash = new TimerTask() {
            @Override
            public void run() {

                if(Userid>0) {
                    Log.e("Loginstgtgatus"," logged in");

//                    String userId = sharedpreferences.getString("user_id",null);
//                    Log.e("USER ID",userId);

                    Intent myIntent = new Intent(getApplicationContext(),
                            MainActivity.class);
                    startActivity(myIntent);
                }else
                {
                    Log.e("Loginstgtgatus","not logged in");
                    startActivity(new Intent(getApplicationContext(),LoginActivtity.class));
                }
                finish();
            }
        };
        RunSplash.schedule(ShowSplash, Delay);

    }
}
