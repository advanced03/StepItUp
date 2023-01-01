package com.sof.stepitup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sof.stepitup.session.SessionManager;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;


public class Home extends AppCompatActivity{
    public static final String ip = "192.168.2.12";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();

        TextView welcome = findViewById(R.id.welcome_txt);
        TextView steps = findViewById(R.id.steps);

//        zet username in welkom
        String welcomeText = getString(R.string.welkom, sessionManager.getUserInfo()[1]);
        welcome.setText(welcomeText);

        String[] field = new String[1];
        field[0] = "user_ID";
        String[] data = new String[1];
        data[0] = sessionManager.getUserInfo()[0].toString();

        PutData putData = new PutData("http://" + ip + "/stepitup/GetLatestUserInfo.php", "GET", field, data);
        if (putData.startPut()) {
            if (putData.onComplete()) {
                String result = putData.getResult();
                try {
                    JSONObject user = new JSONObject(result);
                    int userSteps = user.getInt("stappen");

                    String a = getString(R.string.steps, userSteps);
                    steps.setText(a);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recreate();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutUser();
                finish();
            }
        });
    }
}
