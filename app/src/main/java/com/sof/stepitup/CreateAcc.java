package com.sof.stepitup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class CreateAcc extends AppCompatActivity {
    //Wireless LAN adapter Local Area Connection IP via hotspot
    //Jordi's ethernet 192.168.2.12
    public static final String ip = "192.168.2.12";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_acc);
        TextView maakAccTxt = (TextView) findViewById(R.id.maakacctxt);
        TextView hasAccountText =(TextView) findViewById(R.id.hasacc);
        hasAccountText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAcc.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

        TextView usernameId =(TextView) findViewById(R.id.newuser);
        TextView passwordId =(TextView) findViewById(R.id.newpassword);
        TextView repeatId =(TextView) findViewById(R.id.repeatPassword);
        TextView emailId =(TextView) findViewById(R.id.email);

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
                            String[] field = new String[4];
                            field[0] = "username";
                            field[1] = "password";
                            field[2] = "repeatPassword";
                            field[3] = "email";

                            //Creating array for data
                            String[] data = new String[4];
                            data[0] = usernameId.getText().toString();
                            data[1] = passwordId.getText().toString();
                            data[2] = repeatId.getText().toString();
                            data[3] = emailId.getText().toString();

                            PutData putData = new PutData("http://"+ ip +"/stepitup/signup.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if (result.equals("Success!")) {
                                        Intent intent = new Intent(getApplicationContext(), Login.class);
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
                    Toast.makeText(getApplicationContext(), "Vul alles in aub", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}