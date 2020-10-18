package com.lynxpardinus.search;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lynxpardinus.R;
import com.lynxpardinus.selfview.MyEditView;

public class NewentryActivity extends AppCompatActivity {

    private MyDatabaseHelper helper;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newentry);
        context = this;
        final String[] kind = new String[1];
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String [] kinds = getResources().getStringArray(R.array.kinds);
                kind[0] = kinds[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        MyEditView name = findViewById(R.id.name);
        name.setLeadText("名称");
        MyEditView usage = findViewById(R.id.usage);
        usage.setLeadText("用法");
        MyEditView describe = findViewById(R.id.describe);
        describe.setLeadText("描述");
        MyEditView example = findViewById(R.id.example);
        example.setLeadText("栗子");
        helper = new MyDatabaseHelper(this, "Entries.db",null, 1);
        Button button = findViewById(R.id.save_to_database);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("name",name.getText().toString());
                values.put("kinds",kind[0]);
                values.put("usage",usage.getText().toString());
                values.put("describe",describe.getText().toString());
                values.put("example",example.getText().toString());
                String [] columns = {"kinds","name"};
                Cursor cursor = db.query("Entry", columns, "name=?", new String [] {name.getText().toString()}, null, null, null);
                if(cursor.getCount()==0){
                    db.insert("Entry", null, values);
                    cursor.close();
                }else{
                    Toast.makeText(context, "你已经添加了这个词条，请不要重复点击。",Toast.LENGTH_SHORT).show();
                }
                db.close();
                values.clear();
            }
        });
    }
}