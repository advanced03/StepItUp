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
import com.vishnusivadas.advanced_httpurlconnection.FetchData;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class CreateAcc extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);
        TextView maakAccTxt = (TextView) findViewById(R.id.maakacctxt);
        TextView hasAccountText =(TextView) findViewById(R.id.hasacc);
        hasAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAcc.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        TextView usernameId =(TextView) findViewById(R.id.newuser);
        TextView passwordId =(TextView) findViewById(R.id.newpassword);
        TextView repeatId =(TextView) findViewById(R.id.repeatPassword);

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);

        MaterialButton createBtn = (MaterialButton) findViewById(R.id.createacc_btn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!usernameId.getText().toString().equals("") && !passwordId.getText().toString().equals("") && !repeatId.getText().toString().equals("")) {
                    progressBar.setVisibility(View.VISIBLE);
                    //Start ProgressBar first (Set visibility VISIBLE)
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[3];
                            field[0] = "username";
                            field[1] = "password";
                            field[2] = "repeatPassword";
                            //Creating array for data
                            String[] data = new String[3];
                            data[0] = usernameId.getText().toString();
                            data[1] = passwordId.getText().toString();
                            data[2] = repeatId.getText().toString();

                            PutData putData = new PutData("http://192.168.2.12/loginregister/signup.php", "POST", field, data); //HIER MOET ZEKER JE EIGEN PRIVE IP ADRES ZITTEN IN PLAATS VAN MIJN (JORDI'S IP)
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if (result.equals("Success!")) {
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(), "work bitsh", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}