package com.lynxpardinus.lp;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lynxpardinus.R;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        Context context = this;
        if(bundle!=null){
            String title = bundle.getString("title");
            String content = bundle.getString("content");
            final ArrayList<String> choices = bundle.getStringArrayList("choices");
            final ArrayList<String> answer = bundle.getStringArrayList("answer");
            TextView titleview= findViewById(R.id.choice_title);
            TextView contentview= findViewById(R.id.choice_content);
            titleview.setText(title);
            contentview.setText(content);
            RadioGroup radioGroup= findViewById(R.id.choice_group);
            /*
            下面是一个动态添加选项的方法，在这里面设置选项的各个布局参数即可。
             */
            assert choices != null;
            for(int i = 0; i<choices.size(); i++){
                RadioButton tempButton = new RadioButton(this);
                tempButton.setText(choices.get(i));
                tempButton.setBackgroundColor(Color.WHITE);
                radioGroup.addView(tempButton, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            }

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                    final Button button=radioGroup.findViewById(checkedId);
                    Log.e("ChoiceFragment","checkedId is: "+checkedId);
                    assert answer != null;
                    for(String j:answer){
                        int i=Integer.parseInt(j);
                        if(button.getText().toString().equals(choices.get(i-1))){
                            button.setBackgroundColor(Color.GREEN);
                            Toast.makeText(context,"恭喜！选择正确。",Toast.LENGTH_SHORT).show();
                            /*
                            这里是一个延时写法，因为我的目的是让正确选项先变绿后变成无色。不正确的选项不变色，并且会Toast提示正确。
                             */
                            TimerTask task = new TimerTask() {
                                @Override
                                public void run() {
                                    button.setBackgroundColor(Color.WHITE);
                                }
                            };
                            Timer timer=new Timer();
                            timer.schedule(task,1000);
                        }
                    }

                }
            });
        }

    }
}