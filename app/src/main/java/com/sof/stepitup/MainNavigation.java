package com.sof.stepitup;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.sof.stepitup.session.SessionManager;

public class MainNavigation extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    NewsFragment newsFragment = new NewsFragment();
    ShopFragment shopFragment = new ShopFragment();
    RankFragment rankFragment = new RankFragment();
    PurchasesFragment purchasesFragment = new PurchasesFragment();
    NotificationFragment notificationFragment = new NotificationFragment();
    ShoppingcartFragment shoppingcartFragment = new ShoppingcartFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        SessionManager sessionManager = new SessionManager(getApplicationContext(), "userSession");
        sessionManager.checkLogin();

        bottomNavigationView = findViewById(R.id.bottom_nav1);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_home:
                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        return true;

                    case R.id.menu_news:
                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, newsFragment).commit();
                        return true;

                    case R.id.menu_shopping:
                        getSupportFragmentManager().popBackStack();
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, shopFragment).commit();
                        return true;

                    case R.id.menu_rank:
                        getSupportFragmentManager().popBackStack();
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
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, notificationFragment).commit();
                return true;
            case R.id.menu_shoppingcart:
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, shoppingcartFragment).commit();
                return true;
            case R.id.menu_profile:
                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void replaceFragments() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, purchasesFragment).addToBackStack("").commit();
    }
}

