package com.hp.ocr_googlevisionapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hp.ocr_googlevisionapi.models.LoginModel;
import com.hp.ocr_googlevisionapi.network.Retro;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivtity extends AppCompatActivity {

    private EditText name;
    private EditText phone;
    private Button login;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    Boolean isloggedin = false;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activtity);
        initView();
        dialog=new ProgressDialog(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phone.getText().toString().equals("")||
                 name.getText().toString().equals(""))
                {
                    Toast.makeText(LoginActivtity.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                }else
                {
                    new Retro().getApi().loginCall(phone.getText().toString()).enqueue(new Callback<LoginModel>() {
                        @Override
                        public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                            LoginModel loginModel = response.body();
                            if (loginModel.getStatus().equalsIgnoreCase("success")) {
                                editor.putString("user_id", loginModel.getUser_data().getId());
                                isloggedin = true;
                                editor.putBoolean("isloggedin", isloggedin);
                                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivtity.this);
                                prefs.edit().putInt("userid", Integer.parseInt(loginModel.getUser_data().getId())).commit();

                                dialog.dismiss();

                                Toast.makeText(LoginActivtity.this, "Signing in.", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivtity.this,MainActivity.class));

                            }
                        }

                        @Override
                        public void onFailure(Call<LoginModel> call, Throwable t) {
                            dialog.dismiss();

                            Toast.makeText(LoginActivtity.this, "Login api failed "+t, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

    }

    private void initView() {
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.email);
        login = (Button) findViewById(R.id.login);
    }

    public void navigateSignUp(View view) {
        startActivity(new Intent(getApplicationContext(),SignUp.class));
    }
}
