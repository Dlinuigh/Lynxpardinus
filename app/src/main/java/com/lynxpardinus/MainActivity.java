package com.lynxpardinus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;
import com.lynxpardinus.about.AboutActivity;
import com.lynxpardinus.lp.LpActivity;
import com.lynxpardinus.search.SearchActivity;
import com.lynxpardinus.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    private Context context;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        final boolean darkModeOn=preferences.getBoolean("darkMode",false);
        setNightMode(darkModeOn);
        setContentView(R.layout.activity_main);
        context = this.getApplicationContext();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(item -> false);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_learn);
        navigationView.setNavigationItemSelectedListener(item -> {
           switch (item.getItemId()){
               case R.id.nav_learn:
                   Intent Lintent = new Intent(context, LpActivity.class);
                   Lintent.putExtra("isLearn",true);
                   startActivity(Lintent);
                   break;
               case R.id.nav_practice:
                   Intent Pintent = new Intent(context, LpActivity.class);
                   Pintent.putExtra("isLearn", false);
                   startActivity(Pintent);
                   break;
               case R.id.nav_search:
                   Intent Sintent = new Intent(context, SearchActivity.class);
                   startActivity(Sintent);
                   break;
               case R.id.nav_settings:
                   Intent sintent = new Intent(context, SettingsActivity.class);
                   startActivity(sintent);
                   break;
               case R.id.nav_about:
                   Intent Aintent = new Intent(context, AboutActivity.class);
                   startActivity(Aintent);
                   break;
           }
           return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_toolbar, menu);
        return true;
    }

    private void setNightMode(boolean darkModeOn) {
        AppCompatDelegate.setDefaultNightMode(darkModeOn ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }
}