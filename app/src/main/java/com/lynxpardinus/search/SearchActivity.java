package com.lynxpardinus.search;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lynxpardinus.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    public static final String DB_NAME = "Entries.db"; //保存的数据库文件名
    public static final String PACKAGE_NAME = "com.lynxpardinus";
    public static final String DB_PATH = "/data"
            + Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKAGE_NAME+"/";  //在手机里存放数据库的位置
    public final String CREATE_ENTRY ="create table Entry ( "
            +"id integer primary key autoincrement, "
            +"kinds text, "
            +"name text, "+
            "usage text, "
            +"describe text, "
            +"example text, " +
            "[CreatedTime] TimeStamp NOT NULL DEFAULT (datetime('now','localtime')), " +
            "author text) ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context =this;
        setContentView(R.layout.activity_search);
        //MyDatabaseHelper helper = new MyDatabaseHelper(this,CREATE_ENTRY, "Entries.db",null, 1);
        //SQLiteDatabase db = helper.getWritableDatabase();
        SQLiteDatabase db = openDatabase(DB_PATH+DB_NAME,"Entries.db");
        Cursor cursor = db.query("Entry", null,null, null, null,null,null);
        final ArrayList<String> initList = new ArrayList<>();
        final ArrayList<String> strings = new ArrayList<>();
        if(cursor.moveToFirst()){
            do{
                initList.add(cursor.getString(cursor.getColumnIndex("name"))+"\n" +cursor.getString(cursor.getColumnIndex("usage")));
                strings.add(cursor.getString(cursor.getColumnIndex("describe"))+"\n"+cursor.getString(cursor.getColumnIndex("example")));
            }while (cursor.moveToNext());
        }
        cursor.close();
        RecyclerView recyclerView = findViewById(R.id.search_result);
        ResultAdapter adapter = new ResultAdapter(this, initList, strings);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        SearchView searchView = findViewById(R.id.search);
        searchView.setIconifiedByDefault(false);
        searchView.setOnCloseListener(() -> {
            Toast.makeText(context, "清空",Toast.LENGTH_SHORT).show();
            return false;
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                initList.clear();
                strings.clear();
                Cursor cursor1;
                if(query.equals("*")){
                     cursor1= db.query("Entry", null,null, null, null,null,null);
                }else{
                    cursor1 = db.rawQuery("select * from Entry where name like '%"+query+"%'",null);
                }
                if(cursor1.moveToFirst()) {
                    do {
                        initList.add(cursor1.getString(cursor1.getColumnIndex("name"))+"\n" +cursor1.getString(cursor1.getColumnIndex("usage")));
                        strings.add(cursor1.getString(cursor1.getColumnIndex("describe"))+"\n"+cursor1.getString(cursor1.getColumnIndex("example")));
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
        actionButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, NewentryActivity.class);
            startActivity(intent);
        });
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

}