package com.lynxpardinus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.common.ApiException;
import com.lynxpardinus.about.AboutActivity;
import com.lynxpardinus.account.LoginActivity;
import com.lynxpardinus.account.LogoutActivity;
import com.lynxpardinus.lp.LpActivity;
import com.lynxpardinus.reminder.AlarmActivity;
import com.lynxpardinus.reminder.LongRunningService;
import com.lynxpardinus.search.SearchActivity;
import com.lynxpardinus.settings.SettingsActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final String DB_NAME = "Achievement.db"; //保存的数据库文件名
    public static final String PACKAGE_NAME = "com.lynxpardinus";
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKAGE_NAME;  //在手机里存放数据库的位置
    private static final String TAG = "MainActivity";
    private final String command = "create table Achievement("+
            "id integer primary key autoincrement,"+
            "title text,"+
            "describe text,"+
            "status integer,"+
            "[CreatedTime] TimeStamp NOT NULL DEFAULT (datetime('now','localtime')))";
    private Context context;
    private DrawerLayout drawerLayout;
    private int learn_progress;
    private int practice_progress;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        final boolean darkModeOn=preferences.getBoolean("darkMode",false);
        setNightMode(darkModeOn);
        setContentView(R.layout.activity_main);
        //MyDatabaseHelper helper = new MyDatabaseHelper(this, command, "Achievement.db",null,1);
        //SQLiteDatabase db = helper.getReadableDatabase();
        SQLiteDatabase db = openDatabase(DB_PATH+"/"+DB_NAME,"Achievement.db");
        db.setLocale(Locale.CHINA);
        ArrayList<String> achieveName = new ArrayList<>();
        ArrayList<String> describes = new ArrayList<>();
        Cursor cursor = db.query("Achievement",null, null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                achieveName.add(cursor.getString(cursor.getColumnIndex("title")));
                describes.add(cursor.getString(cursor.getColumnIndex("describe")));
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        RecyclerView recyclerView = findViewById(R.id.achievement);
        AchievementAdapter adapter = new AchievementAdapter(this, achieveName, describes);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        SeekBar lpro = findViewById(R.id.progressBar1);
        SeekBar ppro = findViewById(R.id.progressBar2);
        lpro.setProgress(learn_progress);
        lpro.setMax(100);
        ppro.setProgress(practice_progress);
        ppro.setMax(100);
        /*
        lpro.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                lpro.setProgress(learn_progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ppro.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ppro.setProgress(practice_progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
*/
        ImageButton login = findViewById(R.id.login);
        ImageButton logout = findViewById(R.id.logout);
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo",MODE_PRIVATE);
        if(!sharedPreferences.getBoolean("accountStatus", false)){
            startActivity(new Intent(context, LoginActivity.class));
        }
        login.setOnClickListener(v -> {
            if(!sharedPreferences.getBoolean("accountStatus",false)){
                startActivity(new Intent(context, LoginActivity.class));
            }else{
                Toast.makeText(context, "你已经登录了",Toast.LENGTH_LONG).show();
            }

        });
        logout.setOnClickListener((View v) -> startActivity(new Intent(context, LogoutActivity.class)));

        context = this;
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        }
        toolbar.setOnMenuItemClickListener((MenuItem item) -> {
            switch (item.getItemId()){
                case R.id.action_noticeBox:
                    Toast.makeText(context, "如果你有",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.feedback:
                    Toast.makeText(context, "本来是阿里", Toast.LENGTH_SHORT).show();
                    break;
                default:
            }
            return true;
        });
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_learn);

        View view = navigationView.getHeaderView(0);
        final TextView accountName = view.findViewById(R.id.accountName);
        accountName.setText(sharedPreferences.getString("accountName",""));
        final TextView accountEmail = view.findViewById(R.id.accountEmail);
        accountEmail.setText(sharedPreferences.getString("accountEmail",""));

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
               case R.id.nav_remind:
                   startActivity(new Intent(context, AlarmActivity.class));
           }
           return true;
        });
        getToken();

        if(preferences.getBoolean("reminder",false)){
            startService(new Intent(this, LongRunningService.class));
        }
        //startService(new Intent(this, LongRunningService.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return true;
    }

    private void setNightMode(boolean darkModeOn) {
        AppCompatDelegate.setDefaultNightMode(darkModeOn ?
                AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }
    private SQLiteDatabase openDatabase(String dbfile, String filename) {
        try {
            if (!(new File(dbfile).exists())) {//判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
                InputStream is = getAssets().open(filename); //欲导入的数据库
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[1024];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
            return SQLiteDatabase.openOrCreateDatabase(dbfile, null);
        } catch (FileNotFoundException e) {
            Log.e("Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Database", "IO exception");
            e.printStackTrace();
        }
        return null;
    }
    private void getToken() {
        new Thread() {
            @Override
            public void run() {
                try {
                    // read from agconnect-services.json
                    String appId = AGConnectServicesConfig.fromContext(MainActivity.this).getString("client/app_id");
                    String token = HmsInstanceId.getInstance(MainActivity.this).getToken(appId, "HCM");
                    Log.i(TAG, "get token:" + token);
                    if(!TextUtils.isEmpty(token)) {
                        sendRegTokenToServer(token);
                    }
                } catch (ApiException e) {
                    Log.e(TAG, "get token failed, " + e);
                }
            }
        }.start();
    }
    private void sendRegTokenToServer(String token) {
        Log.i(TAG, "sending token to server. token:" + token);
    }
}