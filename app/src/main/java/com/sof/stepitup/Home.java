package com.sof.stepitup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.sof.stepitup.session.SessionManager;

public class Home extends AppCompatActivity{


    BottomNavigationView bottomNavigationView;

    HuisFragment huisFragment = new HuisFragment();
    NewsFragment newsFragment = new NewsFragment();
    ShoppingFragment shoppingFragment = new ShoppingFragment();
    RankFragment rankFragment = new RankFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        bottomNavigationView = findViewById(R.id.bottom_nav1);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,huisFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.menu_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,huisFragment).commit();
                        return true;

                    case R.id.menu_news:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,newsFragment).commit();
                        return true;

                    case R.id.menu_shopping:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,shoppingFragment).commit();
                        return true;

                    case R.id.menu_rank:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,rankFragment).commit();
                        return true;
                }
                return true;
            }
        });
    }

        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_top_navigation,menu);

            return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();

            if (id == R.id.menu_notifications) {
                Intent intent = new Intent(Home.this, NotaficationFragment.class);
                startActivity(intent);
                return true;
            }
            else
            if (id == R.id.menu_shopping) {
               Intent intent = new Intent(Home.this, ShoppingcartFragment.class);
               startActivity(intent);
               return true;
        }
            else
            if (id == R.id.menu_profile) {
              Intent intent = new Intent(Home.this, ProfileFragment.class);
              startActivity(intent);
              return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

