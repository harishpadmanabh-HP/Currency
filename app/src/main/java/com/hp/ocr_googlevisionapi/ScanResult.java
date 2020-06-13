package com.hp.ocr_googlevisionapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hp.ocr_googlevisionapi.models.StoreMoneyModel;
import com.hp.ocr_googlevisionapi.network.Retro;

import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.hp.ocr_googlevisionapi.MainActivity.MyPREFERENCES;

public class ScanResult extends AppCompatActivity {

    private TextView restext;
    TextToSpeech t1;
    private SharedPreferences sharedPref;
    public static final String MyPREFERENCES = "MyPrefs";
    String userID;
    ProgressDialog dialog;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.menu_logout: {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ScanResult.this);
                prefs.edit().putInt("userid", 0).commit();
                startActivity(new Intent(getApplicationContext(),Splash.class));
                return true;
            }
            case R.id.menu_history:
                startActivity(new Intent(getApplicationContext(),History.class));
                return true;
//            case R.id.item3:
//                Toast.makeText(getApplicationContext(),"Item 3 Selected",Toast.LENGTH_LONG).show();
//                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        initView();
        dialog=new ProgressDialog(this);
        sharedPref = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String scan=sharedPref.getString("scan", null);
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
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ScanResult.this);
                    userID= String.valueOf(prefs.getInt("userid", 0));
                    Log.e("user id ",userID);
                    storeMoney(userID,scan);

                } else {
                    Log.e("TTS", "Initilization Failed!");
                }
            }
        });

       // t1.speak(restext.getText().toString(), TextToSpeech.QUEUE_FLUSH, null,null);


//        restext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                t1.speak(restext.getText().toString(), TextToSpeech.QUEUE_FLUSH, null,null);
//
//            }
//        });
    }

    private void storeMoney( String userID,String scan) {
dialog.show();
Log.e("UserId",userID);
Log.e("scan",scan);
        new Retro().getApi().storeMoneyCall(userID,scan).enqueue(new Callback<StoreMoneyModel>() {
            @Override
            public void onResponse(Call<StoreMoneyModel> call, Response<StoreMoneyModel> response) {
               Log.e("Resposne",response.body().getStatus());
                StoreMoneyModel storeMoneyModel =response.body();
                if(storeMoneyModel.getStatus().equalsIgnoreCase("success")){
                    dialog.dismiss();
                    Toast.makeText(ScanResult.this, "Money Stored", Toast.LENGTH_SHORT).show();


                }else
                {
                    dialog.dismiss();

                    Toast.makeText(ScanResult.this, "Money cant be Stored", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<StoreMoneyModel> call, Throwable t) {
                dialog.dismiss();

                Toast.makeText(ScanResult.this, "Store API failure " +t, Toast.LENGTH_SHORT).show();
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
        //t1.speak(restext.getText().toString(), TextToSpeech.QUEUE_FLUSH, null,null);

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
