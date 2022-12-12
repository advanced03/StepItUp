package com.sof.stepitup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView username =(TextView) findViewById(R.id.user);
        TextView password =(TextView) findViewById(R.id.password);

        TextView createAccount = (TextView) findViewById(R.id.noacc);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateAcc.class);
                startActivity(intent);
            }
        });

        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().equals("beheerder") && password.getText().toString().equals("beheerder")){
                    //correct
                    Intent intent = new Intent(MainActivity.this, Home.class);
                    startActivity(intent);
                }else
                    //incorrect
                    Toast.makeText(MainActivity.this,"Wachtwoord of gebruikersnaam fout!",Toast.LENGTH_SHORT).show();
            }
        });
    }
}