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
import com.google.android.material.textfield.TextInputEditText;
import com.sof.stepitup.session.SessionManager;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    //Wireless LAN adapter Local Area Connection IP via hotspot
    //Jordi's ethernet 192.168.2.12
    // ctrl alt L == reformat code
    public static final String ip = "192.168.2.12";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress);
        TextInputEditText username =(TextInputEditText) findViewById(R.id.usertxt);
        TextInputEditText password =(TextInputEditText) findViewById(R.id.passwordtxt);
        TextView createAccount = (TextView) findViewById(R.id.noacc);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getBaseContext(),"Workdskfsndf",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, CreateAcc.class);
                startActivity(intent);
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
//                            try {
                            PutData putData = new PutData("http://" + ip + "/stepitup/login.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if (!result.equals("Helaas is de combinatie onjuist")) {
                                        try {
                                            JSONObject user = new JSONObject(result);
                                            String userId = user.getString("gebruiker_ID");
                                            String username = user.getString("gebruikersnaam");
                                            String email = user.getString("email");
                                            String role = user.getString("rol");
//                                            String steps = user.getString("stappen");
                                            sessionManager.createLoginSession(Integer.parseInt(userId), username, email, role);
                                            Intent intent = new Intent(getApplicationContext(), Home.class);
                                            startActivity(intent);
                                            finish();
//                                                Toast.makeText(getApplicationContext(), user.getString("gebruiker_ID"), Toast.LENGTH_SHORT).show();

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
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