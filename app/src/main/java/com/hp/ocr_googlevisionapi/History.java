package com.hp.ocr_googlevisionapi;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hp.ocr_googlevisionapi.models.HistoryModel;
import com.hp.ocr_googlevisionapi.models.SignUpModel;
import com.hp.ocr_googlevisionapi.network.Retro;

import java.util.Locale;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class History extends AppCompatActivity {

    private Button sumButton;
    private Button clearButton;
    private TextView textView6;
    private RecyclerView historylist;
    int sum = 0;
    private HistoryModel historyModel;
    TextToSpeech t1;
    Adapter adapter;
     int Userid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initView();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(History.this);
        Userid= prefs.getInt("userid", 0);
        Log.e("USERID", String.valueOf(Userid));

        if(Userid>0)
        {
            new Retro().getApi().historyCall(String.valueOf(Userid)).enqueue(new Callback<HistoryModel>() {
                @Override
                public void onResponse(Call<HistoryModel> call, Response<HistoryModel> response) {
                     historyModel = response.body();
                    if(historyModel.getStatus().equalsIgnoreCase("success")){
                        historylist.setLayoutManager(new LinearLayoutManager(History.this,RecyclerView.VERTICAL,false));
                       adapter=new Adapter(historyModel);
                        historylist.setAdapter(adapter);
                    }else
                    {
                        Toast.makeText(History.this, "Scan data not found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<HistoryModel> call, Throwable t) {
                    Toast.makeText(History.this, "APi failure "+t , Toast.LENGTH_SHORT).show();
                }
            });
        }else
        {
            Toast.makeText(getApplicationContext(), "Failed to get registration details . Please login again.", Toast.LENGTH_SHORT).show();
        }

        sumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(historyModel != null) {
                    sum=0;
                    for (int i = 0; i < historyModel.getAmount_details().size(); i++) {
                        sum = sum + Integer.parseInt(historyModel.getAmount_details().get(i).getAmount());
                    }

                    t1 = new TextToSpeech(History.this, new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if (status == TextToSpeech.SUCCESS) {
                                int result = t1.setLanguage(Locale.US);
                                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                    Log.e("TTS", "This Language is not supported");
                                }
                                speak("You have scanned a sum of "+sum+" rupees");


                            } else {
                                Log.e("TTS", "Initilization Failed!");
                            }
                        }
                    });

                    Toast.makeText(History.this, "You have scanned a sum of "+sum+" rupees" , Toast.LENGTH_SHORT).show();
                }else
                {
                    Toast.makeText(History.this, "Cant load data", Toast.LENGTH_SHORT).show();
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new Retro().getApi().deleteCall(String.valueOf(Userid)).enqueue(new Callback<SignUpModel>() {
                    @Override
                    public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {
                        if (response.body().getStatus().equalsIgnoreCase("success"))
                        {
                            Toast.makeText(History.this, "Scan history cleared", Toast.LENGTH_SHORT).show();
                            t1 = new TextToSpeech(History.this, new TextToSpeech.OnInitListener() {
                                @Override
                                public void onInit(int status) {
                                    if (status == TextToSpeech.SUCCESS) {
                                        int result = t1.setLanguage(Locale.US);
                                        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                            Log.e("TTS", "This Language is not supported");
                                        }
                                        speak("Your scan history has been cleared.");


                                    } else {
                                        Log.e("TTS", "Initilization Failed!");
                                    }
                                }
                            });
                        }else
                        {
                            Toast.makeText(History.this, "Try again later", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SignUpModel> call, Throwable t) {
                        Toast.makeText(History.this, ""+t, Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }

    private void initView() {
        sumButton = (Button) findViewById(R.id.sumButton);
        clearButton = (Button) findViewById(R.id.clearButton);
        textView6 = (TextView) findViewById(R.id.textView6);
        historylist = (RecyclerView) findViewById(R.id.historylist);
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
