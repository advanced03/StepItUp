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

