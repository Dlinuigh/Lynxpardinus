package com.lynxpardinus.about;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.lynxpardinus.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Button button1 = findViewById(R.id.AboutMainpage);
        Button button2 = findViewById(R.id.AboutMarkandComment);
        Button button3 = findViewById(R.id.AboutMailus);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                //Uri uri = Uri.parse("https://www.wulihub.com.cn/go/WneYb4/index.html");
                Uri uri = Uri.parse("http://ding_ling_hui.gitee.io/lynx-pardinus-page");
                intent.setData(uri);
                //指定特定浏览器
                //intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                startActivity(intent);
            }
        });
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Uri uri = Uri.parse("market://details?id="+getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }catch(Exception e){
                    Toast.makeText(AboutActivity.this, "您的手机没有安装Android应用市场", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("mailto:"+"dlinuigh@163.com");
                Intent data=new Intent(Intent.ACTION_SENDTO);
                data.setData(uri);
                //data.putExtra(Intent.EXTRA_CC, "这是抄送人");
                data.putExtra(Intent.EXTRA_SUBJECT, "联系开发者");
                //data.putExtra(Intent.EXTRA_TEXT, "这是内容");
                startActivity(Intent.createChooser(data, "请选择邮件类应用"));
            }
        });
        findViewById(R.id.privacy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                //Uri uri = Uri.parse("https://www.wulihub.com.cn/go/WneYb4/index.html");
                Uri uri = Uri.parse("http://ding_ling_hui.gitee.io/privacy");
                intent.setData(uri);
                //指定特定浏览器
                //intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                startActivity(intent);
            }
        });
    }
}