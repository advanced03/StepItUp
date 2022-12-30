package com.sof.stepitup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Login extends AppCompatActivity {
//REMINDER: SESSIONS KUNNEN GEMAAKT WORDEN VOOR ONTHOUDEN VAN LOGINS
//Wireless LAN adapter Local Area Connection IP via hotspot
    public static String ip = "192.168.137.1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
        TextInputLayout username =(TextInputLayout) findViewById(R.id.user);
        TextInputLayout password =(TextInputLayout) findViewById(R.id.password);
        TextView createAccount = (TextView) findViewById(R.id.noacc);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getBaseContext(),"Workdskfsndf",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, CreateAcc.class);
                startActivity(intent);
                finish();
            }
        });

        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!username.getEditText().toString().equals("") && !password.getEditText().toString().equals("")) {
//                    progressBar.setVisibility(View.VISIBLE);
                    //Start ProgressBar first (Set visibility VISIBLE)
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[2];
                            field[0] = "username";
                            field[1] = "password";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = username.getEditText().toString();
                            data[1] = password.getEditText().toString();
//                            try {
                            PutData putData = new PutData("http://"+ ip +"/stepitup/login.php", "POST", field, data);
                                if (putData.startPut()) {
                                    if (putData.onComplete()) {
                                        progressBar.setVisibility(View.GONE);
                                        String result = putData.getResult();
                                        if (result.equals("Welkom!")) {
                                            Intent intent = new Intent(getApplicationContext(), Home.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else {
                                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
//                            }
//                            catch(Exception error1) {
//                                error1.printStackTrace();
//                            }
                            //End Write and Read data with URL
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(), "Vul alles in aub", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}