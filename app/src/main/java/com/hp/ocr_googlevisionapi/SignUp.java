package com.hp.ocr_googlevisionapi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hp.ocr_googlevisionapi.models.LoginModel;
import com.hp.ocr_googlevisionapi.models.SignUpModel;
import com.hp.ocr_googlevisionapi.network.Retro;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUp extends AppCompatActivity {

    private EditText name;
    private EditText phone;
    private Button signup;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    Boolean isloggedin = false;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initView();
        dialog=new ProgressDialog(this);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("Please wait");
                dialog.show();
                if (name.getText().toString().equals("") ||
                        phone.getText().toString().equals("")) {

                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Fill all fields.", Toast.LENGTH_SHORT).show();
                } else {
                    new Retro().getApi().signUpCall(name.getText().toString(),
                            phone.getText().toString()).enqueue(new Callback<SignUpModel>() {
                        @Override
                        public void onResponse(Call<SignUpModel> call, Response<SignUpModel> response) {

                            SignUpModel signUpModel = response.body();
                            if (signUpModel.getStatus().equals("success")) {

                                editor.putString("user_name", name.getText().toString());
                                editor.putString("user_phone", phone.getText().toString());
                                editor.commit();


                                Toast.makeText(SignUp.this, "Signed Up Successfully", Toast.LENGTH_SHORT).show();

                                new Retro().getApi().loginCall(phone.getText().toString()).enqueue(new Callback<LoginModel>() {
                                    @Override
                                    public void onResponse(Call<LoginModel> call, Response<LoginModel> response) {
                                        LoginModel loginModel = response.body();
                                        if (loginModel.getStatus().equalsIgnoreCase("success")) {
                                            editor.putString("user_id", loginModel.getUser_data().getId());
                                            isloggedin = true;
                                            editor.putBoolean("isloggedin", isloggedin);
                                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SignUp.this);
                                            prefs.edit().putInt("userid", Integer.parseInt(loginModel.getUser_data().getId())).commit();

                                            dialog.dismiss();

                                            Toast.makeText(SignUp.this, "Signing in.", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(SignUp.this,MainActivity.class));

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<LoginModel> call, Throwable t) {
                                        dialog.dismiss();

                                        Toast.makeText(SignUp.this, "Login api failed "+t, Toast.LENGTH_SHORT).show();
                                    }
                                });


                            } else {
                                dialog.dismiss();

                                Toast.makeText(SignUp.this, signUpModel.getStatus(), Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onFailure(Call<SignUpModel> call, Throwable t) {
                            dialog.dismiss();

                            Toast.makeText(SignUp.this, "Api failure " + t, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });




    }

    private void initView() {
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.email);
        signup = (Button) findViewById(R.id.signup);
    }
}
