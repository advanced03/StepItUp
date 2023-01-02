package com.sof.stepitup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.sof.stepitup.session.SessionManager;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.zip.Inflater;

public class Home extends AppCompatActivity {
    public static final String ip = "192.168.2.12";

    BottomNavigationView bottomNavigationView;
    Intent intent;
    HomeFragment homeFragment = new HomeFragment();
    NewsFragment newsFragment = new NewsFragment();
    ShopFragment shopFragment = new ShopFragment();
    RankFragment rankFragment = new RankFragment();
    NotificationFragment notificationFragment = new NotificationFragment();
    ShoppingcartFragment shoppingcartFragment = new ShoppingcartFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.checkLogin();

        TextView welcome = findViewById(R.id.welcome_txt);
        TextView steps = findViewById(R.id.steps);

//        zet username in welkom
//        String welcomeText = getString(R.string.welkom, sessionManager.getUserInfo()[1]);
//        welcome.setText(welcomeText);

        //refresh functie
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swiperefreshlayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recreate();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        //gewoon logout
        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutUser();
                finish();
            }
        });
        //query voor user data
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

//                    String a = getString(R.string.steps, userSteps);
//                    steps.setText(a);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }



        bottomNavigationView = findViewById(R.id.bottom_nav1);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        return true;

                    case R.id.menu_news:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, newsFragment).commit();
                        return true;

                    case R.id.menu_shopping:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, shopFragment).commit();
                        return true;

                    case R.id.menu_rank:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, rankFragment).commit();
                        return true;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_top_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_notifications:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, notificationFragment).commit();
                return true;
            case R.id.menu_shoppingcart:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, shoppingcartFragment).commit();
                return true;
            case R.id.menu_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

