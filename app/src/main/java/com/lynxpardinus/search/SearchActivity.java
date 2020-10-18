package com.lynxpardinus.search;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lynxpardinus.R;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context =this;
        setContentView(R.layout.activity_search);
        MyDatabaseHelper helper = new MyDatabaseHelper(context, "Entries.db",null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor cursor = db.query("Entry", null,null, null, null,null,null);
        final ArrayList<String> initList = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                initList.add(cursor.getString(cursor.getColumnIndex("name")) + " | "+cursor.getString(cursor.getColumnIndex("describe")));
            }while (cursor.moveToNext());
        }
        cursor.close();
        RecyclerView recyclerView = findViewById(R.id.search_result);
        ResultAdapter adapter = new ResultAdapter(this, initList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        final String[] queryText = new String[1];
        SearchView searchView = findViewById(R.id.search);
        searchView.setIconifiedByDefault(false);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Toast.makeText(context, "清空",Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                initList.clear();
                Cursor cursor1;
                if(query.equals("*")){
                     cursor1= db.query("Entry", null,null, null, null,null,null);
                }else{
                    cursor1 = db.rawQuery("select * from Entry where name like '%"+query+"%'",null);
                }
                if(cursor1.moveToFirst()&&cursor1.getCount()!=0) {
                    do {
                        initList.add(cursor1.getString(cursor1.getColumnIndex("name")) + " | " + cursor1.getString(cursor1.getColumnIndex("describe")));
                    } while (cursor1.moveToNext());
                }
                cursor1.close();
                adapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        FloatingActionButton actionButton = findViewById(R.id.create_entry);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewentryActivity.class);
                startActivity(intent);
            }
        });
    }

}