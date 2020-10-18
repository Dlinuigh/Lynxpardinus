package com.lynxpardinus.lp;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lynxpardinus.R;

public class StatementActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        if(bundle!=null) {
            String title = bundle.getString("title");
            String content = bundle.getString("content");
            TextView titleview= findViewById(R.id.statement_title);
            TextView contentview= findViewById(R.id.statement_content);
            titleview.setText(title);
            contentview.setText(content);
        }else{
            /*
            尽管这个地方运行没有任何问题，但是我还是发现bundle是一个空对象，有问题，这个很有可能可以自动赋值。
             */
            Log.e("StatementFragment","bundle is null");
        }
    }
}