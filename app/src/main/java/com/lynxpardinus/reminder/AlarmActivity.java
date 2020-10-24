package com.lynxpardinus.reminder;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.android.material.chip.Chip;
import com.lynxpardinus.R;

public class AlarmActivity extends AppCompatActivity {
    private Intent intent;
    private Button btn1;
    private Button btn2;
    private int min2;
    private int time2;
    private int hour2;
    private String time;
    private int min;
    private int hour;
    private String select_hour;
    private String select_minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        btn1 = findViewById(R.id.button);
        btn2 = findViewById(R.id.button2);
        SharedPreferences preference =  PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preference.edit();
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Chip st = (Chip) findViewById(R.id.switch1);
        if(preference.getBoolean("reminder",false)){
            st.setChecked(true);
            int hour3 = preference.getInt("hour2",0);
            int minute3 = preference.getInt("minute2",0);
            select_hour =String.valueOf(hour3);
            select_minute = String.valueOf(minute3);
            min2 = minute3;
            hour2 = hour3;
            time2 = min2+ 60*hour2;
            time = String.valueOf(time2);
            btn2.setText(time+"分钟");
            int hourOfDay = preference.getInt("hour",0);
            int minute = preference.getInt("minute",0);
            select_hour =String.valueOf(hourOfDay);
            select_minute = String.valueOf(minute);
            min = minute;
            hour = hourOfDay;
            if(hourOfDay<10){
                select_hour = "0"+hourOfDay;
            }
            if(minute<10){
                select_minute = "0"+minute;
            }
            btn1.setText(select_hour+":"+select_minute);
        }else{
            st.setChecked(false);
            btn1.setText("这个已经关闭了");
            btn2.setText("这个也一样");
        }
        st.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @SuppressLint("CommitPrefEdits")
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    editor.putBoolean("reminder",true);
                    editor.apply();
                    btn1.setClickable(true);
                    btn2.setClickable(true);
                    btn1.setOnClickListener(v -> new TimePickerDialog(AlarmActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                            Toast.makeText(AlarmActivity.this,"tgvtyg",Toast.LENGTH_SHORT).show();
                            editor.putInt("hour",hourOfDay);
                            editor.putInt("minute",minute);
                            editor.apply();
                            startService(new Intent(AlarmActivity.this, LongRunningService.class));
                        }
                    },0,0,true).show());
                    int hourOfDay = preference.getInt("hour",0);
                    int minute = preference.getInt("minute",0);
                    select_hour =String.valueOf(hourOfDay);
                    select_minute = String.valueOf(minute);
                    min = minute;
                    hour = hourOfDay;
                    if(hourOfDay<10){
                        select_hour = "0"+hourOfDay;
                    }
                    if(minute<10){
                        select_minute = "0"+minute;
                    }
                    btn1.setText(select_hour+":"+select_minute);
                    btn2.setOnClickListener(v -> new TimePickerDialog(AlarmActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay1, int minute1) {

                            Toast.makeText(AlarmActivity.this,"tgvtyg",Toast.LENGTH_SHORT).show();
                            editor.putInt("hour2", hourOfDay1);
                            editor.putInt("minute2", minute1);
                            editor.apply();
                            startService(new Intent(AlarmActivity.this, LongRunningService.class));
                        }
                    },0,0,true).show());
                    int hour3 = preference.getInt("hour2",0);
                    int minute3 = preference.getInt("minute2",0);
                    select_hour =String.valueOf(hour3);
                    select_minute = String.valueOf(minute3);
                    min2 = minute3;
                    hour2 = hour3;
                    time2 = min2+ 60*hour2;
                    time = String.valueOf(time2);
                    btn2.setText(time+"分钟");
                } else{
                    editor.putBoolean("reminder",false);
                    editor.apply();
                    btn1.setText("这个已经关闭了");
                    btn2.setText("这个也一样");
                    btn1.setClickable(false);
                    btn2.setClickable(false);
                    stopService(new Intent(AlarmActivity.this, LongRunningService.class));
                }
            }
        });
    }
}