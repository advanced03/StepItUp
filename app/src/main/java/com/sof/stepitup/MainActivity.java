package com.sof.stepitup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    BottomNavigationView bottomNavigationView;
    TextView textTabName;



    //REMINDER: SESSIONS KUNNEN GEMAAKT WORDEN VOOR ONTHOUDEN VAN LOGINS
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        textTabName = findViewById(R.id.text_tab_name);

        bottomNavigationView.setOnItemSelectedListener(this);



        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
        TextView logintxt = (TextView) findViewById(R.id.login);
        TextView username =(TextView) findViewById(R.id.user);
        TextView password =(TextView) findViewById(R.id.password);
        TextView createAccount = (TextView) findViewById(R.id.noacc);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getBaseContext(),"Workdskfsndf",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, CreateAcc.class);
                startActivity(intent);
                finish();
            }
        });

        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!username.getText().toString().equals("") && !password.getText().toString().equals("")) {
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
                            data[0] = username.getText().toString();
                            data[1] = password.getText().toString();

                            PutData putData = new PutData("http://143.177.30.40/stepitup/login.php", "POST", field, data); //HIER MOET ZEKER JE EIGEN PRIVE IP ADRES ZITTEN IN PLAATS VAN MIJN (JORDI'S IP)
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
                            //End Write and Read data with URL
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(), "Iets is fout gegaan...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menu_home:

                textTabName.setText(item.getTitle());

                break;


            case R.id.menu_dashboard:

                textTabName.setText(item.getTitle());

                break;


            case R.id.menu_notification:

                textTabName.setText(item.getTitle());

                break;
        }

        return true;
    }
    private void showNotificationIndicator(){

    }
}